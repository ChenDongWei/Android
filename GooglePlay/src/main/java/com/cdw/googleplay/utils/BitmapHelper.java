package com.cdw.googleplay.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by dongwei on 2016/8/26.
 */
public class BitmapHelper {
    private static BitmapUtils mBitmapUtils = null;

    public static BitmapUtils getBitmapUtils(){
        if (mBitmapUtils == null){
            synchronized (BitmapHelper.class){
                if (mBitmapUtils == null){
                    mBitmapUtils = new BitmapUtils(UIUtils.getContext());
                }
            }
        }
        return mBitmapUtils;
    }
}
