package com.cdw.smartbeijing.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cdw.smartbeijing.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class NewsDetailActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.ll_control)
    private LinearLayout llControl;
    @ViewInject(R.id.bt_back)
    private ImageButton btnBack;
    @ViewInject(R.id.bt_share)
    private ImageButton btnShare;
    @ViewInject(R.id.bt_textsize)
    private ImageButton btnTextSize;
    @ViewInject(R.id.bt_menu)
    private ImageButton btnMenu;
    @ViewInject(R.id.wv_news_detail)
    private WebView mWebView;
    @ViewInject(R.id.pb_news_loading)
    private ProgressBar pbLoading;

    private String mUrl;

    private int mTempWhich;//点击确定前记录临时选择的字体大小
    private int mCurrenWhich = 2;//点击确定后记录当前选定的字体大小,默认为正常

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);

        llControl.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);

        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");

        mWebView.loadUrl(mUrl);

        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);//显示缩放按钮(wap网页不支持)
        settings.setUseWideViewPort(true);//支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true);//支持js功能

        mWebView.setWebViewClient(new WebViewClient() {
            //开始加载网页

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("网页加载开始");
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
                pbLoading.setVisibility(View.INVISIBLE);
            }

            //所有链接跳转都会经过此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转到" + url);
                view.loadUrl(url);//跳转链接时强制在当前webview中加载
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * 加载进度
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 网页标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                this.finish();
                break;
            case R.id.bt_textsize:
                //修改网页字体
                showChooseDialog();
                break;
            case R.id.bt_share:
                showShare();
                break;
            default:
                break;
        }
    }

    /**
     * 暂时选择字体大小的弹框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体大小");

        String[] items = new String[]{
                "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"
        };

        builder.setSingleChoiceItems(items, mCurrenWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //根据选择的字体修改网页字体大小
                WebSettings settings = mWebView.getSettings();
                switch (mTempWhich){
                    case 0:
                        settings.setTextZoom(200);
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(75);
                        break;
                    case 4:
                        settings.setTextZoom(50);
                        break;
                    default:
                        break;
                }

                mCurrenWhich = mTempWhich;
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();

        oks.setTheme(OnekeyShareTheme.SKYBLUE);//修改主题样式

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
