package com.zsp.library.animation.reveal;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * @decs: Reveal
 * @author: 郑少鹏
 * @date: 2019/8/27 11:01
 */
public class Reveal {
    /**
     * Reveal a view.
     *
     * @param viewToReveal View to reveal.
     * @return Reveal "reveal" builder.
     */
    public static RevealBuilder reveal(@NonNull final View viewToReveal) {
        return new RevealBuilder(viewToReveal);
    }

    /**
     * Reveal a view.
     *
     * @param viewToUnreveal View to reveal.
     *                       Parent must be instance of RevealViewGroup.
     * @return Reveal "unreveal" builder.
     */
    public static UnrevealBuilder unreveal(@NonNull final View viewToUnreveal) {
        return new UnrevealBuilder(viewToUnreveal);
    }
}
