package com.cdw.smartbeijing.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 五个标签页的基类
 * Created by dongwei on 2016/8/6.
 */
public class BasePager {
    public Activity mActivity;

    public TextView tvTitle;
    public ImageButton btMenu;
    public FrameLayout flContent;//空的帧布局对象，要动态添加

    public ImageButton btnPhoto;//组图切换按钮

    public View mRootView;//当前页面的布局对象

    public BasePager(Activity activity) {
        mActivity = activity;

        mRootView = initView();
    }

    //初始化布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btMenu = (ImageButton) view.findViewById(R.id.bt_menu);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        btnPhoto = (ImageButton) view.findViewById(R.id.bt_title_photo);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    protected void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();
    }
    //初始化数据
    public void initData() {

    }
}
