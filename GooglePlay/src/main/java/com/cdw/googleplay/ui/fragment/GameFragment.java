package com.cdw.googleplay.ui.fragment;

import android.view.View;

import com.cdw.googleplay.domain.AppInfo;
import com.cdw.googleplay.http.protocol.GameProtocol;
import com.cdw.googleplay.ui.adapter.MyBaseAdapter;
import com.cdw.googleplay.ui.holder.BaseHolder;
import com.cdw.googleplay.ui.holder.GameHolder;
import com.cdw.googleplay.ui.view.LoadingPage;
import com.cdw.googleplay.ui.view.MyListView;
import com.cdw.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * 游戏
 */
public class GameFragment extends BaseFragment {
    private ArrayList<AppInfo> data;
    @Override
    public View onCreateSuccessView() {
//        TextView view = new TextView(UIUtils.getContext());
//        view.setText("GameFragment");
        MyListView view = new MyListView(UIUtils.getContext());
        view.setAdapter(new GameAdapter(data));
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        GameProtocol protocol = new GameProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class GameAdapter extends MyBaseAdapter<AppInfo>{

        public GameAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder<AppInfo> getHolder() {
            return new GameHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            GameProtocol protocol = new GameProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
