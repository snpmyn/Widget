package com.zsp.library.conection;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.zsp.library.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * @decs: 连状图
 * @author: 郑少鹏
 * @date: 2019/10/18 17:03
 */
public class ConnectionStatusView extends RelativeLayout {
    private static final int DISMISS_ON_COMPLETE_DELAY = 1000;
    /**
     * current status of status view
     */
    private Status currentStatus;
    /**
     * views for each status
     */
    private View completeView;
    private View errorView;
    private View loadingView;
    /**
     * fade in out animations
     */
    private Animation slideOut;
    private Animation slideIn;
    /**
     * handler
     */
    private Handler handler;
    /**
     * auto dismiss on complete
     */
    private final Runnable autoDismissOnComplete = new Runnable() {
        @Override
        public void run() {
            exitAnimation(getCurrentView(currentStatus));
            handler.removeCallbacks(autoDismissOnComplete);
        }
    };

    public ConnectionStatusView(Context context) {
        super(context);
        init(context, null, 0, 0, 0);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0, 0);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ConnectionStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ConnectionStatusView(Context context, int completeLayout, int errorLayout, int loadingLayout) {
        super(context);
        init(context, null, completeLayout, errorLayout, loadingLayout);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs, int completeLayout, int errorLayout, int loadingLayout) {
        super(context, attrs);
        init(context, attrs, completeLayout, errorLayout, loadingLayout);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs, int defStyleAttr, int completeLayout, int errorLayout, int loadingLayout) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, completeLayout, errorLayout, loadingLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ConnectionStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int completeLayout, int errorLayout, int loadingLayout) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, completeLayout, errorLayout, loadingLayout);
    }

    private void init(Context context, AttributeSet attrs, int completeLayout, int errorLayout, int loadingLayout) {
        // load initial values
        currentStatus = Status.IDLE;
        slideIn = AnimationUtils.loadAnimation(context, R.anim.connection_status_view_slide_in);
        slideOut = AnimationUtils.loadAnimation(context, R.anim.connection_status_view_slide_out);
        // layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        handler = new Handler();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConnectionStatusView);
        // get status layout ids
        int completeLayoutId = typedArray.getResourceId(R.styleable.ConnectionStatusView_complete, 0);
        int errorLayoutId = typedArray.getResourceId(R.styleable.ConnectionStatusView_error, 0);
        int loadingLayoutId = typedArray.getResourceId(R.styleable.ConnectionStatusView_loading, 0);
        // inflate layouts
        if (completeLayout == 0) {
            completeView = layoutInflater.inflate(completeLayoutId, null);
            errorView = layoutInflater.inflate(errorLayoutId, null);
            loadingView = layoutInflater.inflate(loadingLayoutId, null);
        } else {
            completeView = layoutInflater.inflate(completeLayout, null);
            errorView = layoutInflater.inflate(errorLayout, null);
            loadingView = layoutInflater.inflate(loadingLayout, null);
        }
        // default layout params
        completeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // add layout to root
        addView(completeView);
        addView(errorView);
        addView(loadingView);
        // set visibilities of childs
        completeView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        typedArray.recycle();
    }

    public void setOnErrorClickListener(OnClickListener onErrorClickListener) {
        errorView.setOnClickListener(onErrorClickListener);
    }

    public void setOnLoadingClickListener(OnClickListener onLoadingClickListener) {
        loadingView.setOnClickListener(onLoadingClickListener);
    }

    public void setOnCompleteClickListener(OnClickListener onCompleteClickListener) {
        completeView.setOnClickListener(onCompleteClickListener);
    }

    public View getErrorView() {
        return errorView;
    }

    public View getCompleteView() {
        return completeView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setStatus(final Status status) {
        if (currentStatus == Status.IDLE) {
            currentStatus = status;
            enterAnimation(getCurrentView(currentStatus));
        } else if (status != Status.IDLE) {
            View view = getCurrentView(currentStatus);
            if (null != view) {
                switchAnimation(view, getCurrentView(status));
            }
            currentStatus = status;
        } else {
            exitAnimation(getCurrentView(currentStatus));
        }
        handler.removeCallbacksAndMessages(null);
        if (status == Status.COMPLETE) {
            handler.postDelayed(autoDismissOnComplete, DISMISS_ON_COMPLETE_DELAY);
        }
    }

    /**
     * 获状
     *
     * @return 状
     */
    public Status getStatus() {
        return this.currentStatus;
    }

    @Contract(pure = true)
    private @Nullable View getCurrentView(Status status) {
        if (status == Status.IDLE) {
            return null;
        } else if (status == Status.COMPLETE) {
            return completeView;
        } else if (status == Status.ERROR) {
            return errorView;
        } else if (status == Status.LOADING) {
            return loadingView;
        }
        return null;
    }

    private void switchAnimation(@NonNull final View exitView, final View enterView) {
        clearAnimation();
        exitView.setVisibility(View.VISIBLE);
        exitView.startAnimation(slideOut);
        slideOut.setAnimationListener(new BaseAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                slideOut.setAnimationListener(null);
                exitView.setVisibility(View.INVISIBLE);
                enterView.setVisibility(View.VISIBLE);
                enterView.startAnimation(slideIn);
            }
        });
    }

    private void enterAnimation(View enterView) {
        if (enterView == null) {
            return;
        }
        enterView.setVisibility(VISIBLE);
        enterView.startAnimation(slideIn);
    }

    private void exitAnimation(final View exitView) {
        if (exitView == null) {
            return;
        }
        exitView.startAnimation(slideOut);
        slideOut.setAnimationListener(new BaseAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                currentStatus = Status.IDLE;
                exitView.setVisibility(INVISIBLE);
                slideOut.setAnimationListener(null);
            }
        });
    }
}
