package com.thxy.mobileguard.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.thxy.mobileguard.R;

public class LostFindService extends Service {
    private SmsReceiver receiver;
    private boolean isPlay;

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
                if (mess.equals("#*gps*#")) {
                    //获取定位信息
                    Intent service = new Intent(context, LocationService.class);
                    startService(service);
                    //终止广播
                    abortBroadcast();
                } else if (mess.equals("#*lockscreen*#")) {
                    //获取设备管理器
                    DevicePolicyManager dpm = (DevicePolicyManager) getSystemService
                            (DEVICE_POLICY_SERVICE);
                    //设置密码
                    dpm.resetPassword("1", 0);
                    //一键锁屏
                    dpm.lockNow();
                    abortBroadcast();
                }else if (mess.equals("#*wipedata*#")){
                    //远程清除数据
                    DevicePolicyManager dpm = (DevicePolicyManager) getSystemService
                            (DEVICE_POLICY_SERVICE);
                    dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    abortBroadcast();
                }else if (mess.equals("#*music*#")){
                    abortBroadcast();
                    if (isPlay){
                        return;
                    }
                    //播放音乐
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.qqqg);
                    //设置左右声道声音为最大值
                    mp.setVolume(1, 1);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            //音乐播放完毕,触发此方法
                            isPlay = false;
                        }
                    });
                    isPlay = true;
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
