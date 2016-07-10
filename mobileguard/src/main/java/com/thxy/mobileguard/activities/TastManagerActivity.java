package com.thxy.mobileguard.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.domain.TaskBean;
import com.thxy.mobileguard.engine.TaskManagerEngine;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 进程管理界面
 */
public class TastManagerActivity extends AppCompatActivity {
    protected static final int LOADING = 1;
    protected static final int FINISH = 2;
    private TextView tv_tasknumber;
    private TextView tv_meminfo;
    private ListView lv_taskdatas;
    private TextView tv_list_tag;
    private ProgressBar pb_loading;
    private MyAdapter adapter;
    private long availMem = 0;//可用内存大小
    private long totalMem = 0;//总内存大小
    private ActivityManager am;
    private InitDataClass initData;
    //系统进程的数据
    private List<TaskBean> sysTasks = new CopyOnWriteArrayList<TaskBean>();
    //用户进程的数据
    private List<TaskBean> userTasks = new CopyOnWriteArrayList<TaskBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据的封装对象
        initData = new InitDataClass();
        //初始化界面
        initView();
        //设置数据子线程
        //initData();
        //初始化事件
        initEvent();
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    private void initEvent() {
        //给listview加滚动事件
        lv_taskdatas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                //如果显示用户进程，标签要显示用户进程的tag
                if (firstVisibleItem <= userTasks.size()) {
                    //用户的tag
                    tv_list_tag.setText("用户进程(" + userTasks.size() + ")");

                } else {
                    //系统的tag
                    tv_list_tag.setText("系统进程(" + sysTasks.size() + ")");
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            setTitleMessage();
            if (!SpTools.getBoolean(getApplicationContext(), MyConstants.SHOWSYSTEM, false)) {
                return userTasks.size() + 1;
            }
            return sysTasks.size() + 1 + 1 + userTasks.size();
        }

        @Override
        public TaskBean getItem(int position) {
            TaskBean bean = null;
            if (position == 0 || position == userTasks.size() + 1) {
                return bean;
            }
            //判断position
            if (position <= userTasks.size()) {
                //用户软件的进程
                bean = userTasks.get(position - 1);
            } else {
                //系统软件的进程
                bean = sysTasks.get(position - userTasks.size() - 1 - 1);
            }
            return bean;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView tv_userTable = new TextView(getApplicationContext());
                tv_userTable.setText("个人软件(" + userTasks.size() + ")");
                tv_userTable.setTextColor(Color.WHITE);
                tv_userTable.setBackgroundColor(Color.GRAY);
                return tv_userTable;
            } else if (position == userTasks.size() + 1) {
                TextView tv_userTable = new TextView(getApplicationContext());
                tv_userTable.setText("系统软件(" + sysTasks.size() + ")");
                tv_userTable.setTextColor(Color.WHITE);
                tv_userTable.setBackgroundColor(Color.GRAY);
                return tv_userTable;
            } else {
                // 界面的缓存
                ViewHolder holder = new ViewHolder();

                // 判断是否存在缓存
                if (convertView != null
                        && convertView instanceof RelativeLayout) {
                    holder = (ViewHolder) convertView.getTag();
                } else {
                    convertView = View.inflate(getApplicationContext(),
                            R.layout.item_taskmanager_listview_item, null);

                    holder.iv_icon = (ImageView) convertView
                            .findViewById(R.id.iv_taskmanager_listview_item_icon);
                    holder.tv_title = (TextView) convertView
                            .findViewById(R.id.tv_taskmanager_listview_item_title);
                    holder.tv_memsize = (TextView) convertView
                            .findViewById(R.id.tv_taskmanager_listview_item_memsize);
                    holder.cb_checked = (CheckBox) convertView
                            .findViewById(R.id.tv_taskmanager_listview_item_checked);
                    // 绑定tag
                    convertView.setTag(holder);
                }
                final TaskBean bean = getItem(position);
                final ViewHolder mHolder = holder;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getPackName().equals(getPackageName())) {
                            //自己
                            mHolder.cb_checked.setChecked(false);
                        }
                        //复选框的反选操作
                        mHolder.cb_checked.setChecked(!mHolder.cb_checked.isChecked());
                    }
                });

                // 设置数据
                holder.iv_icon.setImageDrawable(bean.getIcon());// 设置图标

                //设置占用的内存大小
                holder.tv_memsize.setText(Formatter.formatFileSize(getApplicationContext(), bean
                        .getMemSize()));

                holder.tv_title.setText(bean.getName());// 设置名字


                //给复选框加事件，记录复选框的状态
                holder.cb_checked.setOnCheckedChangeListener(new CompoundButton
                        .OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!bean.getPackName().equals(getPackageName())) {
                            //记录复选框的状态
                            bean.setChecked(isChecked);
                        }
                    }
                });

                //从bean取出复选框的状态显示
                holder.cb_checked.setChecked(bean.isChecked());

                //判断是否是自己，如果是自己，让checkbox隐藏
                if (bean.getPackName().equals(getPackageName())) {
                    //显示的是自己
                    holder.cb_checked.setVisibility(View.GONE);
                } else {
                    holder.cb_checked.setVisibility(View.VISIBLE);
                }
                return convertView;
            }
        }
    }

    private class ViewHolder {
        ImageView iv_icon;//图标
        TextView tv_title; //名字
        TextView tv_memsize;//占用内存大小
        CheckBox cb_checked;//是否选择
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    pb_loading.setVisibility(View.VISIBLE);
                    lv_taskdatas.setVisibility(View.GONE);
                    tv_list_tag.setVisibility(View.GONE);
                    break;
                case FINISH:
                    pb_loading.setVisibility(View.GONE);
                    lv_taskdatas.setVisibility(View.VISIBLE);
                    tv_list_tag.setVisibility(View.VISIBLE);

                    setTitleMessage();
                    //数据的通知
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void setTitleMessage() {
        if (!SpTools.getBoolean(getApplicationContext(), MyConstants.SHOWSYSTEM, false)) {
            tv_tasknumber.setText("运行中的进程:" + userTasks.size());
        } else {
            tv_tasknumber.setText("运行中的进程:" + (sysTasks.size() + userTasks.size()));
        }
        //格式话显示可用内存
        String availMemFormatter = Formatter.formatFileSize(getApplicationContext(),
                availMem);
        //格式话显示可用内存
        String totalMemFormatter = Formatter.formatFileSize(getApplicationContext(),
                totalMem);
        //设置内存可用信息
        tv_meminfo.setText("可用/总内存：" + availMemFormatter + "/" + totalMemFormatter);
    }

    private class InitDataClass{
        public synchronized void initTheData(){
            //发送加载数据进度的消息
            handler.obtainMessage(LOADING).sendToTarget();

            //加载数据
            List<TaskBean> allTaskDatas = TaskManagerEngine.getAllRunningTaskInfos
                    (getApplicationContext());
            availMem = TaskManagerEngine.getAvailMemSize(getApplicationContext());
            totalMem = TaskManagerEngine.getTotalMemSize(getApplicationContext());
            sysTasks.clear();
            userTasks.clear();
            //分发数据
            for (TaskBean taskBean : allTaskDatas) {
                if (taskBean.isSystem()) {
                    //系统进程
                    sysTasks.add(taskBean);
                } else {
                    //用户进程
                    userTasks.add(taskBean);
                }
            }
            //加载数据完成
            handler.obtainMessage(FINISH).sendToTarget();
        }
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //通过对象来初始化数据
                initData.initTheData();
            }
        }.start();
    }

    private void initView() {
        setContentView(R.layout.activity_tastmanager);
        //显示进程个数
        tv_tasknumber = (TextView) findViewById(R.id.tv_taskmanager_tasknumber);
        //显示使用的内存信息
        tv_meminfo = (TextView) findViewById(R.id.tv_taskmanager_meminfo);
        //显示所有进程信息
        lv_taskdatas = (ListView) findViewById(R.id.lv_taskmanager_appdatas);
        //进程数据的标签
        tv_list_tag = (TextView) findViewById(R.id.tv_taskmanager_listview_lable);

        pb_loading = (ProgressBar) findViewById(R.id.pb_taskmanager_loading);

        adapter = new MyAdapter();
        lv_taskdatas.setAdapter(adapter);

        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 全选
     *
     * @param v
     */
    public void selectAll(View v) {
        //遍历所有app
        for (TaskBean bean : userTasks) {
            //如果是自己，不勾选
            if (bean.getPackName().equals(getPackageName())) {
                bean.setChecked(false);
                continue;
            }
            bean.setChecked(true);
        }
        for (TaskBean bean : sysTasks) {
            bean.setChecked(true);
        }

        //界面要看到的数据
        adapter.notifyDataSetChanged();
    }

    /**
     * 反选
     *
     * @param v
     */
    public void invertSelect(View v) {
        //遍历所有app
        for (TaskBean bean : userTasks) {
            //如果是自己，不勾选
            if (bean.getPackName().equals(getPackageName())) {
                bean.setChecked(false);
                continue;
            }
            bean.setChecked(!bean.isChecked());
        }
        for (TaskBean bean : sysTasks) {
            bean.setChecked(!bean.isChecked());
        }

        //界面要看到的数据
        adapter.notifyDataSetChanged();
    }

    /**
     * 清理选中的进程
     *
     * @param v
     */
    public void clearTask(View v) {
        //有些进程删除不掉，为了增强用户体验，用户选择的进程都要删掉
        long clearMem = 0;//记录内存数量
        int clearNum = 0;//记录清理多少进程
        //循环用户进程
        for (TaskBean bean : userTasks) {
            if (bean.isChecked()) {

                //清理的个数累计
                clearNum++;
                //清理内存数量累计 byte
                clearMem += bean.getMemSize();
                //清理
                am.killBackgroundProcesses(bean.getPackName());
                //从容器中删除该数据
                userTasks.remove(bean);
            }
        }
        //循环系统进程，调用迭代器，不能进行增删改查操作，不然会发生并发异常,把ArrayList换成CopyOnWriteArrayList
        for (TaskBean bean : sysTasks) {
            if (bean.isChecked()) {

                //清理的个数累计
                clearNum++;
                //清理内存数量累计 byte
                clearMem += bean.getMemSize();
                //清理
                am.killBackgroundProcesses(bean.getPackName());
                //从容器中删除该数据
                sysTasks.remove(bean);
            }
        }

        Toast.makeText(getApplicationContext(), "清理了" + clearNum + "个进程，释放了" + Formatter
                .formatFileSize(getApplicationContext(), clearMem), Toast.LENGTH_SHORT).show();
        //增加可用内存
        availMem += clearMem;
        //更新标题信息
        setTitleMessage();
        adapter.notifyDataSetChanged();
    }

    /**
     * 打开进程的设置界面
     *
     * @param v
     */
    public void setting(View v) {
        Intent setting = new Intent(TastManagerActivity.this, TaskManagerSettingActivity.class);
        startActivity(setting);
    }

}
