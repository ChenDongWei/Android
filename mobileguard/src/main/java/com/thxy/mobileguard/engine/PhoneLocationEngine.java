package com.thxy.mobileguard.engine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电话归属地业务封装类
 * Created by dongwei on 2016/6/30.
 */
public class PhoneLocationEngine {

    public static String locationQuery(String phoneNumber, Context context) {
        String location = phoneNumber;
        Pattern p = Pattern.compile("1{1}[3578]{1}[0-9]{9}");
        Matcher m = p.matcher(phoneNumber);
        boolean b = m.matches();
        if (b){
            //如果是手机号
            location = mobileQuery(phoneNumber, context);
        }else if (phoneNumber.length() >= 11){
            //如果是固定号码
            location = phoneQuery(phoneNumber, context);
        }else {
            //如果是服务号
            location = serviceNumberQuery(phoneNumber, context);
        }

        return location;
    }

    /**
     * 查询服务号码
     * @param phoneNumber
     * @param context
     * @return
     */
    public static String serviceNumberQuery(String phoneNumber, Context context){
        String res = "";
        if (phoneNumber.equals("110")){
            res = "匪警";
        }else if (phoneNumber.equals("10086")){
            res = "中国移动";
        }
        return res;
    }

    /**
     * @param phoneNumber 电话号码全称
     * @param context
     * @return 固定电话归属地
     */
    public static String phoneQuery(String phoneNumber, Context context) {
        String res = phoneNumber;
        SQLiteDatabase database = SQLiteDatabase.openDatabase("/data/data/com.thxy" +
                ".mobileguard/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
        String areaCode = "";
        if (phoneNumber.charAt(1) == '1' || phoneNumber.charAt(1) == '2') {
            //2位的区号
            areaCode = phoneNumber.substring(1, 3);
        } else {
            //3位的区号
            areaCode = phoneNumber.substring(1, 4);
        }
        Cursor cursor = database.rawQuery("select location from data2 where area=?", new
                String[]{areaCode});
        if (cursor.moveToNext()) {
            res = cursor.getString(0);
        }
        return res;
    }

    /**
     * @param phoneNumber 电话号码全称
     * @param context
     * @return 手机号码归属地
     */
    public static String mobileQuery(String phoneNumber, Context context) {
        String res = phoneNumber;
        SQLiteDatabase database = SQLiteDatabase.openDatabase("/data/data/com.thxy" +
                ".mobileguard/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.rawQuery("select location from data2 where id = (select outKey " +
                "from data1 where id=?)", new String[]{phoneNumber.substring(0, 7)});
        if (cursor.moveToNext()) {
            res = cursor.getString(0);
        }
        return res;
    }
}
