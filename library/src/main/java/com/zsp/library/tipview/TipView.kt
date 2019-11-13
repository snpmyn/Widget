package com.zsp.library.tipview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import com.zsp.library.R

/**
 * @decs: 提示视图
 * @author: 郑少鹏
 * @date: 2019/11/12 10:51
 */
class TipView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs) {
    private var tipTitle: String? = null
    private var tipMessage: String? = null
    private lateinit var arrowLocation: ArrowLocation
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var closeButton: ImageView
    private var arrowPosition: Int = 0
    private var closeButtonGravity: Int = 0
    private var closeButtonVisibility: Int = 0
    internal var arrowHeight: Int = 0
    internal var arrowWidth: Int = 0
    internal var cornerRadius: Int = 0
    internal var anchoredViewId: Int = 0
    internal var tipBackgroundColor: Int = 0
    internal var tipPaint: Paint? = null
    internal var tipPath: Path? = null

    init {
        init(attrs, defStyle, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int, defStyleRes: Int) {
        setWillNotDraw(false)
        val res = resources
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TipView, defStyle, defStyleRes)
        try {
            with(typedArray) {
                tipTitle = getString(R.styleable.TipView_tipTitle)
                tipMessage = getString(R.styleable.TipView_tipMessage)
                anchoredViewId = getResourceId(R.styleable.TipView_anchoredView, View.NO_ID)
                tipBackgroundColor = getColor(
                        R.styleable.TipView_tipBackgroundColor,
                        ContextCompat.getColor(context, R.color.blue)
                )
                cornerRadius = getDimension(
                        typedArray,
                        R.styleable.TipView_cornerRadius,
                        R.dimen.dp_4
                )
                arrowHeight = getDimension(
                        typedArray,
                        R.styleable.TipView_arrowHeight,
                        R.dimen.dp_8
                )
                arrowWidth = getDimension(
                        typedArray,
                        R.styleable.TipView_arrowWidth,
                        R.dimen.dp_16
                )
                arrowPosition = getInteger(
                        R.styleable.TipView_arrowLocation,
                        res.getInteger(R.integer.one)
                )
                arrowLocation = if (arrowPosition == LOCATION_TOP) TopArrowLocation() else BottomArrowLocation()
                closeButtonGravity = getInteger(
                        R.styleable.TipView_closeButtonGravity,
                        res.getInteger(R.integer.zero)
                )
                closeButtonVisibility = getInteger(R.styleable.TipView_closeButtonVisibility, VISIBILITY_VISIBLE)
            }
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.tip_view, this, true)
            initViews()
        } finally {
            typedArray.recycle()
        }
    }

    private fun initViews() {
        titleTextView = findViewById(R.id.titleTextView)
        messageTextView = findViewById(R.id.messageTextView)
        closeButton = findViewById(R.id.closeButton)
        setCloseButtonProperties()
        setTexts()
    }

    private fun setCloseButtonProperties() {
        if (closeButtonVisibility == VISIBILITY_GONE) {
            closeButton.visibility = View.GONE
            return
        }
        val layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = when (closeButtonGravity) {
            GRAVITY_TOP -> Gravity.TOP
            GRAVITY_CENTER -> Gravity.CENTER_VERTICAL
            GRAVITY_BOTTOM -> Gravity.BOTTOM
            else -> Gravity.TOP
        }
        closeButton.layoutParams = layoutParams
    }

    private fun setTexts() {
        if (tipTitle == null) {
            titleTextView.visibility = View.GONE
        } else {
            titleTextView.text = tipTitle
            titleTextView.visibility = View.VISIBLE
        }
        if (tipMessage == null) {
            messageTextView.visibility = View.GONE
        } else {
            messageTextView.text = tipMessage
            messageTextView.visibility = View.VISIBLE
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (arrowPosition == LOCATION_TOP) {
            setPadding(0, arrowHeight, 0, 0)
        } else {
            setPadding(0, 0, 0, arrowHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight + arrowHeight)
    }

    override fun invalidate() {
        super.invalidate()
        tipPath = null
        tipPaint = null
    }

    override fun onDraw(canvas: Canvas) {
        if (tipPath == null || tipPaint == null) {
            arrowLocation.configureDraw(this, canvas)
        }
        canvas.drawPath(tipPath!!, tipPaint!!)
        super.onDraw(canvas)
    }

    internal fun setPaint(paint: Paint) {
        this.tipPaint = paint
    }

    fun getArrowHeight(): Int {
        return arrowHeight
    }

    fun setArrowHeight(arrowHeight: Int) {
        this.arrowHeight = arrowHeight
        invalidate()
    }

    fun setArrowHeightResource(@DimenRes resId: Int) {
        arrowHeight = resources.getDimensionPixelSize(resId)
        invalidate()
    }

    fun getArrowWidth(): Int {
        return arrowWidth
    }

    fun setArrowWidth(arrowWidth: Int) {
        this.arrowWidth = arrowWidth
        invalidate()
    }

    fun setArrowWidthResource(@DimenRes resId: Int) {
        arrowWidth = resources.getDimensionPixelSize(resId)
        invalidate()
    }

    fun getCornerRadius(): Int {
        return cornerRadius
    }

    fun setCornerRadius(cornerRadius: Int) {
        this.cornerRadius = cornerRadius
        invalidate()
    }

    fun setCornerRadiusResource(@DimenRes resId: Int) {
        cornerRadius = resources.getDimensionPixelSize(resId)
        invalidate()
    }

    fun getAnchoredViewId(): Int {
        return anchoredViewId
    }

    fun setAnchoredViewId(@IdRes anchoredViewId: Int) {
        this.anchoredViewId = anchoredViewId
        invalidate()
    }

    fun getTipBackgroundColor(): Int {
        return tipBackgroundColor
    }

    fun setTipBackgroundColor(tipBackgroundColor: Int) {
        this.tipBackgroundColor = tipBackgroundColor
        invalidate()
    }

    fun setArrowPosition(arrowPosition: Int) {
        arrowLocation = if (arrowPosition == LOCATION_TOP) TopArrowLocation() else BottomArrowLocation()
        if (arrowPosition == LOCATION_TOP) {
            // magic number will be dynamic
            (findViewById<LinearLayout>(R.id.rootLayout)).setPadding(32, arrowHeight + 32, 32, 0)
        } else {
            (findViewById<LinearLayout>(R.id.rootLayout)).setPadding(32, 32, 32, arrowHeight)
        }
        invalidate()
    }

    fun setTitle(title: String) {
        titleTextView.text = title
        titleTextView.visibility = View.VISIBLE
    }

    fun getTitle() = tipTitle
    fun setMessage(message: String) {
        messageTextView.text = message
        messageTextView.visibility = View.VISIBLE
    }

    fun getMessage() = tipMessage
    private fun getDimension(
            typedArray: TypedArray,
            @StyleableRes styleableId: Int,
            @DimenRes defaultDimension: Int
    ): Int {
        var result = typedArray.getDimensionPixelSize(styleableId, NOT_PRESENT)
        if (result == NOT_PRESENT) {
            result = resources.getDimensionPixelSize(defaultDimension)
        }
        return result
    }

    companion object {
        const val VISIBILITY_VISIBLE = 0
        const val VISIBILITY_GONE = 1
        const val LOCATION_TOP = 0
        const val LOCATION_BOTTOM = 1
        const val GRAVITY_TOP = 0
        const val GRAVITY_CENTER = 1
        const val GRAVITY_BOTTOM = 2
        private const val NOT_PRESENT = Integer.MIN_VALUE
    }
}
