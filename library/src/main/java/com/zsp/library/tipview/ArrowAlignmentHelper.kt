package com.zsp.library.tipview

import android.graphics.RectF
import android.view.View

/**
 * @decs: ArrowAlignmentHelper
 * @author: 郑少鹏
 * @date: 2019/11/12 10:50
 */
internal object ArrowAlignmentHelper {
    fun calculateArrowMidPoint(view: TipView, rectF: RectF): Float {
        var middle = rectF.width() / 2
        if (view.anchoredViewId != View.NO_ID) {
            val anchoredView = (view.parent as View).findViewById<View>(view.anchoredViewId)
            middle += (anchoredView.x
                    + (anchoredView.width / 2)
                    - (view.x)
                    - (view.width / 2).toFloat())
        }
        return middle
    }
}
