package com.zsp.library.status.layout;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsp.library.R;

import timber.log.Timber;

/**
 * @decs: 状态布局
 * @author: 郑少鹏
 * @date: 2018/10/23 18:54
 */
public class StatusLayout extends FrameLayout {
    private View mLoadingView;
    private View mEmptyView;
    private View mRetryView;
    private View mContentView;
    private final LayoutInflater mLayoutInflater;

    public StatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public StatusLayout(Context context) {
        this(context, null);
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void showLoading() {
        if (isMainThread()) {
            showView(mLoadingView);
        } else {
            post(() -> showView(mLoadingView));
        }
        // 避Fragment场景碎片叠加致点击穿透
        mLoadingView.setClickable(true);
    }

    public void showEmpty() {
        if (isMainThread()) {
            showView(mEmptyView);
        } else {
            post(() -> showView(mEmptyView));
        }
        // 避Fragment场景碎片叠加致点击穿透
        mEmptyView.setClickable(true);
    }

    public void showRetry(int status) {
        switch (status) {
            // 无网络
            case 0:
                if (isMainThread()) {
                    showView(mRetryView);
                } else {
                    post(() -> showView(mRetryView));
                }
                break;
            // 连接失败
            case 1:
                ImageView statusRetryIv = mRetryView.findViewById(R.id.statusRetryIv);
                TextView statusRetryTv = mRetryView.findViewById(R.id.statusRetryTv);
                TextView statusRetryMb = mRetryView.findViewById(R.id.statusRetryMb);
                statusRetryIv.setImageResource(R.drawable.ic_status_fail);
                statusRetryTv.setText(R.string.serverExceptionAndTryAgainLater);
                statusRetryMb.setText(R.string.retry);
                if (isMainThread()) {
                    showView(mRetryView);
                } else {
                    post(() -> showView(mRetryView));
                }
                break;
            // 加载失败
            case 2:
                statusRetryIv = mRetryView.findViewById(R.id.statusRetryIv);
                statusRetryTv = mRetryView.findViewById(R.id.statusRetryTv);
                statusRetryMb = mRetryView.findViewById(R.id.statusRetryMb);
                statusRetryIv.setImageResource(R.drawable.ic_status_fail);
                statusRetryTv.setText(R.string.loadFailAndTryAgainLater);
                statusRetryMb.setText(R.string.retry);
                if (isMainThread()) {
                    showView(mRetryView);
                } else {
                    post(() -> showView(mRetryView));
                }
                break;
            default:
                break;
        }
        // 避Fragment场景碎片叠加致点击穿透
        mRetryView.setClickable(true);
    }

    public void showContent() {
        if (isMainThread()) {
            showView(mContentView);
        } else {
            post(() -> showView(mContentView));
        }
    }

    private void showView(View view) {
        if (view == null) {
            return;
        }
        if (view == mLoadingView) {
            mLoadingView.setVisibility(View.VISIBLE);
            if (mRetryView != null) {
                mRetryView.setVisibility(View.GONE);
            }
            if (mContentView != null) {
                mContentView.setVisibility(View.GONE);
            }
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.GONE);
            }
        } else if (view == mEmptyView) {
            mEmptyView.setVisibility(View.VISIBLE);
            if (mLoadingView != null) {
                mLoadingView.setVisibility(View.GONE);
            }
            if (mRetryView != null) {
                mRetryView.setVisibility(View.GONE);
            }
            if (mContentView != null) {
                mContentView.setVisibility(View.GONE);
            }
        } else if (view == mRetryView) {
            mRetryView.setVisibility(View.VISIBLE);
            if (mLoadingView != null) {
                mLoadingView.setVisibility(View.GONE);
            }
            if (mContentView != null) {
                mContentView.setVisibility(View.GONE);
            }
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.GONE);
            }
        } else if (view == mContentView) {
            mContentView.setVisibility(View.VISIBLE);
            if (mLoadingView != null) {
                mLoadingView.setVisibility(View.GONE);
            }
            if (mRetryView != null) {
                mRetryView.setVisibility(View.GONE);
            }
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }

    public View setContentView(View view) {
        View contentView = mContentView;
        if (contentView != null) {
            Timber.d("You have already set a retry view and would be instead of this new one.");
        }
        removeView(contentView);
        addView(view);
        mContentView = view;
        return mContentView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public void setLoadingView(int layoutId) {
        setLoadingView(mLayoutInflater.inflate(layoutId, this, false));
    }

    public void setLoadingView(View view) {
        View loadingView = mLoadingView;
        if (loadingView != null) {
            Timber.d("You have already set a loading view and would be instead of this new one.");
        }
        removeView(loadingView);
        addView(view);
        mLoadingView = view;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(int layoutId) {
        setEmptyView(mLayoutInflater.inflate(layoutId, this, false));
    }

    public void setEmptyView(View view) {
        View emptyView = mEmptyView;
        if (emptyView != null) {
            Timber.d("You have already set a empty view and would be instead of this new one.");
        }
        removeView(emptyView);
        addView(view);
        mEmptyView = view;
    }

    public View getRetryView() {
        return mRetryView;
    }

    public void setRetryView(int layoutId) {
        setRetryView(mLayoutInflater.inflate(layoutId, this, false));
    }

    public void setRetryView(View view) {
        View retryView = mRetryView;
        if (retryView != null) {
            Timber.d("You have already set a retry view and would be instead of this new one.");
        }
        removeView(retryView);
        addView(view);
        mRetryView = view;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(int layoutId) {
        setContentView(mLayoutInflater.inflate(layoutId, this, false));
    }
}
