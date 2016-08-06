package com.cdw.smartbeijing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.utils.MyConstants;
import com.cdw.smartbeijing.utils.PrefUtils;

/**
 * 智慧北京的splash界面
 */
public class SplashActivity extends Activity {
    private RelativeLayout rlRoot;
    private AnimationSet as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        startAnimation();
        initEvent();
    }

    private void initEvent() {
        as.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束时，如果第一次进入，则进入新手引导，否则进入主界面
                boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this,
                        MyConstants.ISSETUP, true);
                Intent intent;
                if (isFirstEnter) {
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startAnimation() {
        //旋转
        RotateAnimation animRotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //动画时间
        animRotate.setDuration(2000);
        //播放完动画保持最后状态
        animRotate.setFillAfter(true);

        //缩放
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animScale.setDuration(2000);
        animScale.setFillAfter(true);

        //渐变
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);

        //动画集合
        as = new AnimationSet(true);
        as.addAnimation(animRotate);
        as.addAnimation(animScale);
        as.addAnimation(animAlpha);

        rlRoot.startAnimation(as);
    }

    private void initView() {
        setContentView(R.layout.activity_splash);

        rlRoot = (RelativeLayout) findViewById(R.id.rl_splash_root);
    }
}
