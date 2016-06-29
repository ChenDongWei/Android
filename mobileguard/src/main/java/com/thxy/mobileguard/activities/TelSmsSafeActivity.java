package com.thxy.mobileguard.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.dao.BlackDao;
import com.thxy.mobileguard.domain.BlackBean;
import com.thxy.mobileguard.domain.BlackTable;

import java.util.ArrayList;
import java.util.List;

public class TelSmsSafeActivity extends AppCompatActivity {
    private static final int LOADING = 1;
    private static final int FINISH = 2;
    private ListView lv_safenumber;
    private Button bt_addSafeNumber;
    private TextView tv_nodata;
    private ProgressBar pb_loading;
    private BlackDao dao;
    private MyAdapter adapter;
    private List<BlackBean> moreDatas;
    private AlertDialog dialog;
    private View contextView;
    private PopupWindow pw;
    private ScaleAnimation sa;

    private final int MOREDATASCOUNTS = 20;//分批加载的数据的个数

    private List<BlackBean> datas = new ArrayList<BlackBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        //初始化数据
        initData();
        //初始化事件
        initEvent();
        //弹出窗体
        initPopupWindow();
    }

    private void showPopupWindow() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
        } else {
            int[] location = new int[2];
            //获取添加按钮的坐标
            bt_addSafeNumber.getLocationInWindow(location);
            //显示动画
            contextView.startAnimation(sa);
            //设置右上角对齐
            pw.showAtLocation(bt_addSafeNumber, Gravity.RIGHT | Gravity.TOP, location[0] -
                    (getWindowManager().getDefaultDisplay().getWidth() - bt_addSafeNumber
                            .getWidth()), location[1] + bt_addSafeNumber.getHeight());

        }
    }

    private void closePopupWindow() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
        }
    }

    private void initPopupWindow() {
        contextView = View.inflate(getApplicationContext(), R.layout.popup_blacknumber_item,
                null);
        TextView tv_manual = (TextView) contextView.findViewById(R.id.tv_popup_black_manual);
        TextView tv_contact = (TextView) contextView.findViewById(R.id.tv_popup_black_contacts);
        TextView tv_phonelog = (TextView) contextView.findViewById(R.id.tv_popup_black_phonelog);
        TextView tv_smslog = (TextView) contextView.findViewById(R.id.tv_popup_black_smslog);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_popup_black_manual:
                        //手动导入
                        break;
                    case R.id.tv_popup_black_contacts:
                        //联系人导入
                        break;
                    case R.id.tv_popup_black_phonelog:
                        //通话记录导入
                        break;
                    case R.id.tv_popup_black_smslog:
                        //短信记录导入
                        break;
                    default:
                        break;
                }
                //关闭popupWindow
                closePopupWindow();
            }
        };
        tv_manual.setOnClickListener(listener);
        tv_contact.setOnClickListener(listener);
        tv_phonelog.setOnClickListener(listener);
        tv_smslog.setOnClickListener(listener);

        pw = new PopupWindow(contextView, -2, -2);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //窗体动画的显示
        sa = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0f);
        sa.setDuration(1000);
    }

    private void initEvent() {
        //给listView设置滑动事件
        lv_safenumber.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //监听静止状态
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //获取最后显示的数据位置
                    int lastVisiblePosition = lv_safenumber.getLastVisiblePosition();
                    if (lastVisiblePosition == datas.size() - 1) {
                        initData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    //显示加载数据的进度，隐藏listview和没有数据
                    pb_loading.setVisibility(View.VISIBLE);
                    lv_safenumber.setVisibility(View.GONE);
                    tv_nodata.setVisibility(View.GONE);
                    break;

                case FINISH:
                    //判断是否有数据
                    if (moreDatas.size() != 0) {
                        lv_safenumber.setVisibility(View.VISIBLE);
                        pb_loading.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.GONE);
                        //更新数据
                        adapter.notifyDataSetChanged();
                    } else {
                        if (datas.size() != 0) {
                            Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        tv_nodata.setVisibility(View.VISIBLE);
                        lv_safenumber.setVisibility(View.GONE);
                        pb_loading.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //发消息显示正在加载数据的进度条
                handler.obtainMessage(LOADING).sendToTarget();
                //获取分批加载的数据
                moreDatas = dao.getMoreDatas(MOREDATASCOUNTS, datas.size());

                //把一个容器的所有数据加进来
                datas.addAll(moreDatas);
                handler.obtainMessage(FINISH).sendToTarget();
            }
        }.start();
    }

    private void initView() {
        setContentView(R.layout.activity_telsmssafe);
        lv_safenumber = (ListView) findViewById(R.id.lv_telsms_safenumbers);
        bt_addSafeNumber = (Button) findViewById(R.id.bt_telsms_addsafenumber);
        tv_nodata = (TextView) findViewById(R.id.tv_telsms_nodata);
        pb_loading = (ProgressBar) findViewById(R.id.pb_telsms_loading);

        //黑名单的业务对象
        dao = new BlackDao(getApplicationContext());
        //黑名单适配器
        adapter = new MyAdapter();
        lv_safenumber.setAdapter(adapter);
    }

    private class ItemView {
        TextView tv_phone, tv_mode;
        ImageView iv_delete;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ItemView itemView = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout
                        .item_telsmssafe_listview, null);
                itemView = new ItemView();

                itemView.iv_delete = (ImageView) convertView.findViewById(R.id
                        .iv_telsmssafe_imageview_item_delete);
                itemView.tv_phone = (TextView) convertView.findViewById(R.id
                        .tv_telsmssafe_listview_item_number);
                itemView.tv_mode = (TextView) convertView.findViewById(R.id
                        .tv_telsmssafe_listview_item_mode);

                //设置标记给convertView
                convertView.setTag(itemView);
            } else {
                itemView = (ItemView) convertView.getTag();
            }
            //初始化数据
            final BlackBean bean = datas.get(position);
            itemView.tv_phone.setText(bean.getPhone());
            //设置黑名单的模式
            switch (bean.getMode()) {
                case BlackTable.SMS:
                    itemView.tv_mode.setText("短信拦截");
                    break;
                case BlackTable.TEL:
                    itemView.tv_mode.setText("电话拦截");
                    break;
                case BlackTable.ALL:
                    itemView.tv_mode.setText("全部拦截");
                    break;
                default:
                    break;
            }

            //设置删除数据的事件
            itemView.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(TelSmsSafeActivity.this);
                    ab.setTitle("注意");
                    ab.setMessage("是否删除该数据?");
                    ab.setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //从数据库删除当前数据
                            dao.delete(bean.getPhone());

                            datas.remove(position);

                            adapter.notifyDataSetChanged();
                        }
                    });
                    ab.setNegativeButton("取消", null);
                    ab.show();
                }
            });
            return convertView;
        }
    }

    public void addBlackNumber(View v) {
        //showInputBlacknumberDialog();
        showPopupWindow();
    }

    private void showInputBlacknumberDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_addblacknumber, null);

        final EditText et_blackNumber = (EditText) view.findViewById(R.id
                .et_dialog_telsmssafe_blacknumber);
        final CheckBox cb_sms = (CheckBox) view.findViewById(R.id.cb_telsmssafe_smsmode);
        final CheckBox cb_phone = (CheckBox) view.findViewById(R.id.cb_telsmssafe_phonemode);

        Button bt_add = (Button) view.findViewById(R.id.bt_dialog_telsmssafe_add);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_telsmssafe_cancel);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加黑名单数据
                String phone = et_blackNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "黑名单号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!cb_phone.isChecked() && !cb_sms.isChecked()) {
                    Toast.makeText(getApplicationContext(), "至少选择一种拦截模式", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                int mode = 0;
                if (cb_phone.isChecked()) {
                    mode |= BlackTable.TEL;
                }
                if (cb_sms.isChecked()) {
                    mode |= BlackTable.SMS;
                }

                //在页面显示新添加的数据
                BlackBean bean = new BlackBean();
                bean.setMode(mode);
                bean.setPhone(phone);
                //添加数据到黑名单表中
                dao.add(bean);
                //如果新增的数据已经存在
                datas.remove(bean);
                //在页面展示
                datas.add(0, bean);

                //让listView显示第一条数据
                adapter = new MyAdapter();
                lv_safenumber.setAdapter(adapter);
                dialog.dismiss();

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ab.setView(view);

        dialog = ab.create();
        dialog.show();
    }

}
