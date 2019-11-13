package com.zsp.library.tipview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.RectF

/**
 * @decs: 顶角位
 * @author: 郑少鹏
 * @date: 2019/11/12 10:52
 */
internal class TopArrowLocation : ArrowLocation {
    override fun configureDraw(view: TipView, canvas: Canvas) {
        view.tipPath = Path()
        val rectF = RectF(canvas.clipBounds)
        rectF.top += view.arrowHeight.toFloat()
        with(view.tipPath!!) {
            addRoundRect(rectF, view.cornerRadius.toFloat(), view.cornerRadius.toFloat(), Direction.CW)
            val middle = ArrowAlignmentHelper.calculateArrowMidPoint(view, rectF)
            moveTo(middle, 0f)
            val arrowDx = view.arrowWidth / 2
            lineTo(middle - arrowDx, rectF.top)
            lineTo(middle + arrowDx, rectF.top)
            close()
        }
        view.setPaint(Paint(Paint.ANTI_ALIAS_FLAG))
        view.tipPaint!!.color = view.tipBackgroundColor
    }
}
