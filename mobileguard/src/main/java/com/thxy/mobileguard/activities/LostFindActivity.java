package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 手机防盗界面
 */
public class LostFindActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)){
            //进入显示界面
            initView();
        }else {
            //进入设置向导界面
            Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 重新进入设置向导界面
     * @param v
     */
    public void enterSetup(View v){
        Intent setup1 = new Intent(this, Setup1Activity.class);
        startActivity(setup1);
        finish();
    }

    private void initView() {
        setContentView(R.layout.activity_lostfind);
    }

}
