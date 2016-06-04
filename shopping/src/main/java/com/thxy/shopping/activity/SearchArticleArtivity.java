package com.thxy.shopping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.thxy.shopping.R;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.service.ShopService;

import java.util.ArrayList;
import java.util.List;

public class SearchArticleArtivity extends Activity {

    private EditText word;
    private ListView searchArticle;
    private List<Article> articles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article_artivity);

        word = (EditText) findViewById(R.id.et_word);
        searchArticle = (ListView) findViewById(R.id.lv_searchArticle);

        word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                initSearchView();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initSearchView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                articles = ShopService.searchArticleByWord(word.getText().toString());
            }
        }).start();
    }

}
