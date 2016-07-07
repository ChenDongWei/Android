package com.thxy.mobileguard.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.thxy.mobileguard.domain.AppBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取所有安装的apk详细信息
 * Created by dongwei on 2016/7/5.
 */
public class AppManagerEngine {

    /**
     * @param context
     * @return SD卡可用空间
     */
    public static long getSDAvail(Context context) {
        long sdAvail = 0;
        //获取SD卡目录
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        sdAvail = externalStorageDirectory.getFreeSpace();

        return sdAvail;
    }

    /**
     * @return 返回手机剩余空间
     */
    public static long getRomAvail() {
        long romAvail = 0;
        File dataDirectory = Environment.getDataDirectory();
        romAvail = dataDirectory.getFreeSpace();
        return romAvail;
    }

    /**
     * @param context
     * @return 所有安装的apk信息
     */
    public static List<AppBean> getAllApks(Context context) {
        //获取所有安装的apk信息
        List<AppBean> apks = new ArrayList<AppBean>();
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //所有安装的apk
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            AppBean bean = new AppBean();
            //设置apk的名字
            bean.setAppName(packageInfo.applicationInfo.loadLabel(pm) + "");
            //设置apk的包名
            bean.setPackName(packageInfo.packageName);
            //设置apk的图标
            bean.setIcon(packageInfo.applicationInfo.loadIcon(pm));
            //设置apk的大小
            String sourceDir = packageInfo.applicationInfo.sourceDir;
            File file = new File(sourceDir);
            bean.setSize(file.length());
            //获取当前apk的flag属性
            int flag = packageInfo.applicationInfo.flags;
            //是否是系统apk
            if ((flag & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //系统apk
                bean.setSystem(true);
            } else {
                //用户apk
                bean.setSystem(false);
            }
            //apk安装的位置
            if ((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                //安装在sd卡中的apk
                bean.setSd(true);
            } else {
                //安装在rom中的apk
                bean.setSd(false);
            }

            //添加apk的路径
            bean.setApkPath(packageInfo.applicationInfo.sourceDir);

            //添加一个apk信息
            apks.add(bean);
        }

        return apks;
    }
}
