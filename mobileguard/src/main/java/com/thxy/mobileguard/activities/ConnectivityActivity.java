package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.domain.AppBean;
import com.thxy.mobileguard.engine.AppManagerEngine;
import com.thxy.mobileguard.engine.ConnectivityEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * 流量统计的界面
 */
public class ConnectivityActivity extends Activity {
    private ListView lv_datas;
    private MyAdapter adapter;
    private List<AppBean> datas = new ArrayList<AppBean>();
    private ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, ViewGroup
                parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout
                        .item_connectivity_listview_item, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id
                        .iv_connectivity_listview_item_icon);
                holder.tv_title = (TextView) convertView.findViewById(R.id
                        .tv_connectivity_listview_item_title);
                holder.iv_seedetail = (ImageView) convertView.findViewById(R.id
                        .iv_connectivity_listview_seedetail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //赋值
            final AppBean bean = datas.get(position);
            //给组件赋值
            holder.iv_icon.setImageDrawable(bean.getIcon());
            holder.tv_title.setText(bean.getAppName());
            //事件
            holder.iv_seedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //接收的流量
                    String rev = ConnectivityEngine.getReceive(bean.getUid(),
                            getApplicationContext());
                    String snd = ConnectivityEngine.getSend(bean.getUid(), getApplicationContext());

                    showConnectivityMess(cm.getActiveNetworkInfo().getTypeName() + "\n接受的流量:" + rev + "\n发送的流量:" + snd);
                }
            });
            return convertView;
        }
    }

    /**
     * @param mess 显示流量信息的对话框
     */
    private void showConnectivityMess(String mess) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("流量信息").setMessage(mess).setPositiveButton("确定", null);
        ab.show();
    }

    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        ImageView iv_seedetail;
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                datas = AppManagerEngine.getAllApks(getApplicationContext());
                for (int i = 0; i < datas.size(); i++){
                    AppBean appBean = datas.get(i);
                    if (ConnectivityEngine.getReceive(appBean.getUid(), getApplicationContext()) == null){
                        datas.remove(i);
                        i--;
                    }
                }
                handler.obtainMessage().sendToTarget();
            }
        }.start();
    }

    private void initView() {
        setContentView(R.layout.activity_connectivity);
        lv_datas = (ListView) findViewById(R.id.lv_connectivity_datas);

        adapter = new MyAdapter();
        lv_datas.setAdapter(adapter);

        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

}
