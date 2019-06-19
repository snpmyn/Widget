package com.zsp.library.jellytoolbar

import android.view.View
import androidx.annotation.DimenRes

/**
 * @decs: getDimen
 * @author: 郑少鹏
 * @date: 2019/6/18 15:42
 */
fun View.getDimen(@DimenRes res: Int) = context.resources.getDimensionPixelOffset(res).toFloat()