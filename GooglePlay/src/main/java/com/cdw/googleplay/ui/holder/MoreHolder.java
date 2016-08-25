package com.cdw.googleplay.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdw.googleplay.R;
import com.cdw.googleplay.utils.UIUtils;

/**
 * 列表加载更多
 * Created by dongwei on 2016/8/23.
 */
public class MoreHolder extends BaseHolder<Integer> {
    public static final int STATE_MORE_MORE = 1;//可以加载更多
    public static final int STATE_MORE_ERROR = 2;//加载更多失败
    public static final int STATE_MORE_NONE = 3;//没有更多数据
    private LinearLayout llLoadMore;
    private TextView tvLoadError;

    public MoreHolder(boolean hasMore) {
        if (hasMore){
            setData(STATE_MORE_MORE);
        }else {
            setData(STATE_MORE_NONE);
        }
    }

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_more);
        llLoadMore = (LinearLayout) view.findViewById(R.id.ll_load_more);
        tvLoadError = (TextView) view.findViewById(R.id.tv_load_error);
        return view;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data){
            case STATE_MORE_MORE:
                llLoadMore.setVisibility(View.VISIBLE);
                tvLoadError.setVisibility(View.GONE);
                break;
            case STATE_MORE_NONE:
                llLoadMore.setVisibility(View.GONE);
                tvLoadError.setVisibility(View.GONE);
                break;
            case STATE_MORE_ERROR:
                llLoadMore.setVisibility(View.GONE);
                tvLoadError.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
