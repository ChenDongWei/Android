package com.cdw.smartbeijing.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * Created by dongwei on 2016/8/16.
 */
public class MemoryCacheUtils {
    //private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();
    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCacheUtils(){
        //获取分配给app的内存大小
        long maxMemory = Runtime.getRuntime().maxMemory();

        mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)){
            //返回每个对象的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();

                return byteCount;
            }
        };
    }
    /**
     * 写缓存
     */
    public void setMemoryCache(String url, Bitmap bitmap){
        //mMemoryCache.put(url, bitmap);
        //使用软引用将bitmap包装起来
//        SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);
//        mMemoryCache.put(url, soft);
        mMemoryCache.put(url, bitmap);
    }

    /**
     * 读缓存
     */
    public Bitmap getMemoryCache(String url){
//        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//        if (softReference != null){
//            Bitmap bitmap = softReference.get();
//            return bitmap;
//        }
        return mMemoryCache.get(url);
    }
}
