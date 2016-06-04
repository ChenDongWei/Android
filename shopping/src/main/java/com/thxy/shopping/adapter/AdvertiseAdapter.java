package com.thxy.shopping.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by dongwei on 2016/6/2.
 */
public class AdvertiseAdapter extends PagerAdapter {
    private int[] advertiseImages;
    private Context context;
    private ViewPager advertiseViewPager;

    public AdvertiseAdapter (int[] advertiseImages, Context context, ViewPager advertiseViewPager){
        this.advertiseImages = advertiseImages;
        this.context = context;
        this.advertiseViewPager = advertiseViewPager;
    }

    @Override
    public int getCount() {

        return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /** 循环生成每一个视图 */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        position = position%5;

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(advertiseImages[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int currentPosition = advertiseViewPager.getCurrentItem();
        if (currentPosition == 9){
            advertiseViewPager.setCurrentItem(4,false);
        }else if (currentPosition == 0){
            advertiseViewPager.setCurrentItem(5,false);
        }
    }
}
