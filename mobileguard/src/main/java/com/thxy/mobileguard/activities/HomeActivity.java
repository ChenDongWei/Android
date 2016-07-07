package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.thxy.mobileguard.utils.Md5Utils;
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
                    case 8:
                        //设置中心
                        Intent setting = new Intent(HomeActivity.this, SettingCenterActivity.class);
                        startActivity(setting);
                        break;

                    case 7:
                        //高级工具
                        Intent atool = new Intent(HomeActivity.this, AToolActivity.class);
                        startActivity(atool);
                        break;
                    case 3:
                        //进程管理
                        Intent tmanager = new Intent(HomeActivity.this, TastManagerActivity.class);
                        startActivity(tmanager);
                        break;

                    case 2:
                        //软件管家
                        Intent pmanager = new Intent(HomeActivity.this, AppManagerActivity.class);
                        startActivity(pmanager);
                        break;

                    case 1:
                        //通讯卫士
                        Intent telsafe = new Intent(HomeActivity.this, TelSmsSafeActivity.class);
                        startActivity(telsafe);
                        break;
                    case 0:
                        //手机防盗
                        //自定义输入密码对话框
                        if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(),
                                MyConstants.PASSWORD, ""))) {
                            showSettingPassDialog();
                        } else {
                            //输入密码对话框
                            showEnterPassDialog();
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showEnterPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enter_password, null);
        final EditText et_passone = (EditText) view.findViewById(R.id
                .et_dialog_enter_password_passone);
        Button bt_setpass = (Button) view.findViewById(R.id.bt_dialog_enter_password_login);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_enter_password_cancel);

        builder.setView(view);

        bt_setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passone = et_passone.getText().toString().trim();
                if (TextUtils.isEmpty(passone)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //密码判断,MD5二次加密
                    passone = Md5Utils.md5(Md5Utils.md5(passone));

                    if (passone.equals(SpTools.getString(getApplicationContext(), MyConstants
                            .PASSWORD, ""))) {
                        //进入手机防盗界面
                        Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "密码不正确！", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    //关闭对话框
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
                if (TextUtils.isEmpty(passone) || TextUtils.isEmpty(passtwo)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!passone.equals(passtwo)) {
                    Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //保存密码,MD5二次加密
                    passone = Md5Utils.md5(Md5Utils.md5(passone));
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

    @Override
    protected void onResume() {
        //通知GridView更新数据
        adapter.notifyDataSetChanged();
        super.onResume();
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
            if (position == 0) {
                //判断是否存在新的手机防盗名
                if (!TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants
                        .LOSTFINDNAME, ""))) {
                    tv_name.setText(SpTools.getString(getApplicationContext(), MyConstants
                            .LOSTFINDNAME, ""));
                }
            }

            return view;
        }
    }

    private void initView() {
        setContentView(R.layout.activity_home);
        gv_menus = (GridView) findViewById(R.id.gv_home_menus);
    }
}
