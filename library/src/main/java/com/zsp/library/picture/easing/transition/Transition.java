package com.zsp.library.picture.easing.transition;

import android.graphics.RectF;
import android.view.animation.Interpolator;

import com.zsp.library.picture.easing.exception.IncompatibleRatioException;
import com.zsp.library.picture.easing.util.MathUtils;

/**
 * @decs: 过渡
 * @author: 郑少鹏
 * @date: 2019/10/26 16:44
 */
public class Transition {
    /**
     * The rect the transition will start from.
     */
    private final RectF mSrcRect;
    /**
     * The rect the transition will end at.
     */
    private final RectF mDstRect;
    /**
     * An intermediary rect that changes in every frame according to the transition progress.
     */
    private final RectF mCurrentRect = new RectF();
    /**
     * Precomputed width difference between {@link #mSrcRect} and {@link #mDstRect}.
     */
    private final float mWidthDiff;
    /**
     * Precomputed height difference between {@link #mSrcRect} and {@link #mDstRect}.
     */
    private final float mHeightDiff;
    /**
     * Precomputed X offset between the center points of {@link #mSrcRect} and {@link #mDstRect}.
     */
    private final float xCenterDiff;
    /**
     * Precomputed Y offset between the center points of {@link #mSrcRect} and {@link #mDstRect}.
     */
    private final float yCenterDiff;
    /**
     * The duration of the transition in milliseconds.
     * The default duration is 5000 ms.
     */
    private final long mDuration;
    /**
     * The {@link Interpolator} used to perform the transitions between rects.
     */
    private final Interpolator mInterpolator;

    public Transition(RectF srcRect, RectF dstRect, long duration, Interpolator interpolator) {
        if (!MathUtils.haveSameAspectRatio(srcRect, dstRect)) {
            throw new IncompatibleRatioException();
        }
        mSrcRect = srcRect;
        mDstRect = dstRect;
        mDuration = duration;
        mInterpolator = interpolator;
        // Pre computes a few variables to avoid doing it in onDraw().
        mWidthDiff = dstRect.width() - srcRect.width();
        mHeightDiff = dstRect.height() - srcRect.height();
        xCenterDiff = dstRect.centerX() - srcRect.centerX();
        yCenterDiff = dstRect.centerY() - srcRect.centerY();
    }

    /**
     * Gets the rect that will take the scene when a Ken Burns transition starts.
     *
     * @return The rect that starts the transition.
     */
    public RectF getSourceRect() {
        return mSrcRect;
    }

    /**
     * Gets the rect that will take the scene when a Ken Burns transition ends.
     *
     * @return The rect that ends the transition.
     */
    public RectF getDestinyRect() {
        return mDstRect;
    }

    /**
     * Gets the current rect that represents the part of the image to take the scene in the current frame.
     *
     * @param elapsedTime The elapsed time since this transition started.
     */
    public RectF getInterpolatedRect(long elapsedTime) {
        float elapsedTimeFraction = elapsedTime / (float) mDuration;
        float interpolationProgress = Math.min(elapsedTimeFraction, 1);
        float interpolation = mInterpolator.getInterpolation(interpolationProgress);
        float currentWidth = mSrcRect.width() + (interpolation * mWidthDiff);
        float currentHeight = mSrcRect.height() + (interpolation * mHeightDiff);
        float xCurrentCenter = mSrcRect.centerX() + (interpolation * xCenterDiff);
        float yCurrentCenter = mSrcRect.centerY() + (interpolation * yCenterDiff);
        float left = xCurrentCenter - (currentWidth / 2);
        float top = yCurrentCenter - (currentHeight / 2);
        float right = left + currentWidth;
        float bottom = top + currentHeight;
        mCurrentRect.set(left, top, right, bottom);
        return mCurrentRect;
    }

    /**
     * Gets the duration of this transition.
     *
     * @return The duration, in milliseconds.
     */
    public long getDuration() {
        return mDuration;
    }
}
