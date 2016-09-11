package com.dponline.test.app_test_004;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class MainActivity extends Activity {
    private TextView tv;

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv_show);

        String url = "http://dev.dponline.com.cn/appservice_8_0/register"; // 定义请求的地址
        RequestParams params = new RequestParams();
        params.put("reporterPositionId", 2); // 设置请求的参数名和参数值
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  byte[] responseBody) {
                String json = "";
                if (statusCode == 200) {
                    json = new String(responseBody);
                    tv.setText(json); // 设置显示的文本

                    Gson gson = new Gson();
                    JsonBean data = new JsonBean();
                    data = gson.fromJson(json, JsonBean.class);
                    tv.setText("解析结果:" + data);
                }
            }

            /**
             * 失败处理的方法
             * error：响应失败的错误信息封装到这个异常对象中
             */
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  byte[] responseBody, Throwable error) {
                error.printStackTrace();// 把错误信息打印出轨迹来
            }
        });

    }

}
