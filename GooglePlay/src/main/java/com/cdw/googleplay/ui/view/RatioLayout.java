package com.cdw.googleplay.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cdw.googleplay.R;

/**
 * 自定义控件，按照比例调整布局
 * Created by dongwei on 2016/8/30.
 */
public class RatioLayout extends FrameLayout {

    private float ratio;

    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio, -1);
        typedArray.recycle();//回收typedArray，提高性能
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1.获取宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heigth = MeasureSpec.getSize(heightMeasureSpec);
        int heigthMode = MeasureSpec.getMode(heightMeasureSpec);

        //2.根据宽度和比例，计算控件高度
        if (widthMode == MeasureSpec.EXACTLY && heigthMode != MeasureSpec.EXACTLY && ratio > 0){
            //图片宽度 = 控件宽度-左侧内边距-右侧内边距
            int imageWidth = width - getPaddingLeft() - getPaddingRight();
            //图片高度 = 图片宽度/宽高比例
            int imageHeight = (int) (imageWidth / ratio + 0.5);
            //控件高度 = 图片高度+上侧内边距+下侧内边距
            heigth = imageHeight + getPaddingTop() + getPaddingBottom();
            //根据最新的高度重新生成heightMeasureSpec
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heigth, MeasureSpec.EXACTLY);
        }

        //3.重新测量控件

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
