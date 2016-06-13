package com.thxy.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thxy.shopping.R;
import com.thxy.shopping.dto.ArticleType;
import com.thxy.shopping.utils.WindowUtils;

import java.util.List;

/**
 * Created by dongwei on 2016/6/6.
 */
public class ArticleTypeAdapter extends BaseAdapter {

    private List<ArticleType> articleTypes;

    private Context context;

    public ArticleTypeAdapter(List<ArticleType> articleTypes, Context context){
        this.articleTypes = articleTypes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return articleTypes.size();
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
        ArticleType articleType = articleTypes.get(position);

        View item = LayoutInflater.from(context).inflate(R.layout.layout_article_type_gv_item, null);

        TextView articleTypeName = (TextView) item.findViewById(R.id.tv_typeName);
        articleTypeName.setText(articleType.name);

        ImageView articleTypeLogo = (ImageView) item.findViewById(R.id.iv_typeLogo);
        articleTypeLogo.setImageBitmap(WindowUtils.getScaleBitmap(articleType.typeLogo, context, 1/3f));


        return item;
    }
}
