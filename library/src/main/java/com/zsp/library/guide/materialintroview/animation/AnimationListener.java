package com.zsp.library.guide.materialintroview.animation;

/**
 * @decs: 动画监听
 * @author: 郑少鹏
 * @date: 2019/9/24 11:45
 */
public interface AnimationListener {
    /**
     * We need to make MaterialIntroView visible before fade in animation starts.
     */
    interface OnAnimationStartListener {
        /**
         * Animation start.
         */
        void onAnimationStart();
    }

    /**
     * We need to make MaterialIntroView invisible after fade out animation ends.
     */
    interface OnAnimationEndListener {
        /**
         * Animation end.
         */
        void onAnimationEnd();
    }
}
