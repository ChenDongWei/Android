package com.thxy.shopping.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thxy.shopping.R;
import com.thxy.shopping.adapter.CarArticleAdapter;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.service.ShopService;

import java.util.ArrayList;
import java.util.List;


public class CarFragment extends Fragment implements View.OnClickListener {

    private ListView shopCarLV;

    private List<Article> articles = new ArrayList<>();

    private CarArticleAdapter carArticleAdapter;

    private CheckBox checkAllBuyCar, checkAllDeleteCar;

    private TextView carTitleBar, totalPrice, editCar, deleteCar;

    private RelativeLayout buyMenuCar, editCarMenu;

    private TextView buyRightNow;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0x110:
                    carTitleBar.setText("购物车(" + articles.size() + ")");
                    initData();
                    break;
            }
            return false;
        }
    });

    private void initData() {
        carArticleAdapter = new CarArticleAdapter(articles, getActivity(), checkAllBuyCar,
                checkAllDeleteCar, totalPrice, buyRightNow, carTitleBar);
        shopCarLV.setAdapter(carArticleAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car, container, false);
        shopCarLV = (ListView) view.findViewById(R.id.lv_shopCarLV);

        checkAllBuyCar = (CheckBox) view.findViewById(R.id.cb_checkAllBuyCar);
        checkAllBuyCar.setOnClickListener(this);

        checkAllDeleteCar = (CheckBox) view.findViewById(R.id.cb_checkAlldelteCar);
        checkAllDeleteCar.setOnClickListener(this);

        carTitleBar = (TextView) view.findViewById(R.id.tv_carTitleBar);
        totalPrice = (TextView) view.findViewById(R.id.tv_totalPrice);

        editCar = (TextView) view.findViewById(R.id.tv_editCar);
        editCar.setOnClickListener(this);

        buyRightNow = (TextView) view.findViewById(R.id.tv_buyRightNow);

        deleteCar = (TextView) view.findViewById(R.id.tv_deleteCar);
        deleteCar.setOnClickListener(this);

        buyMenuCar = (RelativeLayout) view.findViewById(R.id.rl_buyCarMenu);
        editCarMenu = (RelativeLayout) view.findViewById(R.id.rl_editCarMenu);

        initShopCar();

        return view;
    }

    private void initShopCar() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shopCar",
                Context.MODE_PRIVATE);

        final String infos = sharedPreferences.getString("infos", null);
        if (infos != null && infos != "") {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object[] objs = ShopService.getShopCarArticle(infos);
                    articles = (List<Article>) objs[1];

                    if (articles != null && articles.size() > 0) {
                        handler.sendEmptyMessage(0x110);
                    }
                }
            }).start();

        } else {
            /** 没有处理 */
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cb_checkAllBuyCar) {
            carArticleAdapter.checkAllOrNot(checkAllBuyCar.isChecked());
            if (checkAllBuyCar.isChecked()) {
                buyRightNow.setText("立即购买(" + articles.size() + ")");
            } else {
                buyRightNow.setText("立即购买(0)");
            }


            /** 统计金额 */
            carArticleAdapter.calcTotalMoney();
        } else if (v.getId() == R.id.cb_checkAlldelteCar) {
            carArticleAdapter.checkAllOrNot(checkAllDeleteCar.isChecked());
        } else if (v.getId() == R.id.tv_deleteCar) {
            carArticleAdapter.deleteCar();
        } else if (v.getId() == R.id.tv_editCar) {
            TextView tv = (TextView) v;

            if (tv.getText().toString().equals("编辑")) {
                tv.setText("完成");
                /** 购买菜单隐藏 */
                buyMenuCar.setVisibility(View.GONE);
                /** 编辑菜单显示 */
                editCarMenu.setVisibility(View.VISIBLE);

                carArticleAdapter.setFlag(true);

                /** 选中状态都清空 */
                carArticleAdapter.initCheckStatus(false);
                checkAllDeleteCar.setChecked(false);

                carArticleAdapter.notifyDataSetChanged();

            } else {
                tv.setText("编辑");

                /** 购买菜单隐藏 */
                buyMenuCar.setVisibility(View.VISIBLE);
                /** 编辑菜单显示 */
                editCarMenu.setVisibility(View.GONE);

                /** */
                carArticleAdapter.setFlag(false);

                carArticleAdapter.notifyDataSetChanged();
            }
        }
    }
}
