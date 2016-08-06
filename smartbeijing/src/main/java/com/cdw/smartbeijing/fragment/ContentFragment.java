package com.cdw.smartbeijing.fragment;


import android.view.View;

import com.cdw.smartbeijing.R;

/**
 * 主页面frament
 */
public class ContentFragment extends BaseFragment {

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        return view;
    }

    @Override
    public void initData() {
    }
}
