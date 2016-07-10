package com.thxy.mobileguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建程序锁的数据库,存放加锁软件的包名
 * Created by dongwei on 2016/7/7.
 */
public class LockedDB extends SQLiteOpenHelper {

    public LockedDB(Context context) {
        super(context, "locked.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建加锁软件表
        db.execSQL("create table locked(_id integer primary key autoincrement,packname text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table locked");
        onCreate(db);
    }
}
