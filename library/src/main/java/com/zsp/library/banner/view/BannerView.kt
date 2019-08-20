package com.zsp.library.banner.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import com.zsp.library.R
import com.zsp.library.banner.*
import com.zsp.library.banner.factory.PagerViewFactory
import com.zsp.library.banner.kit.createPaint
import com.zsp.utilone.density.DensityUtils
import kotlin.properties.Delegates

/**
 * @decs: 轮播图
 * @author: 郑少鹏
 * @date: 2019/8/19 16:19
 */
class BannerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), IBannerViewInstance, ViewTreeObserver.OnGlobalLayoutListener {
    //********************************
    //* 自定属性
    //********************************
    /**
     * 视图区高
     * 小等0时Match Parent
     */
    private var mViewHeight: Float = 0f
    /**
     * 视图圆角半径
     */
    private var mViewCornerRadius: Float = 0f
    /**
     * 据百分百设条目宽
     */
    private var mItemViewWidthRatio: Float = 0f
    /**
     * 设条目间距
     */
    private var mItemViewMargin: Float = 0f
    /**
     * 轮换时
     */
    private var mIntervalInMillis: Int = 0
    /**
     * 滑后页停时（仅SMOOTH模式生效）
     */
    private var mPageHoldInMillis: Int = DEFAULT_PAGE_HOLD_IN_MILLIS
    /**
     * 滚模式
     */
    private var mScrollMode: Int = DEFAULT_SCROLL_MODE
    /**
     * 条目视图对齐方式
     */
    private var mItemViewAlign: Int = DEFAULT_ITEM_VIEW_ALIGN
    //********************************
    //* 绘用属性
    //********************************
    /**
     * 全局画笔
     */
    private val mPaint = createPaint(color = Color.WHITE)
    /**
     * 全局Path
     */
    private val mPath = Path()
    /**
     * PagerView实例
     */
    private lateinit var mPagerViewInstance: IPagerViewInstance
    //********************************
    //* 计算属性
    //********************************
    /**
     * 视图宽
     */
    private var mWidth: Float = -1f
    /**
     * 视图高
     */
    private var mHeight: Float by Delegates.notNull()
    /**
     * 视图区坐标
     */
    private var mViewRectF: RectF = RectF()
    /**
     * 自滚标识位
     */
    private var mFlagAutoScroll: Boolean = false
    /**
     * 指示器实例
     */
    private var mIndicator: IIndicatorInstance? = null
    //********************************
    //* 数据属性
    //********************************
    /**
     * BannerView接口实现
     */
    private var mBannerViewImpl: IBannerView? = null

    fun setBannerViewImpl(impl: IBannerView) {
        this.mBannerViewImpl = impl
        initView()
    }

    /**
     * 开自滚
     */
    fun startAutoScroll() {
        if (::mPagerViewInstance.isInitialized && !mFlagAutoScroll && mBannerViewImpl != null) {
            mFlagAutoScroll = if (mBannerViewImpl!!.getCount() > 1) {
                mPagerViewInstance.startAutoScroll(mIntervalInMillis)
                true
            } else {
                mPagerViewInstance.stopAutoScroll()
                false
            }
        }
    }

    /**
     * 停自滚
     */
    fun stopAutoScroll() {
        if (::mPagerViewInstance.isInitialized && mFlagAutoScroll) {
            mPagerViewInstance.stopAutoScroll()
            mFlagAutoScroll = false
        }
    }

    /**
     * 数据刷后主动重建Banner
     */
    fun doRecreate() {
        initView()
    }

    init {
        initAttributes(context, attrs)
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        viewTreeObserver.removeOnGlobalLayoutListener(this)
        initView()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findIndicator()
    }

