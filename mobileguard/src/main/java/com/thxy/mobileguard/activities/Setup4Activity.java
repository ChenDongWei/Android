package com.thxy.mobileguard.activities;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.service.LostFindService;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.ServiceUtils;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 第四个设置向导界面
 */
public class Setup4Activity extends BaseSetupActivity {
    private CheckBox cb_isprotected;

    /**
     * 子类需要覆盖此方法来完成界面的显示
     */
    public void initView() {
        setContentView(R.layout.activity_setup4);
        cb_isprotected = (CheckBox) findViewById(R.id.cb_setup4_isprotected);

    }

    @Override
    public void initData() {
        if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.thxy.mobileguard.service" +
                ".LostFindService")) {
            cb_isprotected.setChecked(true);
        } else {
            cb_isprotected.setChecked(false);
        }
        super.initData();
    }

    @Override
    public void initEvent() {
        cb_isprotected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //开启防盗保护服务
                if (isChecked) {
                    Intent service = new Intent(Setup4Activity.this, LostFindService.class);
                    startService(service);
                } else {
                    Intent service = new Intent(Setup4Activity.this, LostFindService.class);
                    stopService(service);
                }
            }
        });
        super.initEvent();
    }

    @Override
    public void nextActivity() {
        //保存设置完成的状态
        SpTools.putBoolean(getApplicationContext(), MyConstants.ISSETUP, true);
        startActivity(LostFindActivity.class);
    }

    @Override
    public void prevActivity() {
        startActivity(Setup3Activity.class);
    }

}
