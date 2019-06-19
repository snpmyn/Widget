package com.zsp.library.jellytoolbar.listener

import android.animation.Animator

/**
 * @decs: AnimationListener
 * @author: 郑少鹏
 * @date: 2019/6/18 15:39
 */
abstract class AnimationListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) = Unit
    override fun onAnimationStart(animation: Animator?) = Unit
    override fun onAnimationCancel(animation: Animator?) = Unit
    abstract override fun onAnimationEnd(animation: Animator?)
}