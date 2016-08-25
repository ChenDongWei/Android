package com.cdw.googleplay.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.cdw.googleplay.global.GooglePlayApplication;

/**
 * Created by dongwei on 2016/8/21.
 */
public class UIUtils {
    public static Context getContext(){
        return GooglePlayApplication.getContext();
    }

    public static Handler getHandler() {
        return GooglePlayApplication.getHandler();
    }

    public static int getMainThreadId(){
        return GooglePlayApplication.getMainThreadId();
    }


    //获取字符串
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }
    //获取字符串数组
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }
    //获取图片
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }
    //获取颜色
    public static int getColor(int id){
        return getContext().getResources().getColor(id);
    }
    //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(int id){
        return getContext().getResources().getColorStateList(id);
    }
    //获取尺寸
    public static int getDimen(int id){
        return getContext().getResources().getDimensionPixelSize(id);
    }


    //dp转成px
    public static int dp2px(float dp){
        //获取设备密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
    //px转成dp
    public static float px2dp(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return px / density;
    }


    //加载布局文件
    public static View inflate(int id){
        return View.inflate(getContext(), id, null);
    }


    //判断是否运行在主线程
    public static boolean isRunOnUIThread(){
        //获取当前线程id，如果当前线程id和主线程id相同，那么当前就是主线程
        int myTid = Process.myTid();
        if (myTid == getMainThreadId()){
            return true;
        }
        return false;
    }
    //运行在主线程
    public static void runOnUIThread(Runnable r){
        if (isRunOnUIThread()){
            r.run();
        }else {
            //如果是子线程，借助handler让其运行在主线程
            getHandler().post(r);
        }
    }
}
