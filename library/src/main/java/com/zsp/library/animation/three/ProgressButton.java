package com.zsp.library.animation.three;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

import androidx.core.content.ContextCompat;

import com.zsp.library.R;
import com.zsp.utilone.animation.AnimationManager;
import com.zsp.utilone.density.DensityUtils;
import com.zsp.utilone.screen.ScreenUtils;

import java.math.BigDecimal;

import value.WidgetMagic;

/**
 * @decs: ProgressButton
 * @author: 郑少鹏
 * @date: 2019/4/28 13:30
 */
public class ProgressButton extends View {
    /**
     * 画笔
     */
    private Paint fPaintRect;
    private Paint paintText;
    private Paint paintProcess;
    private int padding = 0;
    private float space = 0.0F;
    /**
     * 动画
     */
    private ProgressButtonAnimation progressButtonAnimation;
    private RotateAnimation rotateAnimation;
    private ScaleAnimation scaleAnimation;
    /**
     * 文色
     */
    private String text = "";
    private int textColor = Color.WHITE;
    private int processColor = Color.WHITE;
    private int bgColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
    /**
     * 停否
     */
    private boolean stop = false;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        stepPaint();
        stepAnimation();
    }

    /**
     * 初始画笔
     */
    private void stepPaint() {
        int strokeWidth = DensityUtils.dipToPxByInt(1);
        padding = DensityUtils.dipToPxByInt(2);
        // fPaintRect
        fPaintRect = new Paint();
        fPaintRect.setAntiAlias(true);
        fPaintRect.setStyle(Paint.Style.FILL);
        fPaintRect.setStrokeWidth(strokeWidth);
        // paintText
        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(DensityUtils.dipToPxByInt(14));
        // paintProcess
        paintProcess = new Paint();
        paintProcess.setAntiAlias(true);
        paintProcess.setStyle(Paint.Style.STROKE);
        paintProcess.setStrokeWidth(new BigDecimal(strokeWidth / 2).floatValue());
    }

    /**
     * 初始动画
     */
    private void stepAnimation() {
        progressButtonAnimation = new ProgressButtonAnimation();
        rotateAnimation = new RotateAnimation(
                0.0F, 360.0F,
                Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F);
        rotateAnimation.setRepeatCount(-1);
        // 不停顿
        rotateAnimation.setInterpolator(new LinearInterpolator());
        // 停于最后
        rotateAnimation.setFillAfter(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintText.setColor(textColor);
        fPaintRect.setColor(bgColor);
        paintProcess.setColor(processColor);
        // RectF对象
        RectF fRect = new RectF();
        fRect.left = padding + space;
        fRect.top = padding;
        fRect.right = getMeasuredWidth() - padding - space;
        fRect.bottom = getMeasuredHeight() - padding;
        float radius = (getMeasuredHeight() - 2 * padding) >> 1;
        canvas.drawRoundRect(fRect, radius, radius, fPaintRect);
        if (fRect.width() == fRect.height() && !stop) {
            RectF fRectProcess = new RectF();
            fRectProcess.left = getMeasuredWidth() / 2.0F - fRect.width() / 4;
            fRectProcess.top = getMeasuredHeight() / 2.0F - fRect.width() / 4;
            fRectProcess.right = getMeasuredWidth() / 2.0F + fRect.width() / 4;
            fRectProcess.bottom = getMeasuredHeight() / 2.0F + fRect.width() / 4;
            float startAngle = 0.0F;
            canvas.drawArc(fRectProcess, startAngle, 100, false, paintProcess);
        }
        if (space < (getMeasuredWidth() - getMeasuredHeight()) / WidgetMagic.FLOAT_EDL) {
            /*if (mRectF.width() > getFontLength(paintText, text)) {*/
            canvas.drawText(text,
                    getMeasuredWidth() / 2.0F - getFontLength(paintText, text) / 2.0F,
                    getMeasuredHeight() / 2.0F + getFontHeight(paintText, text) / 3.0F,
                    paintText);
            /*}*/
        }
    }

    public void startProcessAnimation() {
        setClickable(false);
        stop = false;
        if (progressButtonAnimation != null) {
            clearAnimation();
            progressButtonAnimation.setDuration(200);
        }
        startAnimation(progressButtonAnimation);
    }

    public void stopProcessAnimation() {
        setClickable(true);
        stop = true;
        clearAnimation();
        space = 0;
        invalidate();
        startAnimation(AnimationManager.shake(3.0F, 500));
    }

    public void stopProcessAnimationAndThen(final OnStopScaleAnimationListener onStopScaleAnimationListener) {
        setClickable(true);
        stop = true;
        clearAnimation();
        invalidate();
        if (scaleAnimation == null) {
            int width = ScreenUtils.screenWidth(getContext());
            float f = new BigDecimal(width / getMeasuredHeight()).floatValue();
            scaleAnimation = new ScaleAnimation(
                    1.0F, f * 3.5F,
                    1.0F, f * 3.5F,
                    Animation.RELATIVE_TO_SELF, 0.5F,
                    Animation.RELATIVE_TO_SELF, 0.5F);
        }
        scaleAnimation.setDuration(300);
        startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onStopScaleAnimationListener.scaleAnimationStop();
                clearAnimation();
                space = 0;
                invalidate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setText(String s) {
        this.text = s;
        invalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setProcessColor(int color) {
        this.processColor = color;
    }

    public void setBgColor(int color) {
        this.bgColor = color;
    }

    public float getFontLength(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }

    public float getFontHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    public interface OnStopScaleAnimationListener {
        /**
         * 缩放动画停止
         */
        void scaleAnimationStop();
    }

    private class ProgressButtonAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            space = (getMeasuredWidth() - getMeasuredHeight()) / 2.0F * interpolatedTime;
            invalidate();
            if (interpolatedTime == WidgetMagic.FLOAT_YDL) {
                if (rotateAnimation != null) {
                    clearAnimation();
                    rotateAnimation.setDuration(300);
                }
                startAnimation(rotateAnimation);
            }
        }
    }
}
