package com.thxy.mobileguard.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.thxy.mobileguard.R;
import com.thxy.mobileguard.dao.AntiVirusDao;
import com.thxy.mobileguard.domain.AppBean;
import com.thxy.mobileguard.engine.AppManagerEngine;
import com.thxy.mobileguard.utils.Md5Utils;
import com.thxy.mobileguard.utils.MyConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AntivirusActivity extends AppCompatActivity {
    protected static final int SCANNING = 1;
    protected static final int FINISH = 2;
    protected static final int MESSAGE = 3;
    private ImageView iv_scan;
    private TextView tv_scanappname;
    private ProgressBar pb_scan;
    private LinearLayout ll_scancontent;
    private RotateAnimation ra;
    private AntiVirusDao dao;
    private List<AppBean> allApks;
    private int progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        //初始化动画
        initAnimation();
        //检测病毒库
        checkVersion();
        //开始扫描
        //startScan();

    }

    private void checkVersion() {
        final AlertDialog.Builder ab = new AlertDialog.Builder(this);
        final AlertDialog dialog = ab.setTitle("注意").setMessage("正在联网").create();
        dialog.show();
        //访问网络
        HttpUtils utils = new HttpUtils();
        //设置超时时间
        utils.configTimeout(3000);
        utils.send(HttpMethod.GET, MyConstants.VIRUSVERSIONURL, new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        dialog.dismiss();
                        String version = responseInfo.result;
                        if (dao.isNewVirus(Integer.parseInt(version))) {
                            //病毒库是最新的
                            Toast.makeText(getApplicationContext(), "已经是最新的病毒库", Toast
                                    .LENGTH_SHORT).show();
                            startScan();
                        } else {
                            //有新的病毒库
                            isUpdataVirusDialog();
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "联网失败", Toast.LENGTH_SHORT).show();
                        startScan();
                    }
                });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANNING:
                    iv_scan.startAnimation(ra);
                    break;
                case MESSAGE:
                    pb_scan.setMax(allApks.size());
                    pb_scan.setProgress(progress);
                    AntiVirusBean bean = (AntiVirusBean) msg.obj;
                    TextView tv = new TextView(AntivirusActivity.this);
                    if (bean.isVirus) {
                        tv.setTextColor(Color.RED);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    tv.setText(bean.packName);

                    tv_scanappname.setText("正在扫描:" + bean.packName);
                    //加到线性布局
                    ll_scancontent.addView(tv, 0);
                    break;
                case FINISH:
                    iv_scan.clearAnimation();
                    break;
                default:
                    break;
            }
        }
    };

    protected void isUpdataVirusDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("有新病毒库").setMessage("是否更新病毒库?").setPositiveButton("更新", new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载更新病毒库
                HttpUtils utils = new HttpUtils();
                utils.send(HttpMethod.GET, MyConstants.GETVIRUSVDATASURL, new
                        RequestCallBack<String>() {


                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                String virusJson = responseInfo.result;
                                try {
                                    JSONObject jsonObj = new JSONObject(virusJson);
                                    String md5 = jsonObj.getString("md5");
                                    String desc = jsonObj.getString("desc");
                                    dao.addVirus(md5, desc);//添加新的病毒
                                    Toast.makeText(getApplicationContext(), "更新病毒库成功", Toast
                                            .LENGTH_SHORT).show();
                                    startScan();//开始扫描
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                startScan();
                            }
                        });
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startScan();
            }
        });

        ab.show();
    }

    private class AntiVirusBean {
        String packName;
        boolean isVirus;
    }

    private void startScan() {
        new Thread() {
            @Override
            public void run() {
                handler.obtainMessage(SCANNING).sendToTarget();
                //获取所有安装的APK
                allApks = AppManagerEngine.getAllApks(getApplicationContext());
                AntiVirusBean bean = new AntiVirusBean();
                for (AppBean appBean : AntivirusActivity.this.allApks) {
                    bean.packName = appBean.getPackName();
                    if (dao.isVirus(Md5Utils.getFileMD5(appBean.getApkPath()))) {
                        //是病毒
                        bean.isVirus = true;
                    } else {
                        //不是病毒
                        bean.isVirus = false;
                    }
                    progress++;
                    Message msg = handler.obtainMessage(MESSAGE);
                    msg.obj = bean;
                    handler.sendMessage(msg);
                    SystemClock.sleep(50);
                }
                handler.obtainMessage(FINISH).sendToTarget();
            }
        }.start();
    }

    private void initAnimation() {
        //旋转动画
        ra = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(600);
        ra.setRepeatCount(Animation.INFINITE);//无限次数
        //修改旋转动画插入器 ，数学函数
        ra.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float x) {
                return x;
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_antivirus);

        iv_scan = (ImageView) findViewById(R.id.iv_antivirus_scan);
        tv_scanappname = (TextView) findViewById(R.id.tv_antivirus_title);
        pb_scan = (ProgressBar) findViewById(R.id.pb_antivirus_scanprogress);
        ll_scancontent = (LinearLayout) findViewById(R.id.ll_antivirus_results);

        dao = new AntiVirusDao();
    }

}
