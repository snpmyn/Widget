package com.zsp.library.jellytoolbar.interpolator

import android.view.animation.Interpolator

/**
 * @decs: BounceInterpolator
 * @author: 郑少鹏
 * @date: 2019/6/18 15:38
 */
class BounceInterpolator : Interpolator {
    private val moveTime = 0.46667f
    private val firstBounceTime = 0.26666f
    override fun getInterpolation(t: Float): Float = when {
        t < moveTime ->
            move(t)
        t < moveTime + firstBounceTime ->
            firstBounce(t)
        else ->
            secondBounce(t)
    }

    private fun move(t: Float): Float {
        return 4.592f * t * t
    }

    private fun firstBounce(t: Float): Float {
        return 2.5f * t * t - 3f * t + 1.85556f
    }

    private fun secondBounce(t: Float): Float {
        return 0.625f * t * t - 1.083f * t + 1.458f
    }
}