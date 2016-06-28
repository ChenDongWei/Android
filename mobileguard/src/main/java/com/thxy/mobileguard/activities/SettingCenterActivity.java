package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;
import com.thxy.mobileguard.view.SettingCenterItemView;

public class SettingCenterActivity extends Activity {
    private SettingCenterItemView sciv_autoupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        //初始化组件的事件
        initEvent();
        //初始化组件的数据
        initData();
    }

    private void initData() {
        sciv_autoupdate.setChecked(SpTools.getBoolean(getApplicationContext(), MyConstants
                .AUTOUPDATE, false));
    }

    private void initEvent() {
        sciv_autoupdate.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sciv_autoupdate.setChecked(!sciv_autoupdate.isChecked());
                //记录复选框的状态
                SpTools.putBoolean(getApplicationContext(), MyConstants.AUTOUPDATE,
                        sciv_autoupdate.isChecked());
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_settingcenter);
        sciv_autoupdate = (SettingCenterItemView) findViewById(R.id
                .sciv_setting_center_autoupdate);

    }

}
