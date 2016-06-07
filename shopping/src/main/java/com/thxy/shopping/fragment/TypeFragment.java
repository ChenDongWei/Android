package com.thxy.shopping.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.thxy.shopping.R;
import com.thxy.shopping.adapter.ArticleTypeAdapter;
import com.thxy.shopping.dto.ArticleType;
import com.thxy.shopping.service.ShopService;

import java.util.ArrayList;
import java.util.List;


public class TypeFragment extends Fragment {

    /** 定义类型网格容器 */
    private GridView articleTypesGV;

    private List<ArticleType> articleTypes = new ArrayList<>();

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0x110 :
                    initDate();
                    break;
            }
            return false;
        }
    });

    private void initDate() {
        /** 创建一个适配器 */
        ArticleTypeAdapter articleTypeAdapter = new ArticleTypeAdapter(articleTypes, getActivity());

        articleTypesGV.setAdapter(articleTypeAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View typeView = inflater.inflate(R.layout.fragment_type, container, false);
        articleTypesGV = (GridView) typeView.findViewById(R.id.gv_articleTypes);

        initView(0x110);

        return typeView;
    }

    private void initView(final int msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                articleTypes = ShopService.getArticleTypes();
                if (articleTypes != null && articleTypes.size() > 0){
                    handler.sendEmptyMessage(msg);
                }
            }
        }).start();
    }

}
