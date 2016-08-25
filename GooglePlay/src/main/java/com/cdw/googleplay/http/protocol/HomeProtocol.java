package com.cdw.googleplay.http.protocol;

import com.cdw.googleplay.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 首页网络数据解析
 * Created by dongwei on 2016/8/24.
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getParams() {
        return "";//如果没有参数，就传空字符串，不能传null
    }

    @Override
    public ArrayList<AppInfo> parseData(String result) {
        //使用JsonObject解析：如果遇到{},就是JsonObject；如果遇到[],就是JsonArry
        try {
            JSONObject jo = new JSONObject(result);
            //解析引用列表
            JSONArray jaList = jo.getJSONArray("list");

            ArrayList<AppInfo> appInfoList = new ArrayList<>();
            for (int i = 0; i < jaList.length(); i++) {
                JSONObject jo1 = jaList.getJSONObject(i);

                AppInfo info = new AppInfo();
                info.des = jo1.getString("des");
                info.downloadUrl = jo1.getString("downloadUrl");
                info.iconUrl = jo1.getString("iconUrl");
                info.id = jo1.getString("id");
                info.name = jo1.getString("name");
                info.packageName = jo1.getString("packageName");
                info.size = jo1.getLong("size");
                info.stars = (float) jo1.getDouble("stars");

                appInfoList.add(info);
            }
            //初始化轮播条的数据
            JSONArray jaPicture = jo.getJSONArray("picture");
            ArrayList<String> pictures = new ArrayList<>();
            for (int i = 0; i < jaPicture.length(); i++){
                String pic = jaPicture.getString(i);
                pictures.add(pic);
            }
            return appInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
