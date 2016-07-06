package com.thxy.mobileguard.utils;

/**
 * Created by dongwei on 2016/7/5.
 */
public class JsonStrTools {
    /**
     * @param json  json的字符串
     * @return  把json的特殊字符做了转换处理
     */
    public static String changeStr(String json) {
        json = json.replaceAll(",", "，");
        json = json.replaceAll(":", "：");
        json = json.replaceAll("\\[", "【");
        json = json.replaceAll("\\]", "】");
        json = json.replaceAll("\\{", "<");
        json = json.replaceAll("\\}", ">");
        json = json.replaceAll("\"", "”");

        return json.toString();
    }
}
