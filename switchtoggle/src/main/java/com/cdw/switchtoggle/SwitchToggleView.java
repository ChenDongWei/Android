package com.cdw.switchtoggle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dongwei on 2016/7/31.
 */
public class SwitchToggleView extends View {
    private Bitmap mSwitchBackground;//背景图片
    private Bitmap mSwitchSlide;//滑块图片

    private final static int STATE_NONE = 0;
    private final static int STATE_DOWN = 1;
    private final static int STATE_MOVE = 2;
    private final static int STATE_UP = 3;

    private boolean isOpened = true;//用来标记滑块是否打开
    private int mState = STATE_NONE;//用来标记状态

    private float mCurrentX;
    private OnSwitchListener mListener;

    private Paint mPaint = new Paint();

    public SwitchToggleView(Context context) {
        this(context, null);
    }

    public SwitchToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置背景资源
     *
     * @param resId
     */
    public void setSwitchBackground(int resId) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setSwichSlide(int resId) {
        mSwitchSlide = BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mSwitchBackground != null) {
            int width = mSwitchBackground.getWidth();
            int height = mSwitchBackground.getHeight();

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景显示
        if (mSwitchBackground != null) {
            int left = 0;
            int top = 0;
            canvas.drawBitmap(mSwitchBackground, left, top, mPaint);
        }

        if (mSwitchSlide == null) {
            return;
        }
        int slideWidth = mSwitchSlide.getWidth();//滑块的宽度
        int switchWidth = mSwitchBackground.getWidth();

        switch (mState) {
            case STATE_DOWN:
            case STATE_MOVE:
                if (!isOpened) {
                    //滑块是关闭的
                    if (mCurrentX < slideWidth / 2f) {
                        //绘制在左侧
                        canvas.drawBitmap(mSwitchSlide, 0, 0, mPaint);
                    } else {
                        float left = mCurrentX - slideWidth / 2f;
                        float maxLeft = switchWidth - slideWidth;
                        if (left > maxLeft) {
                            left = maxLeft;
                        }

                        canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                    }
                } else {
                    //滑块是打开的
                    float middle = switchWidth - slideWidth / 2f;
                    if (mCurrentX > middle) {
                        //绘制为打开
                        canvas.drawBitmap(mSwitchSlide, switchWidth - slideWidth, 0, mPaint);
                    } else {
                        float left = mCurrentX - slideWidth / 2f;
                        if (left < 0){
                            left = 0;
                        }
                        canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                    }
                }
                break;
            case STATE_UP:
            case STATE_NONE:
                //绘制滑块
                if (!isOpened) {
                    canvas.drawBitmap(mSwitchSlide, 0, 0, mPaint);
                } else {
                    canvas.drawBitmap(mSwitchSlide, switchWidth - slideWidth, 0, mPaint);
                }

                break;
            default:
                break;

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mState = STATE_DOWN;
                mCurrentX = event.getX();

                invalidate();//触发刷新
                break;
            case MotionEvent.ACTION_MOVE:
                mState = STATE_MOVE;
                mCurrentX = event.getX();

                invalidate();//触发刷新
                break;
            case MotionEvent.ACTION_UP:
                mState = STATE_UP;
                invalidate();//触发刷新
                //判断状态改变
                int switchWidth = mSwitchBackground.getWidth();
                mCurrentX = event.getX();
                if (switchWidth / 2f > mCurrentX && isOpened){
                    isOpened = false;
                    if (mListener != null){
                        mListener.onSwitchChange(isOpened);
                    }
                }else if (switchWidth / 2f < mCurrentX && !isOpened){
                    isOpened = true;
                    if (mListener != null){
                        mListener.onSwitchChange(isOpened);
                    }
                }
                break;
            default:
                break;
        }
        //消费touch事件
        return true;
    }

    public void setOnSwitchListener(OnSwitchListener listener){
        this.mListener = listener;
    }

    public interface OnSwitchListener{
        //开关状态改变
        void onSwitchChange(boolean isOpened);
    }
}
