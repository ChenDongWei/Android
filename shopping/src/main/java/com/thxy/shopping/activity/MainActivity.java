package com.thxy.shopping.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thxy.shopping.R;
import com.thxy.shopping.fragment.CarFragment;
import com.thxy.shopping.fragment.HomeFragment;
import com.thxy.shopping.fragment.TypeFragment;
import com.thxy.shopping.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    /**
     * 四个
     */
    private int[] menusIds = new int[]{
            R.id.ll_main_home,
            R.id.ll_main_classification,
            R.id.ll_main_car,
            R.id.ll_main_user
    };
    private LinearLayout[] menus = new LinearLayout[menusIds.length];

    private ImageView[] menuImages = new ImageView[menusIds.length];
    private int[] menuImageids = new int[]{
            R.id.iv_main_img1,
            R.id.iv_main_img2,
            R.id.iv_main_img3,
            R.id.iv_main_img4
    };

    private TextView[] menuTexts = new TextView[menusIds.length];
    private int[] menuTextids = new int[]{
            R.id.tv_main_text1,
            R.id.tv_main_text2,
            R.id.tv_main_text3,
            R.id.tv_main_text4
    };

    private int[] menuOpenImages = new int[]{
            R.drawable.guide_home_on,
            R.drawable.guide_tfaccount_on,
            R.drawable.guide_cart_on,
            R.drawable.guide_account_on
    };

    private int[] menuCloseImages = new int[]{
            R.drawable.guide_home_nm,
            R.drawable.guide_tfaccount_nm,
            R.drawable.guide_cart_nm,
            R.drawable.guide_account_nm
    };

    private RelativeLayout mainViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment F_HOME, T_HOME, C_HOME, U_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);

        for (int i = 0; i < menusIds.length; i++) {
            menus[i] = (LinearLayout) findViewById(menusIds[i]);
            menuImages[i] = (ImageView) findViewById(menuImageids[i]);
            menuTexts[i] = (TextView) findViewById(menuTextids[i]);
            menus[i].setOnClickListener(this);
        }

        mainViewPager = (RelativeLayout) findViewById(R.id.mainViewPager);

        /** 默认展示第一个主页 */
        if (F_HOME != null) {
            showFragment(F_HOME);
        } else {
            F_HOME = new HomeFragment();
            addFragment(F_HOME);
            showFragment(F_HOME);
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mainViewPager, fragment);
        ft.commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();

        if (F_HOME != null) {
            ft.hide(F_HOME);
        }
        if (T_HOME != null) {
            ft.hide(T_HOME);
        }
        if (C_HOME != null) {
            ft.hide(C_HOME);
        }
        if (U_HOME != null) {
            ft.hide(U_HOME);
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {

        for (int i = 0; i < menusIds.length; i++) {
            menuImages[i].setImageResource(menuCloseImages[i]);
            menuTexts[i].setTextColor(0xff000000);
        }

        switch (v.getId()) {
            case R.id.ll_main_home:
                menuImages[0].setImageResource(menuOpenImages[0]);
                menuTexts[0].setTextColor(0xffff5000);
                if (F_HOME != null) {
                    showFragment(F_HOME);
                } else {
                    F_HOME = new HomeFragment();
                    addFragment(F_HOME);
                    showFragment(F_HOME);
                }
                break;
            case R.id.ll_main_classification:
                menuImages[1].setImageResource(menuOpenImages[1]);
                menuTexts[1].setTextColor(0xffff5000);
                if (T_HOME != null) {
                    showFragment(T_HOME);
                } else {
                    T_HOME = new TypeFragment();
                    addFragment(T_HOME);
                    showFragment(T_HOME);
                }
                break;
            case R.id.ll_main_car:
                menuImages[2].setImageResource(menuOpenImages[2]);
                menuTexts[2].setTextColor(0xffff5000);

                    removeFragment(C_HOME);
                    C_HOME = new CarFragment();
                    addFragment(C_HOME);
                    showFragment(C_HOME);
                
                break;
            case R.id.ll_main_user:
                menuImages[3].setImageResource(menuOpenImages[3]);
                menuTexts[3].setTextColor(0xffff5000);
                if (U_HOME != null) {
                    showFragment(U_HOME);
                } else {
                    U_HOME = new UserFragment();
                    addFragment(U_HOME);
                    showFragment(U_HOME);
                }
                break;
        }
    }

    private void removeFragment(Fragment fragment) {
        if (fragment != null){
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("点击退出");
        builder.setMessage("您确定要退出吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("点错了", null);
        builder.setIcon(R.drawable.logo);
        builder.create().show();
    }
}
