package com.zsp.library.animation.reveal;

import android.view.View;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

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
    @NonNull
    @Contract(value = "_ -> new", pure = true)
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
    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static UnrevealBuilder unreveal(@NonNull final View viewToUnreveal) {
        return new UnrevealBuilder(viewToUnreveal);
    }
}
