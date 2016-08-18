package com.cdw.smartbeijing.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络缓存
 * Created by dongwei on 2016/8/16.
 */
public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
        new BitmapTask().execute(imageView, url);//启动AsyncTask
    }

    /**
     * 三个泛型意义
     * 1.doInBackground里的参数
     * 2.onProgressUpdate里的参数
     * 3.onPostExecute里的参数及doInBackground的返回类型
     */
    class BitmapTask extends AsyncTask<Object, Integer, Bitmap>{

        private ImageView imageView;
        private String url;

        //预加载，运行在主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //正在加载，运行在子线程(核心方法)
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            url = (String) params[1];

            imageView.setTag(url);

            Bitmap bitmap = download(url);
            return bitmap;
        }

        //更新进度，运行在主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //加载结束，运行在主线程(核心方法)
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null){
                //给imageview设置图片
                String url = (String) imageView.getTag();
                if (url.equals(this.url)){
                    imageView.setImageBitmap(result);

                    //写本地缓存
                    mLocalCacheUtils.setLocalCache(url, result);
                    //写内存缓存
                    mMemoryCacheUtils.setMemoryCache(url, result);
                }
            }
            super.onPostExecute(result);
        }
    }

    public Bitmap download(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);//连接超时
            conn.setReadTimeout(5000);//读取超时

            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200){
                InputStream inputStream = conn.getInputStream();
                //根据输入流生成bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn != null){
                conn.disconnect();
            }
        }
        return null;
    }
}
