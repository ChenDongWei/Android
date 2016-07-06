package com.thxy.mobileguard.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.utils.EncryptTools;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 第三个设置向导界面
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText et_safeNumber;//安全号码的编辑框

    @Override
    public void initData() {
        String safenumber = SpTools.getString(getApplicationContext(), MyConstants.SAFENUMBER, "");
        et_safeNumber.setText(EncryptTools.decryption(safenumber));
        super.initData();
    }

    /**
     * 子类需要覆盖此方法来完成界面的显示
     */
    public void initView() {
        setContentView(R.layout.activity_setup3);
        et_safeNumber = (EditText) findViewById(R.id.et_setup3_safenumber);
    }

    /**
     * 从手机联系人获取安全号码
     * @param v
     */
    public void selectSafeNumber(View v) {
        Intent friends = new Intent(this, FriendsActivity.class);
        startActivityForResult(friends, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null){
            String phone = data.getStringExtra(MyConstants.SAFENUMBER);
            et_safeNumber.setText(phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void next(View v) {
        String safeNumber = et_safeNumber.getText().toString().trim();
        if (TextUtils.isEmpty(safeNumber)) {
            Toast.makeText(getApplicationContext(), "安全号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //对安全号码加密
            safeNumber = EncryptTools.encrypt(safeNumber);

            //保存安全号码
            SpTools.putString(getApplicationContext(), MyConstants.SAFENUMBER, safeNumber);
        }
        super.next(v);
    }

    @Override
    public void nextActivity() {
        startActivity(Setup4Activity.class);
    }

    @Override
    public void prevActivity() {
        startActivity(Setup2Activity.class);
    }

}
