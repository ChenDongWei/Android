package com.cdw.googleplay.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdw.googleplay.ui.view.LoadingPage;
import com.cdw.googleplay.ui.view.LoadingPage.ResultState;
import com.cdw.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPage mLoadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
//        TextView view = new TextView(UIUtils.getContext());
//        view.setText(getClass().getSimpleName());
        mLoadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {
                //此处调用的是BaseFragment的onCreateSuccessView()
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };
        return mLoadingPage;
    }

    //加载成功的布局，必须由子类实现
    public abstract View onCreateSuccessView();

    //加载网络资源，必须由子类实现
    public abstract ResultState onLoad();

    //加载数据
    public void loadData() {
        if (mLoadingPage != null) {
            mLoadingPage.loadData();
        }
    }

    //对网络返回数据的合法性进行校验
    public ResultState check(Object obj) {
        if (obj != null) {
            if (obj instanceof ArrayList) {
                ArrayList list = (ArrayList) obj;

                if (list.isEmpty()) {
                    return ResultState.STATE_EMPTY;
                } else {
                    return ResultState.STATE_SUCCESS;
                }
            }
        }
        return ResultState.STATE_ERROR;
    }
}
