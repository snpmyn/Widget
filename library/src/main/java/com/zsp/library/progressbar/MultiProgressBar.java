package com.zsp.library.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.zsp.library.R;

import java.util.Locale;

/**
 * @decs: 多样进度条
 * @author: 郑少鹏
 * @date: 2019/8/7 14:51
 */
public class MultiProgressBar extends ProgressBar {
    private Paint mPaint;
    private Mode mMode;
    private int mTextColor;
    private int mTextSize;
    private int mTextMargin;
    private int mReachedColor;
    private int mReachedHeight;
    private int mUnReachedColor;
    private int mUnReachedHeight;
    private boolean mIsCapRounded;
    private boolean mIsHiddenText;
    private int mRadius;
    private int xMaxUnReachedEnd;
    private final int mMaxStrokeWidth;
    private int mTextHeight;
    private int mTextWidth;
    private RectF fArcRect;
    private final Rect mTextRect = new Rect();
    private String mText;

    public MultiProgressBar(Context context) {
        this(context, null);
    }

    public MultiProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public MultiProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs(context);
        initCustomAttrs(context, attrs);
        mMaxStrokeWidth = Math.max(mReachedHeight, mUnReachedHeight);
    }

    private void initDefaultAttrs(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMode = Mode.System;
        mTextColor = Color.parseColor("#70A800");
        mTextSize = MultiProgressBar.sp2px(context, 10);
        mTextMargin = MultiProgressBar.dp2px(context, 4);
        mReachedColor = Color.parseColor("#70A800");
        mReachedHeight = MultiProgressBar.dp2px(context, 2);
        mUnReachedColor = Color.parseColor("#CCCCCC");
        mUnReachedHeight = MultiProgressBar.dp2px(context, 1);
        mIsCapRounded = false;
        mIsHiddenText = false;
        mRadius = MultiProgressBar.dp2px(context, 16);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiProgressBar);
        final int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    protected void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.MultiProgressBar_multiProgressBarMode) {
            int ordinal = typedArray.getInt(attr, Mode.System.ordinal());
            mMode = Mode.values()[ordinal];
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarTextColor) {
            mTextColor = typedArray.getColor(attr, mTextColor);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarTextSize) {
            mTextSize = typedArray.getDimensionPixelOffset(attr, mTextSize);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarTextMargin) {
            mTextMargin = typedArray.getDimensionPixelOffset(attr, mTextMargin);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarReachedColor) {
            mReachedColor = typedArray.getColor(attr, mReachedColor);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarReachedHeight) {
            mReachedHeight = typedArray.getDimensionPixelOffset(attr, mReachedHeight);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarUnReachedColor) {
            mUnReachedColor = typedArray.getColor(attr, mUnReachedColor);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarUnReachedHeight) {
            mUnReachedHeight = typedArray.getDimensionPixelOffset(attr, mUnReachedHeight);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarIsCapRounded) {
            mIsCapRounded = typedArray.getBoolean(attr, mIsCapRounded);
            if (mIsCapRounded) {
                mPaint.setStrokeCap(Paint.Cap.ROUND);
            }
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarIsHiddenText) {
            mIsHiddenText = typedArray.getBoolean(attr, mIsHiddenText);
        } else if (attr == R.styleable.MultiProgressBar_multiProgressBarRadius) {
            mRadius = typedArray.getDimensionPixelOffset(attr, mRadius);
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMode == Mode.System) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (mMode == Mode.Horizontal) {
            calculateTextWidthAndHeight();
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int expectHeight = getPaddingTop() + getPaddingBottom();
            if (mIsHiddenText) {
                expectHeight += Math.max(mReachedHeight, mUnReachedHeight);
            } else {
                expectHeight += Math.max(mTextHeight, Math.max(mReachedHeight, mUnReachedHeight));
            }
            int height = resolveSize(expectHeight, heightMeasureSpec);
            setMeasuredDimension(width, height);
            xMaxUnReachedEnd = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        } else if (mMode == Mode.Circle) {
            int expectSize = mRadius * 2 + mMaxStrokeWidth + getPaddingLeft() + getPaddingRight();
            int width = resolveSize(expectSize, widthMeasureSpec);
            int height = resolveSize(expectSize, heightMeasureSpec);
            expectSize = Math.min(width, height);
            mRadius = (expectSize - getPaddingLeft() - getPaddingRight() - mMaxStrokeWidth) / 2;
            if (fArcRect == null) {
                fArcRect = new RectF();
            }
            fArcRect.set(0, 0, mRadius * 2, mRadius * 2);
            setMeasuredDimension(expectSize, expectSize);
        } else if (mMode == Mode.Comet) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (mMode == Mode.Wave) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (mMode == Mode.System) {
            super.onDraw(canvas);
        } else if (mMode == Mode.Horizontal) {
            onDrawHorizontal(canvas);
        } else if (mMode == Mode.Circle) {
            onDrawCircle(canvas);
        } else if (mMode == Mode.Comet) {
            super.onDraw(canvas);
        } else if (mMode == Mode.Wave) {
            super.onDraw(canvas);
        }
    }

    private void onDrawHorizontal(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), Integer.valueOf(getMeasuredHeight()).floatValue() / 2);
        float reachedRatio = getProgress() * 1.0f / getMax();
        float xReachedEnd = reachedRatio * xMaxUnReachedEnd;
        if (mIsHiddenText) {
            if (xReachedEnd > xMaxUnReachedEnd) {
                xReachedEnd = xMaxUnReachedEnd;
            }
            if (xReachedEnd > 0) {
                mPaint.setColor(mReachedColor);
                mPaint.setStrokeWidth(Integer.valueOf(mReachedHeight).floatValue());
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(0, 0, xReachedEnd, 0, mPaint);
            }
            float xUnReachedStart = xReachedEnd;
            if (mIsCapRounded) {
                xUnReachedStart += (mReachedHeight + mUnReachedHeight) * 1.0f / 2;
            }
            if (xUnReachedStart < xMaxUnReachedEnd) {
                mPaint.setColor(mUnReachedColor);
                mPaint.setStrokeWidth(Integer.valueOf(mUnReachedHeight).floatValue());
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(xUnReachedStart, 0, xMaxUnReachedEnd, 0, mPaint);
            }
        } else {
            calculateTextWidthAndHeight();
            int xMaxReachedEnd = xMaxUnReachedEnd - mTextWidth - mTextMargin;
            if (xReachedEnd > xMaxReachedEnd) {
                xReachedEnd = xMaxReachedEnd;
            }
            if (xReachedEnd > 0) {
                mPaint.setColor(mReachedColor);
                mPaint.setStrokeWidth(Integer.valueOf(mReachedHeight).floatValue());
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(0, 0, xReachedEnd, 0, mPaint);
            }
            mPaint.setTextAlign(Paint.Align.LEFT);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mTextColor);
            float xTextStart = xReachedEnd > 0 ? xReachedEnd + mTextMargin : xReachedEnd;
            canvas.drawText(mText, xTextStart, Integer.valueOf(mTextHeight).floatValue() / 2, mPaint);
            float xUnReachedStart = xTextStart + mTextWidth + mTextMargin;
            if (xUnReachedStart < xMaxUnReachedEnd) {
                mPaint.setColor(mUnReachedColor);
                mPaint.setStrokeWidth(Integer.valueOf(mUnReachedHeight).floatValue());
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(xUnReachedStart, 0, xMaxUnReachedEnd, 0, mPaint);
            }
        }
        canvas.restore();
    }

    private void onDrawCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft() + Integer.valueOf(mMaxStrokeWidth).floatValue() / 2, getPaddingTop() + Integer.valueOf(mMaxStrokeWidth).floatValue() / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnReachedColor);
        mPaint.setStrokeWidth(Integer.valueOf(mUnReachedHeight).floatValue());
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mReachedColor);
        mPaint.setStrokeWidth(Integer.valueOf(mReachedHeight).floatValue());
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(fArcRect, 0, sweepAngle, false, mPaint);
        if (!mIsHiddenText) {
            calculateTextWidthAndHeight();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mTextColor);
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mText, mRadius, mRadius + Integer.valueOf(mTextHeight).floatValue() / 2, mPaint);
        }
        canvas.restore();
    }

    private void calculateTextWidthAndHeight() {
        // fix by Michael 改参数溢出
        mText = String.format(Locale.CHINA, "%d", (int) (getProgress() * 1.0f / getMax() * 100)) + "%";
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        mTextWidth = mTextRect.width();
        mTextHeight = mTextRect.height();
    }

    private enum Mode {
        /**
         * 系统
         */
        System,
        /**
         * 水平
         */
        Horizontal,
        /**
         * 圆
         */
        Circle,
        /**
         * h彗星
         */
        Comet,
        /**
         * 波浪
         */
        Wave
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
