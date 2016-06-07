package com.thxy.shopping.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2016/6/1.
 */
public class HttpUtils {

    public static String sendPost(String url, Map<String,Object> params) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Commons.SERVER_IP + url);
        if (params != null && params.size() > 0){
            List<NameValuePair> nameAndValue = new ArrayList<>();
            for (Map.Entry<String,Object> param : params.entrySet()){
                String key = param.getKey();
                Object value = param.getValue();
                nameAndValue.add(new BasicNameValuePair(key, value + ""));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameAndValue));
        }

        HttpResponse httpResponse = httpClient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(httpResponse.getEntity());

            return result;
        }

        return null;
    }

    public static Bitmap getBitmapByImage(String uri) throws Exception {
        URL url = new URL(Commons.SERVER_IP + uri);
        InputStream is = url.openStream();

        Bitmap bitmap = BitmapFactory.decodeStream(is);


        return bitmap;
    }
}
