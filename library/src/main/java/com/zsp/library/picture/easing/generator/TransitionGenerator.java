package com.zsp.library.picture.easing.generator;

import android.graphics.RectF;

import com.zsp.library.picture.easing.transition.Transition;
import com.zsp.library.picture.easing.view.EasingView;

/**
 * @decs: 过渡发电机
 * @author: 郑少鹏
 * @date: 2019/10/26 16:44
 */
public interface TransitionGenerator {
    /**
     * Generates the next transition to be played by the {@link EasingView}.
     *
     * @param drawableBounds The bounds of the drawable to be shown in the {@link EasingView}.
     * @param viewport       The rect that represents the viewport where the transition will be played in.
     *                       This is usually the bounds of the {@link EasingView}.
     * @return A {@link Transition} object to be played by the {@link EasingView}.
     */
    Transition generateNextTransition(RectF drawableBounds, RectF viewport);
}
