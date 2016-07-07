package com.thxy.mobileguard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.service.ClearTaskService;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.ServiceUtils;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 进程管理设置界面
 */
public class TaskManagerSettingActivity extends AppCompatActivity {
    private CheckBox cb_lockscreenClear;
    private CheckBox cb_showsystemapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initEvent();

        initData();

    }

    private void initData() {
        //通过标记来初始化是否显示系统进程的标记
        cb_showsystemapp.setChecked(SpTools.getBoolean(this, MyConstants.SHOWSYSTEM, false));
        //通过清理进程的服务判断是否开启
        cb_lockscreenClear.setChecked(ServiceUtils.isServiceRunning(getApplicationContext(), "com" +
                ".thxy.mobileguard.service.ClearTaskService"));
    }

    private void initEvent() {
        cb_showsystemapp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //保存是否显示系统进程的标记
                SpTools.putBoolean(getApplicationContext(), MyConstants.SHOWSYSTEM, isChecked);
            }
        });

        //设置锁屏清理进程
        cb_lockscreenClear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //锁屏的广播
                if (isChecked) {
                    Intent service = new Intent(TaskManagerSettingActivity.this, ClearTaskService
                            .class);
                    startService(service);
                } else {
                    Intent service = new Intent(TaskManagerSettingActivity.this, ClearTaskService
                            .class);
                    stopService(service);
                }
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_taskmanagersetting);
        cb_lockscreenClear = (CheckBox) findViewById(R.id
                .cb_taskmanager_settingcenter_lockscreen_clear);
        cb_showsystemapp = (CheckBox) findViewById(R.id
                .cb_taskmanager_settingcenter_lockscreen_showsystemapp);
    }

}
