package com.thxy.mobileguard.activities;

import com.thxy.mobileguard.R;

/**
 * 第一个设置向导界面
 */
public class Setup1Activity extends BaseSetupActivity {

    /**
     * 子类需要覆盖此方法来完成界面的显示
     */
    public void initView() {
        setContentView(R.layout.activity_setup1);

    }

    @Override
    public void nextActivity() {
        //跳转到第二个设置向导界面
        startActivity(Setup2Activity.class);
    }

    @Override
    public void prevActivity() {

    }

}
