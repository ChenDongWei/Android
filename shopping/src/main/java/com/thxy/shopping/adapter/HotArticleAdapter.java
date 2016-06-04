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
 * Created by dongwei on 2016/6/1.
 */
public class HotArticleAdapter extends BaseAdapter {
    private List<Article> articles;
    private Context context;

    public HotArticleAdapter(List<Article> articles, Context context) {
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
        View hotViewItem = LayoutInflater.from(context).inflate(R.layout.layout_hot_article_gv_item, null);

        ImageView imageView = (ImageView) hotViewItem.findViewById(R.id.iv_hotLogo);
        imageView.setImageBitmap(WindowUtils.getScaleBitmap(article.bitmap, context));

        TextView title = (TextView) hotViewItem.findViewById(R.id.tv_hotTitle);
        title.setText(article.title);

        TextView price = (TextView) hotViewItem.findViewById(R.id.tv_hotPrice);
        price.setText("￥:" + article.price);

        return hotViewItem;
    }
}
