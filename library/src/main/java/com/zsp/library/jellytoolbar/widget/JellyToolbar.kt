package com.zsp.library.jellytoolbar.widget

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.google.android.material.appbar.MaterialToolbar
import com.zsp.library.R
import com.zsp.library.jellytoolbar.listener.JellyListener
import com.zsp.library.jellytoolbar.value.JellyToolbarConstant
import kotlinx.android.synthetic.main.jelly_toolbar.view.*

/**
 * @decs: JellyToolbar
 * @author: 郑少鹏
 * @date: 2019/6/18 16:27
 */
class JellyToolbar : FrameLayout, JellyWidget {
    companion object {
        private const val KEY_IS_EXPANDED = "key_is_expanded"
        private const val KEY_SUPER_STATE = "key_super_state"
    }

    var toolbar: MaterialToolbar? = null
        private set
        get() {
            return jellyToolbarMt
        }
    var textView: TextView? = null
        private set
        get() {
            return jellyToolbarTv
        }
    var contentView: View? = null
        set(value) {
            jellyToolbarCl.contentView = value
            field = value
        }

    @DrawableRes
    var iconRes: Int? = null
        set(value) {
            jellyToolbarCl.iconRes = value
            field = value
        }

    @DrawableRes
    var cancelIconRes: Int? = null
        set(value) {
            jellyToolbarCl.cancelIconRes = value
            field = value
        }

    @ColorInt
    var startColor: Int? = null
        set(value) {
            value?.let {
                jellyToolbarJv.startColor = value
                field = value
            }
        }

    @ColorInt
    var endColor: Int? = null
        set(value) {
            value?.let {
                jellyToolbarJv.endColor = value
                field = value
            }
        }
    private var jellyListener: JellyListener? = null
    private var isExpanded = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.jelly_toolbar, this)
        attrs?.let { retrieveAttributes(attrs) }
        jellyToolbarCl.onIconClickListener = OnClickListener { expand() }
        jellyToolbarCl.onCancelIconClickListener = OnClickListener { jellyListener?.onCancelIconClicked() }
    }

    private fun retrieveAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.JellyToolbar)
        val startColorAttr = typedArray.getColor(R.styleable.JellyToolbar_startColor, 0)
        if (startColorAttr != 0) startColor = startColorAttr
        val endColorAttr = typedArray.getColor(R.styleable.JellyToolbar_endColor, 0)
        if (endColorAttr != 0) endColor = endColorAttr
        val iconResAttr = typedArray.getResourceId(R.styleable.JellyToolbar_icon, 0)
        if (iconResAttr != 0) iconRes = iconResAttr
        val cancelIconResAttr = typedArray.getResourceId(R.styleable.JellyToolbar_cancelIcon, 0)
        if (cancelIconResAttr != 0) cancelIconRes = cancelIconResAttr
        val title = typedArray.getString(R.styleable.JellyToolbar_title)
        if (!TextUtils.isEmpty(title)) toolbar?.title = title
        val titleColor = typedArray.getColor(R.styleable.JellyToolbar_titleTextColor, 0)
        if (titleColor != 0) toolbar?.setTitleTextColor(titleColor)
        typedArray.recycle()
    }

    override fun collapse() {
        if (!isExpanded) return
        jellyToolbarJv.collapse()
        jellyToolbarCl.collapse()
        isExpanded = false
        jellyListener?.onToolbarCollapsingStarted()
        postDelayed({ jellyListener?.onToolbarCollapsed() }, JellyToolbarConstant.ANIMATION_DURATION)
    }

    override fun expand() {
        if (isExpanded) return
        jellyToolbarJv.expand()
        jellyToolbarCl.expand()
        isExpanded = true
        jellyListener?.onToolbarExpandingStarted()
        postDelayed({ jellyListener?.onToolbarExpanded() }, JellyToolbarConstant.ANIMATION_DURATION)
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putBoolean(KEY_IS_EXPANDED, isExpanded)
            putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(KEY_SUPER_STATE))
            val isExpanded = state.getBoolean(KEY_IS_EXPANDED)
            init()
            if (isExpanded) {
                expandImmediately()
            }
        }
    }

    override fun expandImmediately() {
        if (isExpanded) return
        jellyToolbarJv.expandImmediately()
        jellyToolbarCl.expandImmediately()
        isExpanded = true
    }
}