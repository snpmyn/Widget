package com.zsp.library.searchbox.three.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @decs: DataBindingExtensions
 * @author: 郑少鹏
 * @date: 2019/10/12 17:58
 */
fun <T : ViewDataBinding> Context.inflate(@LayoutRes layoutRes: Int): T {
    return DataBindingUtil.inflate(LayoutInflater.from(this), layoutRes, null, false)
}

fun <T : ViewDataBinding> ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToParent: Boolean = true): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, attachToParent)
}

inline fun View.afterMeasured(crossinline f: View.(View) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f(this@afterMeasured)
            }
        }
    })
}