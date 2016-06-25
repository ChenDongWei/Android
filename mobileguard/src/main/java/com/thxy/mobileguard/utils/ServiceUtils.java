package com.thxy.mobileguard.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 判断服务的状态
 * Created by dongwei on 2016/6/24.
 */
public class ServiceUtils {
    public static boolean isServiceRunning(Context context, String serviceName){
        boolean isRunning = false;
        //判断运行中的服务，用ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取android手机中运行的所有服务
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(50);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            //判断服务的名字是否包含我们指定的服务名
            if (runningServiceInfo.service.getClassName().equals(serviceName)){
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
