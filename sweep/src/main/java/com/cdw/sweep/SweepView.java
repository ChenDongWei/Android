package com.cdw.sweep;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dongwei on 2016/8/3.
 */
public class SweepView extends ViewGroup {
    private View mContentView;
    private View mDeleteView;
    private int mDeleteWidth;
    private ViewDragHelper mHelper;
    private boolean isOpened;
    private OnSweepListener mListener;

    public SweepView(Context context) {
        this(context, null);
    }

    public SweepView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHelper = ViewDragHelper.create(this, new MyCallBack());
    }

    @Override
    protected void onFinishInflate() {
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);

        LayoutParams params = mDeleteView.getLayoutParams();
        mDeleteWidth = params.width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量内容
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);
        //测量删除部分
        int deleteWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mDeleteWidth, MeasureSpec.EXACTLY);
        mDeleteView.measure(deleteWidthMeasureSpec, heightMeasureSpec);
        //确定自己的高度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //布局内容区域
        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight
                ());
        //布局删除部分
        mDeleteView.layout(mContentView.getMeasuredWidth(), 0, mContentView.getMeasuredWidth() +
                mDeleteWidth, mDeleteView.getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    class MyCallBack extends ViewDragHelper.Callback {

        /**
         * 是否分析view的touch
         *
         * @param child     触摸的view
         * @param pointerId touch的id
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            return child == mContentView || child == mDeleteView;
        }

        /**
         * 当touch移动后的回调
         *
         * @param child 谁移动了
         * @param left  child的左侧的边距
         * @param dx    增量的x
         * @return 确定要移动多少
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mContentView) {
                if (left < 0 && -left > mDeleteWidth) {
                    return -mDeleteWidth;
                } else if (left > 0) {
                    return 0;
                }
            } else if (child == mDeleteView) {
                int measuredWidth = mContentView.getMeasuredWidth();
                if (left < measuredWidth - mDeleteWidth) {
                    return measuredWidth - mDeleteWidth;
                } else if (left > measuredWidth) {
                    return measuredWidth;
                }
            }

            return left;
        }

        /**
         * 当控件位置移动时的回调
         *
         * @param changedView 哪个view移动了
         * @param left        view的左上角的坐标
         * @param top         view的左上角的坐标
         * @param dx          x方向移动的增量
         * @param dy          y方向移动的增量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            invalidate();

            int contentWidth = mContentView.getMeasuredWidth();
            int contentHeight = mContentView.getMeasuredHeight();
            int deleteHeight = mDeleteView.getMeasuredHeight();
            if (changedView == mContentView) {
                //如果移动的是内容的view

                mDeleteView.layout(contentWidth + left, 0, contentWidth + left + mDeleteWidth,
                        deleteHeight);
            } else if (changedView == mDeleteView) {

                //int deleteWidth = mDeleteView.
                mContentView.layout(left - contentWidth, 0, left, contentHeight);
            }
        }

        /**
         * up时的回调
         *
         * @param releasedChild 松开了哪个view
         * @param xvel          x方向的速率
         * @param yvel          y方向的速率
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left = mContentView.getLeft();
            if (-left < mDeleteWidth / 2f) {
                //关闭
                close();
            } else {
                //打开
                open();
            }
            //invalidate();
        }
    }

    public void open() {
        isOpened = true;
        if (mListener != null){
            mListener.onSweepChanged(SweepView.this, isOpened);
        }
        int contentWidth = mContentView.getMeasuredWidth();
        //布局内容区域
//                mContentView.layout(-mDeleteWidth, 0, contentWidth - mDeleteWidth, mContentView
//                        .getMeasuredHeight());
//                //布局删除部分
//                mDeleteView.layout(contentWidth - mDeleteWidth, 0, contentWidth, mDeleteView
//                        .getMeasuredHeight());
        //数据的模拟
        mHelper.smoothSlideViewTo(mContentView, -mDeleteWidth, 0);
        mHelper.smoothSlideViewTo(mDeleteView, contentWidth - mDeleteWidth, 0);


        ViewCompat.postInvalidateOnAnimation(SweepView.this);
    }

    public void close() {
        isOpened = false;
        if (mListener != null){
            mListener.onSweepChanged(SweepView.this, isOpened);
        }
                //布局内容区域
//                mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView
//                        .getMeasuredHeight());
//                //布局删除部分
//                mDeleteView.layout(mContentView.getMeasuredWidth(), 0, mContentView
//                        .getMeasuredWidth() + mDeleteWidth, mDeleteView.getMeasuredHeight());

        //数据的模拟
        mHelper.smoothSlideViewTo(mContentView, 0, 0);
        mHelper.smoothSlideViewTo(mDeleteView, mContentView.getMeasuredWidth(), 0);

        ViewCompat.postInvalidateOnAnimation(SweepView.this);
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)){
            invalidate();
        }
    }

    public void setOnSweepListener(OnSweepListener listener){
        this.mListener = listener;
    }

    public interface OnSweepListener{
        void onSweepChanged(SweepView view, boolean isOpened);
    }
}
