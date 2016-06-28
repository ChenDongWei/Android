package com.thxy.mobileguard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thxy.mobileguard.db.BlackDB;
import com.thxy.mobileguard.domain.BlackBean;
import com.thxy.mobileguard.domain.BlackTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单数据的业务封装类
 * Created by dongwei on 2016/6/28.
 */
public class BlackDao {
    private BlackDB blackDB;

    public BlackDao(Context context) {
        this.blackDB = new BlackDB(context);
    }

    /**
     * @return 所有的黑名单数据
     */
    public List<BlackBean> getAllDatas() {
        List<BlackBean> datas = new ArrayList<BlackBean>();
        SQLiteDatabase database = blackDB.getReadableDatabase();
        Cursor cursor = database.rawQuery("select " + BlackTable.PHONE + ","
                + BlackTable.MODE + " from " + BlackTable.BLACKTABLE, null);
        while (cursor.moveToNext()) {
            BlackBean bean = new BlackBean();
            bean.setPhone(cursor.getString(0));
            bean.setMode(cursor.getInt(1));
            datas.add(bean);
        }
        cursor.close();
        database.close();
        return datas;
    }

    /**
     * 删除黑名单号码
     *
     * @param phone 要删除的黑名单号码
     */
    public void delete(String phone) {
        SQLiteDatabase db = this.blackDB.getWritableDatabase();
        //根据黑名单号码删除黑名单数据
        db.delete(BlackTable.BLACKTABLE, BlackTable.PHONE + "=?", new String[]{phone});
        db.close();
    }

    /**
     * @param phone 修改的黑名单号码
     * @param mode  修改的新的拦截模式
     */
    public void update(String phone, int mode) {
        SQLiteDatabase db = this.blackDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        //设置新的拦截模式
        values.put(BlackTable.MODE, mode);
        //根据号码更新新的模式
        db.update(BlackTable.BLACKTABLE, values, BlackTable.PHONE + "=?", new String[]{phone});
        db.close();
    }

    /**
     * 添加黑名单号码
     *
     * @param bean
     */
    public void add(BlackBean bean) {
        add(bean.getPhone(), bean.getMode());
    }

    /**
     * 添加黑名单号码
     *
     * @param phone 黑名单号码
     * @param mode  拦截模式
     */
    public void add(String phone, int mode) {
        //获取黑名单数据库
        SQLiteDatabase db = blackDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        //设置黑名单号码
        values.put(BlackTable.PHONE, phone);
        //设置黑名单拦截模式
        values.put(BlackTable.MODE, mode);
        //往黑名单表中插入一条数据
        db.insert(BlackTable.BLACKTABLE, null, values);
        //关闭数据库
        db.close();
    }
}
