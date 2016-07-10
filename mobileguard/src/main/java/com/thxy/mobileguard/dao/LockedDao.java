package com.thxy.mobileguard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thxy.mobileguard.db.LockedDB;
import com.thxy.mobileguard.domain.LockedTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序锁的数据存取层
 * Created by dongwei on 2016/7/7.
 */
public class LockedDao {
    private LockedDB lockdb;
    private Context context;

    public LockedDao(Context context) {
        this.context = context;
        lockdb = new LockedDB(context);
    }

    /**
     * 判断软件是否加锁
     *
     * @param packName app的包名
     * @return 是否加锁
     */
    public boolean isLocked(String packName) {
        boolean res = false;
        SQLiteDatabase database = lockdb.getReadableDatabase();
        Cursor cursor = database.rawQuery("select 1 from " + LockedTable.TABLENAME + " where " +
                LockedTable.PACKNAME + "=?", new String[]{packName});
        if (cursor.moveToNext()) {
            res = true;
        }
        cursor.close();
        database.close();

        return res;
    }

    /**
     * 对程序加锁
     *
     * @param packName 要加锁的软件的包名
     */
    public void add(String packName) {
        SQLiteDatabase database = lockdb.getWritableDatabase();
        //表中列的封装
        ContentValues values = new ContentValues();
        values.put(LockedTable.PACKNAME, packName);
        //往表中添加一条数据
        database.insert(LockedTable.TABLENAME, null, values);

        database.close();
        // 发送内容观察者的通知
        context.getContentResolver().notifyChange(LockedTable.uri, null);
    }

    /**
     * 对程序解锁
     *
     * @param packName 要解锁的软件的包名
     */
    public void remove(String packName) {
        SQLiteDatabase database = lockdb.getWritableDatabase();
        //从表中删除一条数据
        database.delete(LockedTable.TABLENAME, LockedTable.PACKNAME + "=?", new
                String[]{packName});

        database.close();

        //发送内容观察者的通知
        context.getContentResolver().notifyChange(LockedTable.uri, null);
    }

    /**
     * 获取所有软件包名
     *
     * @return 获取所有加锁软件包名
     */
    public List<String> getAllLockedDatas() {
        List<String> lockedNames = new ArrayList<String>();
        SQLiteDatabase database = lockdb.getReadableDatabase();

        Cursor cursor = database.rawQuery("select " + LockedTable.PACKNAME + " from " +
                LockedTable.TABLENAME, null);
        while (cursor.moveToNext()) {
            //取出包名，添加数据
            lockedNames.add(cursor.getString(0));
        }
        cursor.close();
        database.close();

        return lockedNames;
    }
}
