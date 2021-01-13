package com.zsp.library.animation.reveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.PointF;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * @decs: RevealHelper
 * @author: 郑少鹏
 * @date: 2019/8/27 11:02
 */
final class RevealHelper {
    /**
     * Disallow instantiation.
     */
    private RevealHelper() {

    }

    /**
     * Helps to hide then translate a view to another view.
     *
     * @param fromView                       From view.
     * @param toView                         Target view.
     * @param duration                       Duration.
     * @param curvedTranslation              Curved translation.
     * @param controlPoint                   Curved angle.
     * @param hideFromViewAtInterpolatedTime Start hiding from view interpolated time.
     *                                       Must be between 0 and 1.
     * @param animationListener              Animation listener.
     */
    static void translateAndHideView(final View fromView, final View toView, final long duration, final boolean curvedTranslation, final PointF controlPoint, final float hideFromViewAtInterpolatedTime, final Animation.AnimationListener animationListener) {
        // - Determine translate delta.
        final PointF delta = getCenterLocationsDelta(fromView, toView);
        // - Prepare translate animation.
        Animation translateAnimation;
        if (curvedTranslation) {
            translateAnimation = new BezierTranslateAnimation(0, delta.x, 0, delta.y, controlPoint);
        } else {
            translateAnimation = new TranslateAnimation(0, delta.x, 0, delta.y);
        }
        translateAnimation.setDuration(duration);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        // - Prepare hide animation.
        final ScaleAnimation hideAnimation = new ScaleAnimation(fromView.getScaleX(), 0, fromView.getScaleY(), 0, Animation.ABSOLUTE, delta.x + Integer.valueOf(fromView.getMeasuredWidth() / 2).floatValue(), Animation.ABSOLUTE, delta.y + Integer.valueOf(fromView.getMeasuredHeight() / 2).floatValue());
        hideAnimation.setDuration((long) (duration * Math.min(1, Math.max(0, 1 - hideFromViewAtInterpolatedTime))));
        hideAnimation.setStartOffset((long) (duration * Math.min(1, Math.max(0, hideFromViewAtInterpolatedTime))));
        hideAnimation.setInterpolator(new AccelerateInterpolator());
        // - Prepare animations set.
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(hideAnimation);
        animationSet.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                fromView.setVisibility(View.INVISIBLE);
            }
        });
        animationSet.setAnimationListener(animationListener);
        // - Let's move !
        fromView.startAnimation(animationSet);
    }

    /**
     * Helps to translate then show a view to another view.
     *
     * @param viewToTranslate                  View to translate..
     * @param fromView                         From view.
     * @param startDelay                       Start delay.
     * @param duration                         Translate duration.
     * @param curvedTranslation                Curved translation.
     * @param controlPoint                     Curved angle.
     * @param showFromViewInterpolatedDuration Show from view interpolated duration. Must be between 0 and 1.
     */
    static void showAndTranslateView(final View viewToTranslate, final View fromView, final int startDelay, final int duration, final boolean curvedTranslation, final PointF controlPoint, float showFromViewInterpolatedDuration, final Runnable animationEndCallBack) {
        // - Determine translate delta.
        final PointF delta = getCenterLocationsDelta(viewToTranslate, fromView);
        // - Prepare show animation.
        final ScaleAnimation showAnimation = new ScaleAnimation(0, viewToTranslate.getScaleX(), 0, viewToTranslate.getScaleY(), Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        showAnimation.setDuration((long) (duration * Math.min(1, Math.max(0, showFromViewInterpolatedDuration))));
        showAnimation.setInterpolator(new BounceInterpolator());
        // - Prepare translate animation.
        Animation translateAnimation;
        if (curvedTranslation) {
            translateAnimation = new BezierTranslateAnimation(delta.x, 0, delta.y, 0, controlPoint);
        } else {
            translateAnimation = new TranslateAnimation(delta.x, 0, delta.y, 0);
        }
        translateAnimation.setDuration(duration);
        translateAnimation.setStartOffset((long) (duration * 0.1f));
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        // - Prepare animations set.
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.setStartOffset(startDelay);
        animationSet.addAnimation(showAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                viewToTranslate.setVisibility(View.VISIBLE);
                animationEndCallBack.run();
            }
        });
        // - Let's move !
        viewToTranslate.startAnimation(animationSet);
    }

    /**
     * Helps to reveal a view.
     *
     * @param viewToReveal         View to reveal.
     * @param duration             Duration.
     * @param animationEndCallBack Callback fired on animation end.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void revealView(@NonNull final View viewToReveal, final int duration, final Runnable animationEndCallBack) {
        // - Determine circle location and size.
        int xViewCenter = (viewToReveal.getLeft() + viewToReveal.getRight()) / 2;
        int yViewCenter = (viewToReveal.getTop() + viewToReveal.getBottom()) / 2;
        int xViewDiameter = Math.max(xViewCenter, viewToReveal.getWidth() - xViewCenter);
        int yViewDiameter = Math.max(yViewCenter, viewToReveal.getHeight() - yViewCenter);
        float finalRadius = (float) Math.hypot(xViewDiameter, yViewDiameter);
        // - Prepare animation.
        final Animator circularRevealAnimator = ViewAnimationUtils.createCircularReveal(viewToReveal, xViewCenter, yViewCenter, 0, finalRadius);
        circularRevealAnimator.setInterpolator(new DecelerateInterpolator());
        circularRevealAnimator.setDuration(duration);
        circularRevealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewToReveal.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationEndCallBack.run();
            }
        });
        // Let's reveal !
        circularRevealAnimator.start();
    }

    /**
     * Helps to unreveal a view.
     *
     * @param viewToUnreveal       View to unreveal.
     * @param duration             Duration.
     * @param animationEndCallBack Callback fired on animation end.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void unrevealView(@NonNull final View viewToUnreveal, final int duration, final Runnable animationEndCallBack) {
        // - Determine circle location and size.
        int xViewCenter = (viewToUnreveal.getLeft() + viewToUnreveal.getRight()) / 2;
        int yViewCenter = (viewToUnreveal.getTop() + viewToUnreveal.getBottom()) / 2;
        int xViewDiameter = Math.max(xViewCenter, viewToUnreveal.getWidth() - xViewCenter);
        int yViewDiameter = Math.max(yViewCenter, viewToUnreveal.getHeight() - yViewCenter);
        float finalRadius = (float) Math.hypot(xViewDiameter, yViewDiameter);
        // - Prepare animation.
        final Animator circularRevealAnimator = ViewAnimationUtils.createCircularReveal(viewToUnreveal, xViewCenter, yViewCenter, finalRadius, 0);
        circularRevealAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        circularRevealAnimator.setDuration(duration);
        circularRevealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewToUnreveal.setVisibility(View.INVISIBLE);
                animationEndCallBack.run();
            }
        });
        // Let's reveal !
        circularRevealAnimator.start();
    }

    /**
     * Helps to find recursivly all visible childs in a view group.
     *
     * @param viewGroup     View group.
     * @param ordoredChilds Childs list where visible childs will be added.
     */
    static void findAllVisibleChilds(@NonNull final ViewGroup viewGroup, final List<View> ordoredChilds) {
        for (int childViewIndex = 0; childViewIndex < viewGroup.getChildCount(); childViewIndex++) {
            final View childView = viewGroup.getChildAt(childViewIndex);
            if (childView instanceof ViewGroup) {
                findAllVisibleChilds((ViewGroup) childView, ordoredChilds);
                continue;
            }
            if (childView.getVisibility() == View.VISIBLE) {
                ordoredChilds.add(childView);
            }
        }
    }

    /**
     * Helps to show views.
     *
     * @param views             Views to show.
     * @param animationDuration Animation duration.
     * @param animationDelay    Animation delay.
     */
    static void orderedShowViews(final List<View> views, long animationDuration, int animationDelay) {
        if (views != null) {
            for (int viewIndex = 0; viewIndex < views.size(); viewIndex++) {
                final View childView = views.get(viewIndex);
                childView.setVisibility(View.VISIBLE);
                final ScaleAnimation scaleAnimation = new ScaleAnimation(0, childView.getScaleX(), 0, childView.getScaleY(), Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
                scaleAnimation.setInterpolator(new DecelerateInterpolator());
                scaleAnimation.setDuration(animationDuration);
                scaleAnimation.setStartOffset(viewIndex * animationDelay);
                childView.startAnimation(scaleAnimation);
            }
        }
    }

    /**
     * Get center locations delta between two views.
     *
     * @param aView View A.
     * @param bView View B.
     * @return Locations delta as a point.
     */
    @NonNull
    @Contract("_, _ -> new")
    private static PointF getCenterLocationsDelta(@NonNull final View aView, @NonNull final View bView) {
        final int[] fromLocation = new int[2];
        aView.getLocationOnScreen(fromLocation);
        final int[] toLocation = new int[2];
        bView.getLocationOnScreen(toLocation);
        final int xDelta = toLocation[0] - fromLocation[0] + bView.getMeasuredWidth() / 2 - aView.getMeasuredWidth() / 2;
        final int yDelta = toLocation[1] - fromLocation[1] + bView.getMeasuredHeight() / 2 - aView.getMeasuredHeight() / 2;
        return new PointF(xDelta, yDelta);
    }
}
