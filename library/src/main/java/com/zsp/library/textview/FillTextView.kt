package com.zsp.library.textview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DEL
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import com.zsp.library.R
import java.util.regex.Pattern
import kotlin.math.min

/**
 * @decs: FillTextView
 * 布局示例：我是&#060;fill&#062;
 * 代码示例：我是<fill>
 * @author: 郑少鹏
 * @date: 2019/7/19 11:09
 */
class FillTextView : View, MyInputConnection.InputListener, View.OnKeyListener {
    // 编辑字段标记
    private var editTag = "<fill>"
    // 可编辑空白
    private val blanks = "        "
    // 可编辑开始符
    private var mEditStartTag = "【"
    // 可编辑结束符
    private var mEditEndTag = "】"
    // 文本
    private var mText = StringBuffer()
    // 存文字段列表（据<fill>分割为多字段）
    private var mTextList = arrayListOf<AText>()
    // 正输入字段
    private var mEditingText: AText? = null
    // 正编辑文字行数
    private var mEditTextRow = 1
    // 光标[0]：x坐标、[1]：文字基准线
    private var mCursor = arrayOf(-1.0f, -1.0f)
    // 光标所在文字索引
    private var mCursorIndex = 0
    // 光标闪烁标志
    private var mHideCursor = true
    // 控件宽
    private var mWidth = 0
    // 文字画笔
    private val mNormalPaint = Paint()
    // 普通文字色
    private var mNormalColor = Color.BLACK
    // 文字画笔
    private val mFillPaint = Paint()
    // 填文字色
    private var mFillColor = Color.BLACK
    // 光标画笔
    private val mCursorPain = Paint()
    // 光标宽1dp
    private var mCursorWidth = 1.0f
    // 一汉字宽
    private var mOneWordWidth = 0.0f
    // 一行最大文字数
    private var mMaxSizeOneLine = 0
    // 字体大小
    private var mTextSize = sp2px(16.0f).toFloat()
    // 当前绘至第几行
    private var mCurDrawRow = 1
    // 文字起始位
    private var mStartIndex = 0
    // 文字结束位
    private var mEndIndex = 0
    // 存每行文字（算文字长）
    private var mOneRowText = StringBuffer()
    // 一行含字段（普通字段、可编辑字段）
    private var mOneRowTexts = arrayListOf<AText>()
    // 默行距2dp（最小行距，设行距于此基础上叠加。即2 + cst）
    private var mRowSpace = dp2px(2.0f).toFloat()
    // 显下划线否
    private var mUnderlineVisible = false
    // 下划线画笔
    private val mUnderlinePain = Paint().apply {
        strokeWidth = dp2px(1.0f).toFloat()
        color = Color.BLACK
        isAntiAlias = true
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(attrs)
        init()
    }

