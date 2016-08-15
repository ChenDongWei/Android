package com.cdw.smartbeijing.domain;

import java.util.ArrayList;

/**
 * 组图对象
 * Created by dongwei on 2016/8/15.
 */
public class PhotoBean {
    public PhotoData data;

    public class PhotoData{
        public ArrayList<PhotoNews> news;
    }

    public class PhotoNews{
        public int id;
        public String listimage;
        public String title;
    }
}
