package com.cdw.smartbeijing.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by dongwei on 2016/8/8.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;//菜单详情页根布局

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }
}
