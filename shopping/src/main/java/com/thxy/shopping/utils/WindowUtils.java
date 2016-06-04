package com.thxy.shopping.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by apple on 2016/5/26.
 *
 * 窗口工具类
 */
public class WindowUtils {

    public static float getWindowsWidth(Context context){
        /** 1.获取一个窗口管理对象 :可以得到当前手机的分辨率  */
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        /** 2.获取一个显示分辨率对象：用于保存手机的分辨率  */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        /** 3.注入当前手机的分辨率：将当前手机的分辨率存入到 displayMetrics */
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;

    }

    public static float getWindowsHeight(Context context){
        /** 1.获取一个窗口管理对象  */
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        /** 2.获取一个显示分辨率对象  */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        /** 3.注入当前手机的分辨率  */
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;

    }

    public static Bitmap getScaleBitmap(Bitmap bitmap,Context context){

        float screenWidth = WindowUtils.getWindowsWidth(context);
        /** 2.得到一个缩放比例
         *     6   53 60
         *          （6/3）/53  * 53  = 2
         *          （6/3）/60  * 60  = 2
         * */
        float scaleX = (screenWidth / 3) / bitmap.getWidth();
        float scaleY = (screenWidth / 3) / bitmap.getHeight();
        /** 3.定义一个缩放对象:矩形 */
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX,scaleY);
        /** 4.将当前的图片进行缩放:缩放成手机宽度的1/3
         * createBitmap(Bitmap source, int x, int y, int width, int height,
         Matrix m, boolean filter)
         * */
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight()
                ,matrix,true);

        return bitmap;
    }

}
