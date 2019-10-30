package com.zsp.library.card.elastic

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewPropertyAnimator
import com.google.android.material.card.MaterialCardView
import com.zsp.library.R

/**
 * @decs: 弹性卡片视图
 * @author: 郑少鹏
 * @date: 2019/10/29 17:17
 */
class ElasticCardView(context: Context, attrs: AttributeSet? = null) : MaterialCardView(context, attrs) {
    private val animationDuration = 200L
    private val animationDurationShort = 100L
    private var _isAnimating = false
    private var _isActionUpPerformed = false
    private val _debugPath by lazy {
        DebugPath(this)
    }
    private val _shineProvider by lazy {
        ShineProvider(this)
    }
    // will be available in next versions
    private var isShineEnabled = false
    var flexibility = 5.0f
        set(value) {
            require(value in 1.0f..10.0f) { "Flexibility must be between [1.0f..10.0f]." }
            field = value
        }
    var isDebugPathEnabled = false

    init {
        isClickable = true
        init(attrs)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        processTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (isDebugPathEnabled)
            _debugPath.onDispatchDraw(canvas)
        if (isShineEnabled)
            _shineProvider.onDispatchDraw(canvas)
    }

    private fun processTouchEvent(event: MotionEvent) {
        val verticalRotation = calculateRotation((event.x * flexibility * 2) / width)
        val horizontalRotation = -calculateRotation((event.y * flexibility * 2) / height)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                animator {
                    rotationY(verticalRotation)
                    rotationX(horizontalRotation)
                    duration = animationDurationShort
                    withStartAction {
                        _isActionUpPerformed = false
                        _isAnimating = true
                    }
                    withEndAction {
                        if (_isActionUpPerformed) {
                            animateToOriginalPosition()
                        } else {
                            _isAnimating = false
                        }
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                rotationY = verticalRotation
                rotationX = horizontalRotation
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                _isActionUpPerformed = true
                if (!_isAnimating) {
                    animateToOriginalPosition()
                }
            }
        }
    }

    private fun init(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.ElasticCardView).apply {
            if (hasValue(R.styleable.ElasticCardView_flexibility)) {
                flexibility = getFloat(R.styleable.ElasticCardView_flexibility, flexibility)
            }
            recycle()
        }
    }

    private fun animator(body: ViewPropertyAnimator.() -> Unit) {
        animate().apply {
            interpolator = FastOutSlowInInterpolator()
            body()
            start()
        }
    }

    private fun animateToOriginalPosition() {
        animator {
            rotationX(0.0f)
            rotationY(0.0f)
            duration = animationDuration
        }
    }

    private fun calculateRotation(value: Float): Float {
        var tempValue = when {
            value < 0.0f -> 1.0f
            value > flexibility * 2 -> flexibility * 2
            else -> value
        }
        tempValue -= flexibility
        return tempValue
    }
}