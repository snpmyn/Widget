package com.zsp.library.banner.pager

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.zsp.library.banner.IPagerViewInstance
import com.zsp.library.banner.OnPageChangeListener
import java.util.*
import kotlin.concurrent.timer
import kotlin.properties.Delegates

/**
 * @decs: RecyclerView实现ViewPager横向功能
 * @author: 郑少鹏
 * @date: 2019/8/19 16:14
 */
class PagerRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr), ViewTreeObserver.OnGlobalLayoutListener, IPagerViewInstance {
    override fun startAutoScroll(intervalInMillis: Int) {
        this.mFlagStartTimer = true
        this.mSmoothSpeed = intervalInMillis
        if (this.mSmoothMode) {
            if (this.mWidth > 0) {
                this.mPeriodTime = (mSmoothSpeed / (mWidth / DEFAULT_PERIOD_SCROLL_PIXEL)).toLong()
            }
        } else {
            this.mPeriodTime = intervalInMillis.toLong()
        }
        startTimer()
    }

    override fun stopAutoScroll() {
        this.mFlagStartTimer = false
        stopTimer()
    }

    override fun getCurrentPosition(): Int {
        if (mOldPosition < 0) {
            return 0
        }
        return mOldPosition
    }

    override fun getRealCurrentPosition(realCount: Int): Int {
        if (mOldPosition < 0 || realCount <= 0) {
            return 0
        }
        return mOldPosition % realCount
    }

    override fun setSmoothMode(enabled: Boolean) {
        this.mSmoothMode = enabled
    }

    override fun setPageHoldInMillis(pageHoldInMillis: Int) {
        this.mDelayedTime = pageHoldInMillis.toLong()
    }

    override fun setOnPageChangeListener(listener: OnPageChangeListener) {
        this.mPageChangeListener = listener
    }

    override fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

    /**
     * 开smooth模式否（否interval模式）
     */
    private var mSmoothMode: Boolean = false
    /**
     * 计时器
     */
    private var mTimer: Timer? = null
    /**
     * 当前滚状
     */
    private var mScrollState: Int = SCROLL_STATE_IDLE
    /**
     * 滑至具体位帮助器
     */
    private var mSnapHelper: PagerSnapHelper = PagerSnapHelper()
    /**
     * 页切调
     */
    private var mPageChangeListener: OnPageChangeListener? = null
    /**
     * 避同位多触发
     */
    private var mOldPosition = -1
    /**
     * 定时器间隔
     */
    private var mPeriodTime = DEFAULT_PERIOD_TIME
    /**
     * 定时器延迟时间
     */
    private var mDelayedTime = DEFAULT_DELAYED_TIME
    /**
     * 匀滚速（按时间算）
     */
    private var mSmoothSpeed = DEFAULT_SMOOTH_SPEED
    /**
     * 开定时器标志位否
     */
    private var mFlagStartTimer: Boolean = false
    /**
     * 视图宽高
     */
    private var mWidth: Float = -1f
    private var mHeight: Float by Delegates.notNull()

    init {
        mSnapHelper.attachToRecyclerView(this)
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    /**
     * 监听布局完毕
     */
    override fun onGlobalLayout() {
        viewTreeObserver.removeOnGlobalLayoutListener(this)
        correctSnapViewPosition()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = (w - paddingLeft - paddingRight).toFloat()
        mHeight = (h - paddingTop - paddingBottom).toFloat()
        if (mSmoothMode) {
            mPeriodTime = (mSmoothSpeed / (mWidth / DEFAULT_PERIOD_SCROLL_PIXEL)).toLong()
        }
        if (mTimer == null) {
            startTimer()
        }
    }

    /**
     * 矫正首次初始时SnapView位
     */
    private fun correctSnapViewPosition() {
        val layoutManager = getLinearLayoutManager()
        val snapView = mSnapHelper.findSnapView(layoutManager)
        if (snapView != null) {
            val snapDistance = mSnapHelper.calculateDistanceToFinalSnap(layoutManager, snapView)
            if (snapDistance != null) {
                if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                    scrollBy(snapDistance[0], snapDistance[1])
                }
                triggerOnPageSelected()
            }
        }
    }

    /**
     * 开始定时
     */
    private fun startTimer() {
        mTimer?.cancel()
        if (mWidth > 0 && mFlagStartTimer && context != null && context is Activity) {
            mTimer = timer(initialDelay = mDelayedTime, period = mPeriodTime) {
                if (mScrollState == SCROLL_STATE_IDLE) {
                    (context as Activity).runOnUiThread {
                        if (mSmoothMode) {
                            scrollBy(DEFAULT_PERIOD_SCROLL_PIXEL, 0)
                            triggerOnPageSelected()
                        } else {
                            smoothScrollToPosition(++mOldPosition)
                            mPageChangeListener?.onPageSelected(mOldPosition)
                        }
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        mTimer?.cancel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTimer()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }

    override fun onStartTemporaryDetach() {
        super.onStartTemporaryDetach()
        stopTimer()
    }

    override fun onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach()
        startTimer()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    /**
     * 滚状监听
     */
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        mScrollState = state
        if (state == SCROLL_STATE_IDLE) {
            triggerOnPageSelected()
        }
    }

    /**
     * 触发OnPageSelected回调
     */
    private fun triggerOnPageSelected() {
        val layoutManager = getLinearLayoutManager()
        val view = mSnapHelper.findSnapView(layoutManager)
        if (view != null) {
            val position = layoutManager.getPosition(view)
            if (position != mOldPosition) {
                mOldPosition = position
                mPageChangeListener?.onPageSelected(position)
            }
        }
    }

    /**
     * 分发事件流中控制Timer
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                stopTimer()
            }
            MotionEvent.ACTION_UP -> {
                startTimer()
            }
            MotionEvent.ACTION_CANCEL -> {
                startTimer()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 获LinearLayoutManager
     */
    private fun getLinearLayoutManager(): LinearLayoutManager {
        if (layoutManager != null && layoutManager is LinearLayoutManager) {
            return layoutManager as LinearLayoutManager
        }
        throw IllegalStateException("需且只能设LinearLayoutManager类型")
    }

    companion object {
        private const val DEFAULT_PERIOD_TIME = 0L
        private const val DEFAULT_DELAYED_TIME = 0L
        private const val DEFAULT_PERIOD_SCROLL_PIXEL = 1
        private const val DEFAULT_SMOOTH_SPEED = 5000
    }
}