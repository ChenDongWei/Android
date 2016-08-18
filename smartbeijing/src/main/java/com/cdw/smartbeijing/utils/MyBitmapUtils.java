package com.cdw.smartbeijing.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.cdw.smartbeijing.R;

/**
 * 自定义三级缓存图片加载工具
 * Created by dongwei on 2016/8/16.
 */
public class MyBitmapUtils {
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
    }

    public void display(ImageView imageView, String url) {
        //设置默认图片
        imageView.setImageResource(R.drawable.pic_item_list_default);

        //优先从内存缓存加载图片
        Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        bitmap = mLocalCacheUtils.getLocalCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            //写内存缓存
            mMemoryCacheUtils.setMemoryCache(url, bitmap);
            return;
        }
        mNetCacheUtils.getBitmapFromNet(imageView, url);

    }
}
