package com.cdw.googleplay.ui.fragment;

import android.view.View;

import com.cdw.googleplay.domain.AppInfo;
import com.cdw.googleplay.http.protocol.AppProtocol;
import com.cdw.googleplay.ui.adapter.MyBaseAdapter;
import com.cdw.googleplay.ui.holder.AppHolder;
import com.cdw.googleplay.ui.holder.BaseHolder;
import com.cdw.googleplay.ui.view.LoadingPage;
import com.cdw.googleplay.ui.view.MyListView;
import com.cdw.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * 应用
 */
public class AppFragment extends BaseFragment {
    private ArrayList<AppInfo> data;

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UIUtils.getContext());
        view.setAdapter(new AppAdapter(data));
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        AppProtocol protocol = new AppProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class AppAdapter extends MyBaseAdapter<AppInfo> {
        public AppAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder<AppInfo> getHolder() {
            return new AppHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            AppProtocol protocol = new AppProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
