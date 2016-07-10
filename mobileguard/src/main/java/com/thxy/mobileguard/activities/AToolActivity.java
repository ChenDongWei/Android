package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.engine.SmsEngine;

/**
 * 高级工具：电话归属地查询，短信备份和还原，程序锁的设置
 */
public class AToolActivity extends Activity {
    private ProgressDialog pd;
    private ProgressBar pb_bk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 短信的备份
     *
     * @param v
     */
    public void smsBackups(View v) {

        SmsEngine.smsBackupsJson(AToolActivity.this, new SmsEngine.BackupsProgress() {
            @Override
            public void show() {
                pd.show();
                pb_bk.setVisibility(View.VISIBLE);
            }

            @Override
            public void setMax(int max) {
                pd.setMax(max);
                pb_bk.setMax(max);
            }

            @Override
            public void setProgress(int progress) {
                pd.setProgress(progress);
                pb_bk.setProgress(progress);
            }

            @Override
            public void end() {
                pd.dismiss();
                pb_bk.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 短信还原
     *
     * @param v
     */
    public void smsResumn(View v) {
        SmsEngine.smsResumnJson(this, new SmsEngine.BackupsProgress() {
            @Override
            public void setProgress(int progress) {
                // TODO Auto-generated method stub
                //ui程序员可以显示自己 组件
                pd.setProgress(progress);
                pb_bk.setProgress(progress);
            }

            @Override
            public void setMax(int max) {
                pd.setMax(max);
                pb_bk.setMax(max);
            }

            @Override
            public void show() {
                pd.show();
                pb_bk.setVisibility(View.VISIBLE);
            }

            @Override
            public void end() {
                pd.dismiss();
                pb_bk.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 程序锁
     *
     * @param v
     */
    public void lockActivity(View v) {
        Intent lock = new Intent(AToolActivity.this, LockActivity.class);
        startActivity(lock);
    }

    /**
     * 号码归属地查询
     *
     * @param v
     */
    public void phoneQuery(View v) {
        Intent query = new Intent(this, PhoneLocationActivity.class);
        startActivity(query);
    }

    private void initView() {
        setContentView(R.layout.activity_atool);
        //短信备份进度
        pb_bk = (ProgressBar) findViewById(R.id.pb_smsbackups_progress);
    }

}
