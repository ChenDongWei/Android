package com.cdw.smartbeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不允许滑动的viewpager
 * Created by dongwei on 2016/8/7.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //事件拦截(事件中断)
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截子类控件的事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写此方法，触摸时什么都不做
        return true;
    }
}
