package com.thxy.shopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thxy.shopping.R;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.utils.WindowUtils;

public class ArticleItemActivity extends Activity implements View.OnClickListener {

    private ImageView logo,backBtn;
    private TextView articleTitle, articlePrice, articlePrice1, articleDesc, addShopCar;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_article_item);

        Intent intent = getIntent();
        article = (Article) intent.getExtras().getSerializable("article");
        article.bitmap = intent.getExtras().getParcelable("image");

        addShopCar = (TextView) findViewById(R.id.tv_addShopCar);
        addShopCar.setOnClickListener(this);

        logo = (ImageView) findViewById(R.id.iv_articleLogo);
        backBtn = (ImageView) findViewById(R.id.iv_backBtn);
        backBtn.setOnClickListener(this);

        articleTitle = (TextView) findViewById(R.id.tv_articleTitle);
        articlePrice = (TextView) findViewById(R.id.tv_articlePrice);
        articlePrice1 = (TextView) findViewById(R.id.tv_articlePrice1);
        articleDesc = (TextView) findViewById(R.id.tv_articleDesc);

        logo.setImageBitmap(WindowUtils.getScaleBitmap(article.bitmap, this, 1 / 2f));
        articleTitle.setText(article.title);
        articleDesc.setText(
                "标题：" + article.title + "\n"
                        + "产地：天河学院\n"
                        + "价格：" + article.price + "\n"
                        + "详细介绍：" + article.description
        );

        articlePrice.setText("￥:" + article.price);
        articlePrice1.setText("总价：" + article.price);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_backBtn) {
            ArticleItemActivity.this.finish();
        } else if (v.getId() == R.id.tv_addShopCar) {
            SharedPreferences sharedPreferences = getSharedPreferences("shopCar", MODE_PRIVATE);

            SharedPreferences.Editor et = sharedPreferences.edit();
            /** 1.获取之前的购物车信息 */
            StringBuilder infos = new StringBuilder();
            if (sharedPreferences.getString("infos", null) != null && sharedPreferences.getString("infos", null) != "") {
                String articleIdAndNums = sharedPreferences.getString("infos", null);
                String[] id_nums = articleIdAndNums.split(",");

                boolean flag = false;
                for (String id_num : id_nums) {
                    String id = id_num.split("_")[0];
                    String num = id_num.split("_")[1];

                    if (id.equals(article.id)) {
                        flag = true;
                        num = (Integer.valueOf(num) + 1) + "";

                    }
                    infos.append(id + "_" + num + ",");
                }
                if (!flag) {
                    infos.append(article.id + "_1,");
                }
            } else {
                infos.append(article.id + "_1,");
            }
            et.putString("infos", infos.toString().substring(0, infos.toString().length() - 1));
            et.commit();

            Toast.makeText(this, "加入购物车成功", Toast.LENGTH_SHORT).show();

        }
    }
}
