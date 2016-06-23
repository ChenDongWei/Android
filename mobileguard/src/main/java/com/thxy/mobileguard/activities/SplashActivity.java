package com.thxy.mobileguard.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.thxy.mobileguard.R;
import com.thxy.mobileguard.domain.UrlBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 手机卫士splash界面
 */
public class SplashActivity extends Activity {

    private static final int LOADMAIN = 1;//加载主界面
    private static final int SHOWUPDATEDIALOG = 2;//显示是否更新的对话框
    private static final int ERROR = 3;//错误的统一代号
    private RelativeLayout rl_root;//界面根布局组件
    private int versionCode;//版本号
    private String versionName;//版本名
    private UrlBean parseJson;//url信息封装bean
    private TextView tv_versionName;//显示版本名的组件
    private long startTimeMillis;//记录开始反问网络的时间
    private ProgressBar pb_download;//下载新版本的进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
        //检测服务器版本
        checkVersion();
    }

    private void initData() {
        //获取自己的版本信息
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //当前版本号
            versionCode = packageInfo.versionCode;
            //版本名
            versionName = packageInfo.versionName;
            //设置textView显示内容
            tv_versionName.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            //异常不会发生
            e.printStackTrace();
        }
    }

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader bfr = null;
                HttpURLConnection conn = null;
                int errorCode = -1;//正常，没有错误
                try {
                    startTimeMillis = System.currentTimeMillis();
                    URL url = new URL("http://192.168.1.106:8080/guardversion.json");
                    conn = (HttpURLConnection) url.openConnection();
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
                        bfr = new BufferedReader(new InputStreamReader(is));
                        //读取一行信息
                        String line = bfr.readLine();
                        StringBuilder json = new StringBuilder();
                        while (line != null) {
                            json.append(line);
                            line = bfr.readLine();
                        }
                        //解析json数据
                        parseJson = parseJson(json);

                    } else {
                        errorCode = 404;
                    }
                } catch (MalformedURLException e) {
                    errorCode = 4002;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络连接失败
                    errorCode = 4001;
                    e.printStackTrace();
                } catch (JSONException e) {
                    //json格式错误
                    errorCode = 4003;
                    e.printStackTrace();
                } finally {
//                    if (errorCode == -1) {
//                        //判断是否有新版本
//                        isNewVersion(parseJson);
//                    } else {
//                        Message msg = Message.obtain();
//                        msg.what = ERROR;
//                        msg.arg1 = errorCode;
//                        handler.sendMessage(msg);
//                    }
                    Message msg = Message.obtain();
                    if (errorCode == -1){
                        msg.what = isNewVersion(parseJson);
                    } else {
                        msg.what = ERROR;
                        msg.arg1 = errorCode;
                    }
                    long endTimeMillis = System.currentTimeMillis();
                    if (endTimeMillis - startTimeMillis < 3000) {
                        //设置休眠时间，保证至少休眠3秒
                        SystemClock.sleep(3000 - (endTimeMillis - startTimeMillis));
                    }
                    handler.sendMessage(msg);

                    try {
                        if (bfr == null || conn == null) {
                            return;
                        }
                        bfr.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //处理消息
            switch (msg.what) {
                case LOADMAIN:
                    //进入主界面
                    loadMain();
                    break;
                case ERROR:
                    switch (msg.arg1) {
                        case 404:
                            Toast.makeText(getApplicationContext(), "404资源找不到", Toast
                                    .LENGTH_SHORT).show();

                            break;
                        case 4001:
                            Toast.makeText(getApplicationContext(), "4001无法连接网络", Toast
                                    .LENGTH_SHORT).show();

                            break;
                        case 4003:
                            Toast.makeText(getApplicationContext(), "4003JSON数据格式错误", Toast
                                    .LENGTH_SHORT).show();

                            break;
                        default:
                            break;
                    }
                    loadMain();
                    break;
                case SHOWUPDATEDIALOG:
                    showUpdateDialog();
                    break;
                default:
                    break;
            }
        }

    };

    private void loadMain() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    ;

    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //让用户禁用取消操作
        //builder.setCancelable(false);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                loadMain();
            }
        })
                .setTitle("提醒")
                .setMessage("是否更新新版本?新版本功能如下：" + parseJson.getDesc())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //更新apk
                        System.out.println("==========更新。。。");
                        //访问网络，下载新的apk
                        downLoadNewApk();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进入主界面
                loadMain();
            }
        });
        builder.show();
    }

    private void downLoadNewApk() {
        HttpUtils utils = new HttpUtils();
        //先删除xx.apk
        File file = new File("/mnt/sdcard/xx.apk");
        file.delete();
        utils.download(parseJson.getUrl(), "/mnt/sdcard/xx.apk", new RequestCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                pb_download.setVisibility(View.VISIBLE);
                //设置进度条最大值
                pb_download.setMax((int) total);
                //设置当前进度值
                pb_download.setProgress((int) current);
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                //下载成功
                Toast.makeText(getApplicationContext(), "下载新版本成功!", Toast.LENGTH_SHORT)
                        .show();
                //安装apk
                installApk();
                pb_download.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //下载失败
                Toast.makeText(getApplicationContext(), "下载新版本失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void installApk() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String type = "application/vnd.android.package-archive";
        Uri data = Uri.fromFile(new File("/mnt/sdcard/xx.apk"));

        intent.setDataAndType(data, type);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果用户取消更新apk，直接进入主界面
        loadMain();
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected int isNewVersion(UrlBean parseJson) {
        //获取服务器的版本
        int serverCode = parseJson.getVersionCode();

        //对比自己的版本
        if (serverCode == versionCode) {
            return LOADMAIN;

            //进入主界面
//            Message msg = Message.obtain();
//            msg.what = LOADMAIN;
//            handler.sendMessage(msg);
        } else {
            return SHOWUPDATEDIALOG;
        }
    }


    protected UrlBean parseJson(StringBuilder jsonString) throws JSONException {
        UrlBean bean = new UrlBean();
        JSONObject jobject;

        //把json字符串数据封装成json数据
        jobject = new JSONObject(jsonString + "");
        int versionCode = jobject.getInt("version");
        String url = jobject.getString("url");
        String desc = jobject.getString("desc");
        //数据封装到bean对象中
        bean.setVersionCode(versionCode);
        bean.setUrl(url);
        bean.setDesc(desc);

        return bean;
    }


    private void initView() {
        setContentView(R.layout.activity_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);
        pb_download = (ProgressBar) findViewById(R.id.pb_splash_download_progress);
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
