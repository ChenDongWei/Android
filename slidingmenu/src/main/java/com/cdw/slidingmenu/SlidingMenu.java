package com.cdw.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by dongwei on 2016/8/2.
 */
public class SlidingMenu extends ViewGroup {
    private View mLeftView;
    private View mContentView;
    private int mLeftWidth;
    private float mDownX;
    private float mDownY;
    private Scroller mScroller;
    private boolean isLeftShow = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        //xml加载完成时
        mLeftView = getChildAt(0);
        mContentView = getChildAt(1);

        LayoutParams params = mLeftView.getLayoutParams();
        mLeftWidth = params.width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量孩子
        int leftWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mLeftWidth, MeasureSpec.EXACTLY);
        mLeftView.measure(leftWidthMeasureSpec, heightMeasureSpec);
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);
        //设置自己的宽度和高度
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = mLeftView.getMeasuredWidth();
        int height = mLeftView.getMeasuredHeight();
        //给左侧布局

        int lvLeft = -width;
        int lvTop = 0;
        int lvRight = 0;
        int lvBottom = height;
        mLeftView.layout(lvLeft, lvTop, lvRight, lvBottom);

        //给右侧布局
        int cLeft = 0;
        int cTop = 0;
        int cRight = mContentView.getMeasuredWidth();
        int cBottom = mContentView.getMeasuredHeight();
        mContentView.layout(cLeft, cTop, cRight, cBottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                if (Math.abs(moveX - mDownX) > Math.abs(moveY - mDownY)){
                    //水平方向移动
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                int diffX = (int) (mDownX - moveX + 0.5f);

                int scrollX = getScrollX() + diffX;
                if (scrollX < 0 && scrollX < -mLeftView.getMeasuredWidth()){
                    scrollTo(-mLeftView.getMeasuredWidth(), 0);
                }else if (scrollX > 0){
                    scrollTo(0, 0);
                }else {
                    scrollBy(diffX, 0);
                }

                mDownX = moveX;
                mDownY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                //判断是打开还是关闭
                int width = mLeftView.getMeasuredWidth();
                int currentX = getScrollX();
                float middle = -width / 2f;
                switchMenu(currentX <= middle);
                break;
            default:
                break;
        }
        return true;
    }

    private void switchMenu(boolean showLeft) {
        isLeftShow = showLeft;
        int width = mLeftView.getMeasuredWidth();
        int currentX = getScrollX();
        if (!showLeft){
            //scrollTo(0, 0);

            int startX = currentX;
            int startY = 0;
            int endX = 0;
            int endY = 0;
            int dx = endX - startX;
            int dy = endY - startY;
            int duration = Math.abs(dx) * 10;
            if (duration >= 600){
                duration = 600;
            }
            //模拟数据变化
            mScroller.startScroll(startX, startY, dx, dy, duration);
        }else {
            //scrollTo(-width, 0);

            int startX = currentX;
            int startY = 0;
            int endX = -width;
            int endY = 0;
            int dx = endX - startX;
            int dy = endY - startY;
            int duration = Math.abs(dx) * 10;
            if (duration >= 600){
                duration = 600;
            }
            mScroller.startScroll(startX, startY, dx, dy, duration);
        }

        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            //更新位置
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    public void toggle(){
        switchMenu(!isLeftShow);
    }
}
