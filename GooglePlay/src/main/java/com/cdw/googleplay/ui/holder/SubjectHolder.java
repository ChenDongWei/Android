package com.cdw.googleplay.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdw.googleplay.R;
import com.cdw.googleplay.domain.SubjectInfo;
import com.cdw.googleplay.http.HttpHelper;
import com.cdw.googleplay.utils.BitmapHelper;
import com.cdw.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 专题Holder
 * Created by dongwei on 2016/8/29.
 */
public class SubjectHolder extends BaseHolder<SubjectInfo> {
    private ImageView ivPic;
    private TextView tvTitle;
    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_subject);
        ivPic = (ImageView) view.findViewById(R.id.iv_list_subject_pic);
        tvTitle = (TextView) view.findViewById(R.id.tv_list_subject_title);
        mBitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(SubjectInfo data) {
        tvTitle.setText(data.des);
        mBitmapUtils.display(ivPic, HttpHelper.URL + "image?name=" + data.url);
    }
}
