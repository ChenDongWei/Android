package com.thxy.shopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thxy.shopping.R;
import com.thxy.shopping.adapter.SearchArticleAdapter;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.dto.ArticleType;
import com.thxy.shopping.service.ShopService;

import java.util.ArrayList;
import java.util.List;

public class SecondTypeActivity extends Activity implements View.OnClickListener {

    /**
     * 导航按钮组
     */
    private RadioGroup secondTypeRG;

    /**
     * 定义一个集合用于保存当前一级类型对应的二级类型
     */
    private List<ArticleType> secondTypes = new ArrayList<>();

    /**
     * 一级类型
     */
    private String typeCode;

    /**
     * 二级类型
     */
    private String secondTypeCode;

    /**
     * 定义线性容器
     */
    private ListView secondTypeLV;

    /**
     * 定义一个集合存放该类型下的所有商品
     */
    private List<Article> articles = new ArrayList<>();

    private int pageIndex = 1;

    /**
     * 当前商品的分页数
     */
    private int totalSize;

    private int firstVisiableItem1 , visiableItemCount1 , totalItemCount1;

    private SearchArticleAdapter searchArticleAdapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0x110:
                    initMenus();
                    break;
                case 0x112:
                    initArticleData();
                    break;
                case 0x113:
                    searchArticleAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    private void initArticleData() {
        searchArticleAdapter = new SearchArticleAdapter(articles,
                getApplication());

        secondTypeLV.setAdapter(searchArticleAdapter);
    }

    private void initMenus() {
        for (int i = 0; i < secondTypes.size(); i++) {
            ArticleType articleType = secondTypes.get(i);
            RadioButton rb = (RadioButton) LayoutInflater.from(getApplication()).inflate(R.layout
                    .layout_second_type_group_radio_button, null);
            rb.setText(articleType.name);
            rb.setContentDescription(articleType.code);

            /** 给每一个导航菜单按钮绑定一个点击事件 */
            rb.setOnClickListener(SecondTypeActivity.this);

            secondTypeRG.addView(rb);

            if (i == 0) {
                rb.setTextColor(0xffff5000);
                rb.setChecked(true);
                secondTypeCode = rb.getContentDescription()+"";
                initData(0x112);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_type);

        typeCode = getIntent().getExtras().getString("code");

        secondTypeRG = (RadioGroup) findViewById(R.id.rg_secondType);
        secondTypeLV = (ListView) findViewById(R.id.lv_secondType);

        /** 初始化导航菜单栏 */
        initHorizantolMenu();
        /** 初始化商品列表的点击事件 */
        initArticleItemClick();
        /** 给类型线性容器绑定滚动事件   */
        secondTypeLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE){
                    if((firstVisiableItem1+visiableItemCount1) == totalItemCount1){
                        if(pageIndex >= totalSize){
                            /** 没有更多数据了 */
                            Toast.makeText(SecondTypeActivity.this,"没有数据了",Toast.LENGTH_SHORT).show();
                        }else{
                            pageIndex++;
                            /** 加载下一页的数据   */
                            initData(0x113);

                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisiableItem1 = firstVisibleItem;
                visiableItemCount1 = visibleItemCount;
                totalItemCount1 = totalItemCount;
            }
        });
    }

    private void initArticleItemClick() {
        secondTypeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = articles.get(position);
                Intent intent = new Intent(SecondTypeActivity.this, ArticleItemActivity.class);
                intent.putExtra("article", article);
                intent.putExtra("image", article.bitmap);
                startActivity(intent);
            }
        });
    }

    private void initHorizantolMenu() {
        /**  查询当前一级类型的二级类型 */
        new Thread() {
            @Override
            public void run() {
                secondTypes = ShopService.getArticleTypes(typeCode);
                if (secondTypes != null && secondTypes.size() > 0) {
                    handler.sendEmptyMessage(0x110);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RadioButton) {
            /** 获取radioGroup下所有的按钮对象 */
            for (int i = 0; i < secondTypeRG.getChildCount(); i++) {
                RadioButton rbt = (RadioButton) secondTypeRG.getChildAt(i);
                rbt.setTextColor(0xff000000);
            }

            RadioButton rb = (RadioButton) v;
            rb.setTextColor(0xffff5000);

            /** 根据物品类型编号，查询输属于这个类型下的所有商品信息 */
            secondTypeCode = rb.getContentDescription()+"";
            initData(0x112);
        }
    }

    private void initData(final int msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /** 清空上一次的数据，展示新的数据 */
                if(msg == 0x112){
                    articles.clear();
                    pageIndex = 1;
                }

                Object[] objs = ShopService.searchArticlesByWord(null, pageIndex, secondTypeCode);
                totalSize = (int) objs[0];
                List<Article> findArticles = (List<Article>) objs[1];
                if (findArticles != null && findArticles.size() > 0) {
                    articles.addAll(findArticles);
                }
                handler.sendEmptyMessage(msg);
            }
        }).start();
    }
}
