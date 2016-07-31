package com.cdw.archedmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView mIvHome;
    private RelativeLayout mRlLevel1;//一级菜单的容器
    private RelativeLayout mRlLevel2;//二级菜单的容器
    private RelativeLayout mRlLevel3;//三级菜单的容器
    private ImageView mIvMenu;
    private boolean isLevel1Display = true;//一级菜单是否显示的标记
    private boolean isLevel2Display = true;//二级菜单是否显示的标记
    private boolean isLevel3Display = true;//三级菜单是否显示的标记

    private int mAnimationCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mRlLevel1 = (RelativeLayout) findViewById(R.id.rl_level1);
        mRlLevel2 = (RelativeLayout) findViewById(R.id.rl_level2);
        mRlLevel3 = (RelativeLayout) findViewById(R.id.rl_level3);

        mIvHome = (ImageView) findViewById(R.id.level1_iv_home);
        mIvMenu = (ImageView) findViewById(R.id.leve2_iv_menu);

        //设置点击事件
        mIvHome.setOnClickListener(this);
        mIvMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mIvHome) {
            clickLevel1Home();
        }else if (v == mIvMenu){
            clickLevel2Menu();
        }
    }

    private void clickLevel2Menu() {
        if (mAnimationCount > 0){
            return;
        }

        if (isLevel3Display){
            hideLevel(mRlLevel3, 0);
            isLevel3Display = false;
            return;
        }

        if (!isLevel3Display){
            showLevel(mRlLevel3, 0);
            isLevel3Display = true;
            return;
        }
    }

    private void clickLevel1Home() {
        if (mAnimationCount > 0){
            return;
        }

        //点击home按钮，二级菜单和三级菜单都显示的情况下，二级菜单和三级菜单隐藏
        if (isLevel2Display && isLevel3Display) {
            //隐藏二级菜单
            hideLevel(mRlLevel2, 100);

            //隐藏三级菜单
            hideLevel(mRlLevel3, 0);
            //状态改变
            isLevel2Display = false;
            isLevel3Display = false;
            return;
        }

        //点击home按钮，二级菜单和三级菜单都不显示的情况下，显示二级菜单
        if (!isLevel2Display && !isLevel3Display) {
            showLevel(mRlLevel2, 0);
            //状态改变
            isLevel2Display = true;
            return;
        }

        //点击home按钮，二级菜单显示三级菜单不显示的情况下，隐藏二级菜单
        if (isLevel2Display && !isLevel3Display) {
            hideLevel(mRlLevel2, 0);
            isLevel2Display = false;
            return;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            //点击menu按钮
            clickHardwareMenu();
            //消费menu点击事件
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    private void clickHardwareMenu() {
        //如果三个菜单都可见，隐藏三个菜单
        if (isLevel1Display && isLevel2Display && isLevel3Display){
            hideLevel(mRlLevel3, 0);
            hideLevel(mRlLevel2, 100);
            hideLevel(mRlLevel1, 200);

            isLevel1Display = false;
            isLevel2Display = false;
            isLevel3Display = false;
            return;
        }

        //如果三个菜单不可见，显示三个菜单
        if (!isLevel1Display && !isLevel2Display && !isLevel3Display){
            showLevel(mRlLevel1, 0);
            showLevel(mRlLevel2, 100);
            showLevel(mRlLevel3, 200);

            isLevel1Display = true;
            isLevel2Display = true;
            isLevel3Display = true;
            return;
        }

        //如果三级菜单不可见，隐藏一二级菜单
        if (!isLevel3Display && isLevel1Display && isLevel2Display){
            hideLevel(mRlLevel1, 0);
            hideLevel(mRlLevel2, 100);

            isLevel1Display = false;
            isLevel2Display = false;
            return;
        }

        //如果二三级菜单不可见，隐藏一级菜单
        if (!isLevel3Display && isLevel1Display && !isLevel2Display){
            hideLevel(mRlLevel1, 0);

            isLevel1Display = false;
            return;
        }
    }

    /**
     * 隐藏菜单
     *
     * @param container
     */
    private void hideLevel(RelativeLayout container, long startOffset) {
        //container.setVisibility(View.GONE);

        int count = container.getChildCount();
        for (int i = 0; i < count; i++){
            container.getChildAt(i).setEnabled(false);
        }
        RotateAnimation animation = new RotateAnimation(0f, -180f, RotateAnimation
                .RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setStartOffset(startOffset);//设置动画延迟

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mAnimationCount++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimationCount--;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        container.startAnimation(animation);
    }

    /**
     * 显示菜单
     * @param container
     * @param startOffset
     */
    private void showLevel(RelativeLayout container, long startOffset) {
        //container.setVisibility(View.VISIBLE);

        int count = container.getChildCount();
        for (int i = 0; i < count; i++){
            container.getChildAt(i).setEnabled(true);
        }
        RotateAnimation animation = new RotateAnimation(-180f, 0f, RotateAnimation
                .RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(600);
        animation.setFillAfter(true);
        animation.setStartOffset(startOffset);//设置动画延迟

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mAnimationCount++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimationCount--;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        container.startAnimation(animation);
    }
}
