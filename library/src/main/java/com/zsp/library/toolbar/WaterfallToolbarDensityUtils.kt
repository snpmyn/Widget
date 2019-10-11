package com.zsp.library.toolbar

/**
 * @decs: WaterfallToolbarDensityUtils
 * @author: 郑少鹏
 * @date: 2019/10/10 17:34
 */
var density: Float? = null

data class Dip(var value: Float) {
    fun toPx(): Px {
        val innerDensity: Float = density
                ?: throw NullPointerException("You must set density before using DimensionUnits classes.")
        return Px((value * innerDensity + 0.5f).toInt())
    }
}

data class Px(var value: Int) {
    fun toDip(): Dip {
        val innerDensity: Float = density
                ?: throw NullPointerException("You must set density before using DimensionUnits classes.")
        return Dip(value / innerDensity + 0.5f)
    }
}