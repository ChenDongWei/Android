package com.thxy.mobileguard.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.thxy.mobileguard.R;
import com.thxy.mobileguard.domain.UrlBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 手机卫士splash界面
 */
public class SplashActivity extends AppCompatActivity {
    private RelativeLayout rl_root;//界面根布局组件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        //初始化动画
        initAnimation();
        //检测服务器版本
        checkVersion();
    }

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.1.106:8080/guardversion.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //读取数据超时
                    conn.setReadTimeout(5000);
                    //网络连接超时
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        //获取字节流
                        InputStream is = conn.getInputStream();
                        //把字节流转换成缓冲字符流
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
                        //读取一行信息
                        String line = bfr.readLine();
                        StringBuilder json = new StringBuilder();
                        while (line != null){
                            json.append(line);
                            line = bfr.readLine();
                        }
                        //解析json数据
                        UrlBean parseJson = parseJson(json);
                        System.out.println(parseJson.getVersionCode() + "===版本号===");
                        bfr.close();
                        conn.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private UrlBean parseJson(StringBuilder jsonString) {
        UrlBean bean = new UrlBean();
        JSONObject jobject;
        try {
            //把json字符串数据封装成json数据
            jobject = new JSONObject(jsonString + "");
            int versionCode = jobject.getInt("version");
            String url = jobject.getString("url");
            String desc = jobject.getString("desc");
            //数据封装到bean对象中
            bean.setVersionCode(versionCode);
            bean.setUrl(url);
            bean.setDesc(desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }


    private void initView() {
        setContentView(R.layout.activity_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
    }

    private void initAnimation() {
        //创建动画，Alpha动画 arg0:完全透明  arg1:完全显示
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        //设置动画播放时间
        aa.setDuration(3000);
        //界面停留在动画结束的状态
        aa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(3000);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(3000);
        sa.setFillAfter(true);

        AnimationSet as = new AnimationSet(true);
        as.addAnimation(aa);
        as.addAnimation(ra);
        as.addAnimation(sa);

        //显示动画
        rl_root.startAnimation(as);
    }


}
