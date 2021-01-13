package com.zsp.library.animation.login.two;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

/**
 * @decs: 过渡视图帮助
 * @author: 郑少鹏
 * @date: 2019/4/28 17:05
 */
class TransitionViewHelper {
    /**
     * 扩散动画
     *
     * @param targetView                 目标视图
     * @param endSpreadValue             终止扩散值
     * @param baseSimpleAnimatorListener BaseSimpleAnimatorListener
     */
    static void spreadAnimation(View targetView, float endSpreadValue, BaseSimpleAnimatorListener baseSimpleAnimatorListener) {
        // X
        ObjectAnimator xScaleAnimator = ObjectAnimator.ofFloat(targetView, "ScaleX", endSpreadValue);
        xScaleAnimator.setInterpolator(new AccelerateInterpolator());
        // Y
        ObjectAnimator yScaleAnimator = ObjectAnimator.ofFloat(targetView, "ScaleY", endSpreadValue);
        yScaleAnimator.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(xScaleAnimator).with(yScaleAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
        animatorSet.addListener(baseSimpleAnimatorListener);
    }

    /**
     * 收缩动画
     *
     * @param targetView                 目标视图
     * @param baseSimpleAnimatorListener BaseSimpleAnimatorListener
     */
    static void shrinkAnimation(View targetView, BaseSimpleAnimatorListener baseSimpleAnimatorListener) {
        // X
        ObjectAnimator xScaleAnimator = ObjectAnimator.ofFloat(targetView, "ScaleX", 0.0f);
        xScaleAnimator.setInterpolator(new AccelerateInterpolator());
        // Y
        ObjectAnimator yScaleAnimator = ObjectAnimator.ofFloat(targetView, "ScaleY", 0.0f);
        yScaleAnimator.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(xScaleAnimator).with(yScaleAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
        animatorSet.addListener(baseSimpleAnimatorListener);
    }

    /**
     * 线条延长动画
     *
     * @param targetView                 目标视图
     * @param baseSimpleAnimatorListener BaseSimpleAnimatorListener
     */
    static void lineProlongAnimation(@NonNull View targetView, BaseSimpleAnimatorListener baseSimpleAnimatorListener) {
        targetView.setVisibility(View.VISIBLE);
        targetView.setPivotX(0);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(targetView, "ScaleX", 0, 0.6f, 0.9f, 1.0f);
        objectAnimator.setDuration(1500);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(baseSimpleAnimatorListener);
    }

    /**
     * 登录中文本动画
     *
     * @param targetView                 目标视图
     * @param baseSimpleAnimatorListener BaseSimpleAnimatorListener
     */
    static void loggingInTextAnimation(@NonNull View targetView, BaseSimpleAnimatorListener baseSimpleAnimatorListener) {
        targetView.setVisibility(View.VISIBLE);
        int targetHeight = targetView.getMeasuredHeightAndState();
        ObjectAnimator xScaleObjectAnimator = ObjectAnimator.ofFloat(targetView, "ScaleX", 0.75f, 0.87f, 1f, 1.1f, 1.0f);
        ObjectAnimator yScaleObjectAnimator = ObjectAnimator.ofFloat(targetView, "ScaleY", 0.75f, 0.87f, 1f, 1.1f, 1.0f);
        ObjectAnimator yTranslationObjectAnimator = ObjectAnimator.ofFloat(targetView, "TranslationY", targetHeight * 0.8f, -targetHeight * 0.2f, 0);
        ObjectAnimator alphaObjectAnimator = ObjectAnimator.ofFloat(targetView, "Alpha", 0.5f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(xScaleObjectAnimator, yScaleObjectAnimator, yTranslationObjectAnimator, alphaObjectAnimator);
        animatorSet.setDuration(2000);
        animatorSet.start();
        animatorSet.addListener(baseSimpleAnimatorListener);
    }

    /**
     * 成功文本动画
     *
     * @param successView                成功视图
     * @param loggingInView              登录中视图
     * @param baseSimpleAnimatorListener BaseSimpleAnimatorListener
     */
    static void successTextAnimation(@NonNull View successView, View loggingInView, BaseSimpleAnimatorListener baseSimpleAnimatorListener) {
        successView.setVisibility(View.VISIBLE);
        int width = successView.getMeasuredWidth();
        ObjectAnimator successTranslationAni = ObjectAnimator.ofFloat(successView, "TranslationX", width * -1.2f, width * -0.2f, 0);
        ObjectAnimator successAlphaAni = ObjectAnimator.ofFloat(successView, "Alpha", 0.1f, 1.0f, 1.0f);
        ObjectAnimator signUpTranslationAni = ObjectAnimator.ofFloat(loggingInView, "TranslationX", 0, width * 0.75f, width * 1.5f);
        signUpTranslationAni.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator signUpAlphaAni = ObjectAnimator.ofFloat(loggingInView, "Alpha", 1.0f, 1.0f, 0.4f, 0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(successTranslationAni, successAlphaAni, signUpTranslationAni, signUpAlphaAni);
        animatorSet.setDuration(1000);
        animatorSet.start();
        animatorSet.addListener(baseSimpleAnimatorListener);
    }

    /**
     * 动画监听
     */
    static abstract class BaseSimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationStart(Animator animation) {

        }
    }
}
