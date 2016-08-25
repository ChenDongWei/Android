package com.cdw.googleplay.http.protocol;

import com.cdw.googleplay.http.HttpHelper;
import com.cdw.googleplay.http.HttpHelper.HttpResult;
import com.cdw.googleplay.utils.IOUtils;
import com.cdw.googleplay.utils.StringUtils;
import com.cdw.googleplay.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 访问网络的基类
 * Created by dongwei on 2016/8/24.
 */
public abstract class BaseProtocol<T> {
    public T getData(int index) {
        //判断有没有缓存
        String result = getCache(index);
        //判断缓存有没有失效
        if (StringUtils.isEmpty(result)) {
            result = getDataFromServer(index);
        }
        //开始解析
        if (result != null) {
            T data = parseData(result);
            return data;
        }
        return null;
    }

    /**
     * 从网络获取数据
     *
     * @param index 表示从哪个位置开始返回20条数据，用于分页
     */
    private String getDataFromServer(int index) {
        HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey() +
                "?index=" + index + getParams());
        System.out.println("httpResult" + HttpHelper.URL + getKey() +
                "?index=" + index + getParams());
        if (httpResult != null) {
            String result = httpResult.getString();
            //写缓存
            if (!StringUtils.isEmpty(result)) {
                setCache(index, result);
            }
            return result;
        }
        return null;
    }

    //获取网络链接关键词
    public abstract String getKey();

    //获取网络链接参数
    public abstract String getParams();

    /**
     * 写缓存
     *
     * @param index
     * @param json
     */
    public void setCache(int index, String json) {
        //获取本应用的缓存文件夹
        File cacheDir = UIUtils.getContext().getCacheDir();
        //生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParams());

        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
            //缓存失效截止时间
            long deadline = System.currentTimeMillis() + 30 * 60 * 1000;
            writer.write(deadline + "\n");
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    /**
     * 读缓存
     *
     * @param index
     * @return
     */
    public String getCache(int index) {
        //获取本应用的缓存文件夹
        File cacheDir = UIUtils.getContext().getCacheDir();
        //生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParams());
        //判断缓存是否存在
        if (cacheFile.exists()) {
            //判断日期是否有效
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                String deadLine = reader.readLine();
                long deadtime = Long.parseLong(deadLine);
                if (System.currentTimeMillis() < deadtime) {
                    //缓存有效
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

    //解析数据
    public abstract T parseData(String result);
}
