package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thxy.mobileguard.R;

/**
 * 高级工具：电话归属地查询，短信备份和还原，程序锁的设置
 */
public class AToolActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    /**
     * 号码归属地查询
     * @param v
     */
    public void phoneQuery(View v){
        Intent query = new Intent(this, PhoneLocationActivity.class);
        startActivity(query);
    }

    private void initView() {
        setContentView(R.layout.activity_atool);
    }

}
