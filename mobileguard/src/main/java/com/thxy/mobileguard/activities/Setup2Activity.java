package com.thxy.mobileguard.activities;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 第二个设置向导界面
 */
public class Setup2Activity extends BaseSetupActivity {
    private Button bt_bind;
    private ImageView iv_isBind;

    @Override
    public void initData() {
        super.initData();
    }

    /**
     * 子类需要覆盖此方法来完成界面的显示
     */
    public void initView() {
        setContentView(R.layout.activity_setup2);
        bt_bind = (Button) findViewById(R.id.bt_setup2_bindsim);
        iv_isBind = (ImageView) findViewById(R.id.iv_setup2_isbind);
        if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))){
            iv_isBind.setImageResource(R.drawable.unlock);
        }else {
            iv_isBind.setImageResource(R.drawable.lock);
        }
    }

    @Override
    public void initEvent() {
        bt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM,
                        ""))) {
                    //绑定SIM卡，存储SIM卡信息
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context
                            .TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();
                    SpTools.putString(getApplicationContext(), MyConstants.SIM, simSerialNumber);

                    //切换是否绑定SIM卡的图标
                    iv_isBind.setImageResource(R.drawable.lock);
                } else {
                    //解绑SIM卡
                    SpTools.putString(getApplicationContext(), MyConstants.SIM, "");

                    iv_isBind.setImageResource(R.drawable.unlock);
                }
            }
        });
        super.initEvent();
    }

    @Override
    public void next(View v) {
        if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))) {
            Toast.makeText(getApplicationContext(), "请先绑定SIM卡", Toast.LENGTH_SHORT).show();


            return;
        }
        super.next(v);
    }

    @Override
    public void nextActivity() {

        startActivity(Setup3Activity.class);
    }

    @Override
    public void prevActivity() {
        startActivity(Setup1Activity.class);
    }
}
