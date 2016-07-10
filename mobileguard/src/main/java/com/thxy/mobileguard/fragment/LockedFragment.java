package com.thxy.mobileguard.fragment;

import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.thxy.mobileguard.R;

/**
 * 已加锁的Fragment
 */
public class LockedFragment extends BaseLockOrUnlockFragment {
    @Override
    public boolean isMyData(String packName) {
        return allLockedPacks.contains(packName);
    }

    @Override
    protected void setLockNumberTextView() {
        tv_lab.setText("已加锁软件(" + (unlockedSystemDatas.size() + unlockedUserDatas.size()) + ")");
    }

    @Override
    public void setImageViewEventAndBg(ImageView iv_lock, final View convertView, final String packName) {
        //初始化图片选择器
        iv_lock.setImageResource(R.drawable.iv_unlock_selector);

        //写事件
        iv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把数据放到数据库
                dao.remove(packName);
                // 2,动画的实现
                TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                        Animation.RELATIVE_TO_SELF, -1,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                ta.setDuration(300);//1秒动画

                convertView.startAnimation(ta);

                new Thread(){
                    public void run() {
                        SystemClock.sleep(300);
                        // 3,更新自己的数据
                        initData();
                    };
                }.start();
            }
        });
    }
}
