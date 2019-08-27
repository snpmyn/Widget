package com.zsp.library.animation.reveal;

import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @decs: BezierTranslateAnimation
 * @author: 郑少鹏
 * @date: 2019/8/27 10:58
 */
public class BezierTranslateAnimation extends Animation {
    private int xFromType = ABSOLUTE;
    private int xToType = ABSOLUTE;
    private int yFromType = ABSOLUTE;
    private int yToType = ABSOLUTE;
    private float xFromValue;
    private float xToValue;
    private float yFromValue;
    private float yToValue;
    private PointF mStart;
    private PointF mControl;
    private PointF mEnd;

    /**
     * Constructor to use when building a BezierTranslateAnimation from code.
     *
     * @param xFromDelta change in X coordinate to apply at the start of the animation
     * @param xToDelta   change in X coordinate to apply at the end of the animation
     * @param yFromDelta change in Y coordinate to apply at the start of the animation
     * @param yToDelta   change in Y coordinate to apply at the end of the animation
     */
    private BezierTranslateAnimation(float xFromDelta, float xToDelta, float yFromDelta, float yToDelta) {
        xFromValue = xFromDelta;
        xToValue = xToDelta;
        yFromValue = yFromDelta;
        yToValue = yToDelta;
    }

    /**
     * Constructor to use when building a BezierTranslateAnimation from code.
     *
     * @param xFromType  Specifies how fromXValue should be interpreted.
     *                   One of Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT.
     * @param xFromValue Change in X coordinate to apply at the start of the animation.
     *                   This value can either be an absolute number if fromXType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param xToType    Specifies how toXValue should be interpreted.
     *                   One of Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT.
     * @param xToValue   Change in X coordinate to apply at the end of the animation.
     *                   This value can either be an absolute number if toXType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param yFromType  Specifies how fromYValue should be interpreted.
     *                   One of Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT.
     * @param yFromValue Change in Y coordinate to apply at the start of the animation.
     *                   This value can either be an absolute number if fromYType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param yToType    Specifies how toYValue should be interpreted.
     *                   One of Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT.
     * @param yToValue   Change in Y coordinate to apply at the end of the animation.
     *                   This value can either be an absolute number if toYType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     */
    public BezierTranslateAnimation(int xFromType, float xFromValue, int xToType, float xToValue,
                                    int yFromType, float yFromValue, int yToType, float yToValue) {
        this.xFromValue = xFromValue;
        this.xToValue = xToValue;
        this.yFromValue = yFromValue;
        this.yToValue = yToValue;
        this.xFromType = xFromType;
        this.xToType = xToType;
        this.yFromType = yFromType;
        this.yToType = yToType;
    }

    /**
     * Constructor to use when building a BezierTranslateAnimation from code.
     *
     * @param xFromDelta   change in X coordinate to apply at the start of the animation
     * @param xToDelta     change in X coordinate to apply at the end of the animation
     * @param yFromDelta   change in Y coordinate to apply at the start of the animation
     * @param yToDelta     change in Y coordinate to apply at the end of the animation
     * @param controlPoint Control point for Bezier algorithm.
     */
    BezierTranslateAnimation(float xFromDelta, float xToDelta, float yFromDelta, float yToDelta, PointF controlPoint) {
        this(xFromDelta, xToDelta, yFromDelta, yToDelta);
        mControl = controlPoint;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float dx = calculateBezier(interpolatedTime, mStart.x, mControl.x, mEnd.x);
        final float dy = calculateBezier(interpolatedTime, mStart.y, mControl.y, mEnd.y);
        t.getMatrix().setTranslate(dx, dy);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        final float xFromDelta = resolveSize(xFromType, xFromValue, width, parentWidth);
        final float xToDelta = resolveSize(xToType, xToValue, width, parentWidth);
        final float yFromDelta = resolveSize(yFromType, yFromValue, height, parentHeight);
        final float yToDelta = resolveSize(yToType, yToValue, height, parentHeight);
        mStart = new PointF(xFromDelta, yFromDelta);
        mEnd = new PointF(xToDelta, yToDelta);
        // - Define the cross of the two tangents from point 0 and point 1 as control point if necessary.
        if (mControl == null) {
            mControl = new PointF(xFromDelta, yToDelta);
        }
    }

    /**
     * Calculate the position on a quadratic bezier curve by given three points and the percentage of time passed.
     *
     * @param interpolatedTime The fraction of the duration that has passed where 0 <= time <= 1.
     * @param point0           A single dimension of the starting point.
     * @param point1           A single dimension of the control point.
     * @param point2           A single dimension of the ending point.
     */
    private long calculateBezier(float interpolatedTime, float point0, float point1, float point2) {
        return Math.round((Math.pow((1 - interpolatedTime), 2) * point0)
                + (2 * (1 - interpolatedTime) * interpolatedTime * point1)
                + (Math.pow(interpolatedTime, 2) * point2));
    }
}