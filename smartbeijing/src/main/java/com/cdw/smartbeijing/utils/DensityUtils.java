package com.cdw.smartbeijing.utils;

import android.content.Context;

/**
 * Created by dongwei on 2016/8/18.
 */
public class DensityUtils {

    public static int dp2px(float dp, Context ctx){
        float density = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);
        return px;
    }

    public static float px2dp(int px, Context ctx){
        float density = ctx.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
