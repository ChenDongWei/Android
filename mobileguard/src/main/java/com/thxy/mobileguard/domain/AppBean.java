package com.thxy.mobileguard.domain;

import android.graphics.drawable.Drawable;

/**
 * apk信息封装类
 * Created by dongwei on 2016/7/5.
 */
public class AppBean {
    private Drawable icon;//apk的图标
    private String appName;//apk的名字
    private long size;//占用空间的大小 byte
    private boolean isSd;//是否存在sd卡
    private boolean isSystem;//是否是系统apk
    private String packName;//apk的包名
    private String apkPath;//apk的安装路径

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSd() {
        return isSd;
    }

    public void setSd(boolean sd) {
        isSd = sd;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
