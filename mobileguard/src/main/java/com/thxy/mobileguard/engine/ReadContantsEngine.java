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
