package com.zsp.library.wheelview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.zsp.library.R;
import com.zsp.library.wheelview.adapter.WheelAdapter;
import com.zsp.library.wheelview.joggle.IPickerViewData;
import com.zsp.library.wheelview.listener.LoopViewGestureListener;
import com.zsp.library.wheelview.listener.OnItemSelectedListener;
import com.zsp.library.wheelview.timer.InertiaTimerTask;
import com.zsp.library.wheelview.timer.MessageHandler;
import com.zsp.library.wheelview.timer.SmoothScrollTimerTask;
import com.zsp.utilone.thread.ThreadManager;

import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import value.WidgetMagic;

/**
 * Created on 2018/4/3.
 *
 * @author 郑少鹏
 * @desc WheelView
 */
public class WheelView extends View {
    /**
     * 修改该值改滑速
     */
    private static final int VELOCITY_FLING = 5;
    /**
     * 非中间文本用此控高，压扁成3D错觉
     */
    private static final float SCALE_CONTENT = 0.8F;
    /**
     * 分隔线类型
     */
    private DividerType dividerType;
    private Context context;
    private Handler handler;
    private GestureDetector gestureDetector;
    private OnItemSelectedListener onItemSelectedListener;
    private boolean isOptions = false;
    private boolean isCenterLabel = true;
    /**
     * Timer mTimer
     */
    private ScheduledExecutorService mExecutor = ThreadManager.stepScheduledExecutorService();
    private ScheduledFuture<?> mFuture;
    private Paint paintOuterText;
    private Paint paintCenterText;
    private Paint paintIndicator;
    private WheelAdapter adapter;
    /**
     * 附加单位
     */
    private String label;
    /**
     * 选项文本大小
     */
    private int textSize;
    private int maxTextWidth;
    private int maxTextHeight;
    private int xOffsetOfText;
    /**
     * 行高
     */
    private float itemHeight;
    /**
     * 字体样式（默等宽字体）
     */
    private Typeface typeface = Typeface.MONOSPACE;
    private int textColorOut;
    private int textColorCenter;
    private int dividerColor;
    /**
     * 条目间距倍数
     */
    private float lineSpacingMultiplier = 2.0F;
    private boolean isLoop;
    /**
     * 第一条线Y坐标
     */
    private float yFirstLine;
    /**
     * 第二条线Y坐标
     */
    private float ySecondLine;
    /**
     * 中间label绘Y坐标
     */
    private float yCenter;
    /**
     * 当前滚动总高y值
     */
    private float yTotalScroll;
    /**
     * 初始化默选项
     */
    private int initPosition;
    /**
     * 选中Item为第几
     */
    private int selectedItem;
    private int preCurrentIndex;
    /**
     * 绘几个条目，实际第一和最后一项Y轴压缩成0%，故可见实际数9
     */
    private int itemsVisible = 11;
    /**
     * 控件高
     */
    private int measuredHeight;
    /**
     * 控件宽
     */
    private int measuredWidth;
    /**
     * 半径
     */
    private int radius;
    private int mOffset = 0;
    private float yPrevious = 0;
    private long startTime = 0;
    private int widthMeasureSpec;
    private int mGravity = Gravity.CENTER;
    /**
     * 中间选中文本始绘位
     */
    private int drawCenterContentStart = 0;
    /**
     * 非中间文本始绘位
     */
    private int drawOutContentStart = 0;
    /**
     * 偏移量
     */
    private float centerContentOffset;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 默大小
        textSize = getResources().getDimensionPixelSize(R.dimen.sp_14);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 屏幕密度比（0.75/1.0/1.5/2.0/3.0）
        float density = dm.density;
        if (density < 1) {
            // 据密度不同适配
            centerContentOffset = 2.4F;
        } else if (1 <= density && density < WidgetMagic.FLOAT_TWO_DOT_ZERO) {
            centerContentOffset = 3.6F;
        } else if (WidgetMagic.FLOAT_TWO_DOT_ZERO <= density && density < WidgetMagic.FLOAT_THREE_DOT_ZERO) {
            centerContentOffset = 6.0F;
        } else if (density >= WidgetMagic.FLOAT_THREE_DOT_ZERO) {
            centerContentOffset = density * 2.5F;
        }
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView, 0, 0);
            mGravity = a.getInt(R.styleable.WheelView_wheelViewGravity, Gravity.CENTER);
            textColorOut = a.getColor(R.styleable.WheelView_wheelViewTextColorOut, 0xFFa8a8a8);
            textColorCenter = a.getColor(R.styleable.WheelView_wheelViewTextColorCenter, 0xFF2a2a2a);
            dividerColor = a.getColor(R.styleable.WheelView_wheelViewDividerColor, 0xFFd5d5d5);
            textSize = a.getDimensionPixelOffset(R.styleable.WheelView_wheelViewTextSize, textSize);
            lineSpacingMultiplier = a.getFloat(R.styleable.WheelView_wheelViewLineSpacingMultiplier, lineSpacingMultiplier);
            // 回收内存
            a.recycle();
        }
        judgeLineSpace();
        initLoopView(context);
    }

    /**
     * 判间距（1.0-4.0）
     */
    private void judgeLineSpace() {
        if (lineSpacingMultiplier < WidgetMagic.FLOAT_ONE_DOT_ZERO) {
            lineSpacingMultiplier = 1.0f;
        } else if (lineSpacingMultiplier > WidgetMagic.FLOAT_FOUR_DOT_ZERO) {
            lineSpacingMultiplier = 4.0f;
        }
    }

    private void initLoopView(Context context) {
        this.context = context;
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        gestureDetector.setIsLongpressEnabled(false);
        isLoop = true;
        yTotalScroll = 0;
        initPosition = -1;
        initPaints();
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(textColorOut);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(typeface);
        paintOuterText.setTextSize(textSize);
        paintCenterText = new Paint();
        paintCenterText.setColor(textColorCenter);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(1.1F);
        paintCenterText.setTypeface(typeface);
        paintCenterText.setTextSize(textSize);
        paintIndicator = new Paint();
        paintIndicator.setColor(dividerColor);
        paintIndicator.setAntiAlias(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void remeasure() {
        // 重测
        if (adapter == null) {
            return;
        }
        measureTextWidthHeight();
        // 半圆周长=item高乘item数目-1
        int halfCircumference = (int) (itemHeight * (itemsVisible - 1));
        // 整圆周长除以PI得直径，作控件总高
        measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
        // 半径
        radius = (int) (halfCircumference / Math.PI);
        // 控件宽，这里支持weight
        measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        // 算两横线和选中项画笔的基线Y位
        yFirstLine = (measuredHeight - itemHeight) / 2.0F;
        ySecondLine = (measuredHeight + itemHeight) / 2.0F;
        yCenter = ySecondLine - (itemHeight - maxTextHeight) / 2.0f - centerContentOffset;
        // 初始化所显item之position
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (adapter.getItemsCount() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }
        preCurrentIndex = initPosition;
    }

    /**
     * 算最大length的Text宽高
     */
    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < adapter.getItemsCount(); i++) {
            String s1 = getContentText(adapter.getItem(i));
            paintCenterText.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
            // 星期字符编码（作标准高）
            paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect);
            maxTextHeight = rect.height() + 2;
        }
        itemHeight = lineSpacingMultiplier * maxTextHeight;
    }

    public void smoothScroll(ACTION action) {
        // 平滑滚动实现
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DRAG) {
            mOffset = (int) ((yTotalScroll % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / WidgetMagic.FLOAT_TWO_DOT_ZERO) {
                // 超Item高一半，滚动到下一Item
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        // 停止时，位置有偏移，并非都能正确停到中间位，这里把文本位挪回中间
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

    public final void scrollBy(float yVelocity) {
        // 滚动惯性实现
        cancelFuture();
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, yVelocity), 0, VELOCITY_FLING, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * 循环滚动
     *
     * @param cyclic 循环
     */
    public final void setCyclic(boolean cyclic) {
        isLoop = cyclic;
    }

    public final void setTypeface(Typeface font) {
        typeface = font;
        paintOuterText.setTypeface(typeface);
        paintCenterText.setTypeface(typeface);
    }

    public final void setTextSize(float size) {
        if (size > WidgetMagic.FLOAT_ZERO_DOT_ZERO) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public final WheelAdapter getAdapter() {
        return adapter;
    }

    public final void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        remeasure();
        invalidate();
    }

    public final int getCurrentItem() {
        return selectedItem;
    }

    public final void setCurrentItem(int currentItem) {
        // 不添该句，当该wheelView不可见时，默都0，致获时错
        this.selectedItem = currentItem;
        this.initPosition = currentItem;
        // 回归顶部，否重设setCurrentItem则位偏移，显错位数据
        yTotalScroll = 0;
        invalidate();
    }

    public final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(() -> onItemSelectedListener.onItemSelected(getCurrentItem()), 200L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (adapter == null) {
            return;
        }
        // initPosition越界造preCurrentIndex值错
        initPosition = Math.min(Math.max(0, initPosition), adapter.getItemsCount() - 1);
        // 可见item数组
        @SuppressLint("DrawAllocation") Object[] visible = new Object[itemsVisible];
        // 滚动Y值高去每行Item高，得滚动item数，即change数
        int change = (int) (yTotalScroll / itemHeight);
        Log.d("change", "" + change);
        try {
            // 滚动中实际预选中item（即经中间位item）＝滑动前位＋滑动相对位
            preCurrentIndex = initPosition + change % adapter.getItemsCount();
        } catch (ArithmeticException e) {
            Log.e("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配");
        }
        if (!isLoop) {
            // 不循环
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > adapter.getItemsCount() - 1) {
                preCurrentIndex = adapter.getItemsCount() - 1;
            }
        } else {
            // 循环
            if (preCurrentIndex < 0) {
                // 举例：总数5，preCurrentIndex ＝ －1，则preCurrentIndex按循环讲，其实是0上面，即4之位
                preCurrentIndex = adapter.getItemsCount() + preCurrentIndex;
            }
            if (preCurrentIndex > adapter.getItemsCount() - 1) {
                // 同上脑补
                preCurrentIndex = preCurrentIndex - adapter.getItemsCount();
            }
        }
        // 与滚动流畅度有关，总滑动距离与每item高取余，即并非一格格滚动，每item不一定滚到对应Rect中，该item对应格子偏移值
        float itemHeightOffset = (yTotalScroll % itemHeight);
        // 数组中每元素值
        int counter = 0;
        while (counter < itemsVisible) {
            // 索引值，即当前在控件中间item看作数据源中间，算出相对源数据源index值
            int index = preCurrentIndex - (itemsVisible / 2 - counter);
            // 判循环否，循环数据源则用相对循环position获对应item值；非循环则超数据源范围用""空白字符串填充，在界面形成空白无数据item项
            if (isLoop) {
                index = getLoopMappingIndex(index);
                visible[counter] = adapter.getItem(index);
            } else if (index < 0) {
                visible[counter] = "";
            } else if (index > adapter.getItemsCount() - 1) {
                visible[counter] = "";
            } else {
                visible[counter] = adapter.getItem(index);
            }
            counter++;
        }
        // 绘中间两横线
        if (dividerType == DividerType.WRAP) {
            // 横线长仅包内容
            float xStart;
            float xEnd;
            if (TextUtils.isEmpty(label)) {
                // 隐Label
                xStart = ((measuredWidth - maxTextWidth) >> 1) - 12;
            } else {
                xStart = ((measuredWidth - maxTextWidth) >> 2) - 12;
            }
            if (xStart <= 0) {
                // 超WheelView边缘
                xStart = 10;
            }
            xEnd = measuredWidth - xStart;
            canvas.drawLine(xStart, yFirstLine, xEnd, yFirstLine, paintIndicator);
            canvas.drawLine(xStart, ySecondLine, xEnd, ySecondLine, paintIndicator);
        } else {
            canvas.drawLine(0.0F, yFirstLine, measuredWidth, yFirstLine, paintIndicator);
            canvas.drawLine(0.0F, ySecondLine, measuredWidth, ySecondLine, paintIndicator);
        }
        // 仅显选中项Label文本模式且Label文本非空，则绘制
        if (!TextUtils.isEmpty(label) && isCenterLabel) {
            // 绘文本，靠右并留空隙
            int drawRightContentStart = measuredWidth - getTextWidth(paintCenterText, label);
            canvas.drawText(label, drawRightContentStart - centerContentOffset, yCenter, paintCenterText);
        }
        counter = 0;
        while (counter < itemsVisible) {
            canvas.save();
            // 弧长L=itemHeight*counter-itemHeightOffset
            // 弧度α=L/r(弧长/半径)[0,π]
            double radian = ((itemHeight * counter - itemHeightOffset)) / radius;
            // 弧度转角度（把半圆以Y轴为轴心向右转90度，使其处第一及第四象限）
            // angle[-90°,90°]
            // item第一项，从90度开始，逐减-90度
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            // 算取值或有细微偏差（保负90°到90°外不绘）
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                // 据当前角度算偏差系数，用以绘时控文本水平移动、透明度、倾斜度
                float offsetCoefficient = (float) Math.pow(Math.abs(angle) / 90f, 2.2);
                // 内容
                String contentText;
                // 每项都显Label的模式且item内容非空，Label亦非空
                if (!isCenterLabel && !TextUtils.isEmpty(label) && !TextUtils.isEmpty(getContentText(visible[counter]))) {
                    contentText = getContentText(visible[counter]) + label;
                } else {
                    contentText = getContentText(visible[counter]);
                }
                reMeasureTextSize(contentText);
                // 算开始绘位
                measuredCenterContentStart(contentText);
                measuredOutContentStart(contentText);
                float yTranslate = (float) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                // 据Math.sin(radian)改canvas坐标系原点后缩放画布，缩放文本高，成弧形3D视觉差
                canvas.translate(0.0F, yTranslate);
                /*canvas.scale(1.0F, (float) Math.sin(radian));*/
                if (yTranslate <= yFirstLine && maxTextHeight + yTranslate >= yFirstLine) {
                    // 条目经第一条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, yFirstLine - yTranslate);
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, yFirstLine - yTranslate, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - centerContentOffset, paintCenterText);
                    canvas.restore();
                } else if (yTranslate <= ySecondLine && maxTextHeight + yTranslate >= ySecondLine) {
                    // 条目经第二条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, ySecondLine - yTranslate);
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - centerContentOffset, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, ySecondLine - yTranslate, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (yTranslate >= yFirstLine && maxTextHeight + yTranslate <= ySecondLine) {
                    // 中间条目
                    canvas.clipRect(0, 0, measuredWidth, maxTextHeight);
                    // 文本居中
                    // 圆弧角换算的向下取值致角度细微偏差且画笔基线会偏上，故需修正偏移量
                    float y = maxTextHeight - centerContentOffset;
                    canvas.drawText(contentText, drawCenterContentStart, y, paintCenterText);
                    // 选中项
                    selectedItem = preCurrentIndex - (itemsVisible / 2 - counter);
                } else {
                    // 其它条目
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
                    // 控文本倾斜度
                    float defaultTextTargetSlope = 0.5f;
                    paintOuterText.setTextSkewX((Integer.compare(xOffsetOfText, 0)) * (angle > 0 ? -1 : 1) * defaultTextTargetSlope * offsetCoefficient);
                    // 控透明度
                    paintOuterText.setAlpha((int) ((1 - offsetCoefficient) * 255));
                    // 控文本水平偏移距离
                    canvas.drawText(contentText, drawOutContentStart + xOffsetOfText * offsetCoefficient, maxTextHeight, paintOuterText);
                    canvas.restore();
                }
                canvas.restore();
                paintCenterText.setTextSize(textSize);
            }
            counter++;
        }
    }

    /**
     * reset the size of the text Let it can fully display
     *
     * @param contentText item text content.
     */
    private void reMeasureTextSize(String contentText) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
        int width = rect.width();
        int size = textSize;
        while (width > measuredWidth) {
            size--;
            // 两横线中间文本大小
            paintCenterText.setTextSize(size);
            paintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
            width = rect.width();
        }
        // 两横线外文本大小
        paintOuterText.setTextSize(size);
    }

    /**
     * 递归算对应index
     *
     * @param index 索引
     * @return int
     */
    private int getLoopMappingIndex(int index) {
        if (index < 0) {
            index = index + adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        } else if (index > adapter.getItemsCount() - 1) {
            index = index - adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        }
        return index;
    }

    /**
     * 所显数据源
     *
     * @param item data resource
     * @return 所显字符串
     */
    private String getContentText(Object item) {
        if (item == null) {
            return "";
        } else if (item instanceof IPickerViewData) {
            return ((IPickerViewData) item).getPickerViewText();
        } else if (item instanceof Integer) {
            // 整形则最少留两位数
            return String.format(Locale.getDefault(), "%02d", (int) item);
        }
        return item.toString();
    }

    private void measuredCenterContentStart(String content) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity) {
            /*
              显内容居中
             */
            case Gravity.CENTER:
                if (isOptions || label == null || "".equals(label) || !isCenterLabel) {
                    drawCenterContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                } else {
                    // 仅显中间Label时，时间选择器内容偏左一点，留空间绘单位标签
                    drawCenterContentStart = (int) ((measuredWidth - rect.width()) * 0.25);
                }
                break;
            case Gravity.START:
                drawCenterContentStart = 0;
                break;
            case Gravity.END:
                // 添偏移量
                drawCenterContentStart = measuredWidth - rect.width() - (int) centerContentOffset;
                break;
            default:
                break;
        }
    }

    private void measuredOutContentStart(String content) {
        Rect rect = new Rect();
        paintOuterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity) {
            case Gravity.CENTER:
                if (isOptions || label == null || "".equals(label) || !isCenterLabel) {
                    drawOutContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                } else {
                    // 仅显中间Label时，时间选择器内容偏左一点，留空间绘单位标签
                    drawOutContentStart = (int) ((measuredWidth - rect.width()) * 0.25);
                }
                break;
            case Gravity.START:
                drawOutContentStart = 0;
                break;
            case Gravity.END:
                drawOutContentStart = measuredWidth - rect.width() - (int) centerContentOffset;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * Call this view's OnClickListener, if it is defined.  Performs all normal
     * actions associated with clicking: reporting accessibility event, playing
     * a sound, etc.
     *
     * @return True there was an assigned OnClickListener that was called, false
     * otherwise is returned.
     */
    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        // 超边界滑不再绘UI
        boolean isIgnore = false;
        float top = -initPosition * itemHeight;
        float bottom = (adapter.getItemsCount() - 1 - initPosition) * itemHeight;
        float ratio = 0.25f;
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                yPrevious = event.getRawY();
                break;
            // 滑中
            case MotionEvent.ACTION_MOVE:
                float dy = yPrevious - event.getRawY();
                yPrevious = event.getRawY();
                yTotalScroll = yTotalScroll + dy;
                // 非循环模式边界处理
                if (!isLoop) {
                    if (yTotalScroll - itemHeight * ratio < top || yTotalScroll + itemHeight * ratio > bottom) {
                        // 快滑到边界，设已滑到边界标志
                        yTotalScroll -= dy;
                        isIgnore = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                if (yTotalScroll - itemHeight * ratio < top || yTotalScroll + itemHeight * ratio > bottom) {
                    // 设已滑到边界标志
                    isIgnore = true;
                    break;
                }
                if (!eventConsumed) {
                    // 未消费掉事件
                    // Log.e("eventConsumed", "eventConsumed...");
                    // <弧长计算>
                    // 弧长L=α*R
                    // 反余弦arccos(cosα)=α
                    // 因之前有顺时针偏移90度
                    // 故实际弧度范围α2=π/2-α（α=[0,π]α2=[-π/2,π/2]）
                    // 据正弦余弦转换公式cosα=sin(π/2-α)
                    // 代入得cosα=sin(π/2-α)=sinα2=(R-y)/R
                    // 弧长L=arccos(cosα)*R=arccos((R-y)/R)*R
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    // item0有一半在不可见区域，故需加itemHeight / 2
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);
                    float extraOffset = (yTotalScroll % itemHeight + itemHeight) % itemHeight;
                    // 已滑弧长
                    mOffset = (int) ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset);
                    if ((System.currentTimeMillis() - startTime) > WidgetMagic.LONG_ONE_HUNDRED_TWENTY) {
                        // 处理拖拽事件
                        smoothScroll(ACTION.DRAG);
                    } else {
                        // 处理条目点击事件
                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }
        if (!isIgnore && event.getAction() != MotionEvent.ACTION_DOWN) {
            invalidate();
        }
        return true;
    }

    /**
     * Item数
     *
     * @return item数
     */
    public int getItemsCount() {
        return adapter != null ? adapter.getItemsCount() : 0;
    }

    /**
     * 附加在右边单位字符串
     *
     * @param label 单位
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public void isCenterLabel(boolean isCenterLabel) {
        this.isCenterLabel = isCenterLabel;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public int getTextWidth(Paint paint, String str) {
        // 文本宽
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public void setIsOptions(boolean options) {
        isOptions = options;
    }

    public void setTextColorOut(int textColorOut) {
        if (textColorOut != 0) {
            this.textColorOut = textColorOut;
            paintOuterText.setColor(this.textColorOut);
        }
    }

    public void setTextColorCenter(int textColorCenter) {
        if (textColorCenter != 0) {
            this.textColorCenter = textColorCenter;
            paintCenterText.setColor(this.textColorCenter);
        }
    }

    public void setxOffsetOfText(int xOffsetOfText) {
        this.xOffsetOfText = xOffsetOfText;
        if (xOffsetOfText != 0) {
            paintCenterText.setTextScaleX(1.0f);
        }
    }

    public void setDividerColor(int dividerColor) {
        if (dividerColor != 0) {
            this.dividerColor = dividerColor;
            paintIndicator.setColor(this.dividerColor);
        }
    }

    public void setDividerType(DividerType dividerType) {
        this.dividerType = dividerType;
    }

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier != 0) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
            judgeLineSpace();
        }
    }

    public boolean isLoop() {
        return isLoop;
    }

    public float getyTotalScroll() {
        return yTotalScroll;
    }

    public void setyTotalScroll(float yTotalScroll) {
        this.yTotalScroll = yTotalScroll;
    }

    public float getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(float itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getInitPosition() {
        return initPosition;
    }

    public void setInitPosition(int initPosition) {
        this.initPosition = initPosition;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public enum ACTION {
        // 点滑翔（滑到尽头）、拖拽事件
        CLICK, FLING, DRAG
    }

    public enum DividerType {
        // 分隔线类型
        FILL, WRAP
    }
}
