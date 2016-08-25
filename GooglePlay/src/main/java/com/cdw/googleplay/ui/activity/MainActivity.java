package com.cdw.googleplay.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cdw.googleplay.R;
import com.cdw.googleplay.ui.fragment.BaseFragment;
import com.cdw.googleplay.ui.fragment.FragmentFactory;
import com.cdw.googleplay.ui.view.PagerTab;
import com.cdw.googleplay.utils.UIUtils;

public class MainActivity extends BaseActivity {
    private PagerTab mPagerTab;
    private ViewPager mViewPager;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerTab = (PagerTab) findViewById(R.id.pt_main_tab);
        mViewPager = (ViewPager) findViewById(R.id.vp_main_viewpager);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // 将指针和viewpager绑定在一起
        mPagerTab.setViewPager(mViewPager);

        mPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                BaseFragment fragment = FragmentFactory.createFragment(position);
                //开始加载数据
                fragment.loadData();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyAdapter extends FragmentPagerAdapter{

        private String[] mTabNames;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        // 返回页签标题


        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }

        // 返回当前页面位置的fragment对象
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        // fragment数量
        @Override
        public int getCount() {
            return mTabNames.length;
        }
    }
}
