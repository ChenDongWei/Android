package com.thxy.mobileguard.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.engine.TaskManagerEngine;
import com.thxy.mobileguard.receiver.ExampleAppWidgetProvider;

import java.util.Timer;
import java.util.TimerTask;

public class AppWidgetService extends Service {
    private Timer timer;
    private TimerTask task;
    private AppWidgetManager awm;

    public AppWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        //AppWidget管理器
        awm = AppWidgetManager.getInstance(getApplicationContext());
        timer = new Timer();
        //定时器触发的任务
        task = new TimerTask() {
            @Override
            public void run() {
                //数据更新
                ComponentName provider = new ComponentName(getApplication(),
                        ExampleAppWidgetProvider.class);
                //远程界面
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                //运行中的软件个数
                int runningNumber = TaskManagerEngine.getAllRunningTaskInfos
                        (getApplicationContext()).size();
                //可用内存
                long availMem = TaskManagerEngine.getAvailMemSize(getApplicationContext());
                String availMemStr = Formatter.formatFileSize(getApplicationContext(), availMem);

                //给remoteViews的子组件赋值
                views.setTextViewText(R.id.process_count, "正在运行软件:" + runningNumber);
                views.setTextViewText(R.id.process_memory, "可用内存:" + availMemStr);

                //给widget按钮加点击事件
                Intent intent = new Intent("com.thxy.widget.cleartask");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);

                awm.updateAppWidget(provider, views);
            }
        };
        //每隔10秒进行监控
        timer.schedule(task, 0, 1000 * 5);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer = null;
        task = null;
        super.onDestroy();
    }
}
