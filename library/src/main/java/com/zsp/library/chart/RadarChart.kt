package com.zsp.library.chart

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.zsp.library.R
import com.zsp.library.kit.createPaint
import com.zsp.library.kit.resetPaint
import com.zsp.utiltwo.assist.AssistUtils
import com.zsp.utiltwo.calculation.CalculationUtils
import com.zsp.utiltwo.density.DensityUtils
import com.zsp.utiltwo.draw.DrawUtils
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

/**
 * @decs: 雷达图
 * 宽高由属性[mWebRadius]据UI稿比例算得。
 * @author: 郑少鹏
 * @date: 2019/8/21 11:58
 */
class RadarChart @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    //********************************
    //* 自定属性
    //********************************
    /**
     * 雷达图半径
     */
    private var mWebRadius: Float = 0f
    /**
     * 雷达图半径对应最大进度
     */
    private var mWebMaxProgress: Int = 0
    /**
     * 雷达图线色
     */
    @ColorInt
    private var mWebLineColor: Int = 0
    /**
     * 雷达图线宽
     */
    private var mWebLineWidth: Float = 0f
    /**
     * 雷达图各点文本色
     */
    @ColorInt
    private var mTextArrayedColor: Int = 0
    /**
     * 雷达图文本数组字体路径
     */
    private var mTextArrayedFontPath: String? = null
    /**
     * 雷达图中心连接区色
     */
    @ColorInt
    private var mAreaColor: Int = 0
    /**
     * 雷达图中心连接区边框色
     */
    @ColorInt
    private var mAreaBorderColor: Int = 0
    /**
     * 雷达图中心文本名
     */
    private var mTextCenteredName: String = default_textCenteredName
    /**
     * 雷达图中心文本色
     */
    @ColorInt
    private var mTextCenteredColor: Int = 0
    /**
     * 雷达图中心文本字体路径
     */
    private var mTextCenteredFontPath: String? = null
    /**
     * 文本数组且以该数组长确定雷达图为几边形
     */
    private var mTextArray: Array<String> by Delegates.notNull()
    /**
     * 进度数组（对应TextArray）
     */
    private var mProgressArray: Array<Int> by Delegates.notNull()
    /**
     * 执行动画前进度数组（对应TextArray）
     */
    private var mOldProgressArray: Array<Int> by Delegates.notNull()
    /**
     * 动画时（0无动画）
     * 定速模式表从雷达中心执行动画至顶点时
     */
    private var mAnimateTime: Long = 0L
    /**
     * 动画模式（默时一定）
     */
    private var mAnimateMode: Int = default_animateMode
    //********************************
    //* 计算属性
    //********************************
    /**
     * 垂直文本距雷达主图宽
     */
    private var mVerticalSpaceWidth: Float by Delegates.notNull()
    /**
     * 水平文本距雷达主图宽
     */
    private var mHorizontalSpaceWidth: Float by Delegates.notNull()
    /**
     * 文本数组字体大小
     */
    private var mTextArrayedSize: Float by Delegates.notNull()
    /**
     * 文本数组设字体大小后文本宽（取字数最多的）
     */
    private var mTextArrayedWidth: Float by Delegates.notNull()
    /**
     * 文本数组设字体大小后文本高
     */
    private var mTextArrayedHeight: Float by Delegates.notNull()
    /**
     * 图宽
     */
    private var mWidth: Float by Delegates.notNull()
    /**
     * 图高
     */
    private var mHeight: Float by Delegates.notNull()
    //********************************
    //* 绘用属性
    //********************************
    /**
     * 全局画笔
     */
    private val mPaint = createPaint()
    private val mHelperPaint = createPaint()
    /**
     * 全局路径
     */
    private val mPath = Path()
    /**
     * 雷达图虚线效果
     */
    private var mDashPathEffect: DashPathEffect by Delegates.notNull()
    /**
     * 雷达主图各顶点坐标数组
     */
    private var mPointArray: Array<PointF> by Delegates.notNull()
    /**
     * 文本数组各文本坐标数组
     */
    private var mTextArrayedPointArray: Array<PointF> by Delegates.notNull()
    /**
     * 文本数组各进度坐标数组
     */
    private var mProgressPointArray: Array<PointF> by Delegates.notNull()
    /**
     * 作转换用临时变量
     */
    private var mTempPointF: PointF = PointF()
    /**
     * 雷达图文本数组字体
     */
    private var mTextArrayedTypeface: Typeface? = null
    /**
     * 雷达图中心文本字体
     */
    private var mTextCenteredTypeface: Typeface? = null
    /**
     * 动画处理器数组
     */
    private var mAnimatorArray: Array<ValueAnimator?> by Delegates.notNull()
    /**
     * 各雷达属性动画时间数组
     */
    private var mAnimatorTimeArray: Array<Long> by Delegates.notNull()

    //********************************
    //* 设数据属性
    //********************************
    fun setTextArray(textList: List<String>) {
        this.mTextArray = textList.toTypedArray()
        this.mProgressArray = Array(mTextArray.size) { 0 }
        this.mOldProgressArray = Array(mTextArray.size) { 0 }
        initView()
    }

    fun setProgressList(progressList: List<Int>) {
        this.mProgressArray = progressList.toTypedArray()
        initView()
    }

    /**
     * 执行动画前进度
     */
    fun setOldProgressList(oldProgressList: List<Int>) {
        this.mOldProgressArray = oldProgressList.toTypedArray()
        initView()
    }

    init {
        initAttributes(context, attrs)
        initView()
        // 软件渲染类型（解决DashPathEffect不生效问题）
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private fun initView() {
        initCalculateAttributes()
        initDrawAttributes()
        initAnimator()
    }

    /**
     * 初始自定属性
     */
    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RadarChart)
        try {
            a.run {
                mWebRadius = a.getDimension(R.styleable.RadarChart_rc_webRadius, DensityUtils.dipToPxByFloat(context, default_webRadius).toFloat())
                mWebMaxProgress = a.getInt(R.styleable.RadarChart_rc_webMaxProgress, default_webMaxProgress)
                mWebLineColor = a.getColor(R.styleable.RadarChart_rc_webLineColor, default_webLineColor)
                mWebLineWidth = a.getDimension(R.styleable.RadarChart_rc_webLineWidth, DensityUtils.dipToPxByFloat(context, default_webLineWidth).toFloat())
                mTextArrayedColor = a.getColor(R.styleable.RadarChart_rc_textArrayedColor, default_textArrayedColor)
                mTextArrayedFontPath = a.getString(R.styleable.RadarChart_rc_textArrayedFontPath)
                mAreaColor = a.getColor(R.styleable.RadarChart_rc_areaColor, default_areaColor)
                mAreaBorderColor = a.getColor(R.styleable.RadarChart_rc_areaBorderColor, default_areaBorderColor)
                mTextCenteredName = a.getString(R.styleable.RadarChart_rc_textCenteredName)
                        ?: default_textCenteredName
                mTextCenteredColor = a.getColor(R.styleable.RadarChart_rc_textCenteredColor, default_textCenteredColor)
                mTextCenteredFontPath = a.getString(R.styleable.RadarChart_rc_textCenteredFontPath)
                mAnimateTime = a.getInt(R.styleable.RadarChart_rc_animateTime, default_animateTime).toLong()
                mAnimateMode = a.getInt(R.styleable.RadarChart_rc_animateMode, default_animateMode)
                mTextArray = default_textArray
                mProgressArray = default_progressArray
                mOldProgressArray = default_oldProgressArray
            }
        } finally {
            a.recycle()
        }
    }

    /**
     * 初始计算属性（基本宽高、字体大小、间距等）
     * 以UI稿比例据[mWebRadius]算
     */
    private fun initCalculateAttributes() {
        // 据比例算相应属性
        (mWebRadius / 100).let {
            mVerticalSpaceWidth = it * 8
            mHorizontalSpaceWidth = it * 10
            mTextArrayedSize = it * 12
        }
        // 设字体大小后算文本占宽高
        mPaint.textSize = mTextArrayedSize
        mTextArray.maxBy { it.length }?.apply {
            mTextArrayedWidth = mPaint.measureText(this)
            mTextArrayedHeight = mPaint.fontSpacing
        }
        mPaint.resetPaint()
        // 动态算视图实际宽高
        mWidth = (mTextArrayedWidth + mHorizontalSpaceWidth + mWebRadius) * 2.1f
        mHeight = (mTextArrayedHeight + mVerticalSpaceWidth + mWebRadius) * 2.1f
    }

    /**
     * 初始绘制相关属性
     */
    private fun initDrawAttributes() {
        DensityUtils.dipToPxByFloat(context, 2.0f).toFloat().run {
            mDashPathEffect = DashPathEffect(floatArrayOf(this, this), this)
        }
        mPointArray = Array(mTextArray.size) { PointF(0.0f, 0.0f) }
        mTextArrayedPointArray = Array(mTextArray.size) { PointF(0.0f, 0.0f) }
        mProgressPointArray = Array(mTextArray.size) { PointF(0.0f, 0.0f) }
        if (mTextArrayedFontPath != null) {
            mTextArrayedTypeface = Typeface.createFromAsset(context.assets, mTextArrayedFontPath)
        }
        if (mTextCenteredFontPath != null) {
            mTextCenteredTypeface = Typeface.createFromAsset(context.assets, mTextCenteredFontPath)
        }
    }

    /**
     * 初始动画处理器
     */
    private fun initAnimator() {
        mAnimatorArray = Array(mTextArray.size) { null }
        mAnimatorTimeArray = Array(mTextArray.size) { 0L }
        mAnimatorArray.forEachIndexed { index, _ ->
            val sv = mOldProgressArray[index].toFloat()
            val ev = mProgressArray[index].toFloat()
            mAnimatorArray[index] = if (sv == ev) null else ValueAnimator.ofFloat(sv, ev)
            if (mAnimateMode == ANIMATE_MODE_TIME) {
                mAnimatorTimeArray[index] = mAnimateTime
            } else if (mAnimateMode == ANIMATE_MODE_SPEED) {
                // 据最大进度和动画时算恒速
                val v = mWebMaxProgress.toFloat() / mAnimateTime
                mAnimatorTimeArray[index] = if (sv == ev) 0L else ((ev - sv) / v).toLong()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    /**
     * 各属性动画同执行
     */
    fun doInvalidate() {
        mAnimatorArray.forEachIndexed { index, _ ->
            doInvalidate(index)
        }
    }

    /**
     * 指定某属性开始动画
     */
    fun doInvalidate(index: Int, block: ((Int) -> Unit)? = null) {
        if (index >= 0 && index < mAnimatorArray.size) {
            val valueAnimator = mAnimatorArray[index]
            val at = mAnimatorTimeArray[index]
            if (valueAnimator != null && at > 0) {
                valueAnimator.duration = at
                valueAnimator.removeAllUpdateListeners()
                valueAnimator.addUpdateListener {
                    val av = (it.animatedValue as Float)
                    mProgressArray[index] = av.toInt()
                    invalidate()
                }
                // 动画结束监听
                if (block != null) {
                    valueAnimator.removeAllListeners()
                    valueAnimator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}
                        override fun onAnimationEnd(animation: Animator?) {
                            block.invoke(index)
                        }

                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationStart(animation: Animator?) {}
                    })
                }
                valueAnimator.start()
            } else {
                block?.invoke(index)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        AssistUtils.greenCurtainBackground(canvas, debug)
        canvas.save()
        canvas.translate(mWidth / 2, mHeight / 2)
        if (checkIllegalData(canvas)) {
            // 绘网图
            drawWeb(canvas)
            // 绘文本数组
            drawTextArray(canvas)
            // 绘连接区
            drawConnectionArea(canvas)
            // 绘中心文本
            drawCenterText(canvas)
        }
        canvas.restore()
    }

    /**
     * 查数据合法否
     */
    private fun checkIllegalData(canvas: Canvas): Boolean {
        var errorMsg: String? = null
        if (mTextArray.size < 3 && errorMsg == null) {
            errorMsg = "TextArray长度不能小于3"
        }
        if (mTextArray.size != mProgressArray.size && errorMsg == null) {
            errorMsg = "TextArray长度与ProgressArray长度不等"
        }
        if (mTextArray.size != mOldProgressArray.size && errorMsg == null) {
            errorMsg = "TextArray长度与OldProgressArray长度不等"
        }
        if (errorMsg != null) {
            mPaint.textSize = mTextArrayedSize
            mPaint.color = Color.RED
            mPaint.textAlign = Paint.Align.CENTER
            canvas.drawText(errorMsg, 0.0f, DrawUtils.getCenteredY(mPaint), mPaint)
            mPaint.resetPaint()
            return false
        }
        return true
    }

    /**
     * 绘网图
     */
    private fun drawWeb(canvas: Canvas) {
        canvas.save()
        val rDeg = 360.0f / mTextArray.size
        mTextArray.forEachIndexed { index, _ ->
            // 绘虚线
            // 每次逆转坐标系(rDeg * index)度
            canvas.save()
            canvas.rotate(-rDeg * index)
            mPaint.pathEffect = mDashPathEffect
            mPaint.color = mWebLineColor
            mPaint.strokeWidth = mWebLineWidth
            canvas.drawLine(0.0f, 0.0f, 0.0f, -mWebRadius, mPaint)
            mPaint.resetPaint()
            // 三角函数算最长边
            val lineW = mWebRadius * CalculationUtils.degreeSin(rDeg / 2) * 2
            for (i in 1..4) {
                // 绘网边
                // 每次上移坐标系(mWebRadius / 4f)*i且顺转(rDeg / 2)度后绘长(lineW / 4f * i)实线
                canvas.save()
                canvas.translate(0.0f, -mWebRadius / 4.0f * i)
                canvas.rotate(rDeg / 2)
                mPaint.color = mWebLineColor
                mPaint.strokeWidth = mWebLineWidth
                canvas.drawLine(0.0f, 0.0f, lineW / 4.0f * i, 0.0f, mPaint)
                mPaint.resetPaint()
                canvas.restore()
            }
            canvas.restore()
        }
        canvas.restore()
    }

    /**
     * 绘文本数组
     */
    private fun drawTextArray(canvas: Canvas) {
        canvas.save()
        val rDeg = 360.0f / mTextArray.size
        // 先算雷达图各顶点坐标
        mPointArray.forEachIndexed { index, pointF ->
            if (index == 0) {
                pointF.x = 0.0f
                pointF.y = -mWebRadius
            } else {
                CalculationUtils.degreePointF(mPointArray[index - 1], pointF, rDeg)
            }
            // 绘辅助圆点
            if (debug) {
                mHelperPaint.color = Color.RED
                canvas.drawCircle(pointF.x, pointF.y, 5.0f, mHelperPaint)
                mHelperPaint.resetPaint()
            }
        }
        // 基于各顶点坐标算文本坐标并绘文本
        mTextArrayedPointArray.mapIndexed { index, pointF ->
            pointF.x = mPointArray[index].x
            pointF.y = mPointArray[index].y
            return@mapIndexed pointF
        }.forEachIndexed { index, pointF ->
            mPaint.color = mTextArrayedColor
            mPaint.textSize = mTextArrayedSize
            if (mTextArrayedTypeface != null) {
                mPaint.typeface = mTextArrayedTypeface
            }
            when {
                index == 0 -> {
                    // 微调修正文本Y坐标
                    pointF.y += DrawUtils.getBottomedY(mPaint)
                    pointF.y = -(pointF.y.absoluteValue + mVerticalSpaceWidth)
                    mPaint.textAlign = Paint.Align.CENTER
                }
                mTextArray.size / 2.0f == index.toFloat() -> {
                    // 微调修正文本Y坐标
                    pointF.y += DrawUtils.getToppedY(mPaint)
                    pointF.y = (pointF.y.absoluteValue + mVerticalSpaceWidth)
                    mPaint.textAlign = Paint.Align.CENTER
                }
                index < mTextArray.size / 2.0f -> {
                    // 微调修正文本Y坐标
                    if (pointF.y < 0) {
                        pointF.y += DrawUtils.getBottomedY(mPaint)
                    } else {
                        pointF.y += DrawUtils.getToppedY(mPaint)
                    }
                    pointF.x = (pointF.x.absoluteValue + mHorizontalSpaceWidth)
                    mPaint.textAlign = Paint.Align.LEFT
                }
                index > mTextArray.size / 2.0f -> {
                    // 微调修正文本Y坐标
                    if (pointF.y < 0) {
                        pointF.y += DrawUtils.getBottomedY(mPaint)
                    } else {
                        pointF.y += DrawUtils.getToppedY(mPaint)
                    }
                    pointF.x = -(pointF.x.absoluteValue + mHorizontalSpaceWidth)
                    mPaint.textAlign = Paint.Align.RIGHT
                }
            }
            canvas.drawText(mTextArray[index], pointF.x, pointF.y, mPaint)
            mPaint.resetPaint()
        }
        canvas.restore()
    }

    /**
     * 绘雷达连接区
     */
    private fun drawConnectionArea(canvas: Canvas) {
        canvas.save()
        val rDeg = 360.0f / mTextArray.size
        // 据雷达图头坐标作基坐标算各进度坐标
        val bPoint = mPointArray.first()
        mProgressPointArray.forEachIndexed { index, pointF ->
            val progress = mProgressArray[index] / mWebMaxProgress.toFloat()
            pointF.x = bPoint.x * progress
            pointF.y = bPoint.y * progress
            CalculationUtils.degreePointF(pointF, mTempPointF, rDeg * index)
            pointF.x = mTempPointF.x
            pointF.y = mTempPointF.y
            // 绘辅助圆点
            if (debug) {
                mHelperPaint.color = Color.BLACK
                canvas.drawCircle(pointF.x, pointF.y, 5.0f, mHelperPaint)
                mHelperPaint.resetPaint()
            }
            // 路径连接各点
            if (index == 0) {
                mPath.moveTo(pointF.x, pointF.y)
            } else {
                mPath.lineTo(pointF.x, pointF.y)
            }
            if (index == mProgressPointArray.lastIndex) {
                mPath.close()
            }
        }
        // 绘区域路径
        mPaint.color = mAreaColor
        canvas.drawPath(mPath, mPaint)
        mPaint.resetPaint()
        // 绘区域路径边框
        mPaint.color = mAreaBorderColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mWebLineWidth
        mPaint.strokeJoin = Paint.Join.ROUND
        canvas.drawPath(mPath, mPaint)
        mPath.reset()
        mPaint.resetPaint()
        canvas.restore()
    }

    /**
     * 绘中心文本
     */
    private fun drawCenterText(canvas: Canvas) {
        canvas.save()
        // 绘数字
        mPaint.color = mTextCenteredColor
        mPaint.textSize = mTextArrayedSize / 12 * 20
        mPaint.textAlign = Paint.Align.CENTER
        if (mTextCenteredTypeface != null) {
            mPaint.typeface = mTextCenteredTypeface
        }
        // 向下微移坐标系
        canvas.translate(0.0f, mPaint.fontMetrics.bottom)
        var sum = mProgressArray.sum().toString()
        // 添辅助文本
        if (debug) {
            sum += "你好"
        }
        canvas.drawText(sum, 0.0f, DrawUtils.getBottomedY(mPaint), mPaint)
        mPaint.resetPaint()
        // 绘名
        mPaint.color = mTextCenteredColor
        mPaint.textSize = mTextArrayedSize / 12 * 10
        mPaint.textAlign = Paint.Align.CENTER
        if (mTextArrayedTypeface != null) {
            mPaint.typeface = mTextArrayedTypeface
        }
        canvas.drawText(mTextCenteredName, 0.0f, DrawUtils.getToppedY(mPaint), mPaint)
        mPaint.resetPaint()
        // 绘辅助线
        if (debug) {
            mHelperPaint.color = Color.RED
            mHelperPaint.strokeWidth = DensityUtils.dipToPxByFloat(context, 1.0f).toFloat()
            canvas.drawLine(-mWidth, 0.0f, mWidth, 0.0f, mHelperPaint)
            mHelperPaint.resetPaint()
        }
        canvas.restore()
    }

    companion object {
        var debug = false
        private const val ANIMATE_MODE_TIME = 101
        private const val ANIMATE_MODE_SPEED = 102
        private const val default_webRadius = 100.0f
        private const val default_webMaxProgress = 100
        private const val default_webLineColor = Color.BLUE
        private const val default_webLineWidth = 1.0f
        private const val default_textArrayedColor = Color.RED
        private var default_areaColor = Color.parseColor("#CCFFFF00")
        private const val default_areaBorderColor = Color.BLACK
        private const val default_textCenteredName = "雷达值"
        private const val default_textCenteredColor = Color.BLACK
        private const val default_animateTime = 0
        private const val default_animateMode = ANIMATE_MODE_TIME
        private val default_textArray = arrayOf("雷达属性1", "雷达属性2", "雷达属性3", "雷达属性4", "雷达属性5", "雷达属性6")
        private val default_progressArray = if (debug) {
            arrayOf(20, 30, 40, 50, 75, 100)
        } else {
            Array(default_textArray.size) { 0 }
        }
        private val default_oldProgressArray = Array(default_textArray.size) { 0 }
    }
}