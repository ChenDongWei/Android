package com.thxy.mobileguard.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thxy.mobileguard.R;

/**
 * 自定义组合控件
 * Created by dongwei on 2016/6/27.
 */
public class SettingCenterItemView extends LinearLayout {
    private TextView tv_title, tv_content;
    private CheckBox cb_check;
    private String[] contents;
    private View item;

    /**
     * 配置文件中，反射实例化设置参数
     *
     * @param context
     * @param attrs
     */
    public SettingCenterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化界面
        initView();
        //初始化复选框事件
        initEvent();
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.thxy" +
                ".mobileguard", "mtitle");
        String content = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.thxy" +
                ".mobileguard", "mcontent");
        tv_title.setText(title);
        contents = content.split("-");
    }

    private void initEvent() {
        item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_check.setChecked(!cb_check.isChecked());
            }
        });

        cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_content.setTextColor(Color.GREEN);
                    tv_content.setText(contents[1]);
                } else {
                    tv_content.setTextColor(Color.RED);
                    tv_content.setText(contents[0]);
                }
            }
        });
    }

    /**
     * 初始化LinearLayout的子组件
     */
    private void initView() {
        //给LinearLayout添加子组件
        item = View.inflate(getContext(), R.layout.item_settingcenter_view, null);
        tv_title = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_title);
        tv_content = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_content);
        cb_check = (CheckBox) item.findViewById(R.id.cb_settingcenter_autoupdate_checked);
        addView(item);
    }

    /**
     * 代码实例化调用该构造函数
     *
     * @param context
     */
    public SettingCenterItemView(Context context) {
        super(context);
        initView();
    }

}
