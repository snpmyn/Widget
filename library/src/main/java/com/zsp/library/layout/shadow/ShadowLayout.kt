package com.zsp.library.layout.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.zsp.library.R
import com.zsp.library.kit.containFlag
import com.zsp.library.kit.createPaint
import com.zsp.library.kit.resetPaint
import com.zsp.utiltwo.assist.AssistUtils
import com.zsp.utiltwo.density.DensityUtils
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

/**
 * @decs: ShadowLayout
 * ShadowLayout实际宽=内容区宽 +（mShadowRadius + Math.abs(mDx)）*2
 * ShadowLayout实际高=内容区高 +（mShadowRadius + Math.abs(mDy)）*2
 *
 * 仅设一边显阴影时阴影占mShadowRadius + Math.abs(mDx、mDy)
 * @author: 郑少鹏
 * @date: 2019/8/20 17:19
 */
class ShadowLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    //********************************
    //* 自定属性
    //********************************
    /**
     * 阴影色
     */
    @ColorInt
    private var mShadowColor: Int = 0
    /**
     * 阴影发散距
     */
    private var mShadowRadius: Float = 0f
    /**
     * X轴偏移距
     */
    private var mDx: Float = 0f
    /**
     * Y轴偏移距
     */
    private var mDy: Float = 0f
    /**
     * 圆角半径
     */
    private var mCornerRadius: Float = 0f
    /**
     * 边框色
     */
    @ColorInt
    private var mBorderColor: Int = 0
    /**
     * 边框宽
     */
    private var mBorderWidth: Float = 0f
    /**
     * 四边显阴影否
     */
    private var mShadowSides: Int = default_shadowSides
    //********************************
    //* 绘用属性
    //********************************
    /**
     * 全局画笔
     */
    private var mPaint: Paint = createPaint(color = Color.WHITE)
    private var mHelpPaint: Paint = createPaint(color = Color.RED)
    /**
     * 全局Path
     */
    private var mPath = Path()
    /**
     * 合成模式
     */
    private var porterDuffXfermode: PorterDuffXfermode by Delegates.notNull()
    /**
     * 视图内容区RectF实例
     */
    private var mContentRF: RectF by Delegates.notNull()
    /**
     * 视图边框RectF实例
     */
    private var mBorderRF: RectF? = null

    init {
        initAttributes(context, attrs)
        initDrawAttributes()
        processPadding()
        // 软件渲染类型
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    /**
     * 初始自定属性
     */
    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
        try {
            a.run {
                mShadowColor = getColor(R.styleable.ShadowLayout_sl_shadowColor, default_shadowColor)
                mShadowRadius = getDimension(R.styleable.ShadowLayout_sl_shadowRadius, DensityUtils.dipToPxByFloat(context, default_shadowRadius).toFloat())
                mDx = getDimension(R.styleable.ShadowLayout_sl_dx, default_dx)
                mDy = getDimension(R.styleable.ShadowLayout_sl_dy, default_dy)
                mCornerRadius = getDimension(R.styleable.ShadowLayout_sl_cornerRadius, DensityUtils.dipToPxByFloat(context, default_cornerRadius).toFloat())
                mBorderColor = getColor(R.styleable.ShadowLayout_sl_borderColor, default_borderColor)
                mBorderWidth = getDimension(R.styleable.ShadowLayout_sl_borderWidth, DensityUtils.dipToPxByFloat(context, default_borderWidth).toFloat())
                mShadowSides = getInt(R.styleable.ShadowLayout_sl_shadowSides, default_shadowSides)
            }
        } finally {
            a.recycle()
        }
    }

    /**
     * 初始绘相关属性
     */
    private fun initDrawAttributes() {
        // 用porterDuffXfermode于图层上合成（处理圆角）
        porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    /**
     * 处理视图之Padding为阴影留空间
     */
    private fun processPadding() {
        val xPadding = (mShadowRadius + mDx.absoluteValue).toInt()
        val yPadding = (mShadowRadius + mDy.absoluteValue).toInt()
        setPadding(
                if (mShadowSides.containFlag(FLAG_SIDES_LEFT)) xPadding else 0,
                if (mShadowSides.containFlag(FLAG_SIDES_TOP)) yPadding else 0,
                if (mShadowSides.containFlag(FLAG_SIDES_RIGHT)) xPadding else 0,
                if (mShadowSides.containFlag(FLAG_SIDES_BOTTOM)) yPadding else 0
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mContentRF = RectF(
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                (w - paddingRight).toFloat(),
                (h - paddingBottom).toFloat()
        )
        // 以边框宽三分之一微调边框绘位以便边框较宽时获更好视觉效果
        val bw = mBorderWidth / 3
        if (bw > 0) {
            mBorderRF = RectF(
                    mContentRF.left + bw,
                    mContentRF.top + bw,
                    mContentRF.right - bw,
                    mContentRF.bottom - bw
            )
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (canvas == null) return
        AssistUtils.greenCurtainBackground(canvas, debug)
        // 绘阴影
        drawShadow(canvas)
        // 绘子视图
        drawChild(canvas) {
            super.dispatchDraw(it)
        }
        // 绘边框
        drawBorder(canvas)
    }

    /**
     * 绘阴影
     */
    private fun drawShadow(canvas: Canvas) {
        canvas.save()
        mPaint.setShadowLayer(mShadowRadius, mDx, mDy, mShadowColor)
        canvas.drawRoundRect(mContentRF, mCornerRadius, mCornerRadius, mPaint)
        mPaint.resetPaint()
        canvas.restore()
    }

    /**
     * 绘子视图
     */
    private fun drawChild(canvas: Canvas, block: (Canvas) -> Unit) {
        canvas.saveLayer(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), mPaint, Canvas.ALL_SAVE_FLAG)
        // 先绘子控件
        block.invoke(canvas)
        // 用path构建四圆角
        mPath = mPath.apply {
            addRect(
                    mContentRF,
                    Path.Direction.CW
            )
            addRoundRect(
                    mContentRF,
                    mCornerRadius,
                    mCornerRadius,
                    Path.Direction.CW
            )
            fillType = Path.FillType.EVEN_ODD
        }
        // 用porterDuffXfermode于图层上合成（处理圆角）
        mPaint.xfermode = porterDuffXfermode
        canvas.drawPath(mPath, mPaint)
        mPaint.resetPaint()
        mPath.reset()
        canvas.restore()
    }

    /**
     * 绘边框
     */
    private fun drawBorder(canvas: Canvas) {
        mBorderRF?.let {
            canvas.save()
            mPaint.strokeWidth = mBorderWidth
            mPaint.style = Paint.Style.STROKE
            mPaint.color = mBorderColor
            canvas.drawRoundRect(it, mCornerRadius, mCornerRadius, mPaint)
            mPaint.resetPaint()
            canvas.restore()
        }
    }

    companion object {
        const val debug = false
        private const val FLAG_SIDES_TOP = 1
        private const val FLAG_SIDES_RIGHT = 2
        private const val FLAG_SIDES_BOTTOM = 4
        private const val FLAG_SIDES_LEFT = 8
        private const val FLAG_SIDES_ALL = 15
        const val default_shadowColor = Color.BLACK
        const val default_shadowRadius = 0.0f
        const val default_dx = 0.0f
        const val default_dy = 0.0f
        const val default_cornerRadius = 0.0f
        const val default_borderColor = Color.RED
        const val default_borderWidth = 0.0f
        const val default_shadowSides = FLAG_SIDES_ALL
    }
}
