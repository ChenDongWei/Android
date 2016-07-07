package com.thxy.mobileguard.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

/**
 * 该服务主要完成锁屏的注册广播和反注册
 */
public class ClearTaskService extends Service {
    private ActivityManager am;
    private ClearTaskReceiver receiver;

    public ClearTaskService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    /**
     * 清理进程的广播
     */
    private class ClearTaskReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<RunningAppProcessInfo> runningAppProcesses = am
                    .getRunningAppProcesses();
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                am.killBackgroundProcesses(runningAppProcessInfo.processName);
            }
            System.out.println("锁屏清理进程");
        }
    }

    @Override
    public void onCreate() {
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        receiver = new ClearTaskReceiver();
        //注册广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //取消注册
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