    /**
     * 子视图找到指示器
     */
    private fun findIndicator() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is IIndicatorInstance) {
                mIndicator = child
                return
            }
        }
    }

    /**
     * 初始自定属性
     */
    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BannerView)
        try {
            a.run {
                mViewHeight = a.getDimension(R.styleable.BannerView_bv_viewHeight,
                        DensityUtils.dipToPxByFloat(context, DEFAULT_VIEW_HEIGHT).toFloat())
                // 默MATCH_PARENT
                if (mViewHeight <= 0) mViewHeight = (LayoutParams.MATCH_PARENT).toFloat()
                mViewCornerRadius =
                        a.getDimension(
                                R.styleable.BannerView_bv_viewCornerRadius,
                                DensityUtils.dipToPxByFloat(context, DEFAULT_VIEW_CORNER_RADIUS).toFloat()
                        )
                mItemViewWidthRatio =
                        a.getFloat(R.styleable.BannerView_bv_itemViewWidthRatio, DEFAULT_ITEM_VIEW_WIDTH_RATIO)
                mItemViewMargin =
                        a.getDimension(
                                R.styleable.BannerView_bv_itemViewMargin,
                                DensityUtils.dipToPxByFloat(context, DEFAULT_ITEM_VIEW_MARGIN).toFloat()
                        )
                mIntervalInMillis =
                        a.getInteger(R.styleable.BannerView_bv_intervalInMillis, DEFAULT_INTERVAL_IN_MILLIS)
                mPageHoldInMillis =
                        a.getInteger(R.styleable.BannerView_bv_pageHoldInMillis, DEFAULT_PAGE_HOLD_IN_MILLIS)
                mScrollMode =
                        a.getInteger(R.styleable.BannerView_bv_scrollMode, DEFAULT_SCROLL_MODE)
                mItemViewAlign =
                        a.getInteger(R.styleable.BannerView_bv_itemViewAlign, DEFAULT_ITEM_VIEW_ALIGN)
            }
        } finally {
            a.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = (w - paddingLeft - paddingRight).toFloat()
        mHeight = (h - paddingTop - paddingBottom).toFloat()
        mViewRectF.set(0f, 0f, mWidth, if (mViewHeight <= 0) mHeight else mViewHeight)
    }

    /**
     * 初始视图
     */
    private fun initView() {
        if (mBannerViewImpl != null && mWidth > 0) {
            val bvImpl = mBannerViewImpl!!
            removeAllViews()
            // 数据量0、1时设单视图
            if (bvImpl.getCount() <= 1) {
                val view = if (bvImpl.getCount() < 1) {
                    bvImpl.getDefaultView(context)
                } else {
                    bvImpl.getItemView(context).apply {
                        bvImpl.onBindView(this, 0)
                        bvImpl.onPageSelected(0)
                    }
                } ?: View(context).apply { setBackgroundColor(Color.WHITE) }
                val lp = LayoutParams(getItemViewWidth(), mViewHeight.toInt()).apply {
                    addRule(getItemViewAlign())
                }
                addView(view, lp)
                return
            }
            // 处理pager实例逻辑
            mPagerViewInstance = PagerViewFactory(this).getPagerView()
            mPagerViewInstance.setPageHoldInMillis(
                    if (isSmoothMode()) {
                        mPageHoldInMillis
                    } else {
                        mIntervalInMillis
                    }
            )
            mPagerViewInstance.setOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    mIndicator?.doInvalidate()
                    val realPos = position % bvImpl.getCount()
                    bvImpl.onPageSelected(realPos)
                }
            })
            // 初始自滚设置
            mFlagAutoScroll = if (bvImpl.isDefaultAutoScroll()) {
                mPagerViewInstance.startAutoScroll(mIntervalInMillis)
                true
            } else {
                mPagerViewInstance.stopAutoScroll()
                false
            }
            // 添视图
            addView(mPagerViewInstance as View, LayoutParams(LayoutParams.MATCH_PARENT, mViewHeight.toInt()))
            // 初始指示器
            if (mIndicator != null) {
                mIndicator?.setIndicator(object : IIndicator {
                    override fun getCount(): Int {
                        return bvImpl.getCount()
                    }

                    override fun getCurrentIndex(): Int {
                        return mPagerViewInstance.getRealCurrentPosition(bvImpl.getCount())
                    }
                })
                // 添指示器
                addView(mIndicator as View)
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (canvas == null) return
        if (mViewCornerRadius <= 0) {
            super.dispatchDraw(canvas)
            return
        }
        // 一开图层
        canvas.saveLayer(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        // 二绘子视图
        super.dispatchDraw(canvas)
        // 三裁剪合成
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        // 建四圆角
        val path = mPath.apply {
            addRoundRect(
                    mViewRectF,
                    mViewCornerRadius,
                    mViewCornerRadius,
                    Path.Direction.CW
            )
            addRect(
                    mViewRectF,
                    Path.Direction.CW
            )
            fillType = Path.FillType.EVEN_ODD
        }
        canvas.drawPath(path, mPaint)
        mPath.reset()
        canvas.restore()
    }

    override fun getCount(): Int {
        checkDataState()
        return mBannerViewImpl!!.getCount()
    }

    override fun getItemView(context: Context): View {
        checkDataState()
        return mBannerViewImpl!!.getItemView(context)
    }

    override fun onBindView(itemView: View, position: Int) {
        checkDataState()
        return mBannerViewImpl!!.onBindView(itemView, position)
    }

    override fun getItemViewWidth(): Int {
        if (mWidth <= 0 || mItemViewWidthRatio <= 0) throw IllegalStateException("数据状态异常")
        return (mWidth * mItemViewWidthRatio).toInt()
    }

    override fun getItemViewMargin(): Int {
        return mItemViewMargin.toInt()
    }

    override fun getItemViewAlign(): Int {
        return when (mItemViewAlign) {
            ALIGN_ALIGN_PARENT_LEFT -> {
                ALIGN_PARENT_LEFT
            }
            ALIGN_ALIGN_PARENT_RIGHT -> {
                ALIGN_PARENT_RIGHT
            }
            else -> {
                CENTER_HORIZONTAL
            }
        }
    }

    override fun isSmoothMode(): Boolean {
        return mScrollMode == SCROLL_MODE_SMOOTH
    }

    private fun checkDataState() {
        if (mBannerViewImpl == null) {
            throw IllegalStateException("数据状态异常")
        }
    }

    companion object {
        private const val SCROLL_MODE_INTERVAL = 101
        private const val SCROLL_MODE_SMOOTH = 102
        private const val ALIGN_CENTER_HORIZONTAL = 201
        private const val ALIGN_ALIGN_PARENT_LEFT = 202
        private const val ALIGN_ALIGN_PARENT_RIGHT = 203
        private const val DEFAULT_VIEW_HEIGHT = 0f
        private const val DEFAULT_VIEW_CORNER_RADIUS = 0f
        private const val DEFAULT_ITEM_VIEW_WIDTH_RATIO = 1f
        private const val DEFAULT_ITEM_VIEW_MARGIN = 0f
        private const val DEFAULT_INTERVAL_IN_MILLIS = 2000
        private const val DEFAULT_PAGE_HOLD_IN_MILLIS = 0
        private const val DEFAULT_SCROLL_MODE = SCROLL_MODE_INTERVAL
        private const val DEFAULT_ITEM_VIEW_ALIGN = ALIGN_CENTER_HORIZONTAL
    }
}