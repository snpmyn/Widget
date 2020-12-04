package com.zsp.library.card.elastic

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

/**
 * @decs: 中心点提供器
 * @author: 郑少鹏
 * @date: 2019/10/29 17:21
 */
@SuppressLint("ClickableViewAccessibility")
internal abstract class CentrePointProvider(protected val parentView: View) {
    private val screenOffDistance = -300.0f
    protected var cx = screenOffDistance
    protected var cy = screenOffDistance

    init {
        parentView.setOnTouchListener { v, event ->
            attach(event)
            v.onTouchEvent(event)
        }
    }

    private fun attach(event: MotionEvent) {
        val (x, y) = if (event.action == MotionEvent.ACTION_MOVE) {
            event.x to event.y
        } else {
            screenOffDistance to screenOffDistance
        }
        cx = x
        cy = y
        parentView.invalidate()
    }
}