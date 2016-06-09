package com.thxy.shopping.service;

import com.thxy.shopping.dto.Article;
import com.thxy.shopping.dto.ArticleType;
import com.thxy.shopping.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dongwei on 2016/6/1.
 */
public class ShopService {
    private static List<Article> hotArticles;

    public static List<Article> getHotArticles() {

        try {
            String result = HttpUtils.sendPost("android/article.action", null);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if (jsonArray != null && jsonArray.length() > 0) {
                List<Article> articles = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Article article = new Article();
                    article.id = jsonObject1.getString("id");
                    article.title = jsonObject1.getString("title");
                    article.price = jsonObject1.getDouble("price");
                    article.bitmap = HttpUtils.getBitmapByImage("images/article/" + jsonObject1.getString("image"));
                    articles.add(article);
                }
                return articles;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Object[] searchArticleByWord(String word, int pageIndex, String typeCode) {

        try {
            Object[] objs = new Object[2];

            Map<String, Object> params = new HashMap<>();
            params.put("pageIndex", pageIndex);

            if (word != null && word != "") {
                params.put("title", word);
            }
            if (typeCode != null && typeCode != "") {
                params.put("code", typeCode);
            }

            String result = HttpUtils.sendPost("android/article.action", params);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            /** 获取当前商品分页总数 */
            int totalSize = jsonObject.getInt("totalSize");

            objs[0] = totalSize;

            List<Article> articles = null;
            if (jsonArray != null && jsonArray.length() > 0) {

                articles = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Article article = new Article();
                    article.id = jsonObject1.getString("id");
                    article.title = jsonObject1.getString("title");
                    article.price = jsonObject1.getDouble("price");
                    /** 从服务端将图片下载到客户端 */
                    article.bitmap = HttpUtils.getBitmapByImage("images/article/" + jsonObject1.getString("image"));
                    articles.add(article);
                }

            }
            objs[1] = articles;
            return objs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<ArticleType> getArticleTypes(String typeCode) {
        try {
            Map<String, Object> params = null;
            if (typeCode != null && typeCode != "") {
                params = new HashMap<>();
                params.put("code", typeCode);
            }
            String result = HttpUtils.sendPost("android/articleType.action", params);
            /** 将查询出来的商品类型转换成json数组 */
            List<ArticleType> articleTypes = null;
            if (result != null) {
                articleTypes = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ArticleType articleType = new ArticleType();
                    articleType.code = jsonObject.getString("code");
                    articleType.name = jsonObject.getString("name").replaceAll("-", "");
                    if (articleType.code.length() == 4) {
                        articleType.typeLogo = HttpUtils.getBitmapByImage("articleType/" + articleType.code + ".jpg");
                    }
                    articleTypes.add(articleType);
                }
            }
            return articleTypes;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
