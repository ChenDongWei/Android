package com.cdw.googleplay.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by dongwei on 2016/8/26.
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        this.setSelector(new ColorDrawable());//设置默认背景选择器为全透明
        this.setDivider(null);//去掉分割线
        this.setCacheColorHint(Color.TRANSPARENT);//将滑动可能出现的黑色改成全透明

    }
}
