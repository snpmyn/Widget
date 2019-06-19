package com.zsp.library.jellytoolbar.listener

/**
 * @decs: JellyListener
 * @author: 郑少鹏
 * @date: 2019/6/18 15:39
 */
abstract class JellyListener {
    open fun onToolbarExpandingStarted() = Unit
    open fun onToolbarCollapsingStarted() = Unit
    open fun onToolbarExpanded() = Unit
    open fun onToolbarCollapsed() = Unit
    abstract fun onCancelIconClicked()
}