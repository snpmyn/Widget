package com.zsp.library.banner.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.zsp.library.R
import com.zsp.library.banner.IIndicator
import com.zsp.library.banner.IIndicatorInstance
import com.zsp.library.banner.kit.createPaint
import com.zsp.library.banner.kit.resetPaint
import com.zsp.utiltwo.density.DensityUtils
import kotlin.properties.Delegates

/**
 * @decs: 横条指示器
 * @author: 郑少鹏
 * @date: 2019/8/19 16:11
 */
class CrossBarIndicator @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), IIndicatorInstance {
    //********************************
    //* 自定属性
    //********************************
    /**
     * 指示器宽
     */
    private var mItemWidth: Float = 0f
    /**
     * 指示器高
     */
    private var mItemHeight: Float = 0f
    /**
     * 指示器间隔
     */
    private var mItemSpace: Float = 0f
    /**
     * 指示器背景色
     */
    @ColorInt
    private var mItemBackgroundColor: Int = 0
    /**
     * 指示器前景色
     */
    @ColorInt
    private var mItemForegroundColor: Int = 0
    //********************************
    //* 绘用属性
    //********************************
    /**
     * 全局画笔
     */
    private val mPaint = createPaint(color = Color.WHITE)
    //********************************
    //* 计算属性
    //********************************
    /**
     * View宽
     */
    private var mWidth: Float by Delegates.notNull()
    /**
     * View高
     */
    private var mHeight: Float by Delegates.notNull()
    /**
     * 指示器圆角半径
     */
    private var mItemRadius: Float = 0f
    /**
     * 首指示器坐标
     */
    private var mFirstItemRectF: RectF by Delegates.notNull()
    /**
     * 指示器坐标列表
     */
    private val mItemRectFList: MutableList<RectF> = mutableListOf()
    //********************************
    //* 数据属性
    //********************************
    private lateinit var mIndicatorImpl: IIndicator

    override fun setIndicator(impl: IIndicator) {
        this.mIndicatorImpl = impl
        initView()
        this.invalidate()
    }

    override fun doRequestLayout() {
        initView()
        requestLayout()
    }

    override fun doInvalidate() {
        invalidate()
    }

    init {
        initAttributes(context, attrs)
        mItemRadius = mItemHeight / 2
        if (isInEditMode) {
            mIndicatorImpl = object : IIndicator {
                override fun getCount(): Int {
                    return 3
                }

                override fun getCurrentIndex(): Int {
                    return 1
                }
            }
            initView()
        }
    }

    /**
     * 初始View
     */
    private fun initView() {
        initItemRectF()
    }

    /**
     * 初始指示器坐标
     */
    private fun initItemRectF() {
        val count = mIndicatorImpl.getCount()
        if (count <= 0) return
        mFirstItemRectF = RectF(0f, 0f, mItemWidth, mItemHeight)
        // 指示器坐标集
        mItemRectFList.clear()
        (0 until count).forEach { i ->
            if (i == 0) {
                mItemRectFList.add(mFirstItemRectF)
            } else {
                val prev = mItemRectFList[i - 1]
                mItemRectFList.add(
                        RectF(
                                prev.right + mItemSpace,
                                prev.top,
                                prev.right + mItemSpace + prev.width(),
                                prev.bottom
                        )
                )
            }
        }
    }

    /**
     * 初始自定属性
     */
    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CrossBarIndicator)
        try {
            a.run {
                mItemWidth =
                        a.getDimension(
                                R.styleable.CrossBarIndicator_cbi_itemWidth,
                                DensityUtils.dipToPxByFloat(context, DEFAULT_ITEM_WIDTH)
                        )
                mItemHeight =
                        a.getDimension(
                                R.styleable.CrossBarIndicator_cbi_itemHeight,
                                DensityUtils.dipToPxByFloat(context, DEFAULT_ITEM_HEIGHT)
                        )
                mItemSpace =
                        a.getDimension(
                                R.styleable.CrossBarIndicator_cbi_itemSpace,
                                DensityUtils.dipToPxByFloat(context, DEFAULT_ITEM_SPACE)
                        )
                mItemBackgroundColor =
                        a.getColor(
                                R.styleable.CrossBarIndicator_cbi_itemBackgroundColor,
                                DEFAULT_ITEM_BACKGROUND_COLOR
                        )
                mItemForegroundColor =
                        a.getColor(
                                R.styleable.CrossBarIndicator_cbi_itemForegroundColor,
                                DEFAULT_ITEM_FOREGROUND_COLOR
                        )
            }
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (::mIndicatorImpl.isInitialized) {
            val count = mIndicatorImpl.getCount()
            if (count == 0) return
            var width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
            val calcWidth = mFirstItemRectF.width() * count + mItemSpace * (count - 1)
            if (calcWidth < width) {
                width = calcWidth
            }
            // 设当前视图宽高
            setMeasuredDimension(width.toInt(), mFirstItemRectF.height().toInt())
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = (w - paddingLeft - paddingRight).toFloat()
        mHeight = (h - paddingTop - paddingBottom).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null || !::mIndicatorImpl.isInitialized) return
        // 绘背景
        mPaint.color = mItemBackgroundColor
        mItemRectFList.forEach {
            canvas.drawRoundRect(it, mItemRadius, mItemRadius, mPaint)
        }
        mPaint.resetPaint()
        // 绘前景
        mPaint.color = mItemForegroundColor
        val selected = mItemRectFList[mIndicatorImpl.getCurrentIndex()]
        canvas.drawRoundRect(selected, mItemRadius, mItemRadius, mPaint)
        mPaint.resetPaint()
    }

    companion object {
        private const val DEFAULT_ITEM_WIDTH = 24f
        private const val DEFAULT_ITEM_HEIGHT = 2f
        private const val DEFAULT_ITEM_SPACE = 4f
        private const val DEFAULT_ITEM_BACKGROUND_COLOR = Color.GREEN
        private const val DEFAULT_ITEM_FOREGROUND_COLOR = Color.RED
    }
}