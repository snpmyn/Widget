package com.zsp.library.guide.materialintroview.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * @decs: 动画工厂
 * @author: 郑少鹏
 * @date: 2019/9/24 11:43
 */
public class AnimationFactory {
    /**
     * MaterialIntroView will appear on screen with fade in animation.
     * <p>
     * Notifies onAnimationStartListener when fade in animation is about to start.
     *
     * @param view                     视图
     * @param duration                 时长
     * @param onAnimationStartListener 动画开始监听
     */
    public static void animateFadeIn(View view, long duration, final AnimationListener.OnAnimationStartListener onAnimationStartListener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        objectAnimator.setDuration(duration);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (onAnimationStartListener != null) {
                    onAnimationStartListener.onAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    /**
     * MaterialIntroView will disappear from screen with fade out animation.
     * <p>
     * Notifies onAnimationEndListener when fade out animation is ended.
     *
     * @param view                   视图
     * @param duration               时长
     * @param onAnimationEndListener 动画结束监听
     */
    public static void animateFadeOut(View view, long duration, final AnimationListener.OnAnimationEndListener onAnimationEndListener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        objectAnimator.setDuration(duration);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEndListener != null) {
                    onAnimationEndListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    public static void performAnimation(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator xScale = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.6f);
        xScale.setRepeatCount(ValueAnimator.INFINITE);
        xScale.setRepeatMode(ValueAnimator.REVERSE);
        xScale.setDuration(1000L);
        ValueAnimator yScale = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.6f);
        yScale.setRepeatCount(ValueAnimator.INFINITE);
        yScale.setRepeatMode(ValueAnimator.REVERSE);
        yScale.setDuration(1000L);
        animatorSet.playTogether(xScale, yScale);
        animatorSet.start();
    }
}
