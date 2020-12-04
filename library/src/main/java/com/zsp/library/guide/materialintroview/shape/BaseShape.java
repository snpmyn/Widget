package com.zsp.library.guide.materialintroview.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.zsp.library.guide.materialintroview.target.Target;

import value.WidgetLibraryMagic;

/**
 * @decs: 基状
 * @author: 郑少鹏
 * @date: 2019/9/24 11:48
 */
public abstract class BaseShape {
    protected Target target;
    protected int padding;
    Focus focus;
    private final FocusGravity focusGravity;

    BaseShape(Target target) {
        this(target, Focus.MINIMUM);
    }

    BaseShape(Target target, Focus focus) {
        this(target, focus, FocusGravity.CENTER, WidgetLibraryMagic.INT_TEN);
    }

    BaseShape(Target target, Focus focus, FocusGravity focusGravity, int padding) {
        this.target = target;
        this.focus = focus;
        this.focusGravity = focusGravity;
        this.padding = padding;
    }

    /**
     * 绘
     *
     * @param canvas  画布
     * @param eraser  画笔
     * @param padding 边距
     */
    public abstract void draw(Canvas canvas, Paint eraser, int padding);

    Point getFocusPoint() {
        if (focusGravity == FocusGravity.LEFT) {
            int xLeft = target.getRect().left + (target.getPoint().x - target.getRect().left) / 2;
            return new Point(xLeft, target.getPoint().y);
        } else if (focusGravity == FocusGravity.RIGHT) {
            int xRight = target.getPoint().x + (target.getRect().right - target.getPoint().x) / 2;
            return new Point(xRight, target.getPoint().y);
        } else {
            return target.getPoint();
        }
    }

    /**
     * 重算全部
     */
    public abstract void reCalculateAll();

    /**
     * Get point.
     *
     * @return Point
     */
    public abstract Point getPoint();

    /**
     * Get height.
     *
     * @return height
     */
    public abstract int getHeight();

    /**
     * Determines if a click is on the shape.
     *
     * @param x x-axis location of click
     * @param t y-axis location of click
     * @return true if click is inside shape
     */
    public abstract boolean isTouchOnFocus(double x, double t);
}
