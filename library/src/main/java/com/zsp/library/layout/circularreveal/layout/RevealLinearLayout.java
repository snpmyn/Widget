package com.zsp.library.layout.circularreveal.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.zsp.library.layout.circularreveal.RevealViewGroup;
import com.zsp.library.layout.circularreveal.ViewRevealManager;

/**
 * @decs: RevealLinearLayout
 * @author: 郑少鹏
 * @date: 2019/8/27 11:40
 */
public class RevealLinearLayout extends LinearLayout implements RevealViewGroup {
    private ViewRevealManager manager;

    public RevealLinearLayout(Context context) {
        this(context, null);
    }

    public RevealLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RevealLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        manager = new ViewRevealManager();
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, View child, long drawingTime) {
        try {
            canvas.save();
            manager.transform(canvas, child);
            return super.drawChild(canvas, child, drawingTime);
        } finally {
            canvas.restore();
        }
    }

    @Override
    public void setViewRevealManager(ViewRevealManager manager) {
        if (manager == null) {
            throw new NullPointerException("ViewRevealManager is null");
        }

        this.manager = manager;
    }

    @Override
    public ViewRevealManager getViewRevealManager() {
        return manager;
    }
}
