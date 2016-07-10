package com.thxy.mobileguard.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.thxy.mobileguard.service.AppWidgetService;

/**
 * Created by dongwei on 2016/7/7.
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {
    /**
     * 创建第一个小控件调用
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //启动服务监控内存状态
        Intent service = new Intent(context, AppWidgetService.class);
        context.startService(service);
    }

    /**
     * 删除最后一个小控件调用
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //关闭服务监控内存状态
        Intent service = new Intent(context, AppWidgetService.class);
        context.stopService(service);
    }
}
