@file:JvmName("BannerKit")

package com.zsp.library.banner.kit

import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt

/**
 * @decs: BannerKit
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
