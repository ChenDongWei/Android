package com.cdw.googleplay.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by dongwei on 2016/8/31.
 */
public class DrawableUtils {
    //获取一个shape对象
    public static GradientDrawable getGradientDrawable(int color, int radius){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);//矩形

        shape.setCornerRadius(radius);//圆角半径
        shape.setColor(color);//颜色

        return shape;
    }

    //获取状态选择器
    public static StateListDrawable getSelector(Drawable normal, Drawable press){
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, press);
        selector.addState(new int[]{}, normal);

        return selector;
    }

    public static StateListDrawable getSelector(int normal, int press, int radius){
        GradientDrawable bgNormal = getGradientDrawable(normal, radius);
        GradientDrawable bgPress = getGradientDrawable(press, radius);
        StateListDrawable selector = getSelector(bgNormal, bgPress);
        return selector;
    }
}
