package com.cdw.sweep;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends Activity {
    private ListView mListView;
    private List<String> mDatas;
    private List<SweepView> mOpenedViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.lv);

        //模拟数据
        mDatas = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            mDatas.add("第" + i + "条数据");
        }

        //给listView设置数据
        mListView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mDatas != null) {
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDatas != null) {
                return mDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                //没有复用
                convertView = View.inflate(MainActivity.this, R.layout.item, null);
                //初始化Holder
                holder = new ViewHolder();
                //设置标记
                convertView.setTag(holder);
                //view的初始化
                holder.sv = (SweepView) convertView.findViewById(R.id.item_sv);
                holder.tvContent = (TextView) convertView.findViewById(R.id.item_tv_content);
                holder.tvDelete = (TextView) convertView.findViewById(R.id.item_tv_delete);
                holder.sv.setOnSweepListener(new SweepView.OnSweepListener() {
                    @Override
                    public void onSweepChanged(SweepView view, boolean isOpened) {
                        if (isOpened) {
                            //打开，记录下来
                            if (!mOpenedViews.contains(view)) {
                                mOpenedViews.add(view);
                            }
                        } else {
                            //移除
                            mOpenedViews.remove(view);
                        }
                    }
                });
            } else {
                //有复用
                holder = (ViewHolder) convertView.getTag();
            }
            //数据的加载
            final String data = mDatas.get(position);
            holder.tvContent.setText(data);

            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(data);

                    //关闭所有打开的view
                    closeAll();

                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    public void closeAll(){
        ListIterator<SweepView> iterator = mOpenedViews.listIterator();
        while (iterator.hasNext()){
            SweepView view = iterator.next();
            view.close();
        }
    }

    class ViewHolder {
        SweepView sv;
        TextView tvContent;
        TextView tvDelete;
    }
}
