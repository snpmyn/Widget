package com.zsp.library.layout.camber;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.zsp.library.R;

import timber.log.Timber;

/**
 * @decs: 弧形容器
 * @author: 郑少鹏
 * @date: 2019/10/26 14:56
 */
public class CamberContainer extends RelativeLayout {
    Context mContext;
    Path mClipPath;
    Path mOutlinePath;
    int width = 0;
    int height = 0;
    /**
     * Changes the amount of curve.
     * Default is 50.
     */
    int curvatureHeight = 50;
    Paint mPaint;
    private PorterDuffXfermode porterDuffXfermode;

    public CamberContainer(Context context) {
        super(context);
        init(context, null);
    }

    public CamberContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mClipPath = new Path();
        mOutlinePath = new Path();
        TypedArray styledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.CamberContainer, 0, 0);
        if (styledAttributes.hasValue(R.styleable.CamberContainer_CamberContainerCurvature)) {
            curvatureHeight = (int) styledAttributes.getDimension(R.styleable.CamberContainer_CamberContainerCurvature, getDpForPixel(curvatureHeight));
        }
        styledAttributes.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        mClipPath = PathProvider.getClipPath(width, height, curvatureHeight, 0, 0);
        ViewCompat.setElevation(this, ViewCompat.getElevation(this));
        try {
            setOutlineProvider(getOutlineProvider());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                try {
                    outline.setPath(PathProvider.getOutlinePath(width, height, curvatureHeight, 0, 0));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        };
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        int saveCount;
        saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        super.dispatchDraw(canvas);
        mPaint.setXfermode(porterDuffXfermode);
        canvas.drawPath(mClipPath, mPaint);
        canvas.restoreToCount(saveCount);
        mPaint.setXfermode(null);
    }

    private int getDpForPixel(int pixel) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, mContext.getResources().getDisplayMetrics());
    }
}
