package com.thxy.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class LostFindService extends Service {
    private SmsReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 短信的广播接收者
     */
    private class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //实现短信拦截功能
            Bundle extras = intent.getExtras();
            Object datas[] = (Object[]) extras.get("pdus");

            for (Object data : datas) {
                SmsMessage sm = SmsMessage.createFromPdu((byte[]) data);
                //获取短信内容
                String mess = sm.getMessageBody();
                if (mess.equals("#*gps*#")){
                    //获取定位信息
                    Intent service = new Intent(context, LocationService.class);
                    startService(service);
                    //终止广播
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        //注册短信的监听广播
        receiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephone.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        //注册短信监听
        registerReceiver(receiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //取消注册短信的监听广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
