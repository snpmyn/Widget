package com.zsp.library.layout.circularreveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.Property;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @decs: ViewRevealManager
 * @author: 郑少鹏
 * @date: 2019/8/27 11:38
 */
public class ViewRevealManager {
    private static final ClipRadiusProperty REVEAL = new ClipRadiusProperty();
    private final ViewTransformation viewTransformation;
    private final Map<View, RevealValues> targets = new HashMap<>();
    private final Map<Animator, RevealValues> animators = new HashMap<>();
    private final AnimatorListenerAdapter animatorCallback = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            final RevealValues values = getValues(animation);
            values.clip(true);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            endAnimation(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            endAnimation(animation);
        }

        private void endAnimation(Animator animation) {
            final RevealValues values = getValues(animation);
            values.clip(false);
            // clean up after animation is done
            targets.remove(values.target);
            animators.remove(animation);
        }
    };

    public ViewRevealManager() {
        this(new PathTransformation());
    }

    private ViewRevealManager(ViewTransformation transformation) {
        this.viewTransformation = transformation;
    }

    Animator dispatchCreateAnimator(RevealValues data) {
        final Animator animator = createAnimator(data);
        // before animation is started keep them
        targets.put(data.target(), data);
        animators.put(animator, data);
        return animator;
    }

    /**
     * Create custom animator of circular reveal.
     *
     * @param data RevealValues contains information of starting & ending points, animation target and current animation values.
     * @return animator to manage reveal animation
     */
    private Animator createAnimator(RevealValues data) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(data, REVEAL, data.startRadius, data.endRadius);
        animator.addListener(getAnimatorCallback());
        return animator;
    }

    private AnimatorListenerAdapter getAnimatorCallback() {
        return animatorCallback;
    }

    /**
     * Get values.
     *
     * @param animator Animator
     * @return Return Animator
     */
    private RevealValues getValues(Animator animator) {
        return animators.get(animator);
    }

    /**
     * Get values.
     *
     * @param view View
     * @return map of started animators
     */
    private RevealValues getValues(View view) {
        return targets.get(view);
    }

    /**
     * Override native animator.
     *
     * @return True if you don't want use Android native reveal animator in order to use your own custom one.
     */
    boolean overrideNativeAnimator() {
        return false;
    }

    /**
     * Is clipped.
     *
     * @param child View
     * @return True if animation was started and it is still running, otherwise returns false.
     */
    public boolean isClipped(View child) {
        final RevealValues data = getValues(child);
        return data != null && data.isClipping();
    }

    /**
     * Applies path clipping on a canvas before drawing child, you should save canvas state before viewTransformation and restore it afterwards.
     *
     * @param canvas canvas to apply clipping before drawing
     * @param child  reveal animation target
     * @return True if viewTransformation was successfully applied on referenced child, otherwise child be not the target and therefore animation was skipped.
     */
    public final boolean transform(Canvas canvas, View child) {
        final RevealValues revealData = targets.get(child);
        // Target doesn't has animation values
        if (revealData == null) {
            return false;
        }
        // Check whether target consistency
        else if (revealData.target != child) {
            throw new IllegalStateException("Inconsistency detected, contains incorrect target view");
        }
        // View doesn't wants to be clipped therefore transformation is useless
        else if (!revealData.clipping) {
            return false;
        }
        return viewTransformation.transform(canvas, child, revealData);
    }

    public static final class RevealValues {
        private static final Paint DEBUG_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);

        static {
            DEBUG_PAINT.setColor(Color.GREEN);
            DEBUG_PAINT.setStyle(Paint.Style.FILL);
            DEBUG_PAINT.setStrokeWidth(2);
        }

        final int centerX;
        final int centerY;
        final float startRadius;
        final float endRadius;
        // flag that indicates whether view is clipping now, mutable
        boolean clipping;
        // revealed radius
        float radius;
        // animation target
        View target;

        RevealValues(View target, int xCenter, int yCenter, float startRadius, float endRadius) {
            this.target = target;
            this.centerX = xCenter;
            this.centerY = yCenter;
            this.startRadius = startRadius;
            this.endRadius = endRadius;
        }

        public void radius(float radius) {
            this.radius = radius;
        }

        /**
         * radius
         *
         * @return current clipping radius
         */
        public float radius() {
            return radius;
        }

        /**
         * target
         *
         * @return animating view
         */
        public View target() {
            return target;
        }

        void clip(boolean clipping) {
            this.clipping = clipping;
        }

        /**
         * Is clipping.
         *
         * @return view clip status
         */
        boolean isClipping() {
            return clipping;
        }
    }

    /**
     * Custom View viewTransformation extension used for applying different reveal techniques.
     */
    interface ViewTransformation {
        /**
         * Apply view viewTransformation.
         *
         * @param canvas main canvas
         * @param child  target to be clipped & revealed
         * @param values RevealValues
         * @return True if viewTransformation is applied, otherwise return false.
         */
        boolean transform(Canvas canvas, View child, RevealValues values);
    }

    public static class PathTransformation implements ViewTransformation {
        // Android Canvas is tricky, we cannot clip circles directly with Canvas API but it is allowed using Path, therefore we use it :|.
        private final Path path = new Path();
        private Region.Op op = Region.Op.REPLACE;

        @Override
        public boolean transform(Canvas canvas, View child, RevealValues values) {
            path.reset();
            // trick to applyTransformation animation, when even x & y translations are running
            path.addCircle(child.getX() + values.centerX, child.getY() + values.centerY, values.radius, Path.Direction.CW);
            canvas.clipPath(path, op);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                child.invalidateOutline();
            }
            return false;
        }
    }

    /**
     * Property animator. For performance improvements better to use directly variable member (but it's little enhancement that always caught as dangerous, let's see)
     */
    private static final class ClipRadiusProperty extends Property<RevealValues, Float> {
        ClipRadiusProperty() {
            super(Float.class, "supportCircularReveal");
        }

        @Override
        public void set(RevealValues data, Float value) {
            data.radius = value;
            data.target.invalidate();
        }

        @Override
        public Float get(RevealValues v) {
            return v.radius();
        }
    }

    /**
     * As class name cue's it changes layer type of {@link View} on animation createAnimator in order to improve animation smooth & performance and returns original value on animation end.
     */
    static class ChangeViewLayerTypeAdapter extends AnimatorListenerAdapter {
        private RevealValues viewData;
        private int featuredLayerType;
        private int originalLayerType;

        ChangeViewLayerTypeAdapter(RevealValues viewData, int layerType) {
            this.viewData = viewData;
            this.featuredLayerType = layerType;
            this.originalLayerType = viewData.target.getLayerType();
        }

        @Override
        public void onAnimationStart(Animator animation) {
            viewData.target().setLayerType(featuredLayerType, null);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            viewData.target().setLayerType(originalLayerType, null);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            viewData.target().setLayerType(originalLayerType, null);
        }
    }
}
