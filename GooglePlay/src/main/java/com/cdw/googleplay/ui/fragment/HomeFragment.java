package com.cdw.googleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cdw.googleplay.domain.AppInfo;
import com.cdw.googleplay.http.protocol.HomeProtocol;
import com.cdw.googleplay.ui.adapter.MyBaseAdapter;
import com.cdw.googleplay.ui.holder.BaseHolder;
import com.cdw.googleplay.ui.holder.HomeHolder;
import com.cdw.googleplay.ui.view.LoadingPage.ResultState;
import com.cdw.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {
    private ArrayList<AppInfo> data;

    //如果加载数据成功，回调此方法,在主线程
    @Override
    public View onCreateSuccessView() {
//        TextView view = new TextView(UIUtils.getContext());
//        view.setText(this.getClass().getSimpleName());
        ListView view = new ListView(UIUtils.getContext());
        view.setAdapter(new HomeAdapter(data));
        return view;
    }

    //运行在子线程
    @Override
    public ResultState onLoad() {
        //请求网络
        HomeProtocol protocol = new HomeProtocol();
        data = protocol.getData(0);//加载第一页数据

        return check(data);
    }

    class HomeAdapter extends MyBaseAdapter<AppInfo> {

        public HomeAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder<AppInfo> getHolder() {
            return new HomeHolder();
        }

        //此方法在子线程执行
        @Override
        public ArrayList<AppInfo> onLoadMore() {
            HomeProtocol protocol = new HomeProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                //1.加载布局文件
//                convertView = UIUtils.inflate(R.layout.list_item_home);
//                holder = new ViewHolder();
//                //2.初始化控件findViewById
//                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_item_home_content);
//                //3.打一个标记
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            //4.根据数据刷新页面
//            String content = getItem(position);
//            holder.tvContent.setText(content);
//            return convertView;
//        }
    }

    static class ViewHolder {
        public TextView tvContent;
    }
}
