package com.thxy.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thxy.shopping.R;
import com.thxy.shopping.dto.Article;
import com.thxy.shopping.utils.WindowUtils;

import java.util.List;

/**
 * Created by dongwei on 2016/6/5.
 */
public class SearchArticleAdapter extends BaseAdapter {
    private List<Article> articles;
    private Context context;

    public SearchArticleAdapter(List<Article> articles, Context context){
        this.articles = articles;
        this.context = context;
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

        View item = LayoutInflater.from(context).inflate(R.layout.layout_search_article_lv_item, null);

        ImageView logo = (ImageView) item.findViewById(R.id.iv_articleLogo);
        logo.setImageBitmap(WindowUtils.getScaleBitmap(article.bitmap, context, 1/3f));

        TextView title = (TextView) item.findViewById(R.id.tv_articleTitle);
        title.setText(article.title);

        TextView price = (TextView) item.findViewById(R.id.tv_articlePrice);
        price.setText("￥" + article.price);

        return item;
    }
}
