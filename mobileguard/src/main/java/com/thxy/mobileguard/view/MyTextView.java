package com.thxy.mobileguard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.widget.TextView;

/**
 * Created by dongwei on 2016/6/18.
 */
public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    public MyTextView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public MyTextView(Context context) {
        super(context);
    }

    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        return true;
    }


}
