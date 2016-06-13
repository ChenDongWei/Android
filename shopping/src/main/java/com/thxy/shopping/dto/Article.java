package com.thxy.shopping.dto;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by dongwei on 2016/6/1.
 */
public class Article implements Serializable {
    public String id;
    public String title;
    public String supplier;
    public double price;
    /** 位图不参与序列化 */
    public transient Bitmap bitmap;
    public String description;
    public int buyNum;
}
