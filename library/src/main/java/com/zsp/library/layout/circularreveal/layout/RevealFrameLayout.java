package com.zsp.library.layout.circularreveal.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.zsp.library.layout.circularreveal.RevealViewGroup;
import com.zsp.library.layout.circularreveal.ViewRevealManager;

/**
 * @decs: RevealFrameLayout
 * @author: 郑少鹏
 * @date: 2019/8/27 11:39
 */
public class RevealFrameLayout extends FrameLayout implements RevealViewGroup {
    private ViewRevealManager manager;

    public RevealFrameLayout(Context context) {
        this(context, null);
    }

    public RevealFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RevealFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        manager = new ViewRevealManager();
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        try {
            canvas.save();
            return manager.transform(canvas, child)
                    & super.drawChild(canvas, child, drawingTime);
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
