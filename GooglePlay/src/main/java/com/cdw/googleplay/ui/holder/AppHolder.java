package com.cdw.googleplay.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cdw.googleplay.R;
import com.cdw.googleplay.domain.AppInfo;
import com.cdw.googleplay.http.HttpHelper;
import com.cdw.googleplay.utils.BitmapHelper;
import com.cdw.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 应用holder
 * Created by dongwei on 2016/8/23.
 */
public class AppHolder extends BaseHolder<AppInfo> {

    private TextView tvName;
    private TextView tvSize;
    private TextView tvDes;
    private ImageView ivIcon;
    private RatingBar rbStar;
    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        //1.加载布局
        View view = UIUtils.inflate(R.layout.list_item_home);
        //2.初始化控件
        tvName = (TextView) view.findViewById(R.id.tv_item_home_name);
        tvSize = (TextView) view.findViewById(R.id.tv_item_home_size);
        tvDes = (TextView) view.findViewById(R.id.tv_item_home_des);
        ivIcon = (ImageView) view.findViewById(R.id.iv_item_home_icon);
        rbStar = (RatingBar) view.findViewById(R.id.rb_item_home_star);

        mBitmapUtils = BitmapHelper.getBitmapUtils();

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        tvName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
        tvDes.setText(data.des);
        rbStar.setRating(data.stars);

        mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
    }
}
