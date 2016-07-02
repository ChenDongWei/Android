package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.service.ComingPhoneService;
import com.thxy.mobileguard.service.TelSmsBlackService;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.ServiceUtils;
import com.thxy.mobileguard.utils.SpTools;
import com.thxy.mobileguard.view.SettingCenterItemView;

public class SettingCenterActivity extends Activity {
    private SettingCenterItemView sciv_autoupdate;
    private SettingCenterItemView sciv_blackservice;
    private SettingCenterItemView sciv_phoneLocationService;

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
        //判断来电归属地服务来设置复选框的初始值
        sciv_phoneLocationService.setChecked(ServiceUtils.isServiceRunning(getApplicationContext
                (), "com.thxy.mobileguard.service.ComingPhoneService"));
        //判断黑名单服务来设置复选框的初始值
        sciv_blackservice.setChecked(ServiceUtils.isServiceRunning(getApplicationContext(), "com" +
                ".thxy.mobileguard.service.TelSmsBlackService"));
        //自动更新的初始值
        sciv_autoupdate.setChecked(SpTools.getBoolean(getApplicationContext(), MyConstants
                .AUTOUPDATE, false));
    }

    private void initEvent() {
        //来电归属地服务启动和关闭
        sciv_phoneLocationService.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断来电归属地服务是否运行
                if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.thxy.mobileguard" +
                        ".service.ComingPhoneService")) {
                    Intent comingPhoneService = new Intent(SettingCenterActivity.this,
                            ComingPhoneService.class);
                    stopService(comingPhoneService);
                    //设置复选框的状态
                    sciv_phoneLocationService.setChecked(false);
                } else {
                    Intent comingPhoneService = new Intent(SettingCenterActivity.this,
                            ComingPhoneService.class);
                    startService(comingPhoneService);
                    sciv_phoneLocationService.setChecked(true);
                }
            }
        });

        //黑名单服务启动和关闭
        sciv_blackservice.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断黑名单拦截服务是否运行
                if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.thxy.mobileguard" +
                        ".service.TelSmsBlackService")) {
                    Intent blackService = new Intent(SettingCenterActivity.this,
                            TelSmsBlackService.class);
                    stopService(blackService);
                    //设置复选框的状态
                    sciv_blackservice.setChecked(false);
                } else {
                    Intent blackService = new Intent(SettingCenterActivity.this,
                            TelSmsBlackService.class);
                    startService(blackService);
                    sciv_blackservice.setChecked(true);
                }
            }
        });

        //自动更新的事件处理
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

        sciv_blackservice = (SettingCenterItemView) findViewById(R.id
                .sciv_setting_center_blackservice);

        sciv_phoneLocationService = (SettingCenterItemView) findViewById(R.id
                .sciv_setting_center_phonelocationservice);


    }

}
