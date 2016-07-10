package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.service.ComingPhoneService;
import com.thxy.mobileguard.service.TelSmsBlackService;
import com.thxy.mobileguard.service.WatchDogService;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.ServiceUtils;
import com.thxy.mobileguard.utils.SpTools;
import com.thxy.mobileguard.view.SettingCenterItemView;

public class SettingCenterActivity extends Activity {
    private SettingCenterItemView sciv_autoupdate;
    private SettingCenterItemView sciv_blackservice;
    private SettingCenterItemView sciv_phoneLocationService;
    private SettingCenterItemView sciv_watchdog;
    private TextView tv_locationStyle_content;
    private ImageView iv_changeStyle;
    private String[] styleNames = new String[]{"卫士蓝", "金属灰", "苹果绿", "活力橙",
            "半透明"};
    private AlertDialog dialog;
    private RelativeLayout rl_style_root;

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
        //判断看门狗服务来设置复选框的初始值
        sciv_watchdog.setChecked(ServiceUtils.isServiceRunning(getApplicationContext(), "com" +
                ".thxy.mobileguard.service.WatchDogService"));
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
        //看门狗服务事件
        sciv_watchdog.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断看门狗服务是否运行
                if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.thxy.mobileguard" +
                        ".service.WatchDogService")) {
                    Intent watchDogService = new Intent(SettingCenterActivity.this,
                            WatchDogService.class);
                    stopService(watchDogService);
                    //设置复选框的状态
                    sciv_watchdog.setChecked(false);
                } else {
                    Intent comingPhoneService = new Intent(SettingCenterActivity.this,
                            WatchDogService.class);
                    startService(comingPhoneService);
                    sciv_watchdog.setChecked(true);
                }
            }
        });

        //归属地根布局点击事件
        rl_style_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStyleDialog();
            }
        });
        //箭头的点击事件
        iv_changeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStyleDialog();
            }
        });
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

    private void showStyleDialog() {
        //通过对话框让用户选择样式
        AlertDialog.Builder ab = new AlertDialog.Builder(SettingCenterActivity.this);
        ab.setTitle("选择来电归属地样式");
        ab.setSingleChoiceItems(styleNames, Integer.parseInt(SpTools.getString
                (getApplicationContext(), MyConstants.STYLEBGINDEX, "0")), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //字符串的方式保存归属地样式
                        SpTools.putString(getApplicationContext(), MyConstants.STYLEBGINDEX,
                                which + "");
                        tv_locationStyle_content.setText(styleNames[which]);
                        dialog.dismiss();
                    }
                });
        dialog = ab.create();
        dialog.show();
    }

    private void initView() {
        setContentView(R.layout.activity_settingcenter);
        sciv_autoupdate = (SettingCenterItemView) findViewById(R.id
                .sciv_setting_center_autoupdate);

        sciv_blackservice = (SettingCenterItemView) findViewById(R.id
                .sciv_setting_center_blackservice);

        sciv_phoneLocationService = (SettingCenterItemView) findViewById(R.id
                .sciv_setting_center_phonelocationservice);

        sciv_watchdog = (SettingCenterItemView) findViewById(R.id
                .sciv_setting_center_watchdogservice);

        rl_style_root = (RelativeLayout) findViewById(R.id
                .rl_settingcenter_locationstyle_root);

        tv_locationStyle_content = (TextView) findViewById(R.id
                .tv_settingcenter_locationstyle_content);

        iv_changeStyle = (ImageView) findViewById(R.id
                .iv_settingcenter_locationstyle_select);

    }

}
