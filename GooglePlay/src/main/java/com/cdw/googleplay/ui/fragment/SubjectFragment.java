package com.cdw.googleplay.ui.fragment;

import android.view.View;

import com.cdw.googleplay.domain.SubjectInfo;
import com.cdw.googleplay.http.protocol.SubjectProtocol;
import com.cdw.googleplay.ui.adapter.MyBaseAdapter;
import com.cdw.googleplay.ui.holder.BaseHolder;
import com.cdw.googleplay.ui.holder.SubjectHolder;
import com.cdw.googleplay.ui.view.LoadingPage;
import com.cdw.googleplay.ui.view.MyListView;
import com.cdw.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * 专题
 */
public class SubjectFragment extends BaseFragment {

    private ArrayList<SubjectInfo> data;
    private SubjectProtocol protocol;

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UIUtils.getContext());
        view.setAdapter(new SubjectAdapter(data));
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        protocol = new SubjectProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class SubjectAdapter extends MyBaseAdapter<SubjectInfo> {
        public SubjectAdapter(ArrayList data) {
            super(data);
        }

        @Override
        public BaseHolder getHolder() {
            return new SubjectHolder();
        }

        @Override
        public ArrayList onLoadMore() {
            protocol = new SubjectProtocol();
            ArrayList<SubjectInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
