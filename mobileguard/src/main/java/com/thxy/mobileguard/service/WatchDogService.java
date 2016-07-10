package com.thxy.mobileguard.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.thxy.mobileguard.dao.LockedDao;

/**
 * 看门狗服务(未实现)
 */
public class WatchDogService extends Service {
    private boolean isWatch;
    private ActivityManager am;
    private LockedDao dao;
    private WatchDogReceiver receiver;
    private String shuRen = "";

    public WatchDogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    /**
     * 看门狗的广播接收者
     */
    private class WatchDogReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            shuRen = intent.getStringExtra("packname");
        }
    }

    @Override
    public void onCreate() {
        /*receiver = new WatchDogReceiver();
        IntentFilter filter = new IntentFilter("com.thxy.watchdog");
        registerReceiver(receiver, filter);
        dao = new LockedDao(getApplicationContext());
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        watchDog();
        super.onCreate();*/
    }

    private void watchDog() {
        new Thread() {
            @Override
            public void run() {
                isWatch = true;
                while (isWatch) {
                    //获取最新的任务栈
                    /*List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                    RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                    //任务栈顶部的activity
                    String packageName = runningTaskInfo.topActivity.getPackageName();

                    //判断
                    if (dao.isLocked(packageName)) {
                        if (packageName.equals(shuRen)){
                            //什么也不拦截
                        }
                        //输入口令
                        Intent enterpass = new Intent(getApplicationContext(),
                                WatchDogEnterPassActivity.class);
                        enterpass.putExtra("packname", packageName);
                        enterpass.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(enterpass);
                    } else {

                    }
                    //每隔50毫秒监控一次任务栈
                    SystemClock.sleep(50);*/

                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        //unregisterReceiver(receiver);
        isWatch = false;
        super.onDestroy();
    }
}
