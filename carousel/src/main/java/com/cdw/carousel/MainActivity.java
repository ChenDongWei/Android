package com.cdw.carousel;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    private LinearLayout mPointContainer;
    private TextView mTvTitle;

    private List<ImageView> mListDatas;

    int[] imgs = {
            R.drawable.icon_1,
            R.drawable.icon_2,
            R.drawable.icon_3,
            R.drawable.icon_4,
            R.drawable.icon_5
    };
    String[] title = {
            "第一张图片",
            "第二张图片",
            "第三张图片",
            "第四张图片",
            "第五张图片"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPointContainer = (LinearLayout) findViewById(R.id.point_container);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        //初始化数据

        mListDatas = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            //给集合添加ImageView
            ImageView iv = new ImageView(this);
            iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            mListDatas.add(iv);

            //添加点
            View point = new View(this);
            point.setBackgroundResource(R.drawable.point_normal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);

            if (i != 0) {
                params.leftMargin = 10;
            } else {
                point.setBackgroundResource(R.drawable.point_selected);

                mTvTitle.setText(title[0]);
            }
            mPointContainer.addView(point, params);


        }

        //设置数据的方式
        mPager.setAdapter(new MyAdapter());

        //设置监听器
        mPager.addOnPageChangeListener(this);

        //设置默认选中的item
        int middle = Integer.MAX_VALUE / 2;
        int extra = middle % mListDatas.size();
        int item = middle - extra;
        mPager.setCurrentItem(item);
    }

    class MyAdapter extends PagerAdapter {

        /**
         * @return 页面的数量
         */
        @Override
        public int getCount() {
            if (mListDatas != null) {
//                return mListDatas.size();
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        /**
         * 标记方法，用来判断缓存标记
         *
         * @param view   显示的view
         * @param object 标记
         * @return 有缓存返回ture
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化item
         *
         * @param container
         * @param position  要加载的位置
         * @return 添加要显示的view
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mListDatas.size();

            ImageView iv = mListDatas.get(position);

            mPager.addView(iv);

            return iv;
        }

        /**
         * 销毁item条目
         *
         * @param container
         * @param position
         * @param object    标记
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            position = position % mListDatas.size();

            ImageView iv = mListDatas.get(position);
            mPager.removeView(iv);
        }
    }

    /**
     * 当viewpager滚动时的回调
     *
     * @param position             当前选中的位置
     * @param positionOffset       滑动的百分比
     * @param positionOffsetPixels 偏移的距离，滑动的像素
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 当viewpager的某个页面选中时的回调
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        position = position % mListDatas.size();

        //设置选中点的样式
        int count = mPointContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mPointContainer.getChildAt(i);
//            if (position == i) {
//                view.setBackgroundResource(R.drawable.point_selected);
//            } else {
//                view.setBackgroundResource(R.drawable.point_normal);
//            }
            view.setBackgroundResource(position == i ? R.drawable.point_selected : R.drawable
                    .point_normal);
        }
            mTvTitle.setText(title[position]);
    }

    /**
     * viewpager滑动状态改变时的回调
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
