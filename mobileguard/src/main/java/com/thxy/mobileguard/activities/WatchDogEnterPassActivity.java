package com.thxy.mobileguard.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.thxy.mobileguard.R;

/**
 * 看门狗服务并没有实现
 */
public class WatchDogEnterPassActivity extends AppCompatActivity {

    private ImageView iv_icon;//加锁的app的图标
    private EditText et_pass;
    private Button bt_enter;
    private String packName;
//    private HomeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();

    }

    private void initData() {
        //注册home键的广播

        /*receiver = new HomeReceiver();

        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(receiver, filter);*/


        Intent intent = getIntent();
        //获取app的包名
        packName = intent.getStringExtra("packname");

        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packName, 0);
            iv_icon.setImageDrawable(applicationInfo.loadIcon(pm));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initEvent() {
        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断输入的密码
                String pass = et_pass.getText().toString().trim();
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.equals("")){
                    Intent intent = new Intent();
                    intent.setAction("com.thxy.watchdog");
                    intent.putExtra("packname", packName);
                    sendBroadcast(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_watchdog_enterpass);
        iv_icon = (ImageView) findViewById(R.id.iv_watchdog_enterpass_icon);
        et_pass = (EditText) findViewById(R.id.et_watchdog_enterpass_password);
        bt_enter = (Button) findViewById(R.id.bt_watchdog_enterpass_enter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //回到手机主界面
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addCategory("android.intent.category.MONKEY");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
