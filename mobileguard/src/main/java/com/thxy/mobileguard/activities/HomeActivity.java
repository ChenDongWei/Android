package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

public class HomeActivity extends Activity {

    private GridView gv_menus;//主界面的按钮

    private int icons[] = {
            R.drawable.safe,
            R.drawable.callmsgsafe,
            R.drawable.app,
            R.drawable.taskmanager,
            R.drawable.netmanager,
            R.drawable.trojan,
            R.drawable.sysoptimize,
            R.drawable.atools,
            R.drawable.settings
    };

    private String names[] = {
            "手机防盗",
            "通讯卫士",
            "软件管家",
            "进程管理",
            "流量统计",
            "病毒查杀",
            "缓存清理",
            "高级工具",
            "设置中心"
    };

    private MyAdapter adapter;//gridview的适配器

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化界面
        initView();
        //给GridView设置数据
        initData();
        //初始化事件
        initEvent();
    }

    /**
     * 初始化组件的事件
     */
    private void initEvent() {
        gv_menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //自定义对话框
                        showSettingPassDialog();
                        break;
                }
            }
        });
    }

    private void showSettingPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setting_password, null);
        final EditText et_passone = (EditText) view.findViewById(R.id
                .et_dialog_setting_password_passone);
        final EditText et_passtwo = (EditText) view.findViewById(R.id
                .et_dialog_setting_password_passtwo);
        Button bt_setpass = (Button) view.findViewById(R.id.bt_dialog_setting_password_setpass);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_setting_password_cancel);

        builder.setView(view);

        bt_setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passone = et_passone.getText().toString().trim();
                String passtwo = et_passtwo.getText().toString().trim();
                if (TextUtils.isEmpty(passone) || TextUtils.isEmpty(passtwo)){
                    Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();;
                    return;
                }else if (!passone.equals(passtwo)){
                    Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();;
                    return;
                }else {
                    //保存密码
                    SpTools.putString(getApplicationContext(), MyConstants.PASSWORD, passone);
                    dialog.dismiss();
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    /**
     * 初始化组件的数据
     */
    private void initData() {
        adapter = new MyAdapter();
        gv_menus.setAdapter(adapter);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_home_gridview, null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_home_gv_icon);
            TextView tv_name = (TextView) view.findViewById(R.id.iv_item_home_gv_name);
            iv_icon.setImageResource(icons[position]);
            tv_name.setText(names[position]);
            return view;
        }
    }

    private void initView() {
        setContentView(R.layout.activity_home);
        gv_menus = (GridView) findViewById(R.id.gv_home_menus);
    }
}