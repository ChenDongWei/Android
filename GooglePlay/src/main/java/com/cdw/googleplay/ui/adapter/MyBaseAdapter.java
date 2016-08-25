package com.cdw.googleplay.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cdw.googleplay.ui.holder.BaseHolder;
import com.cdw.googleplay.ui.holder.MoreHolder;
import com.cdw.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * 对adapter的封装
 * Created by dongwei on 2016/8/23.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private static final int TYPE_NORMAL = 0;//正常布局
    private static final int TYPE_MORE = 1;//加载更多

    private boolean isLoadMore = false;//标记是否在加载更多

    public ArrayList<T> data;

    public MyBaseAdapter(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() + 1;//增加加载更多的数量
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回布局类型个数
    @Override
    public int getViewTypeCount() {
        return 2;//返回两种类型，普通+加载更多
    }

    //返回当前位置应该显示哪种布局
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return TYPE_MORE;
        } else {
            return getInnerType();
        }
    }

    public int getInnerType() {
        return TYPE_NORMAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            if (getItemViewType(position) == TYPE_MORE) {
                //加载更多
                holder = new MoreHolder(hasMore());
            } else {
                holder = getHolder();
            }
        } else {
            holder = (BaseHolder) convertView.getTag();
        }

        if (getItemViewType(position) != TYPE_MORE) {
            holder.setData(getItem(position));
        } else {
            //如果有更多数据，加载更多
            MoreHolder moreHolder = (MoreHolder) holder;
            if (moreHolder.getData() == MoreHolder.STATE_MORE_MORE) {
                loadMore(moreHolder);
            }
        }

        return holder.getmRootView();
    }

    public boolean hasMore() {
        return true;
    }

    public abstract BaseHolder<T> getHolder();

    //加载更多数据
    public void loadMore(final MoreHolder holder) {
        if (!isLoadMore) {
            isLoadMore = true;
            new Thread() {
                @Override
                public void run() {
                    final ArrayList<T> moreData = onLoadMore();

                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (moreData != null) {
                                //每一页20条数据，如果返回的数据小于20条，就认为到最后一页了
                                if (moreData.size() < 20) {
                                    holder.setData(MoreHolder.STATE_MORE_NONE);
                                    Toast.makeText(UIUtils.getContext(), "没有更多数据", Toast
                                            .LENGTH_SHORT).show();
                                } else {
                                    holder.setData(MoreHolder.STATE_MORE_MORE);
                                }
                                //将更多数据追加到当前集合
                                data.addAll(moreData);
                                //刷新界面
                                MyBaseAdapter.this.notifyDataSetChanged();
                            } else {
                                holder.setData(MoreHolder.STATE_MORE_ERROR);
                            }
                            isLoadMore = false;
                        }
                    });
                }
            }.start();
        }

    }

    public abstract ArrayList<T> onLoadMore();

    //获取当前集合的大小
    public int getListSize(){
        return data.size();
    }
}
