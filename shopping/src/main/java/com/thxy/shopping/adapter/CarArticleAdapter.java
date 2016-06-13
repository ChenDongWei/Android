package com.thxy.shopping.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.thxy.shopping.R;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.utils.WindowUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dongwei on 2016/6/5.
 */
public class CarArticleAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Article> articles;
    private Context context;
    private CheckBox checkAllBuyCar, checkAllDeleteCar;
    private TextView totalPrice, buyRightNow, carTitleBar;

    private boolean flag = false;

    private Map<String, Boolean> shopCarItem = new HashMap<>();

    private Map<String, Boolean> deleteShopCarItem = new HashMap<>();

    public CarArticleAdapter(List<Article> articles,
                             Context context, CheckBox checkAllBuyCar,
                             CheckBox checkAllDeleteCar, TextView totalPrice,
                             TextView buyRightNow, TextView carTitleBar) {
        this.articles = articles;
        this.context = context;
        this.checkAllBuyCar = checkAllBuyCar;
        this.checkAllDeleteCar = checkAllDeleteCar;
        this.totalPrice = totalPrice;
        this.buyRightNow = buyRightNow;
        this.carTitleBar = carTitleBar;
        /** 初始化时所有选项都不选中 */
        initCheckStatus(false);
    }

    public void initCheckStatus(Boolean flag1) {
        if (flag) {
            for (Article article : articles) {
                deleteShopCarItem.put(article.id, flag1);
            }
        } else {
            for (Article article : articles) {
                shopCarItem.put(article.id, flag1);
            }
        }

    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = articles.get(position);

        View item = LayoutInflater.from(context).inflate(R.layout.layout_car_article_lv_item, null);

        ImageView logo = (ImageView) item.findViewById(R.id.iv_articleLogo);
        logo.setImageBitmap(WindowUtils.getScaleBitmap(article.bitmap, context, 1 / 3f));

        /** 选项按钮 */
        CheckBox checkBox = (CheckBox) item.findViewById(R.id.cb_itemCheckBox);
        if (flag) {
            checkBox.setChecked(deleteShopCarItem.get(article.id));
        } else {
            checkBox.setChecked(shopCarItem.get(article.id));
        }
        checkBox.setContentDescription(article.id);
        checkBox.setOnClickListener(this);

        TextView title = (TextView) item.findViewById(R.id.tv_articleTitle);
        title.setText(article.title);

        TextView buyNum = (TextView) item.findViewById(R.id.tv_buyNum);
        buyNum.setText("X" + article.buyNum);

        TextView articleprice = (TextView) item.findViewById(R.id.tv_articlePrice);
        articleprice.setText("￥:" + article.price);

        TextView totalprice = (TextView) item.findViewById(R.id.tv_totalPrice);
        NumberFormat numberFormat = new DecimalFormat("0.00");
        totalprice.setText("总价" + numberFormat.format(article.price * article.buyNum));

        return item;
    }

    public void checkAllOrNot(Boolean flag1) {
        if (flag) {
            //清空之前的状态
            deleteShopCarItem.clear();
            initCheckStatus(flag1);

            /** 通知当前适配器重新更新数据 */
            this.notifyDataSetChanged();
        } else {
            shopCarItem.clear();
            initCheckStatus(flag1);
            /** 通知当前适配器重新更新数据 */
            this.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        if (v instanceof CheckBox) {
            if (!flag) {


                CheckBox itemCk = (CheckBox) v;
//            if (itemCk.isChecked()){
//                shopCarItem.put(itemCk.getContentDescription().toString(), true);
//            }else {
//                shopCarItem.put(itemCk.getContentDescription().toString(), false);
//            }
                shopCarItem.put(itemCk.getContentDescription().toString(), itemCk.isChecked());

                /** 控制全选按钮是否应该选中 */
                int count = 0;
                for (Map.Entry<String, Boolean> entry : shopCarItem.entrySet()) {
                    Boolean value = entry.getValue();
                    if (value) {
                        count++;
                    }
                }
                buyRightNow.setText("立即购买(" + count + ")");

//            if (count == articles.size()){
//                checkAll.setChecked(true);
//            }else {
//                checkAll.setChecked(false);
//            }
                checkAllBuyCar.setChecked(count == articles.size());

                calcTotalMoney();
            } else {
                CheckBox itemCk = (CheckBox) v;
                // 等价于
                deleteShopCarItem.put(itemCk.getContentDescription().toString(), itemCk.isChecked
                        ());

                /** 控制全选按钮是否应该选中 */
                int count = 0;
                for (Map.Entry<String, Boolean> entry : deleteShopCarItem.entrySet()) {
                    Boolean value = entry.getValue();
                    if (value) {
                        count++;
                    }
                }

//            if(count == articles.size()){
//                checkAll.setChecked(true);
//            }else{
//                checkAll.setChecked(false);
//            }
                checkAllDeleteCar.setChecked(count == articles.size());
            }
        }

    }

    public void calcTotalMoney() {
        /** 统计金额 */
        double totalAllMoney = 0;
        if (shopCarItem.size() > 0) {
            for (Map.Entry<String, Boolean> entry : shopCarItem.entrySet()) {
                Boolean value = entry.getValue();
                String id = entry.getKey();
                if (value) {
                    for (Article article : articles) {
                        if (article.id.equals(id)) {
                            totalAllMoney += (article.buyNum * article.price);
                        }
                    }
                }
            }
        }
        NumberFormat numberFormat = new DecimalFormat("0.00");
        totalPrice.setText("合计：" + numberFormat.format(totalAllMoney));
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void deleteCar() {
        /** 获取当前选中的需要删除的商品id */
        if (deleteShopCarItem.size() > 0) {
            Map<String, Boolean> deleteShopCarItem1 = new HashMap<>();
            deleteShopCarItem1.putAll(deleteShopCarItem);
            for (Map.Entry<String, Boolean> id_flag : deleteShopCarItem.entrySet()) {
                String id = id_flag.getKey();
                Boolean value = id_flag.getValue();
                if (value) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("shopCar",
                            Context.MODE_PRIVATE);
                    String infos = sharedPreferences.getString("infos", null);

                    if (infos != null && infos.length() > 0) {
                        String[] id_nums = infos.split(",");

                        StringBuilder rs = new StringBuilder();
                        for (String id_num : id_nums) {
                            String carId = id_num.split("_")[0];
                            if (!id.equals(carId)) {
                                rs.append(id_num + ",");
                            }
                        }
                        String result = rs.toString();
                        if (result != null && result.length() > 0) {
                            result = result.substring(0, result.length() - 1);
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("infos", result);
                        editor.commit();
                    }

                    List<Article> articles1 = new ArrayList<>();
                    articles1.addAll(articles);
                    /** 更新购物车集合中的数据即可 */
                    for (Article article : articles) {
                        if (article.id.equals(id)) {
                            articles1.remove(article);
                        }
                    }
                    articles.clear();
                    articles.addAll(articles1);
                    /** 更新状态 */
                    shopCarItem.remove(id);

                    deleteShopCarItem1.remove(id);


                }

            }
            deleteShopCarItem.clear();
            deleteShopCarItem.putAll(deleteShopCarItem1);
            carTitleBar.setText("购物车(" + articles.size() + ")");

            /** 遍历购物车集合，看是否都选中了 */
            int count = 0;
            for (Map.Entry<String, Boolean> entry : shopCarItem.entrySet()) {
                Boolean value = entry.getValue();
                if (value) {
                    count++;
                }
            }

            if (count == articles.size()) {
                checkAllBuyCar.setChecked(true);
            } else {
                checkAllBuyCar.setChecked(false);
            }

            buyRightNow.setText("立即购买(" + count + ")");

            if (articles.size() == 0) {
                checkAllBuyCar.setChecked(false);
                checkAllDeleteCar.setChecked(false);
            }
            calcTotalMoney();

            this.notifyDataSetChanged();

        }

    }
}
