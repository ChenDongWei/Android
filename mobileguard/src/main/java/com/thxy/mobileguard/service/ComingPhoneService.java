package com.thxy.mobileguard.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.engine.PhoneLocationEngine;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 该服务用于监控来电，并显示归属地
 */
public class ComingPhoneService extends Service {
    private TelephonyManager tm;
    private PhoneStateListener listener;
    private WindowManager.LayoutParams params;
    private WindowManager wm;
    private View view;
    int[] bgStyles = new int[]{
            R.drawable.call_locate_blue,
            R.drawable.call_locate_gray,
            R.drawable.call_locate_green,
            R.drawable.call_locate_orange,
            R.drawable.call_locate_white
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        //初始化窗体管理器
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        //初始化吐司的参数
        initToastParams();
        //获取电话管理器
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //电话状态监听器
        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        //关闭吐司
                        closeLocationToast();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //关闭吐司
                        closeLocationToast();
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        //弹出吐司
                        showLocationToast(incomingNumber);
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

    private void initToastParams() {
        // 土司的初始化参数
        params = new WindowManager.LayoutParams();

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        //对齐方式左上角
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        /* | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE */
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //初始化土司的位置
        params.x = (int) Float.parseFloat(SpTools.getString(getApplicationContext(), MyConstants
                .TOASTX, "0"));
        params.y = (int) Float.parseFloat(SpTools.getString(getApplicationContext(), MyConstants
                .TOASTY, "0"));
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// 土司天生不相应时间,改变类型
        params.setTitle("Toast");
    }

    protected void closeLocationToast() {
        //初始先执行一次
        if (view != null) {
            wm.removeView(view);
            view = null;
        }

    }

    protected void showLocationToast(String incomingNumber) {
        //显示自定义吐司
        view = View.inflate(getApplicationContext(), R.layout.sys_toast, null);
        int index = Integer.parseInt(SpTools.getString(getApplicationContext(), MyConstants
                .STYLEBGINDEX, "0"));
        view.setBackgroundResource(bgStyles[index]);
        TextView tv_location = (TextView) view.findViewById(R.id.tv_toast_location);
        tv_location.setText(PhoneLocationEngine.locationQuery(incomingNumber,
                getApplicationContext()));

        //初始化view的触摸事件
        view.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //拖动吐司
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //记录x，y的坐标
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //新的x，y坐标变化值
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();

                        float dx = moveX - startX;
                        float dy = moveY - startY;
                        //改变吐司的坐标
                        params.x += dx;
                        params.y += dy;
                        //重新获取新的x，y坐标
                        startX = moveX;
                        startY = moveY;
                        //更新吐司的位置
                        wm.updateViewLayout(view, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        //把x，y坐标保存到sp中
                        if (params.x < 0) {
                            params.x = 0;
                        } else if (params.x + view.getWidth() > wm.getDefaultDisplay().getWidth()) {
                            params.x = wm.getDefaultDisplay().getWidth() - view.getWidth();
                        }

                        if (params.y < 0) {
                            params.y = 0;
                        } else if (params.y + view.getHeight() > wm.getDefaultDisplay().getHeight
                                ()) {
                            params.y = wm.getDefaultDisplay().getHeight() - view.getHeight();
                        }
                        SpTools.putString(getApplicationContext(), MyConstants.TOASTX, params.x +
                                "");
                        SpTools.putString(getApplicationContext(), MyConstants.TOASTY, params.y +
                                "");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        wm.addView(view, params);
    }

    @Override
    public void onDestroy() {
        //取消电话状态监听
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
}
