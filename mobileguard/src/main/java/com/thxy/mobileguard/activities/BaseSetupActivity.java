package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.thxy.mobileguard.R;

public abstract class BaseSetupActivity extends Activity {
    private GestureDetector gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        //初始化手势识别
        initGesture();
        //初始化数据
        initData();
        //初始化组件的事件
        initEvent();
    }

    public void initData() {

    }

    public void initEvent() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //绑定onTouch事件
        gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void initGesture() {
        //绑定onTouch事件才能让手势识别器生效
        gd = new GestureDetector(new GestureDetector.OnGestureListener() {
            /**
             * 覆盖此方法完成手势的切换效果
             * @param e1：按下的点
             * @param e2：松开屏幕的点
             * @param velocityX：X轴方向的速度
             * @param velocityY：Y轴方向的速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float
                    velocityY) {
                //判断X轴方向的速度是否满足横向滑动的条件 单位px/s
                if (velocityX > 200){
                    //X轴滑动的间距
                    float dx = e2.getX() - e1.getX();
                    if (Math.abs(dx) < 100){
                        return false;
                    }
                    if (dx < 0){
                        //从右往左
                        next(null);
                    }else {
                        //从左往右
                        prev(null);
                    }
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float
                    distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

        });
    }


    public abstract void initView();

    /**
     * 下一个页面的事件处理
     *
     * @param v
     */
    public void next(View v) {
        //1.完成页面的切换
        nextActivity();
        //2.完成动画的效果
        nextAnimation();
    }

    public void startActivity(Class type) {
        Intent next = new Intent(this, type);
        startActivity(next);
        finish();
    }

    public abstract void nextActivity();

    public abstract void prevActivity();

    /**
     * 下一个页面显示的动画
     */
    private void nextAnimation() {
        overridePendingTransition(R.anim.next_in, R.anim.next_out);
    }

    /**
     * 上一个页面的事件处理
     *
     * @param v
     */
    public void prev(View v) {
        //1.完成页面的切换
        prevActivity();
        //2.完成动画的效果
        prevAnimation();
    }

    private void prevAnimation() {
        overridePendingTransition(R.anim.prev_in, R.anim.prev_out);
    }
}
