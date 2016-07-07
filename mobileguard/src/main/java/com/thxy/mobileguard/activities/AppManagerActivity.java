package com.thxy.mobileguard.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;
import com.thxy.mobileguard.R;
import com.thxy.mobileguard.domain.AppBean;
import com.thxy.mobileguard.engine.AppManagerEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 软件管家的页面
 */
public class AppManagerActivity extends AppCompatActivity {
    protected static final int LOADING = 1;
    protected static final int FINISH = 2;
    private TextView tv_sdAvail;
    private TextView tv_romAvail;
    private ListView lv_datas;
    private ProgressBar pb_loading;
    private MyAdapter adapter;
    private TextView tv_listview_lable;
    private View popupView;
    private PopupWindow pw;
    private ScaleAnimation sa;
    private AppBean clickBean;//当前点击的bean
    private PackageManager pm;
    private BroadcastReceiver receiver;

    private long sdAvail;
    private long romAvail;

    //用户apk的容器
    private List<AppBean> userApks = new ArrayList<AppBean>();
    //系统apk的容器
    private List<AppBean> sysApks = new ArrayList<AppBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化界面
        initView();
        //初始化数据
        initData();
        //初始化事件
        initEvent();
        //初始化弹出窗体
        initPopupWindow();
        //删除apk的广播
        initRemoveApkReceiver();
    }

    private void initRemoveApkReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initData();
            }
        };

        //注册删除apk广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
    }

    private void closePopupWindow() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
        }
    }

    private void showPopupWindow(View parent, int x, int y) {
        closePopupWindow();
        pw.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x, y);
        popupView.startAnimation(sa);
    }

    private void initPopupWindow() {
        //弹出窗体的界面
        popupView = View.inflate(getApplicationContext(), R.layout.popup_appmanager, null);
        LinearLayout ll_remove = (LinearLayout) popupView.findViewById(R.id
                .ll_appmanager_pop_remove);
        LinearLayout ll_start = (LinearLayout) popupView.findViewById(R.id.ll_appmanager_pop_start);
        LinearLayout ll_share = (LinearLayout) popupView.findViewById(R.id.ll_appmanager_pop_share);
        LinearLayout ll_setting = (LinearLayout) popupView.findViewById(R.id
                .ll_appmanager_pop_setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_appmanager_pop_remove:
                        //卸载apk
                        removeApk();
                        break;
                    case R.id.ll_appmanager_pop_start:
                        //启动apk
                        startApk();
                        break;
                    case R.id.ll_appmanager_pop_share:
                        //软件分享
                        shareApk();
                        break;
                    case R.id.ll_appmanager_pop_setting:
                        //设置中心
                        settingCenter();
                        break;
                    default:
                        break;
                }
                closePopupWindow();
            }
        };
        ll_remove.setOnClickListener(listener);
        ll_start.setOnClickListener(listener);
        ll_share.setOnClickListener(listener);
        ll_setting.setOnClickListener(listener);

        //初始化弹出窗体
        pw = new PopupWindow(popupView, -2, -2);
        //有动画效果必须设置背景
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //弹出窗体的动画
        sa = new ScaleAnimation(0, 1, 0.5f, 1, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
    }

    private void settingCenter() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + clickBean.getPackName()));
        startActivity(intent);
    }

    private void shareApk() {
        //分享到短信
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "downloadurl:baidu yixia");
        startActivity(intent);

    }

    private void startApk() {
        //获取包名
        String packName = clickBean.getPackName();
        //通过包名获取意图
        Intent launchIntentForPackage = pm.getLaunchIntentForPackage(packName);
        startActivity(launchIntentForPackage);
    }

    private void removeApk() {
        if (!clickBean.isSystem()) {
            //卸载用户apk
            Intent intent = new Intent("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:" + clickBean.getPackName()));
            startActivity(intent);
            //刷新数据。注册删除数据广播，通过广播来更新数据
        } else {
            //系统app，默认不能删除,root刷机并赋予root权限才可以删除

            try {
                //判断是否root刷机
                if (!RootTools.isRootAvailable()) {
                    Toast.makeText(getApplicationContext(), "请先进行root刷机", Toast.LENGTH_SHORT).show();
                    return;
                }
                //是否root授权给当前apk
                if (!RootTools.isAccessGiven()){
                    Toast.makeText(getApplicationContext(), "请先授予root权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                //直接使用命令删除apk
                RootTools.sendShell("mount -o remount rw /system", 8000);//设置命令的超时时间为8秒
                System.out.println("安装路径:" + clickBean.getApkPath());
                RootTools.sendShell("rm -r " + clickBean.getApkPath(), 8000);
                RootTools.sendShell("mount -o remount r /system", 8000);

            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootToolsException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void initEvent() {
        lv_datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前点击的位置的值，如果点击系统标签不做处理
                if (position == userApks.size() + 1) {
                    return;
                } else {
                    //listview.getItemAtPosition()本质调用adapter.getItem()
                    clickBean = (AppBean) lv_datas.getItemAtPosition(position);
                    int[] location = new int[2];
                    view.getLocationInWindow(location);
                    showPopupWindow(view, location[0] + 70, location[1]);
                }
            }
        });
        //listview滑动的事件处理
        lv_datas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                closePopupWindow();
                //判断显示位置
                if (firstVisibleItem >= userApks.size() + 1) {
                    //显示系统apk
                    tv_listview_lable.setText("系统软件(" + sysApks.size() + ")");
                } else {
                    tv_listview_lable.setText("用户软件(" + userApks.size() + ")");
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    //显示加载数据的进度
                    pb_loading.setVisibility(View.VISIBLE);
                    //隐藏listView
                    lv_datas.setVisibility(View.GONE);
                    tv_listview_lable.setVisibility(View.GONE);
                    break;
                case FINISH:
                    //隐藏加载数据的进度
                    pb_loading.setVisibility(View.GONE);
                    //显示listView
                    lv_datas.setVisibility(View.VISIBLE);
                    tv_listview_lable.setVisibility(View.VISIBLE);
                    //设置内存剩余大小
                    tv_sdAvail.setText("sd卡可用空间:" + Formatter.formatFileSize
                            (getApplicationContext(), sdAvail));
                    tv_romAvail.setText("rom可用空间" + Formatter.formatFileSize
                            (getApplicationContext(), romAvail));
                    //初始化标签
                    tv_listview_lable.setText("用户软件(" + userApks.size() + ")");
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_location;
        TextView tv_size;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userApks.size() + 1 + sysApks.size() + 1;
        }

        @Override
        public android.view.View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                //用户apk的标签
                TextView tv_userTable = new TextView(getApplicationContext());
                tv_userTable.setText("个人软件(" + userApks.size() + ")");
                tv_userTable.setTextColor(Color.WHITE);
                tv_userTable.setBackgroundColor(Color.GRAY);
                return tv_userTable;
            } else if (position == userApks.size() + 1) {
                //系统的apk标签
                TextView tv_sysTable = new TextView(getApplicationContext());
                tv_sysTable.setText("系统软件(" + sysApks.size() + ")");
                tv_sysTable.setTextColor(Color.WHITE);
                tv_sysTable.setBackgroundColor(Color.GRAY);
                return tv_sysTable;
            } else {
                //界面的缓存
                ViewHolder holder = new ViewHolder();
                if (convertView != null && convertView instanceof RelativeLayout) {
                    holder = (ViewHolder) convertView.getTag();
                } else {
                    convertView = View.inflate(getApplicationContext(), R.layout
                            .item_appmanager_listview_item, null);

                    holder.iv_icon = (ImageView) convertView.findViewById(R.id
                            .iv_appmanager_listview_item_icon);
                    holder.tv_title = (TextView) convertView.findViewById(R.id
                            .tv_appmanager_listview_item_title);
                    holder.tv_location = (TextView) convertView.findViewById(R.id
                            .tv_appmanager_listview_item_location);
                    holder.tv_size = (TextView) convertView.findViewById(R.id
                            .tv_appmanager_listview_item_size);
                    //绑定tag
                    convertView.setTag(holder);
                }


                //获取数据
                AppBean bean = getItem(position);

                //设置数据
                holder.iv_icon.setImageDrawable(bean.getIcon());
                if (bean.isSd()) {
                    holder.tv_location.setText("SD存储");
                } else {
                    holder.tv_location.setText("Rom存储");
                }

                holder.tv_title.setText(bean.getAppName());
                holder.tv_size.setText(Formatter.formatFileSize(getApplicationContext(), bean
                        .getSize()));
                return convertView;
            }
        }

        /*@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                //用户apk的标签
                TextView tv_userTable = new TextView(getApplicationContext());
                tv_userTable.setText("个人软件(" + userApks.size() + ")");
                tv_userTable.setTextColor(Color.WHITE);
                tv_userTable.setBackgroundColor(Color.GRAY);
                return tv_userTable;
            } else if (position == userApks.size() + 1) {
                //系统的apk标签
                TextView tv_sysTable = new TextView(getApplicationContext());
                tv_sysTable.setText("系统软件(" + sysApks.size() + ")");
                tv_sysTable.setTextColor(Color.WHITE);
                tv_sysTable.setBackgroundColor(Color.GRAY);
                return tv_sysTable;
            } else {
                View view = null;
                if (convertView != null && convertView instanceof RelativeLayout) {
                    view = convertView;
                } else {
                    view = View.inflate(getApplicationContext(), R.layout
                            .item_appmanager_listview_item, null);
                }

                ImageView iv_icon = (ImageView) view.findViewById(R.id
                        .iv_appmanager_listview_item_icon);
                TextView tv_title = (TextView) view.findViewById(R.id
                        .tv_appmanager_listview_item_title);
                TextView tv_location = (TextView) view.findViewById(R.id
                        .tv_appmanager_listview_item_location);
                TextView tv_size = (TextView) view.findViewById(R.id
                        .tv_appmanager_listview_item_size);

                //获取数据
                AppBean bean = getItem(position);

                //设置数据
                iv_icon.setImageDrawable(bean.getIcon());
                if (bean.isSd()) {
                    tv_location.setText("SD存储");
                } else {
                    tv_location.setText("Rom存储");
                }

                tv_title.setText(bean.getAppName());
                tv_size.setText(Formatter.formatFileSize(getApplicationContext(), bean.getSize()));
                return view;
            }
        }*/

        /**
         * 通过位置获取数据
         *
         * @param position
         * @return
         */
        @Override
        public AppBean getItem(int position) {
            AppBean bean = null;
            if (position <= userApks.size()) {
                bean = userApks.get(position - 1);
            } else {
                bean = sysApks.get(position - 1 - 1 - userApks.size());
            }
            return bean;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //发送正在加载数据的消息
                handler.obtainMessage(LOADING).sendToTarget();
                //获取所有的apk数据
                List<AppBean> datas = AppManagerEngine.getAllApks(getApplicationContext());
                //添加新的数据前，先清空原来的数据
                sysApks.clear();
                userApks.clear();
                //分类系统和用户apk
                for (AppBean appBean : datas) {
                    if (appBean.isSystem()) {
                        //系统apk
                        sysApks.add(appBean);
                    } else {
                        userApks.add(appBean);
                    }
                }
                sdAvail = AppManagerEngine.getSDAvail(getApplicationContext());
                romAvail = AppManagerEngine.getRomAvail();
                //数据处理完毕，发送消息
                handler.obtainMessage(FINISH).sendToTarget();
            }
        }.start();
    }

    private void initView() {
        setContentView(R.layout.activity_appmanager);

        tv_sdAvail = (TextView) findViewById(R.id.tv_appmanager_sdsize);
        tv_romAvail = (TextView) findViewById(R.id.tv_appmanager_romsize);
        lv_datas = (ListView) findViewById(R.id.lv_appmanager_appdatas);
        pb_loading = (ProgressBar) findViewById(R.id.pb_appmanager_loading);
        tv_listview_lable = (TextView) findViewById(R.id.tv_appmanager_listview_lable);

        //listView的适配器
        adapter = new MyAdapter();
        lv_datas.setAdapter(adapter);

        pm = getPackageManager();
    }

    @Override
    protected void onDestroy() {
        //取消注册删除apk的广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
