package com.zsp.library.wheelview.listener;

import android.view.MotionEvent;

import com.zsp.library.wheelview.view.WheelView;

/**
 * Created on 2018/4/3.
 *
 * @author 郑少鹏
 * @desc 手势监听
 */
public final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {
    private final WheelView wheelView;

    public LoopViewGestureListener(WheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float xVelocity, float yVelocity) {
        wheelView.scrollBy(yVelocity);
        return true;
    }
}

