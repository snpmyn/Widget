package com.zsp.library.jellytoolbar.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import com.zsp.library.R
import com.zsp.library.jellytoolbar.getDimen
import com.zsp.library.jellytoolbar.interpolator.BounceInterpolator
import com.zsp.library.jellytoolbar.value.JellyToolbarConstant
import kotlinx.android.synthetic.main.jelly_content.view.*

/**
 * @decs: ContentLayout
 * @author: 郑少鹏
 * @date: 2019/6/18 15:39
 */
class ContentLayout : RelativeLayout, JellyWidget {
    var contentView: View? = null
        set(value) {
            value?.let {
                container.removeAllViews()
                container.addView(it)
                field = value
            }
        }
    @DrawableRes
    var iconRes: Int? = null
        set(value) {
            value?.let {
                jellyContentAciv.setBackgroundResource(it)
                field = value
            }
        }
    @DrawableRes
    var cancelIconRes: Int? = null
        set(value) {
            value?.let {
                jellyContentAcivCancel.setBackgroundResource(it)
                field = value
            }
        }
    internal var onIconClickListener: OnClickListener? = null
        set(value) {
            jellyContentAciv.setOnClickListener(value)
            field = value
        }
    internal var onCancelIconClickListener: OnClickListener? = null
        set(value) {
            jellyContentAcivCancel.setOnClickListener(value)
            field = value
        }
    private var startPosition = 0f
    private var endPosition = 0f
    private var isInitialized = false
    private val iconFullSize = getDimen(R.dimen.dp_64)
    private val iconPadding = getDimen(R.dimen.dp_12)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.jelly_content, this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!isInitialized) {
            init()
            isInitialized = true
        }
    }

    override fun init() {
        translationX = width.toFloat() - iconFullSize
        startPosition = width.toFloat() - iconFullSize
        endPosition = -height.toFloat() + iconFullSize - iconPadding * 0.5f
    }

    override fun collapse() {
        ValueAnimator.ofFloat(endPosition, startPosition).apply {
            startDelay = 50
            translationX = endPosition
            duration = JellyToolbarConstant.ANIMATION_DURATION / 3
            interpolator = BounceInterpolator()
            addUpdateListener {
                translationX = animatedValue as Float
                jellyContentAciv.alpha = 0.5f + 0.5f * animatedFraction
                with(jellyContentAcivCancel) {
                    rotation = 360 * animatedFraction
                    scaleX = 1 - animatedFraction
                    scaleY = 1 - animatedFraction
                    alpha = 1 - animatedFraction
                    translationX = endPosition - animatedValue as Float
                }
            }
        }.start()
    }

    override fun expand() {
        ValueAnimator.ofFloat(startPosition, endPosition).apply {
            startDelay = 50
            translationX = startPosition
            duration = JellyToolbarConstant.ANIMATION_DURATION / 3
            interpolator = BounceInterpolator()
            with(jellyContentAcivCancel) {
                translationX = 0f
                alpha = 1f
                scaleX = 1f
                scaleY = 1f
            }
            addUpdateListener {
                translationX = animatedValue as Float
                jellyContentAciv.alpha = 1f - 0.5f * animatedFraction
            }
        }.start()
    }

    override fun expandImmediately() {
        expand()
    }
}