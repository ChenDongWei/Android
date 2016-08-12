package com.cdw.smartbeijing.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cdw.smartbeijing.activity.MainActivity;
import com.cdw.smartbeijing.base.BaseMenuDetailPager;
import com.cdw.smartbeijing.base.BasePager;
import com.cdw.smartbeijing.base.impl.menu.InteractMenuDetailPager;
import com.cdw.smartbeijing.base.impl.menu.NewsMenuDetailPager;
import com.cdw.smartbeijing.base.impl.menu.PhotosMenuDetailPager;
import com.cdw.smartbeijing.base.impl.menu.TopicMenuDetailPager;
import com.cdw.smartbeijing.domain.NewsMenu;
import com.cdw.smartbeijing.fragment.LeftMenuFragment;
import com.cdw.smartbeijing.utils.CacheUtils;
import com.cdw.smartbeijing.utils.MyConstants;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * 新闻中心
 * Created by dongwei on 2016/8/6.
 */
public class NewsCenterPage extends BasePager {
    private NewsMenu mNewsData;
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;//菜单详情集合

    public NewsCenterPage(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
//        TextView view = new TextView(mActivity);
//        view.setText("新闻中心");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//
//        flContent.addView(view);

        //修改页面标题
        tvTitle.setText("新闻中心");
        //先判断是否有缓存，有的话加载缓存
        String cache = CacheUtils.getCache(mActivity, MyConstants.CATEGORY_URL);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
            //从服务器获取数据,XUtils
            getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, MyConstants.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println(result);
                //Gson解析数据
                processData(result);
                //写缓存
                CacheUtils.setCache(mActivity, MyConstants.CATEGORY_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(String json) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(json, NewsMenu.class);

        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment fragment = mainUI.getLeftMenuFragment();
        //给侧边栏设置数据
        fragment.setMenuData(mNewsData.data);
        //初始化四个菜单详情页面
        mMenuDetailPagers = new ArrayList<>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
        //将新闻菜单详情页设置成默认页面
        setCurrentDetailPager(0);
    }

    //设置菜单详情页
    public void setCurrentDetailPager(int position){
        //重新给frameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);//获取当前应该显示的页面
        View view = pager.mRootView;//当前页面的布局
        flContent.removeAllViews();//清楚以前的所有布局
        flContent.addView(view);

        //初始化数据
        pager.initData();
        //更新标题
        tvTitle.setText(mNewsData.data.get(position).title);
    }

}
