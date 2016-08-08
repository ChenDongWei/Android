package com.cdw.smartbeijing.fragment;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.activity.MainActivity;
import com.cdw.smartbeijing.base.BasePager;
import com.cdw.smartbeijing.base.impl.GovAffairsPage;
import com.cdw.smartbeijing.base.impl.HomePage;
import com.cdw.smartbeijing.base.impl.NewsCenterPage;
import com.cdw.smartbeijing.base.impl.SettingPage;
import com.cdw.smartbeijing.base.impl.SmartServicePage;
import com.cdw.smartbeijing.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * 主页面frament
 */
public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mPagers;
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();

        mPagers.add(new HomePage(mActivity));
        mPagers.add(new NewsCenterPage(mActivity));
        mPagers.add(new SmartServicePage(mActivity));
        mPagers.add(new GovAffairsPage(mActivity));
        mPagers.add(new SettingPage(mActivity));

        mViewPager.setAdapter(new ContentAdapter());
        //底栏标签切换监听
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        /**
                         * 首页，false为取消平滑效果
                         */
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();

                if (position == 0 || position == mPagers.size() - 1){
                    //禁用侧边栏
                    setSlidingMenuEnable(false);
                }else{
                    //启用侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动加载第一页数据
        mPagers.get(0).initData();
        //首页禁用侧边栏
        setSlidingMenuEnable(false);
    }

    private void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class ContentAdapter extends PagerAdapter {

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
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView;//获取当前页面对象的布局
            //pager.initData();//初始化数据,viewpager会自动加载下一个页面，为了节省性能，不在此处调用
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //获取新闻中心页面
    public NewsCenterPage getNewsCenterPager(){
        NewsCenterPage pager = (NewsCenterPage) mPagers.get(1);
        return pager;
    }
}
