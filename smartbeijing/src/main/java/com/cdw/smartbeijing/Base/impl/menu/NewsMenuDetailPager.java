package com.cdw.smartbeijing.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.activity.MainActivity;
import com.cdw.smartbeijing.base.BaseMenuDetailPager;
import com.cdw.smartbeijing.domain.NewsMenu.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * Created by dongwei on 2016/8/8.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    private ArrayList<NewsTabData> mTabData;//页签网络数据
    private ArrayList<TabDetailPager> mPagers;//页签页面集合

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
        super(activity);
        mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));
            mPagers.add(pager);
        }
        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);//将viewPager和指示器绑定在一起,必须在viewPager设置完数据再绑定

        //设置页面滑动监听,必须给指示器设置页面监听，不能设置给viewPager
        //mViewPager.addOnPageChangeListener(this);
        mIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0){
            //开启侧边栏
            setSlidingMenuEnable(true);
        }else {
            //禁用侧边栏
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 开启或禁用侧边栏
     * @param enable
     */
    protected void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class NewsMenuDetailAdapter extends PagerAdapter {
        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            NewsTabData data = mTabData.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);

            View view = pager.mRootView;
            container.addView(view);

            pager.initData();

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @OnClick(R.id.bt_next)
    public void nextPage(View view){
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }
}
