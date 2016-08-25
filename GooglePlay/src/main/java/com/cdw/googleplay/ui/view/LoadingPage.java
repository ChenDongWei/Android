package com.cdw.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.cdw.googleplay.R;
import com.cdw.googleplay.utils.UIUtils;

/**
 * 根据当前状态显示不同页面的自定义控件
 * Created by dongwei on 2016/8/22.
 */
public abstract class LoadingPage extends FrameLayout {
    private static final int STATE_LOAD_UNDO = 1;//未加载
    private static final int STATE_LOAD_LOADING = 2;//正在加载
    private static final int STATE_LOAD_ERROR = 3;//加载失败
    private static final int STATE_LOAD_EMPTY = 4;//数据为空
    private static final int STATE_LOAD_SUCCESS = 5;//加载成功

    private int mCurrentState = STATE_LOAD_UNDO;//当前状态
    private View mLoadingPage;
    private View mErrorPage;
    private View mEmptyPage;
    private View mSuccessPage;

    public LoadingPage(Context context) {
        super(context);
        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //初始化加载中的布局
        if (mLoadingPage == null) {
            mLoadingPage = UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);
        }

        //初始化加载失败布局
        if (mErrorPage == null) {
            mErrorPage = UIUtils.inflate(R.layout.page_error);
            addView(mErrorPage);
        }

        //初始化没有数据的布局
        if (mEmptyPage == null) {
            mEmptyPage = UIUtils.inflate(R.layout.page_empty);
            addView(mEmptyPage);
        }

        showRightPage();
    }

    private void showRightPage() {
        if (mCurrentState == STATE_LOAD_UNDO || mCurrentState == STATE_LOAD_LOADING) {
            mLoadingPage.setVisibility(View.VISIBLE);
        } else {
            mLoadingPage.setVisibility(View.GONE);
        }

        if (mCurrentState == STATE_LOAD_ERROR) {
            mErrorPage.setVisibility(View.VISIBLE);
        } else {
            mErrorPage.setVisibility(View.GONE);
        }

        if (mCurrentState == STATE_LOAD_EMPTY) {
            mEmptyPage.setVisibility(View.VISIBLE);
        } else {
            mEmptyPage.setVisibility(View.GONE);
        }

        if (mSuccessPage == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessPage = onCreateSuccessView();
            if (mSuccessPage != null) {
                addView(mSuccessPage);
            }
        }

        if (mSuccessPage != null) {
            mSuccessPage.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View
                    .GONE);
        }
    }

    //开始加载数据
    public void loadData() {
        if (mCurrentState != STATE_LOAD_LOADING) {
            mCurrentState = STATE_LOAD_LOADING;
            new Thread() {
                @Override
                public void run() {
                    final ResultState resultState = onLoad();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resultState != null) {
                                //网络加载结束后，更新网络状态
                                mCurrentState = resultState.getState();
                                //根据最新数据刷新页面
                                showRightPage();
                            }
                        }
                    });

                }
            }.start();
        }
    }

    //加载成功后显示的布局，必须由调用者实现
    public abstract View onCreateSuccessView();

    //加载网络数据，返回值表示请求网络结束后的状态
    public abstract ResultState onLoad();

    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS),
        STATE_EMPTY(STATE_LOAD_EMPTY),
        STATE_ERROR(STATE_LOAD_ERROR);

        private int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
