package com.thxy.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.thxy.shopping.R;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rl_root;// 界面的根布局组建
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化界面
        initView();
        // 初始化动画
        initAnimation();

        handler.sendEmptyMessageDelayed(0x111,4000);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        //进入主界面后结束自己
        finish();
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
    }

    private void initAnimation() {
        // Alpha动画
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        // 动画播放的时间
        aa.setDuration(3000);
        // 界面停留在动画结束的页面
        aa.setFillAfter(true);

        // 旋转动画
        RotateAnimation ra = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(3000);
        ra.setFillAfter(true);

        // 比例动画
        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        sa.setDuration(3000);
        sa.setFillAfter(true);

        // 动画集
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(aa);
        as.addAnimation(ra);
        as.addAnimation(sa);
        // 显示动画
        rl_root.startAnimation(as);
    }

}
