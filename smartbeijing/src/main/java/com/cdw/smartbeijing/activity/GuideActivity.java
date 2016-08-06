package com.cdw.smartbeijing.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.utils.MyConstants;
import com.cdw.smartbeijing.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 新手引导页面
 */
public class GuideActivity extends Activity {
    private ViewPager mViewPager;
    private ArrayList<ImageView> guids;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private int mPointDis;//小红点移动距离
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initData();

        initEvent();
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                //小红点的左边距
                int leftMargin = (int) (mPointDis * positionOffset) + (position * mPointDis);
                //更新小红点距离
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == guids.size() - 1){
                    btnStart.setVisibility(View.VISIBLE);
                }else {
                    btnStart.setVisibility(View.GONE);
                }
            }



            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp
                PrefUtils.setBoolean(getApplication(), MyConstants.ISSETUP, false);
                //跳转到主页面
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        //
        //计算两个圆点之间的距离
        //measure-->layout(确定位置)-->draw(activity的oncreate方法执行完之后才执行这个流程)
        //mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();

        //监听layout方法结束事件，位置确定好后再获取圆点间距
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            /**
             * layout方法结束时回调
             */
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                //移除监听，避免重复回调
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0)
                        .getLeft();
            }
        });
    }

    private void initData() {
        int[] pics = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

        // 定义Viewpager使用的容器
        guids = new ArrayList<>();
        // 初始化容器中的数据
        for (int i = 0; i < pics.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(pics[i]);
            // 添加界面的数据
            guids.add(view);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);
            //初始化布局参数，父控件是谁，就是谁声明的布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 10;
            }
            //设置布局对象
            point.setLayoutParams(params);
            //给容器添加圆点
            llContainer.addView(point);
        }
        // 设置适配器
        mViewPager.setAdapter(new GuideAdapter());
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guids.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = guids.get(position);
            container.addView(view);

            return view;
        }
    }

    private void initView() {
        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.vp_guide_pages);
        llContainer = (LinearLayout) findViewById(R.id.ll_guide_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        btnStart = (Button) findViewById(R.id.bt_guide_startexp);
    }
}
