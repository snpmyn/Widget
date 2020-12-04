package com.zsp.library.jellytoolbar.interpolator

import android.view.animation.Interpolator
import kotlin.math.min
import kotlin.math.sin

/**
 * @decs: JellyInterpolator
 * @author: 郑少鹏
 * @date: 2019/6/18 15:39
 */
class JellyInterpolator : Interpolator {
    override fun getInterpolation(t: Float) = (min(1.0, sin(28 * t - 6.16) / (5 * t - 1.1))).toFloat()
}