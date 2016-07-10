package com.thxy.mobileguard.activities;

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.dao.LockedDao;
import com.thxy.mobileguard.domain.LockedTable;
import com.thxy.mobileguard.fragment.LockedFragment;
import com.thxy.mobileguard.fragment.UnlockedFragment;

import java.util.List;

public class LockActivity extends FragmentActivity {
    private TextView tv_locked;//加锁的textView
    private TextView tv_unlock;//未加锁的textView
    private FrameLayout fl_content;//替换成fragment的组件
    private FragmentManager fm;//fragment的管理器
    private LockedFragment lockedFragment;//已加锁的Fragment
    private UnlockedFragment unlockedFragment;//未加锁的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();

        initEvent();
    }

    private void initEvent() {
        //注册内容观察者
        ContentObserver observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                new Thread(){
                    @Override
                    public void run() {
                        LockedDao dao = new LockedDao(getApplicationContext());
                        //读取dao层读取数据
                        List<String> allLockedDatas = dao.getAllLockedDatas();
                        lockedFragment.setAllLockedPacks(allLockedDatas);
                        unlockedFragment.setAllLockedPacks(allLockedDatas);
                    }
                }.start();
                super.onChange(selfChange);
            }
        };
        getContentResolver().registerContentObserver(LockedTable.uri, true, observer);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fm.beginTransaction();

                if (v.getId() == R.id.tv_lockedactivity_locked) {
                    transaction.replace(R.id.fl_lockedactivity_content, lockedFragment);
                    tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);
                    tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                } else {
                    transaction.replace(R.id.fl_lockedactivity_content, unlockedFragment);
                    tv_locked.setBackgroundResource(R.drawable.tab_right_default);
                    tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                }
                transaction.commit();
            }
        };
        tv_locked.setOnClickListener(listener);
        tv_unlock.setOnClickListener(listener);
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                LockedDao dao = new LockedDao(getApplicationContext());
                //读取dao层读取数据
                List<String> allLockedDatas = dao.getAllLockedDatas();
                lockedFragment.setAllLockedPacks(allLockedDatas);
                unlockedFragment.setAllLockedPacks(allLockedDatas);
            }
        }.start();

        fm = getSupportFragmentManager();
        //获取事物
        FragmentTransaction transaction = fm.beginTransaction();
        //默认显示未加锁界面，fragment替换framelayout
        transaction.replace(R.id.fl_lockedactivity_content, unlockedFragment);
        //提交事物
        transaction.commit();
    }

    private void initView() {
        setContentView(R.layout.activity_lock);
        tv_locked = (TextView) findViewById(R.id.tv_lockedactivity_locked);
        tv_unlock = (TextView) findViewById(R.id.tv_lockedactivity_unlock);
        fl_content = (FrameLayout) findViewById(R.id.fl_lockedactivity_content);

        lockedFragment = new LockedFragment();
        unlockedFragment = new UnlockedFragment();

    }

}
