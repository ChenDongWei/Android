package com.thxy.mobileguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 黑名单数据库
 * Created by dongwei on 2016/6/28.
 */
public class BlackDB extends SQLiteOpenHelper {

    /**
     * 初始化版本信息
     *
     * @param context
     */
    public BlackDB(Context context) {
        super(context, "black.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacktb(_id integer primary key autoincrement,phone text,mode " +
                "integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table blacktb");
        onCreate(db);
    }
}
