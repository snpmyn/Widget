package com.zsp.library.layout.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.zsp.library.R;

/**
 * @decs: 进度布局
 * @author: 郑少鹏
 * @date: 2019/10/18 10:22
 */
public class ProgressLayout extends View implements Animatable {
    private static final int COLOR_EMPTY_DEFAULT = 0x00000000;
    private static final int COLOR_LOADED_DEFAULT = 0x11FFFFFF;
    private static final int PROGRESS_SECOND_MS = 1000;
    private static Paint paintProgressLoaded;
    private static Paint paintProgressEmpty;
    private boolean isPlaying = false;
    private boolean isAutoProgress;
    private int mHeight;
    private int mWidth;
    private int maxProgress;
    private int currentProgress = 0;
    private Handler handlerProgress;
    private ProgressLayoutListener progressLayoutListener;

    public ProgressLayout(Context context) {
        this(context, null);
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public boolean isRunning() {
        return isPlaying;
    }

    @Override
    public void start() {
        if (isAutoProgress) {
            isPlaying = true;
            handlerProgress.removeCallbacksAndMessages(null);
            handlerProgress.postDelayed(mRunnableProgress, 0);
        }
    }

    @Override
    public void stop() {
        isPlaying = false;
        handlerProgress.removeCallbacks(mRunnableProgress);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, mWidth, mHeight, paintProgressEmpty);
        canvas.drawRect(0, 0, calculatePositionIndex(currentProgress), mHeight, paintProgressLoaded);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressLayout);
        isAutoProgress = a.getBoolean(R.styleable.ProgressLayout_autoProgress, true);
        maxProgress = a.getInt(R.styleable.ProgressLayout_maxProgress, 0);
        maxProgress = maxProgress * 10;
        int loadedColor = a.getColor(R.styleable.ProgressLayout_loadedColor, COLOR_LOADED_DEFAULT);
        int emptyColor = a.getColor(R.styleable.ProgressLayout_emptyColor, COLOR_EMPTY_DEFAULT);
        a.recycle();
        paintProgressEmpty = new Paint();
        paintProgressEmpty.setColor(emptyColor);
        paintProgressEmpty.setStyle(Paint.Style.FILL);
        paintProgressEmpty.setAntiAlias(true);
        paintProgressLoaded = new Paint();
        paintProgressLoaded.setColor(loadedColor);
        paintProgressLoaded.setStyle(Paint.Style.FILL);
        paintProgressLoaded.setAntiAlias(true);
        handlerProgress = new Handler();
    }

    private int calculatePositionIndex(int currentProgress) {
        return (currentProgress * mWidth) / maxProgress;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void cancel() {
        isPlaying = false;
        currentProgress = 0;
        handlerProgress.removeCallbacks(mRunnableProgress);
        postInvalidate();
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress * 10;
        postInvalidate();
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress * 10;
        postInvalidate();
    }

    public void setAutoProgress(boolean isAutoProgress) {
        this.isAutoProgress = isAutoProgress;
    }

    public void setProgressLayoutListener(ProgressLayoutListener progressLayoutListener) {
        this.progressLayoutListener = progressLayoutListener;
    }

    private final Runnable mRunnableProgress = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                if (currentProgress == maxProgress) {
                    if (progressLayoutListener != null) {
                        progressLayoutListener.onProgressCompleted();
                    }
                    currentProgress = 0;
                    setCurrentProgress(currentProgress);
                    stop();
                } else {
                    postInvalidate();
                    currentProgress += 1;
                    if (progressLayoutListener != null) {
                        progressLayoutListener.onProgressChanged(currentProgress / 10);
                    }
                    handlerProgress.postDelayed(mRunnableProgress, PROGRESS_SECOND_MS / 10);
                }
            }
        }
    };
}
