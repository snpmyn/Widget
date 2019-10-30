package com.zsp.library.card.elastic

import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import com.zsp.library.R

/**
 * @decs: 发光提供器
 * @author: 郑少鹏
 * @date: 2019/10/29 17:21
 */
internal class ShineProvider(parentView: View) : CentrePointProvider(parentView) {
    private val _paint by lazy {
        Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
        }
    }
    private val _centreColor by lazy {
        ContextCompat.getColor(parentView.context, R.color.white59)
    }
    private val _shineRadius by lazy {
        parentView.height / 2.5f
    }

    fun onDispatchDraw(canvas: Canvas?) {
        _paint.shader = RadialGradient(cx, cy, _shineRadius, _centreColor, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        canvas?.drawCircle(cx, cy, _shineRadius, _paint)
    }
}