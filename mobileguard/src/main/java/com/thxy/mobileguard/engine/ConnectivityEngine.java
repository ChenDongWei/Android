package com.thxy.mobileguard.engine;

import android.content.Context;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 流量统计的业务类
 * Created by dongwei on 2016/7/11.
 */
public class ConnectivityEngine {
    /**
     * @param uid
     * @param context
     * @return 接收的流量信息
     */
    public static String getReceive(int uid, Context context) {
        String res = null;
        //读取流量信息的文件 /proc/uid_stat/uid/tcp_rcv
        String path = "/proc/uid_stat/" + uid + "/tcp_rcv";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream
                        (path)));
            String line = reader.readLine();
            long size = Long.parseLong(line);
            res = Formatter.formatFileSize(context, size);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param uid
     * @param context
     * @return 发送的流量信息
     */
    public static String getSend(int uid, Context context) {
        String res = null;
        String path = "/proc/uid_stat/" + uid + "/tcp_snd";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream
                    (path)));
            String line = reader.readLine();
            long size = Long.parseLong(line);
            res = Formatter.formatFileSize(context, size);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
