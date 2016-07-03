package com.thxy.mobileguard.engine;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 短信备份和还原的业务类封装
 * Created by dongwei on 2016/7/2.
 */
public class SmsEngine {

    public  interface BackupsProgress{
        /**
         * 进度的显示回调
         */
        void show();

        /**
         * @param max   回调显示进度的最大值
         */
        void setMax(int max);

        /**
         * @param progress  回调显示当前进度
         */
        void setProgress(int progress);

        /**
         * 进度完成的回调
         */
        void end();
    }

    private static class Data {
        int progress;
    }

    public static void smsBackupsJson(Activity context, final BackupsProgress pd) {
        //通过内容提供者获取短信
        Uri uri = Uri.parse("content://sms");
        //获取短信记录游标
        final Cursor cursor = context.getContentResolver().query(uri, new String[]{"address",
                "date",
                "body", "type"}, null, null, "_id desc");
        //写到文件中
        File file = new File(Environment.getExternalStorageDirectory(), "sms.json");


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            PrintWriter out = new PrintWriter(fos);
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //进度条的总进度
                    pd.show();
                    pd.setMax(cursor.getCount());
                }
            });

            final Data data = new Data();

            //写根标记     {"count":"10"
            out.println("{\"count\":\"" + cursor.getCount()  + "\"");
            // ,"smses":[
            out.println(",\"smses\":[");
            while (cursor.moveToNext()) {
                data.progress++;
                SystemClock.sleep(300);//模拟进度条显示效果
                //取短信，封装成json标记
                if (cursor.getPosition() == 0) {
                    out.println("{");
                } else {
                    out.println(",{");
                }

                //address 封装  "address":"hello"
                out.println("\"address\":\"" + cursor.getString(0) + "\"," );
                //date 封装
                out.println("\"date\":\"" + cursor.getString(1) + "\"," );
                //body 封装
                out.println("\"body\":\"" + cursor.getString(2) + "\"," );
                //type 封装
                out.println("\"type\":\"" + cursor.getString(3) + "\"" );

                out.println("}");

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.setProgress(data.progress);
                    }
                });

            }

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.end();
                }
            });

            //写根标记结束标记
            out.println("]}");

            out.flush();
            out.close();
            cursor.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void smsBackupsXml(Activity context, final BackupsProgress pd) {
        //通过内容提供者获取短信
        Uri uri = Uri.parse("content://sms");
        //获取短信记录游标
        final Cursor cursor = context.getContentResolver().query(uri, new String[]{"address",
                "date",
                "body", "type"}, null, null, "_id desc");
        //写到文件中
        File file = new File(Environment.getExternalStorageDirectory(), "sms.xml");


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            PrintWriter out = new PrintWriter(fos);
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //进度条的总进度
                    pd.show();
                    pd.setMax(cursor.getCount());
                }
            });

            final Data data = new Data();

            out.println("<smses count='" + cursor.getCount() + "'>");

            while (cursor.moveToNext()) {
                data.progress++;
                SystemClock.sleep(300);//模拟进度条显示效果
                //取短信，封装成xml标记
                out.println("<sms>");
                //address 封装
                out.println("<address>" + cursor.getString(0) + "</address>");
                //date 封装
                out.println("<date>" + cursor.getString(1) + "</date>");
                //body 封装
                out.println("<body>" + cursor.getString(2) + "</body>");
                //type 封装
                out.println("<type>" + cursor.getString(3) + "</type>");

                out.println("</sms>");

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.setProgress(data.progress);
                    }
                });

            }

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.end();
                }
            });

            //写根标记结束标记
            out.println("</smses>");

            out.flush();
            out.close();
            cursor.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
