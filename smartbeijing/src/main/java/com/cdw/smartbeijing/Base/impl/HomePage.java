package com.cdw.smartbeijing.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cdw.smartbeijing.base.BasePager;

/**
 * 首页
 * Created by dongwei on 2016/8/6.
 */
public class HomePage extends BasePager {
    public HomePage(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView view = new TextView(mActivity);
        view.setText("首页");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);

        //修改页面标题
        tvTitle.setText("智慧北京");

        btMenu.setVisibility(View.GONE);
    }
}
