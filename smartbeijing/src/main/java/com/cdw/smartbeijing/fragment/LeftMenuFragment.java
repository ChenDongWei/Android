package com.cdw.smartbeijing.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cdw.smartbeijing.R;
import com.cdw.smartbeijing.activity.MainActivity;
import com.cdw.smartbeijing.base.impl.NewsCenterPage;
import com.cdw.smartbeijing.domain.NewsMenu.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 侧边栏fragment
 */
public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_left_list)
    private ListView lvList;
    private ArrayList<NewsMenuData> mNewsMenuData;
    private int mCurrentPos;//当前被选中的item的位置
    private LeftMenuAdapter mAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this, view);//注入view和事件
        return view;
    }

    @Override
    public void initData() {

    }

    //给侧边栏设置数据
    public void setMenuData(ArrayList<NewsMenuData> data){
        mCurrentPos = 0;//当前所选位置归零
        mNewsMenuData = data;
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;//更新当前选中的位置
                mAdapter.notifyDataSetChanged();//刷新listview

                //收起侧边栏
                toggle();
                //侧边栏点击后，要修改新闻中心的FrameLayout中的内容
                setCurrentDetailPager(position);
            }
        });
    }

    protected void setCurrentDetailPager(int position) {
        //获取新闻中心的对象
        MainActivity mainUI = (MainActivity) mActivity;
        //获取ContentFragment
        ContentFragment fragment = mainUI.getContentFragment();
        //获取NewsCenterPager
        NewsCenterPage centerPager = fragment.getNewsCenterPager();
        //修改新闻中心Fragment
        centerPager.setCurrentDetailPager(position);
    }

    protected void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu,null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            NewsMenuData item = getItem(position);
            tvMenu.setText(item.title);
            if (mCurrentPos == position){
                //选中
                tvMenu.setEnabled(true);
            }else {
                tvMenu.setEnabled(false);
            }
            return view;
        }
    }
}
