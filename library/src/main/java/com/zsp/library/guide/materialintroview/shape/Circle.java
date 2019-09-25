package com.zsp.library.guide.materialintroview.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.zsp.library.guide.materialintroview.target.Target;

/**
 * @decs: 圆
 * @author: 郑少鹏
 * @date: 2019/9/24 11:52
 */
public class Circle extends BaseShape {
    private int radius;
    private Point circlePoint;

    public Circle(Target target, Focus focus, FocusGravity focusGravity, int padding) {
        super(target, focus, focusGravity, padding);
        circlePoint = getFocusPoint();
        calculateRadius(padding);
    }

    @Override
    public void draw(Canvas canvas, Paint eraser, int padding) {
        calculateRadius(padding);
        circlePoint = getFocusPoint();
        canvas.drawCircle(circlePoint.x, circlePoint.y, radius, eraser);
    }

    @Override
    public void reCalculateAll() {
        calculateRadius(padding);
        circlePoint = getFocusPoint();
    }

    private void calculateRadius(int padding) {
        int side;
        if (focus == Focus.MINIMUM) {
            side = Math.min(target.getRect().width() / 2, target.getRect().height() / 2);
        } else if (focus == Focus.ALL) {
            side = Math.max(target.getRect().width() / 2, target.getRect().height() / 2);
        } else {
            int minSide = Math.min(target.getRect().width() / 2, target.getRect().height() / 2);
            int maxSide = Math.max(target.getRect().width() / 2, target.getRect().height() / 2);
            side = (minSide + maxSide) / 2;
        }
        radius = side + padding;
    }

    private int getRadius() {
        return radius;
    }

    @Override
    public Point getPoint() {
        return circlePoint;
    }

    @Override
    public int getHeight() {
        return 2 * getRadius();
    }

    @Override
    public boolean isTouchOnFocus(double x, double y) {
        int xPoint = getPoint().x;
        int yPoint = getPoint().y;
        double dx = Math.pow(x - xPoint, 2);
        double dy = Math.pow(y - yPoint, 2);
        return (dx + dy) <= Math.pow(radius, 2);
    }
}
