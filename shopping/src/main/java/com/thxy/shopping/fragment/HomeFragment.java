package com.thxy.shopping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thxy.shopping.R;
import com.thxy.shopping.activity.ArticleItemActivity;
import com.thxy.shopping.activity.SearchArticleActivity;
import com.thxy.shopping.adapter.AdvertiseAdapter;
import com.thxy.shopping.adapter.HotArticleAdapter;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.service.ShopService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private GridView hotArticlesGV;
    private List<Article> articles;
    /**
     * 定义广告轮播的视图切换对象
     */
    private ViewPager advertiseCarousel;

    private int[] advertiseImages = new int[]{
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5
    };

    private int[] menuIds = new int[]{
            R.id.iv_menuImag1,
            R.id.iv_menuImag2,
            R.id.iv_menuImag3,
            R.id.iv_menuImag4,
            R.id.iv_menuImag5
    };

    private ImageView[] advertiseMenuImages = new ImageView[menuIds.length];
    /**
     * 定义一个定时器
     */
    private Timer timer;

    /** 搜索按钮 */
    private TextView searchMenu;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0x110:
                    initHotArticleView();
                    break;
                case 0x111:
                    Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case 0x113:
                    switchAdvertiseImage();
                    break;
            }
            return false;
        }
    });

    private void switchAdvertiseImage() {
        int currentPosition = advertiseCarousel.getCurrentItem();
        currentPosition += 1;
        advertiseCarousel.setCurrentItem(currentPosition % 5);
        checkAdvertiseMenu(currentPosition % 5);
    }

    private void initHotArticleView() {
        HotArticleAdapter hotArticleAdapter = new HotArticleAdapter(articles, getActivity());
        hotArticlesGV.setAdapter(hotArticleAdapter);

        hotArticlesGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = articles.get(position);
                Intent intent = new Intent(HomeFragment.this.getActivity(), ArticleItemActivity.class);
                intent.putExtra("article", article);
                intent.putExtra("image", article.bitmap);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View homeView = inflater.inflate(R.layout.fragment_home, container, false);


        hotArticlesGV = (GridView) homeView.findViewById(R.id.gv_hotArticlesGV);
        advertiseCarousel = (ViewPager) homeView.findViewById(R.id.vp_advertisingCarousel);
        searchMenu = (TextView) homeView.findViewById(R.id.tv_searchMenu);

        searchMenu.setOnClickListener(this);

        for (int i = 0; i < menuIds.length; i++) {
            advertiseMenuImages[i] = (ImageView) homeView.findViewById(menuIds[i]);


        }

        initView();

        /** 初始化广告轮播 */
        initAdvertiseCarousel();

        initAdvertiseTimer();

        return homeView;
    }

    private void initAdvertiseTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x113);
            }
        }, 5000, 5000);
    }

    private void initAdvertiseCarousel() {
        AdvertiseAdapter advertiseAdapter = new AdvertiseAdapter(advertiseImages, getActivity(), advertiseCarousel);

        advertiseCarousel.setAdapter(advertiseAdapter);

        /** 给广告轮播绑定滚动改变事件 */
        advertiseCarousel.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkAdvertiseMenu(position % 5);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /** 给广告轮播绑定操作触摸事件 */
        advertiseCarousel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    timer.cancel();
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    initAdvertiseTimer();
                }
                return false;
            }
        });
    }

    private void checkAdvertiseMenu(int position) {
        for (int i = 0; i < menuIds.length; i++) {
            ImageView menuImage = advertiseMenuImages[i];
            menuImage.setImageResource(R.drawable.indicator_unchecked);
            if (i == position) {
                menuImage.setImageResource(R.drawable.indicator_checked);
            }
        }
    }

    private void initView() {

        new Thread() {
            @Override
            public void run() {
                articles = ShopService.getHotArticles();
                if (articles != null && articles.size() > 0) {
                    handler.sendEmptyMessage(0x110);
                } else {
                    handler.sendEmptyMessage(0x111);
                }
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_searchMenu) {
            Intent intent = new Intent(getActivity(), SearchArticleActivity.class);
            startActivity(intent);
        }
    }
}
