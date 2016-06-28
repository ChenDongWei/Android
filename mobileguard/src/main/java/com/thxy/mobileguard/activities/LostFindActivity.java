package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 手机防盗界面
 */
public class LostFindActivity extends Activity {
    private AlertDialog dialog;
    private View popupView;
    private ScaleAnimation sa;
    private PopupWindow pw;
    private LinearLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)) {
            //进入显示界面
            initView();
            //初始化修改名字的界面
            initPopupView();
            //初始化弹出窗体
            initPopupWindow();
        } else {
            //进入设置向导界面
            Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initPopupWindow() {
        pw = new PopupWindow(popupView, -2, -2);
        //获取焦点
        pw.setFocusable(true);
        sa = new ScaleAnimation(
                1, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0f);
        sa.setDuration(1000);
    }

    /**
     * 重新进入设置向导界面
     *
     * @param v
     */
    public void enterSetup(View v) {
        Intent setup1 = new Intent(this, Setup1Activity.class);
        startActivity(setup1);
        finish();
    }

    private void initView() {
        setContentView(R.layout.activity_lostfind);
        rl_root = (LinearLayout) findViewById(R.id.rl_lostfind_root);
    }

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    /**
     * 处理菜单事件的方法
     *
     * @param featureId
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_modify_name:
                Toast.makeText(getApplicationContext(), "修改菜单名", Toast.LENGTH_SHORT).show();
                //弹出对话框，让用户输入新的手机防盗名
                showModifyNameDialog();
                break;

            case R.id.mn_test_menu:
                Toast.makeText(getApplicationContext(), "测试", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void showModifyNameDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);

        //创建对话框
        dialog = ab.create();
        dialog.show();
    }

    private void initPopupView() {
        //对话框显示的界面
        popupView = View.inflate(getApplicationContext(), R.layout.dialog_modify_name, null);
        //处理界面和事件
        final EditText et_name = (EditText) popupView.findViewById(R.id
                .et_dialog_lostfind_modify_name);
        Button bt_modify = (Button) popupView.findViewById(R.id.bt_dialog_lostfind_modify);
        Button bt_cancel = (Button) popupView.findViewById(R.id.bt_dialog_lostfind_modify_cancel);
        //处理按钮事件
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        bt_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取修改的名字
                String name = et_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "名字不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //保存新名字到sp中
                SpTools.putString(getApplicationContext(), MyConstants.LOSTFINDNAME, name);
                pw.dismiss();
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //处理menu键的事件

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (pw != null && pw.isShowing()) {
                pw.dismiss();
            } else {
                //设置弹出窗体的背景
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupView.startAnimation(sa);
                //设置弹出窗体显示的位置
                int height = getWindowManager().getDefaultDisplay().getHeight();
                int width = getWindowManager().getDefaultDisplay().getWidth();
                pw.showAtLocation(rl_root, Gravity.LEFT | Gravity.TOP, width / 4, height / 4);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
