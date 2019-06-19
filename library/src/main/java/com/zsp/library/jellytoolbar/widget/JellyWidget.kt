package com.zsp.library.jellytoolbar.widget

/**
 * @decs: JellyWidget
 * @author: 郑少鹏
 * @date: 2019/6/18 15:42
 */
interface JellyWidget {
    fun collapse()
    fun expand()
    fun init() {}
    fun expandImmediately()
}