package com.thxy.mobileguard.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程的数据封装类
 * Created by dongwei on 2016/7/6.
 */
public class TaskBean {
    private Drawable icon;//apk的图标
    private String name;//apk的名字
    private String packName;//apk的包名
    private long memSize;//apk占用内存的大小
    private boolean isSystem;//是否是系统apk
    private boolean isChecked;//是否被选中

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
