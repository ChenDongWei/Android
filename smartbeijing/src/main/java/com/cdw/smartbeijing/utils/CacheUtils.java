package com.cdw.smartbeijing.utils;

import android.content.Context;

/**
 * 网络缓存的工具类
 * Created by dongwei on 2016/8/7.
 */
public class CacheUtils {
    /**
     * 以url为key，以json为value
     * @param ctx
     * @param url
     * @param json
     */
    public static void setCache(Context ctx, String url, String json){
        PrefUtils.setString(ctx, url, json);
    }

    /**
     * 获取缓存
     * @param ctx
     * @param url
     * @return
     */
    public static String getCache(Context ctx, String url){
        return PrefUtils.getString(ctx, url, null);
    }
}
