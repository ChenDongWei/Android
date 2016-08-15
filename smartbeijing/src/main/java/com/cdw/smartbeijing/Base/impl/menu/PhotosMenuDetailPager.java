package com.cdw.smartbeijing.base.impl.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.base.BaseMenuDetailPager;
import com.cdw.smartbeijing.domain.PhotoBean;
import com.cdw.smartbeijing.utils.CacheUtils;
import com.cdw.smartbeijing.utils.MyConstants;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * 菜单详情页-组图
 * Created by dongwei on 2016/8/8.
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {
    private ListView lvPhoto;
    private GridView gvPhoto;
    private ArrayList<PhotoBean.PhotoNews> mNewsList;
    private boolean isListView = true;//标记当前是否是listView
    private ImageButton btnPhoto;

    public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.btnPhoto = btnPhoto;
        btnPhoto.setOnClickListener(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        lvPhoto = (ListView) view.findViewById(R.id.lv_photo_menu_detail);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo_menu_detail);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, MyConstants.PHOTOS_URL);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFromServer();
    }

    private void processData(String result) {
        Gson gson = new Gson();
        PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);

        mNewsList = photoBean.data.news;

        lvPhoto.setAdapter(new PhotoAdapter());
        gvPhoto.setAdapter(new PhotoAdapter());
    }

    @Override
    public void onClick(View v) {
        if (isListView){
            //切换成gridView
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);
            btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
            isListView = false;
        }else {
            //切换成listView
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.GONE);
            btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
            isListView = true;
        }
    }

    class PhotoAdapter extends BaseAdapter{

        private BitmapUtils mBitmapUtils;

        public PhotoAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }
        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotoBean.PhotoNews getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(mActivity, R.layout.list_item_photo, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_list_item_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_list_item_title);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoBean.PhotoNews item = getItem(position);
            holder.tvTitle.setText(item.title);
            mBitmapUtils.display(holder.ivPic, item.listimage);
            return convertView;
        }
    }

    static class ViewHolder{
        public ImageView ivPic;
        public TextView tvTitle;
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, MyConstants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);

                CacheUtils.setCache(mActivity, MyConstants.PHOTOS_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
