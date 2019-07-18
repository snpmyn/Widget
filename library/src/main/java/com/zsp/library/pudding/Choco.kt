package com.zsp.library.pudding

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.zsp.library.R
import kotlinx.android.synthetic.main.choco.view.*
import timber.log.Timber

/**
 * @decs: Choco
 * @author: 郑少鹏
 * @date: 2019/7/18 9:59
 */
class Choco @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var animEnter: ObjectAnimator
    private val animEnterInterceptor = OvershootInterpolator()
    private var enableIconPulse = true
    var enableInfiniteDuration = false
    private var enableProgress = false
    private var enabledVibration = false
    private var materialButtons = ArrayList<MaterialButton>()
    private var onShow: (() -> Unit)? = null
    private var onDismiss: (() -> Unit)? = null
    private var onlyOnce = true

    init {
        inflate(context, R.layout.choco, this)
    }

    /**
     * 初始配置
     *
     * loading显示、icon动画、触摸反馈等。
     */
    private fun initConfiguration() {
        if (enableIconPulse) {
            chocoAciv?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pudding_pulse))
        }
        if (enableProgress) {
            chocoAciv.visibility = View.GONE
            chocoPb.visibility = View.VISIBLE
        } else {
            chocoAciv.visibility = View.VISIBLE
            chocoPb.visibility = View.GONE
        }
        materialButtons.forEach {
            chocoLlContainer.addView(it)
        }
        if (enabledVibration) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Timber.e(TAG, "onAttachedToWindow")
        initConfiguration()
        onShow?.invoke()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Timber.e(TAG, "onDetachedFromWindow")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Timber.e(TAG, "onMeasure")
        if (onlyOnce) {
            onlyOnce = false
            animEnter = ObjectAnimator.ofFloat(this@Choco, "translationY", -this@Choco.measuredHeight.toFloat(), -80F)
            animEnter.interpolator = animEnterInterceptor
            animEnter.duration = ANIMATION_DURATION
            animEnter.start()
        }
    }

    fun onShow(onShow: () -> Unit) {
        this.onShow = onShow
    }

    fun onDismiss(onDismiss: () -> Unit) {
        this.onDismiss = onDismiss
    }

    fun hide(removeNow: Boolean = false) {
        if (!this@Choco.isAttachedToWindow) {
            return
        }
        val windowManager = (this.context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)
                ?: return
        if (removeNow) {
            if (this@Choco.isAttachedToWindow) {
                onDismiss?.invoke()
                windowManager.removeViewImmediate(this@Choco)
            }
            return
        }
        chocoCl.isClickable = false
        val anim = ObjectAnimator.ofFloat(this@Choco, "translationY", -80F, -this@Choco.measuredHeight.toFloat())
        anim.interpolator = AnticipateOvershootInterpolator()
        anim.duration = ANIMATION_DURATION
        anim.start()
        Handler().postDelayed({
            if (this@Choco.isAttachedToWindow) {
                onDismiss?.invoke()
                windowManager.removeViewImmediate(this@Choco)
            }
        }, ANIMATION_DURATION)
    }

    fun setChocoBackgroundColor(@ColorInt color: Int) {
        chocoCl.setBackgroundColor(color)
    }

    /**
     * Sets the choco background drawable resource.
     *
     * @param resource the qualified drawable integer
     */
    fun setChocoBackgroundResource(@DrawableRes resource: Int) {
        chocoCl.setBackgroundResource(resource)
    }

    /**
     * Sets the choco background drawable.
     *
     * @param drawable the qualified drawable
     */
    fun setChocoBackgroundDrawable(drawable: Drawable) {
        chocoCl.background = drawable
    }

    /**
     * Sets the Title of the Choco.
     *
     * @param titleId string resource id of the choco title
     */
    fun setTitle(@StringRes titleId: Int) {
        setTitle(context.getString(titleId))
    }

    /**
     * Sets the Title of the Choco.
     *
     * @param title string object to be used as the choco title
     */
    fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            chocoActvTitle.visibility = View.VISIBLE
            chocoActvTitle.text = title
        }
    }

    /**
     * Set the title's text appearance of the title.
     *
     * @param textAppearance the style resource id
     */
    fun setTitleAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            chocoActvTitle.setTextAppearance(textAppearance)
        } else {
            chocoActvTitle.setTextAppearance(chocoActvTitle.context, textAppearance)
        }
    }

    /**
     * Set the title's typeface.
     *
     * @param typeface the typeface to use
     */
    fun setTitleTypeface(typeface: Typeface) {
        chocoActvTitle.typeface = typeface
    }

    /**
     * Sets the text of the choco.
     *
     * @param textId string resource id of the choco text
     */
    fun setText(@StringRes textId: Int) {
        setText(context.getString(textId))
    }

    /**
     * Set the text's typeface.
     *
     * @param typeface the typeface to use
     */
    fun setTextTypeface(typeface: Typeface) {
        chocoActvContent.typeface = typeface
    }

    /**
     * Sets the text of the choco.
     *
     * @param text string resource id of the Choco text
     */
    fun setText(text: String) {
        if (!TextUtils.isEmpty(text)) {
            this.chocoActvContent.visibility = View.VISIBLE
            this.chocoActvContent.text = text
        }
    }

    /**
     * Set the text's text appearance of the title.
     *
     * @param textAppearance the style resource id
     */
    fun setTextAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            chocoActvContent.setTextAppearance(textAppearance)
        } else {
            chocoActvContent.setTextAppearance(chocoActvContent.context, textAppearance)
        }
    }

    /**
     * Set the inline icon for the choco.
     *
     * @param iconId drawable resource id of the icon to use in the choco
     */
    fun setIcon(@DrawableRes iconId: Int) {
        chocoAciv.setImageDrawable(AppCompatResources.getDrawable(context, iconId))
    }

    /**
     * Set the icon color for the choco.
     *
     * @param color Int
     */
    fun setIconColorFilter(@ColorInt color: Int) {
        chocoAciv.setColorFilter(color)
    }

    /**
     * Set the icon color for the choco.
     *
     * @param colorFilter ColorFilter
     */
    fun setIconColorFilter(colorFilter: ColorFilter) {
        chocoAciv.colorFilter = colorFilter
    }

    /**
     * Set the icon color for the choco.
     *
     * @param color Int
     * @param mode  PorterDuff.Mode
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode) {
        chocoAciv.setColorFilter(color, mode)
    }

    /**
     * Set the inline icon for the choco.
     *
     * @param bitmap bitmap image of the icon to use in the choco
     */
    fun setIcon(bitmap: Bitmap) {
        chocoAciv.setImageBitmap(bitmap)
    }

    /**
     * Set the inline icon for the choco.
     *
     * @param drawable drawable image of the icon to use in the choco
     */
    fun setIcon(drawable: Drawable) {
        chocoAciv.setImageDrawable(drawable)
    }

    /**
     * Set whether to show the icon in the choco or not.
     *
     * @param showIcon true to show the icon; false otherwise
     */
    fun showIcon(showIcon: Boolean) {
        chocoAciv.visibility = if (showIcon) View.VISIBLE else View.GONE
    }

    /**
     * Set if the Icon should pulse or not.
     *
     * @param shouldPulse true if the icon should be animated
     */
    fun pulseIcon(shouldPulse: Boolean) {
        this.enableIconPulse = shouldPulse
    }

    /**
     * Enable or disable progress bar.
     *
     * @param enableProgress true to enable; false to disable
     */
    fun setEnableProgress(enableProgress: Boolean) {
        this.enableProgress = enableProgress
    }

    /**
     * Set the progress bar color from a color resource.
     *
     * @param color the color resource
     */
    fun setProgressColorRes(@ColorRes color: Int) {
        chocoPb?.progressDrawable?.colorFilter = LightingColorFilter(MUL, ContextCompat.getColor(context, color))
    }

    /**
     * Set the progress bar color from a color resource.
     *
     * @param color the color resource
     */
    fun setProgressColorInt(@ColorInt color: Int) {
        chocoPb?.progressDrawable?.colorFilter = LightingColorFilter(MUL, color)
    }

    /**
     * Enable or disable haptic feedback.
     */
    fun setEnabledVibration(enabledVibration: Boolean) {
        this.enabledVibration = enabledVibration
    }

    /**
     * Show a material button with the given text, and on click listener.
     *
     * @param text the text to display on the button
     * @param onClickListener the on click listener
     */
    fun addMaterialButton(text: String, @StyleRes style: Int, onClickListener: OnClickListener) {
        MaterialButton(ContextThemeWrapper(context, style), null, style).apply {
            this.text = text
            this.setOnClickListener(onClickListener)
            materialButtons.add(this)
        }
        // alter padding
        /*body?.apply {
            this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom / 2)
        }*/
    }

    /**
     * Set whether to enable swipe to dismiss or not.
     */
    fun enableSwipeToDismiss() {
        chocoCl.setOnTouchListener(SwipeDismissTouchListener(chocoCl, object : SwipeDismissTouchListener.DismissCallbacks {
            override fun canDismiss(): Boolean {
                return true
            }

            override fun onDismiss(view: View) {
                hide(true)
            }

            override fun onTouch(view: View, touch: Boolean) {
                // ignore
            }
        }))
    }

    companion object {
        private val TAG = Choco::class.java.simpleName
        const val DISPLAY_TIME: Long = 3000
        const val ANIMATION_DURATION: Long = 500
        private const val MUL = -0x1000000
    }
}