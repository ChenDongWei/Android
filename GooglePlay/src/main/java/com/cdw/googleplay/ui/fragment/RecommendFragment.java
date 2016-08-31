package com.cdw.googleplay.ui.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cdw.googleplay.http.protocol.RecommendProtocol;
import com.cdw.googleplay.ui.view.LoadingPage;
import com.cdw.googleplay.ui.view.fly.ShakeListener;
import com.cdw.googleplay.ui.view.fly.StellarMap;
import com.cdw.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * 推荐
 */
public class RecommendFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View onCreateSuccessView() {
        final StellarMap stellar = new StellarMap(UIUtils.getContext());
        stellar.setAdapter(new RecommendAdapter());
        //随机的方式,将控件划分为9行6列的格子，在格子随机展示
        stellar.setRegularity(6, 9);
        //设置内边距
        int padding = UIUtils.dp2px(10);
        stellar.setInnerPadding(padding, padding, padding, padding);
        //设置默认页面,第一组数据
        stellar.setGroup(0, true);

        ShakeListener shake = new ShakeListener(UIUtils.getContext());
        shake.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                stellar.zoomIn();//跳到下一页
            }
        });
        return stellar;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        RecommendProtocol protocol = new RecommendProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class RecommendAdapter implements StellarMap.Adapter {
        //返回组的个数
        @Override
        public int getGroupCount() {
            return 2;
        }

        //返回某组的item个数
        @Override
        public int getCount(int group) {
            int count = data.size() / getGroupCount();
            if (group == getGroupCount() - 1) {
                //最后一页，将除不尽的数据追加在最后一页
                count += data.size() % getGroupCount();
            }
            return count;
        }

        //初始化布局
        @Override
        public View getView(int group, int position, View convertView) {
            //因为position每组都从0考试，所以需要将前面几组的数据加起来，才能确定当前组数据的脚标位置
            position += group * getCount(group - 1);
            final String keyword = data.get(position);

            final TextView view = new TextView(UIUtils.getContext());
            view.setText(keyword);

            Random random = new Random();
            //随机大小,16-25
            int size = 16 + random.nextInt(10);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            //随机颜色,0-255->30-230,颜色不能太大或太小，从而避免整体颜色过亮或者过暗
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            view.setTextColor(Color.rgb(r, g, b));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), keyword, Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        //返回下一组的id
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (isZoomIn) {
                //往下滑加载上一页
                if (group > 0) {
                    group--;
                } else {
                    //跳到最后一页
                    group = getGroupCount() - 1;
                }
            } else {
                //往上滑加载下一页
                if (group < getGroupCount() - 1) {
                    group++;
                } else {
                    group = 0;
                }
            }
            return group;
        }
    }
}
