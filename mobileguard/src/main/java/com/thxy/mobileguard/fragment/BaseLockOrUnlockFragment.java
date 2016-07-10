package com.thxy.mobileguard.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.dao.LockedDao;
import com.thxy.mobileguard.domain.AppBean;
import com.thxy.mobileguard.engine.AppManagerEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * 未加锁的Fragment
 */
public class BaseLockOrUnlockFragment extends Fragment {
    protected static final int LOADING = 1;
    protected static final int FINISH = 2;
    protected TextView tv_lab;
    protected ListView lv_datas;
    protected ProgressBar pb_loading;
    protected TextView tv_listview_tag;
    protected MyAdapter adapter;
    protected LockedDao dao;
    protected List<String> allLockedPacks;//所有加锁app的包名

    public List<String> getAllLockedPacks() {
        return allLockedPacks;
    }

    public void setAllLockedPacks(List<String> allLockedPacks) {
        this.allLockedPacks = allLockedPacks;
    }

    //存放未加锁的APP
    protected List<AppBean> unlockedSystemDatas = new ArrayList<AppBean>();
    protected List<AppBean> unlockedUserDatas = new ArrayList<AppBean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //程序锁的业务类对象
        dao = new LockedDao(getActivity());

        super.onCreate(savedInstanceState);
    }

    private void initEvent() {
        lv_datas.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem <= unlockedUserDatas.size()) {
                    tv_listview_tag.setText("用户软件(" + unlockedUserDatas.size() + ")");
                } else {
                    tv_listview_tag.setText("系统软件(" + unlockedSystemDatas.size() + ")");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_unlocked, null);

        tv_lab = (TextView) root.findViewById(R.id.tv_fragment_unlocked_lab);
        lv_datas = (ListView) root.findViewById(R.id.lv_fragment_unlocked_datas);
        tv_listview_tag = (TextView) root.findViewById(R.id.tv_fragment_unlocked_listview_tag);
        pb_loading = (ProgressBar) root.findViewById(R.id.pb_fragment_unlocked_loading);

        adapter = new MyAdapter();
        lv_datas.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        //初始化数据
        initData();
        //初始化事件
        initEvent();
        super.onResume();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    pb_loading.setVisibility(View.VISIBLE);
                    tv_listview_tag.setVisibility(View.GONE);
                    lv_datas.setVisibility(View.GONE);
                    break;

                case FINISH:
                    pb_loading.setVisibility(View.GONE);
                    tv_listview_tag.setVisibility(View.VISIBLE);
                    lv_datas.setVisibility(View.VISIBLE);

                    //设置未加锁app个数
                    setLockNumberTextView();

                    tv_listview_tag.setText("用户软件(" + unlockedUserDatas.size() + ")");
                    adapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    protected void setLockNumberTextView() {

    }

    protected void initData() {
        new Thread() {
            @Override
            public void run() {
                synchronized (new Object()) {
                    handler.obtainMessage(LOADING).sendToTarget();
                    //取数据
                    List<AppBean> allApks = AppManagerEngine.getAllApks(getActivity());
                    //清空容器的数据
                    unlockedSystemDatas.clear();
                    unlockedUserDatas.clear();
                    //判断是否加锁
                    for (AppBean appBean : allApks) {
                        if (isMyData(appBean.getPackName())) {
                            //未加锁数据
                            if (appBean.isSystem()) {
                                unlockedSystemDatas.add(appBean);
                            } else {
                                unlockedUserDatas.add(appBean);
                            }

                        }
                    }
                    handler.obtainMessage(FINISH).sendToTarget();
                }

            }
        }.start();
    }

    public boolean isMyData(String packName) {
        return false;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return unlockedSystemDatas.size() + 1 + unlockedUserDatas.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            try {
                if (position <= unlockedUserDatas.size()) {
                    return unlockedUserDatas.get(position - 1);
                } else {
                    return unlockedSystemDatas.get(position - 1 - 1 - unlockedUserDatas.size());
                }
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                //用户apk的标签
                TextView tv_userTable = new TextView(getActivity());
                tv_userTable.setText("个人软件(" + unlockedUserDatas.size() + ")");
                tv_userTable.setTextColor(Color.WHITE);
                tv_userTable.setBackgroundColor(Color.GRAY);
                return tv_userTable;
            } else if (position == unlockedUserDatas.size() + 1) {
                //系统的apk标签
                TextView tv_sysTable = new TextView(getActivity());
                tv_sysTable.setText("系统软件(" + unlockedSystemDatas.size() + ")");
                tv_sysTable.setTextColor(Color.WHITE);
                tv_sysTable.setBackgroundColor(Color.GRAY);
                return tv_sysTable;
            } else {
                //界面的缓存
                ViewHolder holder = new ViewHolder();
                if (convertView != null && convertView instanceof RelativeLayout) {
                    holder = (ViewHolder) convertView.getTag();
                } else {
                    convertView = View.inflate(getActivity(), R.layout
                            .item_fragment_unlock_listview_item, null);

                    holder.iv_icon = (ImageView) convertView.findViewById(R.id
                            .iv_fragment_unlock_listview_item_icon);
                    holder.tv_name = (TextView) convertView.findViewById(R.id
                            .tv_fragment_unlock_listview_item_name);
                    holder.iv_lock = (ImageView) convertView.findViewById(R.id
                            .iv_fragment_unlock_listview_item_lock);

                    //绑定tag
                    convertView.setTag(holder);
                }


                //获取数据
                final AppBean bean = (AppBean) getItem(position);
                if (bean == null) {
                    return convertView;
                }
                //设置数据
                holder.iv_icon.setImageDrawable(bean.getIcon());

                holder.tv_name.setText(bean.getAppName());

                setImageViewEventAndBg(holder.iv_lock, convertView, bean.getPackName());
                return convertView;
            }

        }
    }

    public void setImageViewEventAndBg(ImageView iv_lock, View convertView, String packName) {

    }

    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_lock;
    }
}
