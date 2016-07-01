package com.thxy.mobileguard.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.thxy.mobileguard.domain.ContantBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取手机联系人的功能类
 * Created by dongwei on 2016/6/24.
 */
public class ReadContantsEngine {

    public static List<ContantBean> readSmslog(Context context) {
        //通过内容提供者访问电话日志数据库
        Uri uri = Uri.parse("content://sms");
        //获取电话记录的联系人游标
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"address", "person"},
                null, null, " _id desc");
        List<ContantBean> datas = new ArrayList<ContantBean>();
        while (cursor.moveToNext()) {
            ContantBean bean = new ContantBean();

            String phone = cursor.getString(0);
            String name = cursor.getString(1);
            bean.setName(name);
            bean.setPhone(phone);

            datas.add(bean);
        }
        return datas;
    }

    /**
     * @param context
     * @return 电话日志记录
     */
    public static List<ContantBean> readCalllog(Context context) {
        //通过内容提供者访问电话日志数据库
        Uri uri = Uri.parse("content://call_log/calls");
        //获取电话记录的联系人游标
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"number", "name"},
                null, null, " _id desc");
        List<ContantBean> datas = new ArrayList<ContantBean>();
        while (cursor.moveToNext()) {
            ContantBean bean = new ContantBean();

            String phone = cursor.getString(0);
            String name = cursor.getString(1);
            bean.setName(name);
            bean.setPhone(phone);

            datas.add(bean);
        }
        return datas;
    }

    /**
     * 读取手机联系人
     *
     * @param context
     * @return
     */
    public static List<ContantBean> readContants(Context context) {
        List<ContantBean> datas = new ArrayList<ContantBean>();
        Uri uriContants = Uri.parse("content://com.android.contacts/contacts");
        Uri uriDatas = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = context.getContentResolver().query(uriContants, new String[]{"_id"}, null,
                null, null);
        while (cursor.moveToNext()) {
            //联系人信息的封装bean
            ContantBean bean = new ContantBean();

            String id = cursor.getString(0);

            Cursor cursor2 = context.getContentResolver().query(uriDatas, new String[]{"data1",
                    "mimetype"}, "raw_contact_id = ?", new String[]{id}, null);
            while (cursor2.moveToNext()) {
                String data = cursor2.getString(0);
                String mimeType = cursor2.getString(1);

                if (mimeType.equals("vnd.android.cursor.item/name")) {
                    bean.setName(data);
                } else if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {
                    bean.setPhone(data);
                }
            }
            cursor2.close();
            //添加一条联系人信息
            datas.add(bean);
        }
        cursor.close();
        return datas;
    }
}
