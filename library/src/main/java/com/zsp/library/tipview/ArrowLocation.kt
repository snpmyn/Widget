package com.zsp.library.tipview

import android.graphics.Canvas

/**
 * @decs: ArrowLocation
 * @author: 郑少鹏
 * @date: 2019/11/12 10:51
 */
internal interface ArrowLocation {
    fun configureDraw(view: TipView, canvas: Canvas)
}
