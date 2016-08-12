package com.cdw.smartbeijing.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.base.BaseMenuDetailPager;
import com.cdw.smartbeijing.domain.NewsMenu.NewsTabData;
import com.cdw.smartbeijing.domain.NewsTabBean;
import com.cdw.smartbeijing.domain.NewsTabBean.NewsData;
import com.cdw.smartbeijing.domain.NewsTabBean.TopNews;
import com.cdw.smartbeijing.utils.CacheUtils;
import com.cdw.smartbeijing.utils.MyConstants;
import com.cdw.smartbeijing.view.PullToRefreshListView;
import com.cdw.smartbeijing.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * 页签页面对象
 * Created by dongwei on 2016/8/8.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private NewsTabData mTabData;
    //    private TextView view;
    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;
    @ViewInject(R.id.tv_tab_detail_title)
    private TextView tvTitle;
    @ViewInject(R.id.tab_detail_indicator)
    private CirclePageIndicator mIndicator;
    @ViewInject(R.id.lv_tab_detail_list)
    private PullToRefreshListView lvList;

    private String mUrl;//请求得到的网络链接
    private ArrayList<TopNews> mTopNews;
    private ArrayList<NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;

    public TabDetailPager(Activity activity, NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;

        mUrl = MyConstants.SERVICE_URL + mTabData.url;
    }

    @Override
    public View initView() {
//        view = new TextView(mActivity);
//        //view.setText(mTabData.title);此处空指针异常
//        view.setTextColor(Color.RED);
//        view.setTextSize(26);
//        view.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        //给listView添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_head, null);
        ViewUtils.inject(this, mHeaderView);
        lvList.addHeaderView(mHeaderView);

        /**
         * 5.前端界面设置回调
         */
        lvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                getDataFromService();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        //view.setText(mTabData.title);
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromService();
    }

    private void getDataFromService() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);

                CacheUtils.setCache(mActivity, mUrl, result);

                //收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //请求失败
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

                //收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    protected void processData(String result) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);

        mTopNews = newsTabBean.data.topnews;
        if (mTopNews != null) {
            mViewPager.setAdapter(new TopNewsAdapter());
            mIndicator.setViewPager(mViewPager);
            mIndicator.setSnap(true);

            //事件设置给Indicator
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int
                        positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //更新头条新闻的标题
                    TopNews topNews = mTopNews.get(position);
                    tvTitle.setText(topNews.title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //更新第一个头条新闻标题
            tvTitle.setText(mTopNews.get(0).title);
            //默认让第一个选中
            mIndicator.onPageSelected(0);
        }
        //列表新闻
        mNewsList = newsTabBean.data.news;
        mNewsAdapter = new NewsAdapter();
        lvList.setAdapter(mNewsAdapter);
    }

    //头条新闻数据适配器
    class TopNewsAdapter extends PagerAdapter {
        private BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);

            view.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片缩放方式，填充父控件
            String imageUrl = mTopNews.get(position).topimage;
            //下载图片-将图片设置给imageView，添加缓存避免内存溢出
            mBitmapUtils.display(view, imageUrl);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class NewsAdapter extends BaseAdapter {
        private BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_news_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_news_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_item_news_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsData news = getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);
            mBitmapUtils.display(holder.ivIcon, news.listimage);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
