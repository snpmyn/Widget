package com.zsp.library.sidebar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.zsp.library.R;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import value.WidgetLibraryMagic;

/**
 * @decs: 侧边波浪栏
 * @author: 郑少鹏
 * @date: 2019/9/12 15:25
 */
public class WaveSideBar extends View {
    /**
     * 贝塞尔曲线角弧长
     */
    private static final double ANGLE = Math.PI * 45 / 180;
    private static final double ANGLE_R = Math.PI * 90 / 180;
    private OnTouchLetterChangeListener mListener;
    /**
     * 渲染字母表
     */
    private List<String> mLetters;
    /**
     * 当前选位
     */
    private int mChoosePosition = -1;
    private int mOldPosition;
    private int mNewPosition;
    /**
     * 字母列表画笔
     */
    private final Paint mLettersPaint = new Paint();
    /**
     * 提示字母画笔
     */
    private final Paint mTextPaint = new Paint();
    /**
     * 波浪画笔
     */
    private Paint mWavePaint = new Paint();
    private int mTextSize;
    private int mTextColor;
    private int mTextColorChoose;
    private int mWidth;
    private int mHeight;
    private int mItemHeight;
    private int mPadding;
    /**
     * 波浪路径
     */
    private final Path mWavePath = new Path();
    /**
     * 圆形路径
     */
    private final Path mCirclePath = new Path();
    /**
     * 手指滑Y点作中心点
     */
    private int yCenter;
    /**
     * 贝塞尔曲线分布半径
     */
    private int mRadius;
    /**
     * 圆形半径
     */
    private int mCircleRadius;
    /**
     * 过渡效果计算
     */
    private ValueAnimator mRatioAnimator;
    /**
     * 贝塞尔曲线比率
     */
    private float mRatio;
    /**
     * 选中字坐标
     */
    private float xPoint, yPoint;
    /**
     * 圆形中心点X
     */
    private float xCircleCenter;

    public WaveSideBar(Context context) {
        this(context, null);
    }

