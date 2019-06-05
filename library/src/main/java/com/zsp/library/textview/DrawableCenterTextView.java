package com.zsp.library.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created on 2018/10/25.
 *
 * @author 郑少鹏
 * @desc 上下图居中（xml设android:gravity="center_horizontal"）背景自白
 * 左右图居中（xml设android:gravity="center_vertical"）背景自白
 */
public class DrawableCenterTextView extends AppCompatTextView {
    private Rect rect;

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获TextView之Drawable对象，返数组长应4（对应左上右下）
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[0];
        if (drawable != null) {
            // 左Drawable非空，测需绘文本宽
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawable.getIntrinsicWidth();
            // 总宽（文本宽 + drawablePadding + drawableWidth）
            float bodyWidth = textWidth + drawablePadding + drawableWidth;
            // 移画布绘X轴
            canvas.translate((getWidth() - bodyWidth) / 2, 0);
        } else if ((drawable = drawables[1]) != null) {
            // 上Drawable非空则获文本高
            getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), rect);
            float textHeight = rect.height();
            int drawablePadding = getCompoundDrawablePadding();
            int drawableHeight = drawable.getIntrinsicHeight();
            // 总高（文本高 + drawablePadding + drawableHeight）
            float bodyHeight = textHeight + drawablePadding + drawableHeight;
            // 移画布绘Y轴
            canvas.translate(0, (getHeight() - bodyHeight) / 2);
        }
        super.onDraw(canvas);
    }
}
