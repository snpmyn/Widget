package com.zsp.library.searchbox.three.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import com.zsp.library.searchbox.three.helper.SimpleAnimationListener

/**
 * @decs: AnimatorExtensions
 * @author: 郑少鹏
 * @date: 2019/10/12 17:58
 */
fun ValueAnimator.endListener(onAnimationEnd: () -> Unit) {
    addListener(object : SimpleAnimationListener() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            onAnimationEnd.invoke()
        }
    })
}