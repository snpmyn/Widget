package com.zsp.library.layout.circularreveal;

import android.view.ViewGroup;

/**
 * @decs: RevealViewGroup
 * Indicator for internal API that {@link ViewGroup} support circular reveal animation.
 * @author: 郑少鹏
 * @date: 2019/8/27 11:40
 */
public interface RevealViewGroup {
    /**
     * Get view reveal manager.
     *
     * @return bridge between view and circular reveal animation
     */
    ViewRevealManager getViewRevealManager();

    /**
     * Set view reveal manager.
     *
     * @param manager ViewRevealManager
     */
    void setViewRevealManager(ViewRevealManager manager);
}