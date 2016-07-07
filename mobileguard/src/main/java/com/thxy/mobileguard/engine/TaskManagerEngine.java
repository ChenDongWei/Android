package com.thxy.mobileguard.engine;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.thxy.mobileguard.domain.TaskBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取所有运行中进程的信息
 * Created by dongwei on 2016/7/6.
 */
public class TaskManagerEngine {
    /**
     * @param context
     * @return 所有运行中的进程数据
     */
    public static List<TaskBean> getAllRunningTaskInfos(Context context) {
        List<TaskBean> datas = new ArrayList<TaskBean>();
        // 获取运行中的进程
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取包管理器
        PackageManager pm = context.getPackageManager();

            // 获取运行中的进程
            List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                TaskBean bean = new TaskBean();
                //apk的包名
                String processName = runningAppProcessInfo.processName;
                //设置apk的包名
                bean.setPackName(processName);

                //有些进程是无名进程
                PackageInfo packageInfo = null;
                try {
                    packageInfo = pm.getPackageInfo(processName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    //继续循环,不添加没有名字的进程
                    continue;
                }
                //获取apk的图标
                bean.setIcon(packageInfo.applicationInfo.loadIcon(pm));
                //获取apk的名字
                bean.setName(packageInfo.applicationInfo.loadLabel(pm) + "");
                //获取是否是系统apk
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    //系统apk
                    bean.setSystem(true);
                } else {
                    //用户apk
                    bean.setSystem(false);
                }
                //获取占用的内存大小
                android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new
                        int[]{runningAppProcessInfo.pid});
                //获取占用的内存，byte为单位
                long totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
                bean.setMemSize(totalPrivateDirty);
                datas.add(bean);
            }




        return datas;
    }

    /**
     * @param context
     * @return 获取可用内存大小
     */
    public static long getAvailMemSize(Context context) {
        long size = 0;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        //把kb转换成byte
        size = outInfo.availMem;
        return size;
    }

    /**
     * @param context
     * @return 总内存的大小
     */
    public static long getTotalMemSize(Context context) {
        long size = 0;

        /*ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        //获取总内存大小,16级别以上才能用。
        size = outInfo.totalMem;*/

        //读取配置文件来获取内存总大小
        File file = new File("/proc/meminfo");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream
                    (file)));
            String totalMemInfo = reader.readLine();
            int startIndex = totalMemInfo.indexOf(':');
            int endIndex = totalMemInfo.indexOf('k');
            totalMemInfo = totalMemInfo.substring(startIndex + 1, endIndex).trim();
            size = Long.parseLong(totalMemInfo);
            size *= 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
