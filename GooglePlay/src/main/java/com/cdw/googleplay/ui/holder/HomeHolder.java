package com.cdw.googleplay.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.cdw.googleplay.R;
import com.cdw.googleplay.domain.AppInfo;
import com.cdw.googleplay.utils.UIUtils;

/**
 * 首页holder
 * Created by dongwei on 2016/8/23.
 */
public class HomeHolder extends BaseHolder<AppInfo> {

    private TextView tvContent;

    @Override
    public View initView() {
        //1.加载布局
        View view = UIUtils.inflate(R.layout.list_item_home);
        //2.初始化控件
        tvContent = (TextView) view.findViewById(R.id.tv_item_home_content);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        tvContent.setText(data.name);
    }
}
