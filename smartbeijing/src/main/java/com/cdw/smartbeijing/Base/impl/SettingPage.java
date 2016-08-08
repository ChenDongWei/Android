package com.cdw.smartbeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cdw.smartbeijing.base.BasePager;

/**
 * 设置
 * Created by dongwei on 2016/8/6.
 */
public class SettingPage extends BasePager {
    public SettingPage(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView view = new TextView(mActivity);
        view.setText("设置");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);

        //修改页面标题
        tvTitle.setText("设置");

        btMenu.setVisibility(View.GONE);
    }
}
