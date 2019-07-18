package com.zsp.library.pudding

import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.android.synthetic.main.choco.view.*
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * @decs: Pudding
 * @author: 郑少鹏
 * @date: 2019/7/18 10:14
 */
class Pudding : LifecycleObserver {
    private lateinit var choco: Choco
    private var windowManager: WindowManager? = null
    // after create
    fun show() {
        windowManager?.also {
            try {
                it.addView(choco, initLayoutParameter())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // time over dismiss
        choco.postDelayed({
            if (choco.enableInfiniteDuration) {
                return@postDelayed
            }
            choco.hide()
        }, Choco.DISPLAY_TIME)
        // click dismiss
        choco.chocoCl.setOnClickListener {
            choco.hide()
        }
    }

    // window manager must associate activity's lifecycle
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        // this owner is your activity instance
        choco.hide(true)
        owner.lifecycle.removeObserver(this)
        if (puddingMapX.containsKey(owner.toString())) {
            puddingMapX.remove(owner.toString())
        }
    }

    private fun initLayoutParameter(): WindowManager.LayoutParams {
        // init layout params
        val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        )
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.TOP
        layoutParams.flags =
                // 不获焦以便弹出时下层界面仍可操作
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        // 保装饰物（如状态栏）不遮内容
                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
        // popWindow层级为TYPE_APPLICATION_PANEL
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL
        return layoutParams
    }

    // must invoke first
    private fun setActivity(activity: AppCompatActivity, block: Choco.() -> Unit) {
        activityWeakReference = WeakReference(activity)
        choco = Choco(activity)
        windowManager = activity.windowManager
        activity.lifecycle.addObserver(this)
        // 高阶函数named dsl配choco属性
        choco.apply(block)
    }

    companion object {
        @JvmStatic
        private fun log(e: String) {
            Timber.e(this::class.java.simpleName, "${this} $e")
        }

        private var activityWeakReference: WeakReference<AppCompatActivity>? = null
        // each Activity hold itself pudding list
        private val puddingMapX: MutableMap<String, Pudding> = mutableMapOf()

        @JvmStatic
        fun create(activity: AppCompatActivity, block: Choco.() -> Unit): Pudding {
            val pudding = Pudding()
            pudding.setActivity(activity, block)
            Handler(Looper.getMainLooper()).post {
                puddingMapX[activity.toString()]?.choco?.let {
                    if (it.isAttachedToWindow) {
                        ViewCompat.animate(it).alpha(0F).withEndAction {
                            if (it.isAttachedToWindow) {
                                activity.windowManager.removeViewImmediate(it)
                            }
                        }
                    }
                }
                puddingMapX[activity.toString()] = pudding
            }
            return pudding
        }
    }
}