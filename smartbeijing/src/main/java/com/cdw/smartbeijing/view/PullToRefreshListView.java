package com.cdw.smartbeijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cdw.smartbeijing.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的listView
 * Created by dongwei on 2016/8/12.
 */
public class PullToRefreshListView extends ListView {
    private static final int START_PULL_TO_REFRESH = 1;
    private static final int START_RELEASE_TO_REFRESH = 2;
    private static final int START_REFRESHING = 3;

    private int mCurrentState = START_PULL_TO_REFRESH;

    private View mHeaderView;
    private int mHeaderViewHeight;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;

    private int startY = -1;
    private RotateAnimation raUp;
    private RotateAnimation raDown;

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) findViewById(R.id.tv_refresh_title);
        tvTime = (TextView) findViewById(R.id.tv_refresh_time);
        ivArrow = (ImageView) findViewById(R.id.iv_refresh_arrow);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_refresh_loading);

        //隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        initAnimation();
        setCurrentTime();
    }

    //设置刷新时间
    private void setCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());

        tvTime.setText(time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    //当用户按住头条新闻的viewPager进行下拉时，ACTION_DOWN会被viewPager消费，
                    //导致startY没有赋值，此处需要重新获取一下
                    startY = (int) ev.getY();
                }

                if (mCurrentState == START_REFRESHING){
                    break;
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;

                int firstVisiblePosition = getFirstVisiblePosition();//当前显示第一个iten位置
                //必须下拉，并且当前显示第一个item
                if (dy > 0 && firstVisiblePosition == 0) {
                    int padding = dy - mHeaderViewHeight;
                    mHeaderView.setPadding(0, padding, 0, 0);
                    if (padding > 0 && mCurrentState != START_RELEASE_TO_REFRESH) {
                        //改为松开刷新
                        mCurrentState = START_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != START_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState = START_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;

                if (mCurrentState == START_RELEASE_TO_REFRESH){
                    mCurrentState = START_REFRESHING;
                    refreshState();

                    //完整显示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);

                    //4.进行回调
                    if (mListener != null){
                        mListener.onRefresh();
                    }

                }else if (mCurrentState == START_PULL_TO_REFRESH){
                    //隐藏头布局
                    mHeaderView.setPadding(0, -mCurrentState, 0, 0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void initAnimation() {
        raUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raUp.setDuration(200);
        raUp.setFillAfter(true);

        raDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raDown.setDuration(200);
        raDown.setFillAfter(true);
    }

    /**
     * 根据当前界面刷新页面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case START_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(raDown);
                break;
            case START_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(raUp);
                break;
            case START_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();//清除箭头动画，否则无法隐藏
                pbProgress.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success){
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        mCurrentState = START_PULL_TO_REFRESH;
        tvTitle.setText("下拉刷新");
        pbProgress.setVisibility(View.INVISIBLE);
        ivArrow.setVisibility(View.VISIBLE);

        if (success){
            //只有刷新成功后才刷新时间
            setCurrentTime();
        }
    }

    /**
     * 3.定义成员变量，接收监听对象
     */
    private OnRefreshListener mListener;

    /**
     * 2.暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }

    /**
     * 1.下拉刷新的回调接口
     */
    public interface OnRefreshListener{
        public void onRefresh();
    }
}
