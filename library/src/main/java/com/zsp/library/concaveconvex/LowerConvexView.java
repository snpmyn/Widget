package com.zsp.library.concaveconvex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zsp.library.R;

import java.math.BigDecimal;

/**
 * Created on 2019/6/11.
 *
 * @author 郑少鹏
 * @desc 下凸视图
 */
public class LowerConvexView extends View {
    /**
     * 宽高
     */
    private int width;
    private int height;
    /**
     * 弧高
     */
    private final int arcHeight;
    /**
     * 背景色
     */
    private final int backgroundColor;
    /**
     * 画笔
     */
    private final Paint paint;

    public LowerConvexView(Context context) {
        this(context, null);
    }

    public LowerConvexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LowerConvexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LowerConvexView);
        arcHeight = typedArray.getDimensionPixelSize(R.styleable.LowerConvexView_lowerConvexViewArcHeight, 0);
        backgroundColor = typedArray.getColor(R.styleable.LowerConvexView_lowerConvexViewBackgroundColor, Color.parseColor("#303F9F"));
        typedArray.recycle();
        paint = new Paint();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroundColor);
        Rect rect = new Rect(0, 0, width, height - arcHeight);
        canvas.drawRect(rect, paint);
        Path path = new Path();
        path.moveTo(0, height - arcHeight);
        path.quadTo(new BigDecimal(width / 2).floatValue(), height, width, height - arcHeight);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }
}
