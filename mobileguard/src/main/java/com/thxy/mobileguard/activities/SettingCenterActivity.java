package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;

import com.thxy.mobileguard.R;

public class SettingCenterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_settingcenter);
    }

}