    public WaveSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {
        mLetters = Arrays.asList(context.getResources().getStringArray(R.array.WaveSideBarLetters));
        mTextColor = Color.parseColor("#969696");
        int mWaveColor = Color.parseColor("#bef9b81b");
        mTextColorChoose = ContextCompat.getColor(context, android.R.color.white);
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.sp_10);
        int mHintTextSize = context.getResources().getDimensionPixelSize(R.dimen.sp_32);
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.dp_20);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WaveSideBar);
            mTextColor = a.getColor(R.styleable.WaveSideBar_textColor, mTextColor);
            mTextColorChoose = a.getColor(R.styleable.WaveSideBar_chooseTextColor, mTextColorChoose);
            mTextSize = a.getDimensionPixelSize(R.styleable.WaveSideBar_textSize, mTextSize);
            mHintTextSize = a.getDimensionPixelSize(R.styleable.WaveSideBar_hintTextSize, mHintTextSize);
            mWaveColor = a.getColor(R.styleable.WaveSideBar_backgroundColor, mWaveColor);
            mRadius = a.getDimensionPixelSize(R.styleable.WaveSideBar_radius, context.getResources().getDimensionPixelSize(R.dimen.dp_20));
            mCircleRadius = a.getDimensionPixelSize(R.styleable.WaveSideBar_circleRadius, context.getResources().getDimensionPixelSize(R.dimen.dp_24));
            a.recycle();
        }
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(mWaveColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColorChoose);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mHintTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        final float y = event.getY();
        final float x = event.getX();
        mOldPosition = mChoosePosition;
        mNewPosition = (int) (y / mHeight * mLetters.size());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 限触范围
                if (x < mWidth - WidgetLibraryMagic.FLOAT_ONE_DOT_FIVE * mRadius) {
                    return false;
                }
                yCenter = (int) y;
                startAnimator(1.0F);
                break;
            case MotionEvent.ACTION_MOVE:
                yCenter = (int) y;
                if (mOldPosition != mNewPosition) {
                    if (mNewPosition >= 0 && mNewPosition < mLetters.size()) {
                        mChoosePosition = mNewPosition;
                        if (mListener != null) {
                            mListener.onLetterChange(mLetters.get(mNewPosition));
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAnimator(0.0F);
                mChoosePosition = -1;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mItemHeight = (mHeight - mPadding) / mLetters.size();
        xPoint = mWidth - 1.6F * mTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘字母列表
        drawLetters(canvas);
        // 绘波浪
        drawWavePath(canvas);
        // 绘圆
        drawCirclePath(canvas);
        // 绘选中字
        drawChooseText(canvas);
    }

    /**
     * 绘字母列表
     *
     * @param canvas 画布
     */
    private void drawLetters(@NonNull Canvas canvas) {
        RectF fRect = new RectF();
        fRect.left = xPoint - mTextSize;
        fRect.right = xPoint + mTextSize;
        fRect.top = new BigDecimal(mTextSize / 2).floatValue();
        fRect.bottom = mHeight - new BigDecimal(mTextSize / 2).floatValue();
        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.FILL);
        mLettersPaint.setColor(Color.parseColor("#F9F9F9"));
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(fRect, mTextSize, mTextSize, mLettersPaint);
        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.STROKE);
        mLettersPaint.setColor(mTextColor);
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(fRect, mTextSize, mTextSize, mLettersPaint);
        for (int i = 0; i < mLetters.size(); i++) {
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColor);
            mLettersPaint.setAntiAlias(true);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = mLettersPaint.getFontMetrics();
            float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);
            float yPoint = mItemHeight * i + baseline / 2 + mPadding;
            if (i == mChoosePosition) {
                this.yPoint = yPoint;
            } else {
                canvas.drawText(mLetters.get(i), xPoint, yPoint, mLettersPaint);
            }
        }
    }

    /**
     * 绘选中字母
     *
     * @param canvas 画布
     */
    private void drawChooseText(Canvas canvas) {
        if (mChoosePosition != -1) {
            // 绘右选中字符
            mLettersPaint.reset();
            mLettersPaint.setColor(mTextColorChoose);
            mLettersPaint.setTextSize(mTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mLetters.get(mChoosePosition), xPoint, yPoint, mLettersPaint);
            // 绘提示字符
            if (mRatio >= WidgetLibraryMagic.FLOAT_ZERO_DOT_NINE) {
                String target = mLetters.get(mChoosePosition);
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);
                float x = xCircleCenter;
                float y = yCenter + baseline / 2;
                canvas.drawText(target, x, y, mTextPaint);
            }
        }
    }

    /**
     * 绘波浪
     *
     * @param canvas 画布
     */
    private void drawWavePath(@NonNull Canvas canvas) {
        mWavePath.reset();
        // 移至起始点
        mWavePath.moveTo(mWidth, yCenter - 3 * mRadius);
        // 上部控制点Y轴位
        int yControlTop = yCenter - 2 * mRadius;
        // 上部结束点坐标
        int xEndTop = (int) (mWidth - mRadius * Math.cos(ANGLE) * mRatio);
        int yEndTop = (int) (yControlTop + mRadius * Math.sin(ANGLE));
        mWavePath.quadTo(mWidth, yControlTop, xEndTop, yEndTop);
        // 中心控制点坐标
        int xControlCenter = (int) (mWidth - 1.8F * mRadius * Math.sin(ANGLE_R) * mRatio);
        int yControlCenter = yCenter;
        // 下部结束点坐标
        int yControlBottom = yCenter + 2 * mRadius;
        int yEndBottom = (int) (yControlBottom - mRadius * Math.cos(ANGLE));
        mWavePath.quadTo(xControlCenter, yControlCenter, xEndTop, yEndBottom);
        mWavePath.quadTo(mWidth, yControlBottom, mWidth, yControlBottom + mRadius);
        mWavePath.close();
        canvas.drawPath(mWavePath, mWavePaint);
    }

    /**
     * 绘左提示圆
     *
     * @param canvas 画布
     */
    private void drawCirclePath(Canvas canvas) {
        // X轴移路径
        xCircleCenter = (mWidth + mCircleRadius) - (2.0F * mRadius + 2.0F * mCircleRadius) * mRatio;
        mCirclePath.reset();
        mCirclePath.addCircle(xCircleCenter, yCenter, mCircleRadius, Path.Direction.CW);
        mCirclePath.op(mWavePath, Path.Op.DIFFERENCE);
        mCirclePath.close();
        canvas.drawPath(mCirclePath, mWavePaint);
    }

    private void startAnimator(float value) {
        if (mRatioAnimator == null) {
            mRatioAnimator = new ValueAnimator();
        }
        mRatioAnimator.cancel();
        mRatioAnimator.setFloatValues(value);
        mRatioAnimator.addUpdateListener(value1 -> {
            mRatio = (float) value1.getAnimatedValue();
            // 球弹到位且点击位变，即点时显当前选位
            if (mRatio == WidgetLibraryMagic.FLOAT_ONE_DOT_ZERO && mOldPosition != mNewPosition) {
                if (mNewPosition >= 0 && mNewPosition < mLetters.size()) {
                    mChoosePosition = mNewPosition;
                    if (mListener != null) {
                        mListener.onLetterChange(mLetters.get(mNewPosition));
                    }
                }
            }
            invalidate();
        });
        mRatioAnimator.start();
    }

    public void setOnTouchLetterChangeListener(OnTouchLetterChangeListener listener) {
        this.mListener = listener;
    }

    public List<String> getLetters() {
        return mLetters;
    }

    public void setLetters(List<String> letters) {
        this.mLetters = letters;
        invalidate();
    }

    public interface OnTouchLetterChangeListener {
        /**
         * 字母变
         *
         * @param letter 字母
         */
        void onLetterChange(String letter);
    }
}