    private fun getAttrs(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FillTextView)
        mTextSize = ta.getDimension(R.styleable.FillTextView_fillTextSize, mTextSize)
        mText = mText.append(ta.getText(R.styleable.FillTextView_filledText) ?: "")
        mNormalColor = ta.getColor(R.styleable.FillTextView_normalColor, Color.BLACK)
        mFillColor = ta.getColor(R.styleable.FillTextView_fillColor, Color.BLACK)
        mRowSpace += ta.getDimension(R.styleable.FillTextView_rowSpace, 0.0f)
        ta.recycle()
    }

    private fun init() {
        initCursorPaint()
        initTextPaint()
        initFillPaint()
        splitTexts()
        initHandler()
        setOnKeyListener(this)
    }

    /**
     * 初始光标画笔
     */
    private fun initCursorPaint() {
        mCursorWidth = dp2px(mCursorWidth).toFloat()
        mCursorPain.strokeWidth = mCursorWidth
        mCursorPain.color = mFillColor
        mCursorPain.isAntiAlias = true
    }

    /**
     * 初始文字画笔
     */
    private fun initTextPaint() {
        /*mTextSize = sp2px(mTextSize).toFloat()*/
        /*mRowSpace = dp2px(mRowSpace).toFloat()*/
        mNormalPaint.color = mNormalColor
        mNormalPaint.textSize = mTextSize
        mNormalPaint.isAntiAlias = true
        mOneWordWidth = measureTextLength("测")
    }

    private fun initFillPaint() {
        mFillPaint.color = mFillColor
        mFillPaint.textSize = mTextSize
        mFillPaint.isAntiAlias = true
    }

    private fun dp2px(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5).toInt()
    }

    private fun sp2px(sp: Float): Int {
        val density = resources.displayMetrics.scaledDensity
        return (sp * density + 0.5).toInt()
    }

    /**
     * 拆分文字（普通文字、可编辑文字）
     */
    private fun splitTexts() {
        mTextList.clear()
        val texts = mText.split(editTag)
        for (i in 0 until texts.size - 1) {
            var text = texts[i]
            if (i > 0) {
                text = mEditEndTag + text
            }
            text += mEditStartTag
            mTextList.add(AText(text))
            mTextList.add(AText(blanks, true))
        }
        mTextList.add(AText(mEditEndTag + texts[texts.size - 1]))
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = widthSize
        var height = heightSize
        val realText = StringBuffer()
        for (aText in mTextList) {
            realText.append(aText.text)
        }
        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                width = widthSize
                // 指定宽高
                mWidth = width
                mMaxSizeOneLine = (width / mOneWordWidth).toInt()
            }
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {
                // 绘宽高为文字最大长（超用父布局可用最大长）
                width = if (mText.isEmpty()) 0
                else min(widthSize, measureTextLength(realText.toString()).toInt())
                // 配最大宽高
                mWidth = widthSize
                mMaxSizeOneLine = (widthSize / mOneWordWidth).toInt()
            }
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> height = heightSize
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST ->
                height = if (realText.isEmpty()) 0
                else
                // mRowSpace + mNormalPaint.fontMetrics.descent为末行距底间距
                    (getRowHeight() * (mCurDrawRow - 1) + mRowSpace + mNormalPaint.fontMetrics.descent).toInt()
        }
        setMeasuredDimension(width, height)
    }

    override fun draw(canvas: Canvas) {
        clear()
        canvas.save()
        mStartIndex = 0
        mEndIndex = mMaxSizeOneLine
        for (i in 0 until mTextList.size) {
            val aText = mTextList[i]
            val text = aText.text
            while (true) {
                if (mEndIndex > text.length) {
                    mEndIndex = text.length
                }
                // 编辑初始位
                addEditStartPos(aText)
                val cs = text.subSequence(mStartIndex, mEndIndex)
                mOneRowTexts.add(AText(cs.toString(), aText.isFill))
                mOneRowText.append(cs)
                val textWidth = measureTextLength(mOneRowText.toString())
                if (textWidth <= mWidth) {
                    val left = mWidth - textWidth
                    val textCount = left / mOneWordWidth
                    if (mEndIndex < text.length) {
                        mStartIndex = mEndIndex
                        mEndIndex += textCount.toInt()
                        if (mStartIndex == mEndIndex) {
                            val one = measureTextLength(text.substring(mEndIndex, mEndIndex + 1))
                            if (one + textWidth < mWidth) {
                                // 可多放一字
                                mEndIndex++
                            } else {
                                // 绘文字
                                addEditEndPos(aText)
                                drawOneRow(canvas)
                                // 编辑段落或进下行
                                addEditStartPosFromZero(aText, mStartIndex)
                            }
                        }
                    } else {
                        // 进下段文字
                        // 编辑结束位
                        addEditEndPos(aText)
                        if (i < mTextList.size - 1) {
                            mStartIndex = 0
                            mEndIndex = textCount.toInt()
                            if (mStartIndex == mEndIndex) {
                                val one = measureTextLength(mTextList[i + 1].text.substring(0, 1))
                                if (one + textWidth < mWidth) {
                                    // 可多放一字
                                    // 只读下段头字
                                    mEndIndex = 1
                                } else {
                                    // 绘文字
                                    drawOneRow(canvas)
                                }
                            }
                        } else {
                            // 绘文字
                            drawOneRow(canvas)
                        }
                        break
                    }
                } else {
                    // 绘文字
                    drawOneRow(canvas)
                }
            }
        }
        if (isFocused) {
            drawCursor(canvas)
        }
        super.draw(canvas)
        canvas.restore()
    }

    private var mHandler: Handler? = null
    /**
     * 光标闪烁定时
     */
    private fun initHandler() {
        mHandler = Handler(Handler.Callback {
            mHideCursor = !mHideCursor
            mHandler!!.sendEmptyMessageDelayed(1, 500)
            invalidate()
            true
        })
        mHandler!!.sendEmptyMessageDelayed(1, 500)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (gainFocus) {
            mHandler?.removeMessages(1)
            mHandler?.sendEmptyMessageDelayed(1, 500)
        } else {
            // 失焦停刷光标
            mHandler?.removeMessages(1)
        }
    }

    fun destroy() {
        mHandler!!.removeCallbacksAndMessages(null)
    }

    /**
     * 清过期状
     */
    private fun clear() {
        mCurDrawRow = 1
        mStartIndex = 0
        mEndIndex = 0
        mOneRowText.delete(0, mOneRowText.length)
        mOneRowTexts.clear()
        mEditingText?.posInfo?.clear()
    }

    /**
     * 绘一行文字
     */
    private fun drawOneRow(canvas: Canvas) {
        // drawText中Y坐标为文字基线
        /*canvas.drawText(mOneRowText.toString(), 0f, getRowHeight() * mCurDrawRow, mNormalPaint)*/
        // 文字基准线问题
        val fm = mNormalPaint.fontMetrics
        var x = 0f
        for (aText in mOneRowTexts) {
            canvas.drawText(aText.text, x, getRowHeight() * mCurDrawRow, if (aText.isFill) mFillPaint else mNormalPaint)
            val lineStartX = x
            x += measureTextLength(aText.text)
            if (aText.isFill && mUnderlineVisible) {
                canvas.drawLine(lineStartX, getRowHeight() * mCurDrawRow + fm.descent, x, (getRowHeight() * mCurDrawRow + fm.descent), mUnderlinePain)
            }
        }
        mCurDrawRow++
        mEndIndex += mMaxSizeOneLine
        mOneRowText.delete(0, mOneRowText.length)
        mOneRowTexts.clear()
        requestLayout()
    }

    /**
     * 绘光标
     */
    private fun drawCursor(canvas: Canvas) {
        if (mHideCursor) {
            mCursorPain.alpha = 0
        } else {
            mCursorPain.alpha = 255
        }
        if (mCursor[0] >= 0 && mCursor[1] >= 0) {
            if (mEditingText?.text == blanks &&
                    // 光标或需换至上行
                    (mCursor[0] == 0f || (mCursor[0] == mCursorWidth && mEditingText!!.posInfo.size > 1))) {
                if (mEditingText!!.posInfo.size > 1) {
                    mEditTextRow = mEditingText!!.getStartPos()
                    // 可编辑字段最上行起始位
                    val posInfo = mEditingText!!.posInfo[mEditTextRow]
                    mCursor[0] = posInfo!!.rect.left.toFloat()
                    mCursor[1] = posInfo.rect.bottom.toFloat()
                    // 矫正光标X轴坐标
                    if (mCursor[0] <= 0) mCursor[0] = mCursorWidth
                }
            }
            // 文字基准线问题
            val fm = mNormalPaint.fontMetrics
            canvas.drawLine(mCursor[0], mCursor[1] + fm.ascent, mCursor[0], (mCursor[1] + fm.descent), mCursorPain)
        }
    }

    /**
     * 添编辑字段起始位
     */
    private fun addEditStartPos(aText: AText) {
        if (aText.isFill && mStartIndex == 0) {
            aText.posInfo.clear()
            val width = measureTextLength(mOneRowText.toString()).toInt()
            val rect = Rect(width, (getRowHeight() * (mCurDrawRow - 1) + mRowSpace/*加上行距*/).toInt(), 0, 0)
            val info = EditPosInfo(mStartIndex, rect)
            aText.posInfo[mCurDrawRow] = info
        }
    }

    /**
     * 添编辑字段起始位（换行场景）
     */
    private fun addEditStartPosFromZero(aText: AText, index: Int) {
        if (aText.isFill) {
            val rect = Rect(0, (getRowHeight() * (mCurDrawRow - 1) + mRowSpace/*加行距*/).toInt(), 0, 0)
            val info = EditPosInfo(index, rect)
            aText.posInfo[mCurDrawRow] = info
        }
    }

    /**
     * 添编辑字段结束位
     */
    private fun addEditEndPos(aText: AText) {
        if (aText.isFill) {
            val width = measureTextLength(mOneRowText.toString())
            aText.posInfo[mCurDrawRow]?.rect?.right = width.toInt()
            aText.posInfo[mCurDrawRow]?.rect?.bottom = (getRowHeight() * mCurDrawRow).toInt()
        }
    }

    /**
     * 文字长（px）
     */
    private fun measureTextLength(text: String): Float {
        return mNormalPaint.measureText(text)
    }

    /**
     * 行高
     */
    private fun getRowHeight(): Float {
        return mTextSize + mRowSpace
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!hasWindowFocus) {
            hideInput()
        }
    }

    /**
     * 隐输入法
     */
    private fun hideInput() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (touchCollision(event)) {
                    // important
                    isFocusableInTouchMode = true
                    isFocusable = true
                    requestFocus()
                    try {
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
                        imm.restartInput(this)
                    } catch (ignore: Exception) {
                    }
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 检点击碰撞
     */
    private fun touchCollision(event: MotionEvent): Boolean {
        for (aText in mTextList) {
            if (aText.isFill) {
                for ((row, posInfo) in aText.posInfo) {
                    if (event.x > posInfo.rect.left && event.x < posInfo.rect.right && event.y > posInfo.rect.top && event.y < posInfo.rect.bottom) {
                        mEditTextRow = row
                        if (aText.text == blanks) {
                            val firstRow = aText.getStartPos()
                            if (firstRow >= 0) {
                                // 或存换行
                                mEditTextRow = firstRow
                            }
                        }
                        mEditingText = aText
                        calculateCursorPos(event, aText.posInfo[mEditTextRow]!!, aText.text)
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 光标位
     */
    private fun calculateCursorPos(event: MotionEvent, posInfo: EditPosInfo, text: String) {
        val eX = event.x
        val innerWidth = eX - posInfo.rect.left
        var nWord = (innerWidth / mOneWordWidth).toInt()
        var wordsWidth: Int
        if (nWord <= 0) nWord = 1
        if (text == blanks) {
            mCursor[0] = posInfo.rect.left.toFloat()
            mCursor[1] = posInfo.rect.bottom.toFloat()
            mCursorIndex = 0
        } else {
            // 循算至最后一个真正超显范围文字（汉字、英文、数字占位不同，此处汉字作初始占位）
            do {
                wordsWidth = measureTextLength(text.substring(posInfo.index, posInfo.index + nWord)).toInt()
                nWord++
            } while (wordsWidth < innerWidth && posInfo.index + nWord <= text.length)
            mCursorIndex = posInfo.index + nWord - 1
            // 点击位超所点文字一半否
            val leftWidth = wordsWidth - innerWidth
            if (leftWidth > measureTextLength(text.substring(mCursorIndex - 1, mCursorIndex)) / 2) {
                mCursorIndex--
            }
            mCursor[0] = posInfo.rect.left + measureTextLength(text.substring(posInfo.index, mCursorIndex))
            mCursor[1] = posInfo.rect.bottom.toFloat()
        }
        invalidate()
    }

    /**
     * 键盘输入
     */
    override fun onTextInput(text: CharSequence) {
        if (mEditingText != null) {
            val filledText = StringBuffer(mEditingText!!.text.replace(blanks, ""))
            if (filledText.isEmpty()) {
                filledText.append(text)
                mCursorIndex = text.length
            } else {
                filledText.insert(mCursorIndex, text)
                mCursorIndex += text.length
            }
            mEditingText!!.text = filledText.toString()
            if (mCursor[0] + measureTextLength(text.toString()) > mWidth) {
                // 实际可放字数
                var restCount = ((mWidth - mCursor[0]) / mOneWordWidth).toInt()
                var realWidth = mCursor[0] + measureTextLength(text.substring(0, restCount))
                // 循算至最后一个真正超显范围文字（汉字、英文、数字占位不同，此处汉字作初始占位）
                while (realWidth <= mWidth && restCount < text.length) {
                    restCount++
                    realWidth = mCursor[0] + measureTextLength(text.substring(0, restCount))
                }
                mEditTextRow += ((mCursor[0] + measureTextLength(text.toString())) / mWidth).toInt()
                if (mEditTextRow < 1) mEditTextRow = 1
                val realCount = if (restCount - 1 > 0) restCount - 1 else 0
                mCursor[0] = measureTextLength(text.substring(realCount, text.length))
                mCursor[1] = getRowHeight() * (mEditTextRow)
            } else {
                mCursor[0] += measureTextLength(text.toString())
            }
            invalidate()
        }
    }

    override fun onCheckIsTextEditor(): Boolean {
        return true
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        outAttrs.inputType = InputType.TYPE_CLASS_TEXT
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE
        return MyInputConnection(this, false, this)
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (keyCode == KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN) {
            onDeleteWord()
            return true
        }
        return false
    }

    override fun onDeleteWord() {
        if (mEditingText != null) {
            val text = mEditingText?.text?.let { StringBuffer(it) }
            if (!text.isNullOrEmpty() && text.toString() != blanks && mCursorIndex >= 1) {
                val cursorPos = (mCursor[0] - measureTextLength(text.substring(mCursorIndex - 1, mCursorIndex))).toInt()
                if (cursorPos > 0 || (cursorPos == 0 && mEditingText!!.posInfo.size == 1)) {
                    // 光标仍同行
                    mCursor[0] = cursorPos.toFloat()
                } else {
                    // 光标回上行
                    mEditTextRow--
                    val posInfo = mEditingText!!.posInfo[mEditTextRow]!!
                    mCursor[0] = posInfo.rect.left + measureTextLength(text.substring(posInfo.index, mCursorIndex - 1))
                    mCursor[1] = getRowHeight() * (mEditTextRow)
                }
                mEditingText?.text = text.replace(mCursorIndex - 1, mCursorIndex, "").toString()
                mCursorIndex--
                if (mEditingText?.text?.length ?: 0 <= 0) {
                    if (text.toString() != blanks) {
                        mEditingText?.text = blanks
                        mCursorIndex = 1
                        val firstRow = mEditingText!!.getStartPos()
                        if (firstRow > 0) {
                            // 或存换行
                            mEditTextRow = firstRow
                        }
                        mCursor[0] = mEditingText!!.posInfo[mEditTextRow]!!.rect.left.toFloat()
                        mCursor[1] = getRowHeight() * (mEditTextRow)
                    }
                }
                invalidate()
            }
        }
    }

    /**
     * 文本
     */
    fun setText(text: String) {
        mText = StringBuffer(text)
        splitTexts()
        invalidate()
    }

    /**
     * 原始文本
     */
    fun getOriginalText(): String {
        return mText.toString()
    }

    /**
     * 字体大小（sp）
     */
    fun setTextSize(sp: Float) {
        mTextSize = sp2px(sp).toFloat()
        initTextPaint()
        initFillPaint()
        invalidate()
    }

    /**
     * 行距（dp）
     */
    fun setRowSpace(dp: Float) {
        mRowSpace = dp2px(2 + dp).toFloat()
        invalidate()
    }

    /**
     * 可编辑标记开始和结束符
     */
    fun setEditTag(startTag: String, endTag: String) {
        mEditStartTag = startTag
        mEditEndTag = endTag
        invalidate()
    }

    /**
     * 显可编辑字段下划线否
     */
    fun displayUnderline(visible: Boolean) {
        mUnderlineVisible = visible
    }

    /**
     * 下划线色
     */
    fun setUnderlineColor(color: Int) {
        mUnderlinePain.color = color
        invalidate()
    }

    /**
     * 文字段落
     */
    fun getTextPassage(): List<AText> {
        return mTextList
    }

    /**
     * 所填内容
     */
    fun getFillTexts(): List<String> {
        val list = arrayListOf<String>()
        for (value in mTextList) {
            if (value.isFill) {
                list.add(value.text)
            }
        }
        return list
    }
}

internal class MyInputConnection(targetView: View, fullEditor: Boolean, private val mListener: InputListener) : BaseInputConnection(targetView, fullEditor) {
    override fun commitText(text: CharSequence, newCursorPosition: Int): Boolean {
        if (!isEmoji(text)) {
            // 滤emojj表情
            mListener.onTextInput(text)
        }
        return super.commitText(text, newCursorPosition)
    }

    private fun isEmoji(string: CharSequence): Boolean {
        // 滤Emoji表情
        val p = Pattern.compile("[^\\u0000-\\uFFFF]")
        // 滤Emoji表情和文字
        /*Pattern p = Pattern . compile ("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\ud83e\\udd00-\\ud83e\\uddff]|[\\u2300-\\u23ff]|[\\u2500-\\u25ff]|[\\u2100-\\u21ff]|[\\u0000-\\u00ff]|[\\u2b00-\\u2bff]|[\\u2d06]|[\\u3030]");*/
        val m = p.matcher(string)
        return m.find()
    }

    override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
        // 软键盘删键DEL无法直监听（自发DEL事件）
        return if (beforeLength == 1 && afterLength == 0) {
            super.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_DEL)) && super.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KEYCODE_DEL))
        } else super.deleteSurroundingText(beforeLength, afterLength)
    }

    interface InputListener {
        fun onTextInput(text: CharSequence)
        fun onDeleteWord()
    }
}

/**
 * 文字段落
 */
class AText(
        // 段落文字
        var text: String,
        // 填字段否
        var isFill: Boolean = false) {
    // 文字位信息（行、文字框）
    var posInfo: MutableMap<Int, EditPosInfo> = mutableMapOf()

    fun getStartPos(): Int {
        if (posInfo.isEmpty()) return -1
        var firstRow = Int.MAX_VALUE
        for ((row, _) in posInfo) {
            if (firstRow > row) {
                firstRow = row
            }
        }
        return firstRow
    }
}

data class EditPosInfo(var index: Int, var rect: Rect)