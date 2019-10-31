package com.zsp.library.clock

import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zsp.utiltwo.density.DensityUtils
import java.util.*
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

/**
 * @decs: 时钟视图
 * @author: 郑少鹏
 * @date: 2019/10/30 14:55
 */
class ClockView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 画边缘线
     */
    private val mPaintOutCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 画边缘文字
     */
    private val mPaintOutText = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 画进度条
     */
    private val mPaintProgressBg = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintProgress = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 画三角形
     */
    private val mPaintTriangle = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 画时针
     */
    private val mPaintHour = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 画分针
     */
    private val mPaintMinute = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 画中间小球
     */
    private val mPaintBall = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 半透明白色
     */
    private val colorHalfWhite: Int = Color.argb(255 - 180, 255, 255, 255)
    /**
     * 纯白色
     */
    private val colorWhite: Int = Color.parseColor("#FFFFFF")
    /**
     * 宽高
     */
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    /**
     * 中心坐标
     */
    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private val paddingOut: Float = DensityUtils.dipToPxByFloat(context, 25.0f).toFloat()
    private val innerRadius: Float = DensityUtils.dipToPxByFloat(context, 6.0f).toFloat()
    private var mHourDegress: Int = 0
    private var mMinuteDegress: Int = 0
    private var mSecondMillsDegress: Float = 0.0f
    private var mSecondDegress: Int = 0
    /**
     * 时钟半径（不含padding）
     */
    private var mRadius: Float = 0.toFloat()
    /**
     * 手指松开时时钟晃动动画
     */
    private var mShakeAnim: ValueAnimator? = null
    /**
     * 触摸时作用于Camera的矩阵
     */
    private val mCameraMatrix: Matrix by lazy { Matrix() }
    /**
     * 照相机（旋转时钟实现3D效果）
     */
    private val mCamera: Camera by lazy { Camera() }
    /**
     * Camera绕X轴旋转角度
     */
    private var mCameraRotateX: Float = 0.0f
    /**
     * Camera绕Y轴旋转角度
     */
    private var mCameraRotateY: Float = 0.0f
    /**
     * Camera旋转最大角度
     */
    private val mMaxCameraRotate = 10.0f
    /**
     * 指针于X轴位移
     */
    private var mCanvasTranslateX: Float = 0.0f
    /**
     * 指针于Y轴位移
     */
    private var mCanvasTranslateY: Float = 0.0f
    /**
     * 指针最大位移
     */
    private var mMaxCanvasTranslate: Float = 0.0f
    /**
     * 画布
     */
    private lateinit var mCanvas: Canvas

    init {
        mPaintOutCircle.color = colorHalfWhite
        mPaintOutCircle.strokeWidth = DensityUtils.dipToPxByFloat(context, 1.0f).toFloat()
        mPaintOutCircle.style = Paint.Style.STROKE
        mPaintOutText.color = colorHalfWhite
        mPaintOutText.strokeWidth = DensityUtils.dipToPxByFloat(context, 1.0f).toFloat()
        mPaintOutText.style = Paint.Style.STROKE
        mPaintOutText.textSize = DensityUtils.spToPxByFloat(context, 11.0f).toFloat()
        mPaintOutText.textAlign = Paint.Align.CENTER
        mPaintProgressBg.color = colorHalfWhite
        mPaintProgressBg.strokeWidth = DensityUtils.dipToPxByFloat(context, 2.0f).toFloat()
        mPaintProgressBg.style = Paint.Style.STROKE
        mPaintProgress.color = colorHalfWhite
        mPaintProgress.strokeWidth = DensityUtils.dipToPxByFloat(context, 2.0f).toFloat()
        mPaintProgress.style = Paint.Style.STROKE
        mPaintTriangle.color = colorWhite
        mPaintTriangle.style = Paint.Style.FILL
        mPaintHour.color = colorHalfWhite
        mPaintHour.style = Paint.Style.FILL
        mPaintMinute.color = colorWhite
        mPaintMinute.strokeWidth = DensityUtils.dipToPxByFloat(context, 3.0f).toFloat()
        mPaintMinute.style = Paint.Style.STROKE
        mPaintMinute.strokeCap = Paint.Cap.ROUND
        mPaintBall.color = Color.parseColor("#4169E1")
        mPaintBall.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        // 设正方形
        val imageSize = if (width < height) width else height
        setMeasuredDimension(imageSize, imageSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mCenterX = mWidth / 2
        mCenterY = mHeight / 2
        mRadius = (min(w - paddingLeft - paddingRight, h - paddingTop - paddingBottom) / 2).toFloat()
        mMaxCanvasTranslate = 0.02f * mRadius
    }

    override fun onDraw(canvas: Canvas) {
        mCanvas = canvas
        // 平移至视图中心
        canvas.translate(mCenterX.toFloat(), mCenterY.toFloat())
        setCameraRotate()
        drawArcCircle(canvas)
        drawOutText(canvas)
        drawCalibrationLine(canvas)
        drawSecond(canvas)
        drawMinute(canvas)
        drawHour(canvas)
        drawBall(canvas)
    }

    /**
     * 绘中间小球
     */
    private fun drawBall(canvas: Canvas) {
        canvas.drawCircle(0.0f, 0.0f, innerRadius / 2, mPaintBall)
    }

    /**
     * 绘分
     */
    private fun drawMinute(canvas: Canvas) {
        canvas.save()
        canvas.rotate(mMinuteDegress.toFloat())
        canvas.drawLine(0.0f, 0.0f, 0.0f, -(width / 3).toFloat(), mPaintMinute)
        canvas.restore()
    }

    /**
     * 绘时
     */
    private fun drawHour(canvas: Canvas) {
        canvas.save()
        canvas.rotate(mHourDegress.toFloat())
        canvas.drawCircle(0.0f, 0.0f, innerRadius, mPaintTriangle)
        val path = Path()
        path.moveTo(-innerRadius / 2, 0.0f)
        path.lineTo(innerRadius / 2, 0.0f)
        path.lineTo(innerRadius / 6, -(width / 4).toFloat())
        path.lineTo(-innerRadius / 6, -(width / 4).toFloat())
        path.close()
        canvas.drawPath(path, mPaintHour)
        canvas.restore()
    }

    /**
     * 绘秒
     */
    private fun drawSecond(canvas: Canvas) {
        // 先绘秒针三角形
        canvas.save()
        canvas.rotate(mSecondMillsDegress)
        val path = Path()
        path.moveTo(0.0f, -width * 3.0f / 8 + DensityUtils.dipToPxByFloat(context, 5.0f).toFloat())
        path.lineTo(DensityUtils.dipToPxByFloat(context, 8.0f).toFloat(), -width * 3.0f / 8 + DensityUtils.dipToPxByFloat(context, 20.0f).toFloat())
        path.lineTo(-DensityUtils.dipToPxByFloat(context, 8.0f).toFloat(), -width * 3.0f / 8 + DensityUtils.dipToPxByFloat(context, 20.0f).toFloat())
        path.close()
        canvas.drawPath(path, mPaintTriangle)
        canvas.restore()
        // 绘渐变刻度
        val min = min(width, mHeight) / 2
        for (i in 0..90 step 2) {
            // 头参设透明度实现渐变效果（255到0）
            canvas.save()
            mPaintProgress.setARGB((255 - 2.7 * i).toInt(), 255, 255, 255)
            // 先减90°旋转至开始角度（开始角度Y轴负向）
            canvas.rotate(((mSecondDegress - 90 - i).toFloat()))
            canvas.drawLine(min.toFloat() * 3 / 4, 0.0f, min * 3 / 4 + DensityUtils.dipToPxByFloat(context, 10.0f).toFloat(), 0.0f, mPaintProgress)
            canvas.restore()
        }
    }

    /**
     * 绘刻度线
     * 每2°绘一刻度（180刻度）
     */
    private fun drawCalibrationLine(canvas: Canvas) {
        val min = min(width, mHeight) / 2
        for (i in 0 until 360 step 2) {
            canvas.save()
            canvas.rotate(i.toFloat())
            canvas.drawLine(min.toFloat() * 3 / 4, 0f, min * 3 / 4 + DensityUtils.dipToPxByFloat(context, 10.0f).toFloat(), 0.0f, mPaintProgressBg)
            canvas.restore()
        }
    }

    /**
     * 然后绘4个文字刻度（3、6、9、12）
     */
    private fun drawOutText(canvas: Canvas) {
        val min = min(width, mHeight)
        val textRadius = (min - paddingOut) / 2
        val fm = mPaintOutText.fontMetrics
        // 文字高
        val mTxtHeight = ceil((fm.leading - fm.ascent).toDouble()).toInt()
        canvas.drawText("3", textRadius, (mTxtHeight / 2).toFloat(), mPaintOutText)
        canvas.drawText("9", -textRadius, (mTxtHeight / 2).toFloat(), mPaintOutText)
        canvas.drawText("6", 0.0f, textRadius + mTxtHeight / 2, mPaintOutText)
        canvas.drawText("12", 0.0f, -textRadius + mTxtHeight / 2, mPaintOutText)
    }

    /**
     * 首先画外边缘弧度，分四弧度。
     * 假设每弧度中间间隔6弧度，则弧度位5-85、95-175、185 -265、275-355。
     */
    private fun drawArcCircle(canvas: Canvas) {
        val min = min(width, mHeight)
        val rect = RectF(-(min - paddingOut) / 2, -(min - paddingOut) / 2, (min - paddingOut) / 2, (min - paddingOut) / 2)
        canvas.drawArc(rect, 5.0f, 80.0f, false, mPaintOutCircle)
        canvas.drawArc(rect, 95.0f, 80.0f, false, mPaintOutCircle)
        canvas.drawArc(rect, 185.0f, 80.0f, false, mPaintOutCircle)
        canvas.drawArc(rect, 275.0f, 80.0f, false, mPaintOutCircle)
    }

    // 指针转动法
    private fun startTick() {
        // 一秒刷一次
        postDelayed(mRunnable, 150)
    }

    private val mRunnable = Runnable {
        calculateDegree()
        invalidate()
        startTick()
    }

    private fun calculateDegree() {
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = System.currentTimeMillis()
        val minute = mCalendar.get(Calendar.MINUTE)
        val secondMills = mCalendar.get(Calendar.MILLISECOND)
        val second = mCalendar.get(Calendar.SECOND)
        val hour = mCalendar.get(Calendar.HOUR)
        mHourDegress = hour * 30
        mMinuteDegress = minute * 6
        mSecondMillsDegress = second * 6 + secondMills * 0.006f
        mSecondDegress = second * 6
        // 因每2°旋转一刻度，故此处据毫秒算
        when (secondMills * 0.006f) {
            in 2 until 4 -> {
                mSecondDegress += 2
            }
            in 4 until 6 -> {
                mSecondDegress += 4
            }
        }
    }

    /**
     * 头次onDraw前调且仅调一次
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTick()
    }

    /**
     * 销毁View时调
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(mRunnable)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mShakeAnim?.let {
                    if (it.isRunning) {
                        it.cancel()
                    }
                }
                getCameraRotate(event)
                getCanvasTranslate(event)
            }
            MotionEvent.ACTION_MOVE -> {
                // 据手指坐标算Camera应旋转大小
                getCameraRotate(event)
                getCanvasTranslate(event)
            }
            MotionEvent.ACTION_UP ->
                // 松开手指时钟复原并伴随晃动动画
                startShakeAnim()
        }
        return true
    }

    /**
     * 绘图前设3D时钟效果、触摸矩阵相关设置、照相机旋转大小，否无效。
     * Camera坐标系是左手坐标系。
     * 手机平放桌面，X轴手机水平方向，Y轴手机竖直方向，Z轴垂直于手机向里方向。
     * Camera内部机制实际仍OpenGL，但极大简化使用。
     */
    private fun setCameraRotate() {
        mCameraMatrix.reset()
        mCamera.save()
        // 绕X轴旋转角度
        mCamera.rotateX(mCameraRotateX)
        // 绕Y轴旋转角度
        mCamera.rotateY(mCameraRotateY)
        // 相关属性设置到matrix中
        mCamera.getMatrix(mCameraMatrix)
        mCamera.restore()
        // matrix关联canvas
        mCanvas.concat(mCameraMatrix)
    }

    /**
     * 时钟晃动动画
     */
    private fun startShakeAnim() {
        val cameraRotateXName = "cameraRotateX"
        val cameraRotateYName = "cameraRotateY"
        val canvasTranslateXName = "canvasTranslateX"
        val canvasTranslateYName = "canvasTranslateY"
        val cameraRotateXHolder = PropertyValuesHolder.ofFloat(cameraRotateXName, mCameraRotateX, 0.0f)
        val cameraRotateYHolder = PropertyValuesHolder.ofFloat(cameraRotateYName, mCameraRotateY, 0.0f)
        val canvasTranslateXHolder = PropertyValuesHolder.ofFloat(canvasTranslateXName, mCanvasTranslateX, 0.0f)
        val canvasTranslateYHolder = PropertyValuesHolder.ofFloat(canvasTranslateYName, mCanvasTranslateY, 0.0f)
        mShakeAnim = ValueAnimator.ofPropertyValuesHolder(cameraRotateXHolder, cameraRotateYHolder, canvasTranslateXHolder, canvasTranslateYHolder)
        mShakeAnim?.interpolator = TimeInterpolator { input ->
            val f = 0.571429f
            (2.0.pow((-2 * input).toDouble()) * sin((input - f / 4) * (2 * Math.PI) / f) + 1).toFloat()
        }
        mShakeAnim?.duration = 1000
        mShakeAnim?.addUpdateListener { animation ->
            mCameraRotateX = animation.getAnimatedValue(cameraRotateXName) as Float
            mCameraRotateY = animation.getAnimatedValue(cameraRotateYName) as Float
            mCanvasTranslateX = animation.getAnimatedValue(canvasTranslateXName) as Float
            mCanvasTranslateY = animation.getAnimatedValue(canvasTranslateYName) as Float
        }
        mShakeAnim?.start()
    }

    /**
     * 获Camera旋转大小
     * @param event motionEvent
     */
    private fun getCameraRotate(event: MotionEvent) {
        val rotateX = -(event.y - height / 2)
        val rotateY = event.x - width / 2
        // 此时旋转大小与半径比
        val percentArr = getPercent(rotateX, rotateY)
        // 最终旋转大小按比例匀称改变
        mCameraRotateX = percentArr[0] * mMaxCameraRotate
        mCameraRotateY = percentArr[1] * mMaxCameraRotate
    }

    /**
     * 拨动时钟时发现时针、分针、秒针和刻度盘有一较小偏移量，呈近大远小立体偏移效果。
     */
    private fun getCanvasTranslate(event: MotionEvent) {
        val translateX = event.x - width / 2
        val translateY = event.y - height / 2
        // 此时位移大小与半径比
        val percentArr = getPercent(translateX, translateY)
        // 最终位移大小按比例匀称改变
        mCanvasTranslateX = percentArr[0] * mMaxCanvasTranslate
        mCanvasTranslateY = percentArr[1] * mMaxCanvasTranslate
    }

    /**
     * 获一操作旋转或位移大小比例
     * @return 装有XY比例float数组
     */
    private fun getPercent(x: Float, y: Float): FloatArray {
        val percentArr = FloatArray(2)
        var percentX = x / mRadius
        var percentY = y / mRadius
        if (percentX > 1) {
            percentX = 1.0f
        } else if (percentX < -1) {
            percentX = -1.0f
        }
        if (percentY > 1) {
            percentY = 1.0f
        } else if (percentY < -1) {
            percentY = -1.0f
        }
        percentArr[0] = percentX
        percentArr[1] = percentY
        return percentArr
    }
}