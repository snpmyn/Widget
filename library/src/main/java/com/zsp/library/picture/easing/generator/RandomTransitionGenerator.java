package com.zsp.library.picture.easing.generator;

import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import com.zsp.library.picture.easing.transition.Transition;
import com.zsp.library.picture.easing.util.MathUtils;

import org.jetbrains.annotations.Contract;

import java.util.Random;

/**
 * @decs: 随机过渡发电机
 * @author: 郑少鹏
 * @date: 2019/10/26 16:43
 */
public class RandomTransitionGenerator implements TransitionGenerator {
    /**
     * Default value for the transition duration in milliseconds.
     */
    private static final int DEFAULT_TRANSITION_DURATION = 10000;
    /**
     * Minimum rect dimension factor, according to the maximum one.
     */
    private static final float MIN_RECT_FACTOR = 0.75f;
    /**
     * Random object used to generate arbitrary rects.
     */
    private final Random mRandom = new Random(System.currentTimeMillis());
    /**
     * The duration, in milliseconds, of each transition.
     */
    private long mTransitionDuration;
    /**
     * The {@link Interpolator} to be used to create transitions.
     */
    private Interpolator mTransitionInterpolator;
    /**
     * The last generated transition.
     */
    private Transition mLastGenTrans;
    /**
     * The bounds of the drawable when the last transition was generated.
     */
    private RectF mLastDrawableBounds;

    public RandomTransitionGenerator() {
        this(DEFAULT_TRANSITION_DURATION, new AccelerateDecelerateInterpolator());
    }

    public RandomTransitionGenerator(long transitionDuration, Interpolator transitionInterpolator) {
        setTransitionDuration(transitionDuration);
        setTransitionInterpolator(transitionInterpolator);
    }

    @Override
    public Transition generateNextTransition(RectF drawableBounds, RectF viewport) {
        boolean firstTransition = mLastGenTrans == null;
        boolean drawableBoundsChanged = true;
        boolean viewportRatioChanged = true;
        RectF srcRect;
        RectF dstRect = null;
        if (!firstTransition) {
            dstRect = mLastGenTrans.getDestinyRect();
            drawableBoundsChanged = !drawableBounds.equals(mLastDrawableBounds);
            viewportRatioChanged = !MathUtils.haveSameAspectRatio(dstRect, viewport);
        }
        if (dstRect == null || drawableBoundsChanged || viewportRatioChanged) {
            srcRect = generateRandomRect(drawableBounds, viewport);
        } else {
            // Sets the destiny rect of the last transition as the source one if the current drawable has the same dimensions as the one of the last transition.
            srcRect = dstRect;
        }
        dstRect = generateRandomRect(drawableBounds, viewport);
        mLastGenTrans = new Transition(srcRect, dstRect, mTransitionDuration, mTransitionInterpolator);
        mLastDrawableBounds = new RectF(drawableBounds);
        return mLastGenTrans;
    }

    /**
     * Generates a random rect that can be fully contained within {@code drawableBounds} and has the same aspect ratio of {@code viewportRect}.
     * The dimensions of this random rect won't be higher than the largest rect with the same aspect ratio of {@code viewportRect} that {@code drawableBounds} can contain.
     * They also won't be lower than the dimensions of this upper rect limit weighted by {@code MIN_RECT_FACTOR}.
     *
     * @param drawableBounds The bounds of the drawable that will be zoomed and panned.
     * @param viewportRect   The bounds of the view that the drawable will be shown.
     * @return An arbitrary generated rect with the same aspect ratio of {@code viewportRect} that will be contained within {@code drawableBounds}.
     */
    @NonNull
    @Contract("_, _ -> new")
    private RectF generateRandomRect(RectF drawableBounds, RectF viewportRect) {
        float drawableRatio = MathUtils.getRectRatio(drawableBounds);
        float viewportRectRatio = MathUtils.getRectRatio(viewportRect);
        RectF maxCrop;
        float r;
        float b;
        if (drawableRatio > viewportRectRatio) {
            r = (drawableBounds.height() / viewportRect.height()) * viewportRect.width();
            b = drawableBounds.height();
        } else {
            r = drawableBounds.width();
            b = (drawableBounds.width() / viewportRect.width()) * viewportRect.height();
        }
        maxCrop = new RectF(0, 0, r, b);
        float randomFloat = MathUtils.truncate(mRandom.nextFloat(), 2);
        float factor = MIN_RECT_FACTOR + ((1 - MIN_RECT_FACTOR) * randomFloat);
        float width = factor * maxCrop.width();
        float height = factor * maxCrop.height();
        int widthDiff = (int) (drawableBounds.width() - width);
        int heightDiff = (int) (drawableBounds.height() - height);
        int left = widthDiff > 0 ? mRandom.nextInt(widthDiff) : 0;
        int top = heightDiff > 0 ? mRandom.nextInt(heightDiff) : 0;
        return new RectF(left, top, left + width, top + height);
    }

    /**
     * Sets the duration, in milliseconds, for each transition generated.
     *
     * @param transitionDuration the transition duration.
     */
    private void setTransitionDuration(long transitionDuration) {
        mTransitionDuration = transitionDuration;
    }

    /**
     * Sets the {@link Interpolator} for each transition generated.
     *
     * @param interpolator the transition interpolator.
     */
    private void setTransitionInterpolator(Interpolator interpolator) {
        mTransitionInterpolator = interpolator;
    }
}
