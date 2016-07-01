package com.thxy.mobileguard.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.domain.ContantBean;
import com.thxy.mobileguard.utils.MyConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示所有好友信息的界面
 */
public abstract class BaseFriendsCallSmsActivity extends ListActivity {

    protected static final int LOADING = 1;
    protected static final int FINISH = 2;

    private MyAdapter adapter;
    private ListView lv_datas;

    //获取联系人的数据
    private List<ContantBean> datas = new ArrayList<ContantBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用listview组件显示好友信息
        lv_datas = getListView();
        adapter = new MyAdapter();
        //设置适配器，读取适配器数据显示
        lv_datas.setAdapter(adapter);
        //初始化数据
        initData();
        //初始化事件
        initEvent();
    }

    private void initEvent() {
        lv_datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //处理条目点击事件
                //获取当前条目的数据
                ContantBean contantBean = datas.get(position);
                //获取号码
                String phone = contantBean.getPhone();
                Intent datas = new Intent();
                datas.putExtra(MyConstants.SAFENUMBER, phone);
                setResult(1, datas);
                finish();
            }
        });
    }

    private Handler handler = new Handler() {
        private ProgressDialog pd;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    //加载数据的对话框
                    pd = new ProgressDialog(BaseFriendsCallSmsActivity.this);
                    pd.setTitle("注意");
                    pd.setMessage("正在加载数据");
                    pd.show();
                    break;
                case FINISH:
                    if (pd != null){
                        pd.dismiss();
                        pd = null;
                    }

                    //把数据显示在listview中
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_friend_listview, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_friends_item_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_friends_item_phone);
            ContantBean bean = datas.get(position);
            tv_name.setText(bean.getName());
            tv_phone.setText(bean.getPhone());
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
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
                //显示获取数据的进度条
                Message msg = Message.obtain();
                msg.what = LOADING;
                handler.sendMessage(msg);
                //为了展示进度条，休眠2秒
                //SystemClock.sleep(2000);
                //获取数据
                //datas = ReadContantsEngine.readContants(getApplicationContext());
                datas = getDatas();
                msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public abstract List<ContantBean> getDatas();

}
