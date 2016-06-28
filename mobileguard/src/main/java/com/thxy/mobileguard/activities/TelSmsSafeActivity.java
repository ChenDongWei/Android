package com.thxy.mobileguard.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private List<BlackBean> datas = new ArrayList<BlackBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        //初始化数据
        initData();
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
                    if (datas.size() != 0) {
                        lv_safenumber.setVisibility(View.VISIBLE);
                        pb_loading.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.GONE);
                        //更新数据
                        adapter.notifyDataSetChanged();
                    } else {
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
                //取数据
                SystemClock.sleep(2000);
                datas = dao.getAllDatas();

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
        public View getView(int position, View convertView, ViewGroup parent) {
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
            BlackBean bean = datas.get(position);
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
            return convertView;
        }
    }

}
