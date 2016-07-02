package com.thxy.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.thxy.mobileguard.dao.BlackDao;
import com.thxy.mobileguard.domain.BlackTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 监听电话和短信
 */
public class TelSmsBlackService extends Service {
    private SmsReceiver receiver;
    private BlackDao dao;
    private PhoneStateListener listener;
    private TelephonyManager tm;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    /**
     * 短信监听的广播
     */
    private class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] datas = (Object[]) intent.getExtras().get("pdus");
            for (Object sms : datas) {
                SmsMessage sm = SmsMessage.createFromPdu((byte[]) sms);
                String address = sm.getOriginatingAddress();

                int mode = dao.getMode(address);
                if ((mode & BlackTable.SMS) != 0) {
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        dao = new BlackDao(getApplicationContext());
        //注册短信的监听
        receiver = new SmsReceiver();
        //短信广播的意图
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        //设置拦截模式为最高
        filter.setPriority(Integer.MAX_VALUE);
        //注册短信广播
        registerReceiver(receiver, filter);

        //注册电话的监听
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //监听电话的状态
        listener = new PhoneStateListener() {
            /**
             * 监听电话的状态
             * @param state
             * @param incomingNumber
             */
            @Override
            public void onCallStateChanged(int state, final String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        //挂断(空闲)状态
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        //响铃状态
                        int mode = dao.getMode(incomingNumber);
                        if ((mode & BlackTable.TEL) != 0) {
                            //挂断电话,先注册内容观察者
                            getContentResolver().registerContentObserver(Uri.parse
                                    ("content://call_log/calls"), true, new ContentObserver(new
                                    Handler()) {
                                @Override
                                public void onChange(boolean selfChange) {
                                    //删除电话日志
                                    deleteCalllog(incomingNumber);
                                    //取消内容观察者注册
                                    getContentResolver().unregisterContentObserver(this);
                                    super.onChange(selfChange);
                                }
                            });
                            endCall();


                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //通话状态
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        //注册电话监听
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    protected void deleteCalllog(String incomingNumber) {
        Uri uri = Uri.parse("content://call_log/calls");
        //删除日志
        getContentResolver().delete(uri, "number=?", new String[]{incomingNumber});
    }


    protected void endCall() {
        //反射调用实现ServiceManager.getService();
        try {
            Class clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
            iTelephony.endCall();//挂断电话
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        //取消电话监听
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        //取消短信监听
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
