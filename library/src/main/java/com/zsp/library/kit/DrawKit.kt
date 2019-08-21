@file:JvmName("DrawKit")

package com.zsp.library.kit

import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt

/**
 * @decs: DrawKit
 * @author: 郑少鹏
 * @date: 2019/8/20 12:13
 */
//********************************
//* BannerKit
//********************************
/**
 * 创画笔
 */
@JvmOverloads
internal fun createPaint(colorString: String? = null, @ColorInt color: Int? = null): Paint {
    return Paint().apply {
        this.resetPaint(colorString, color)
    }
}

/**
 * 重置画笔
 */
@JvmOverloads
internal fun Paint.resetPaint(colorString: String? = null, @ColorInt color: Int? = null) {
    this.reset()
    // 默白色（处理系统渲染抗锯齿时人眼观察到像素色）
    this.color = color ?: Color.parseColor(colorString ?: "#FFFFFF")
    this.isAntiAlias = true
    this.style = Paint.Style.FILL
    this.strokeWidth = 0f
}

/**
 * FlagSet添Flag
 */
internal fun Int.addFlag(flag: Int): Int {
    return this or flag
}

/**
 * FlagSet移Flag
 */
internal fun Int.removeFlag(flag: Int): Int {
    return this and (flag.inv())
}

/**
 * FlagSet含Flag否
 */
internal fun Int.containFlag(flag: Int): Boolean {
    return this or flag == this
}
