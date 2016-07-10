package com.thxy.mobileguard.domain;

import android.net.Uri;

/**
 * 程序锁数据库表的结构
 * Created by dongwei on 2016/7/7.
 */
public interface LockedTable {
    String TABLENAME = "locked";//程序锁表名
    String PACKNAME = "packname";//程序锁表的列名
    Uri uri = Uri.parse("content://thxy/locked");
}
