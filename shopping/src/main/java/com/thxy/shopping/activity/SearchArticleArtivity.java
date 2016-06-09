package com.thxy.shopping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.thxy.shopping.R;
import com.thxy.shopping.adapter.SearchArticleAdapter;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.service.ShopService;

import java.util.ArrayList;
import java.util.List;

public class SearchArticleArtivity extends Activity {

    private EditText word;
    /** 定义线性容器对象 */
    private ListView searchArticle;
    /** 定义搜索出来的商品 */
    private List<Article> articles = new ArrayList<>();
    /** 当前商品应该分多少页 */
    private int totalSize;
    /**定义查询的页码*/
    private int pageIndex = 1;

    private int firstVisibleItem1,visibleItemCount1,totalItemCount1;

    SearchArticleAdapter searchArticleAdapter;

    private ImageView backBtn;

    private Handler handler = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0x110 :
                    initView();
                    break;
                case 0x111 :
                    /** 通知适配器更新数据 */
                    searchArticleAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    private void initView() {
        searchArticleAdapter = new SearchArticleAdapter(articles, getApplication());

        searchArticle.setAdapter(searchArticleAdapter);

        //searchArticle.setSelection((pageIndex - 1) * 6);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article_acticle);

        word = (EditText) findViewById(R.id.et_word);
        searchArticle = (ListView) findViewById(R.id.lv_searchArticle);
        backBtn = (ImageView) findViewById(R.id.iv_backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchArticleArtivity.this.finish();
            }
        });

        initSearchView(0x110);

        word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /** 开始搜索商品 */
                initSearchView(0x110);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /** 线性容器绑定滚动事件 */
        searchArticle.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == SCROLL_STATE_IDLE){
                    if((firstVisibleItem1 + visibleItemCount1) == totalItemCount1){
                        /** 加载下一页 */
                        if (pageIndex >= totalSize){
                            Toast.makeText(SearchArticleArtivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        }else {
                            pageIndex++;
                            initSearchView(0x111);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisibleItem1 = firstVisibleItem;
                visibleItemCount1 = visibleItemCount;
                totalItemCount1 = totalItemCount;
            }
        });
    }

    private void initSearchView(final int msg) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (msg == 0x110){
                    pageIndex = 1;
                    articles.clear();
                }

                Object[] objs = ShopService.searchArticleByWord(word.getText().toString(), pageIndex, null);
                totalSize = (int) objs[0];

                List<Article> searchArticles = (List<Article>) objs[1];

                if (searchArticles != null && searchArticles.size() > 0){
                    /** 如果查询出来是有数据的，就加到展示的集合中 */
                    articles.addAll(searchArticles);
                }

                handler.sendEmptyMessage(msg);
            }
        }).start();
    }

}
