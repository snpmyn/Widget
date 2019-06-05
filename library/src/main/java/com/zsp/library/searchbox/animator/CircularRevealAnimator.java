package com.zsp.library.searchbox.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;

/**
 * @decs: 波纹动画
 * @author: 郑少鹏
 * @date: 2019/4/23 11:36
 */
public class CircularRevealAnimator {
    private static final long DURATION = 200;
    private AnimationListener animationListener;

    private void actionOtherVisible(final boolean isShow, final View triggerView, final View animView) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (isShow) {
                animView.setVisibility(View.VISIBLE);
                if (animationListener != null) {
                    animationListener.onAnimationShowEnd();
                }
            } else {
                animView.setVisibility(View.GONE);
                if (animationListener != null) {
                    animationListener.onAnimationHideEnd();
                }
            }
            return;
        }
        // 算triggerView中心位
        int[] tvLocation = new int[2];
        triggerView.getLocationInWindow(tvLocation);
        int tvX = tvLocation[0] + triggerView.getWidth() / 2;
        int tvY = tvLocation[1] + triggerView.getHeight() / 2;
        // 算animView中心位
        int[] avLocation = new int[2];
        animView.getLocationInWindow(avLocation);
        int avX = avLocation[0] + animView.getWidth() / 2;
        int avY = avLocation[1] + animView.getHeight() / 2;
        int rippleW = tvX < avX ? animView.getWidth() - tvX : tvX - avLocation[0];
        int rippleH = tvY < avY ? animView.getHeight() - tvY : tvY - avLocation[1];
        float maxRadius = (float) Math.sqrt(rippleW * rippleW + rippleH * rippleH);
        float startRadius;
        float endRadius;
        if (isShow) {
            startRadius = 0;
            endRadius = maxRadius;
        } else {
            startRadius = maxRadius;
            endRadius = 0;
        }
        Animator animator = ViewAnimationUtils.createCircularReveal(animView, tvX, tvY, startRadius, endRadius);
        animView.setVisibility(View.VISIBLE);
        animator.setDuration(DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isShow) {
                    animView.setVisibility(View.VISIBLE);
                    if (animationListener != null) {
                        animationListener.onAnimationShowEnd();
                    }
                } else {
                    animView.setVisibility(View.GONE);
                    if (animationListener != null) {
                        animationListener.onAnimationHideEnd();
                    }
                }
            }
        });
        animator.start();
    }

    public void show(View triggerView, View showView) {
        actionOtherVisible(true, triggerView, showView);
    }

    public void hide(View triggerView, View hideView) {
        actionOtherVisible(false, triggerView, hideView);
    }

    public void setAnimationListener(AnimationListener listener) {
        animationListener = listener;
    }

    public interface AnimationListener {
        /**
         * 隐
         */
        void onAnimationHideEnd();

        /**
         * 显
         */
        void onAnimationShowEnd();
    }
}