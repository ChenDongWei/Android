package com.cdw.smartbeijing.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cdw.smartbeijing.base.BaseMenuDetailPager;

/**
 * 菜单详情页-专题
 * Created by dongwei on 2016/8/8.
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager {
    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-专题");
        view.setTextColor(Color.RED);
        view.setTextSize(26);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
