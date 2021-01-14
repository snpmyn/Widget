package com.zsp.library.layout.ripple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.zsp.library.R;

import java.math.BigDecimal;

import value.WidgetLibraryMagic;

/**
 * @decs: RippleLayout
 * Custom layout that allows to use ripple UI pattern above API 21.
 * @author: 郑少鹏
 * @date: 2019/8/26 17:50
 */
public class RippleLayout extends RelativeLayout {
    private int width;
    private int height;
    private int frameRate = 10;
    private int rippleDuration = 400;
    private int rippleAlpha = 90;
    private Handler canvasHandler;
    private float radiusMax = 0;
    private boolean animationRunning = false;
    private int timer = 0;
    private int timerEmpty = 0;
    private int durationEmpty = -1;
    private float x = -1;
    private float y = -1;
    private int zoomDuration;
    private float zoomScale;
    private ScaleAnimation scaleAnimation;
    private Boolean hasToZoom;
    private Boolean isCentered;
    private Integer rippleType;
    private Paint paint;
    private Bitmap originBitmap;
    private int rippleColor;
    private int ripplePadding;
    private GestureDetector gestureDetector;
    private final Runnable runnable = this::invalidate;
    private OnRippleCompleteListener onCompletionListener;

    public RippleLayout(Context context) {
        super(context);
    }

    public RippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * Method that initializes all fields and sets listeners.
     *
     * @param context context used to create this view
     * @param attrs   attribute used to initialize fields
     */
    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleLayout);
        rippleColor = typedArray.getColor(R.styleable.RippleLayout_RippleLayoutColor, ContextCompat.getColor(context, R.color.white));
        rippleType = typedArray.getInt(R.styleable.RippleLayout_RippleLayoutType, 0);
        hasToZoom = typedArray.getBoolean(R.styleable.RippleLayout_RippleLayoutZoom, false);
        isCentered = typedArray.getBoolean(R.styleable.RippleLayout_RippleLayoutCentered, false);
        rippleDuration = typedArray.getInteger(R.styleable.RippleLayout_RippleLayoutRippleDuration, rippleDuration);
        frameRate = typedArray.getInteger(R.styleable.RippleLayout_RippleLayoutFrameRate, frameRate);
        rippleAlpha = typedArray.getInteger(R.styleable.RippleLayout_RippleLayoutAlpha, rippleAlpha);
        ripplePadding = typedArray.getDimensionPixelSize(R.styleable.RippleLayout_RippleLayoutRipplePadding, 0);
        canvasHandler = new Handler(context.getMainLooper());
        zoomScale = typedArray.getFloat(R.styleable.RippleLayout_RippleLayoutZoomScale, 1.03f);
        zoomDuration = typedArray.getInt(R.styleable.RippleLayout_RippleLayoutZoomDuration, 200);
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(rippleColor);
        paint.setAlpha(rippleAlpha);
        this.setWillNotDraw(false);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent event) {
                super.onLongPress(event);
                animateRipple(event);
                sendClickEvent(true);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (animationRunning) {
            canvas.save();
            if (rippleDuration <= timer * frameRate) {
                animationRunning = false;
                timer = 0;
                durationEmpty = -1;
                timerEmpty = 0;
                // There is problem on Android M where canvas.restore() seems to be called automatically
                // For now, don't call canvas.restore() manually on Android M (API 23)
                if (Build.VERSION.SDK_INT != WidgetLibraryMagic.INT_TWENTY_THREE) {
                    canvas.restore();
                }
                invalidate();
                if (onCompletionListener != null) {
                    onCompletionListener.onComplete(this);
                }
                return;
            } else {
                canvasHandler.postDelayed(runnable, frameRate);
            }
            if (timer == 0) {
                canvas.save();
            }
            canvas.drawCircle(x, y, (radiusMax * (((float) timer * frameRate) / rippleDuration)), paint);
            paint.setColor(Color.parseColor("#ffff4444"));
            if (rippleType == 1 && originBitmap != null && (((float) timer * frameRate) / rippleDuration) > WidgetLibraryMagic.FLOAT_ZERO_DOT_FOUR) {
                if (durationEmpty == -1) {
                    durationEmpty = rippleDuration - timer * frameRate;
                }
                timerEmpty++;
                final Bitmap tmpBitmap = getCircleBitmap((int) ((radiusMax) * (((float) timerEmpty * frameRate) / (durationEmpty))));
                canvas.drawBitmap(tmpBitmap, 0, 0, paint);
                tmpBitmap.recycle();
            }
            paint.setColor(rippleColor);
            if (rippleType == 1) {
                if ((((float) timer * frameRate) / rippleDuration) > WidgetLibraryMagic.FLOAT_ZERO_DOT_SIX) {
                    paint.setAlpha((int) (rippleAlpha - ((rippleAlpha) * (((float) timerEmpty * frameRate) / (durationEmpty)))));
                } else {
                    paint.setAlpha(rippleAlpha);
                }
            } else {
                paint.setAlpha((int) (rippleAlpha - ((rippleAlpha) * (((float) timer * frameRate) / rippleDuration))));
            }
            timer++;
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.width = width;
        this.height = height;
        scaleAnimation = new ScaleAnimation(1.0f, zoomScale, 1.0f, zoomScale, new BigDecimal(width / 2).floatValue(), new BigDecimal(height / 2).floatValue());
        scaleAnimation.setDuration(zoomDuration);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(1);
    }

    /**
     * Launch ripple animation for the current view with a MotionEvent.
     *
     * @param event MotionEvent registered by the ripple gesture listener
     */
    public void animateRipple(@NonNull MotionEvent event) {
        createAnimation(event.getX(), event.getY());
    }

    /**
     * Launch ripple animation for the current view centered at x and y position.
     *
     * @param x horizontal position of the ripple center
     * @param y vertical position of the ripple center
     */
    public void animateRipple(final float x, final float y) {
        createAnimation(x, y);
    }

    /**
     * Create ripple animation centered at x, y.
     *
     * @param x horizontal position of the ripple center
     * @param y vertical position of the ripple center
     */
    private void createAnimation(final float x, final float y) {
        if (this.isEnabled() && !animationRunning) {
            if (hasToZoom) {
                this.startAnimation(scaleAnimation);
            }
            radiusMax = Math.max(width, height);
            if (rippleType != WidgetLibraryMagic.INT_TWO) {
                radiusMax /= 2;
            }
            radiusMax -= ripplePadding;
            if (isCentered || rippleType == 1) {
                this.x = new BigDecimal(getMeasuredWidth() / 2).floatValue();
                this.y = new BigDecimal(getMeasuredHeight() / 2).floatValue();
            } else {
                this.x = x;
                this.y = y;
            }
            animationRunning = true;
            if (rippleType == 1 && originBitmap == null) {
                originBitmap = getDrawingCache(true);
            }
            invalidate();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            animateRipple(event);
            sendClickEvent(false);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        this.onTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }

    /**
     * Send a click event if parent view is a ListView instance.
     *
     * @param isLongClick Is the event a long click?
     */
    private void sendClickEvent(final Boolean isLongClick) {
        if (getParent() instanceof AdapterView) {
            final AdapterView adapterView = (AdapterView) getParent();
            final int position = adapterView.getPositionForView(this);
            final long id = adapterView.getItemIdAtPosition(position);
            if (isLongClick) {
                if (adapterView.getOnItemLongClickListener() != null) {
                    adapterView.getOnItemLongClickListener().onItemLongClick(adapterView, this, position, id);
                }
            } else {
                if (adapterView.getOnItemClickListener() != null) {
                    adapterView.getOnItemClickListener().onItemClick(adapterView, this, position, id);
                }
            }
        }
    }

    private Bitmap getCircleBitmap(final int radius) {
        final Bitmap output = Bitmap.createBitmap(originBitmap.getWidth(), originBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius));
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(x, y, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(originBitmap, rect, rect, paint);
        return output;
    }

    /**
     * Set Ripple color, default is #FFFFFF.
     *
     * @param rippleColor new color resource
     */
    public void setRippleColor(int rippleColor) {
        this.rippleColor = ContextCompat.getColor(getContext(), rippleColor);
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public RippleType getRippleType() {
        return RippleType.values()[rippleType];
    }

    /**
     * Set ripple type, default is RippleType.SIMPLE.
     *
     * @param rippleType new ripple type for next animation
     */
    public void setRippleType(@NonNull final RippleType rippleType) {
        this.rippleType = rippleType.ordinal();
    }

    public Boolean isCentered() {
        return isCentered;
    }

    /**
     * Set if ripple animation has to be centered in its parent view or not, default is False.
     *
     * @param isCentered Boolean
     */
    public void setCentered(final Boolean isCentered) {
        this.isCentered = isCentered;
    }

    public int getRipplePadding() {
        return ripplePadding;
    }

    /**
     * Set ripple padding if you want to avoid some graphic glitch.
     *
     * @param ripplePadding new ripple padding in pixel, default is 0px
     */
    public void setRipplePadding(int ripplePadding) {
        this.ripplePadding = ripplePadding;
    }

    public Boolean isZooming() {
        return hasToZoom;
    }

    /**
     * At the end of ripple effect, the child views has to zoom.
     *
     * @param hasToZoom Do the child views have to zoom? default is false
     */
    public void setZooming(Boolean hasToZoom) {
        this.hasToZoom = hasToZoom;
    }

    public float getZoomScale() {
        return zoomScale;
    }

    /**
     * Scale of the end animation.
     *
     * @param zoomScale value of scale animation, default is 1.03f
     */
    public void setZoomScale(float zoomScale) {
        this.zoomScale = zoomScale;
    }

    public int getZoomDuration() {
        return zoomDuration;
    }

    /**
     * Duration of the ending animation in ms.
     *
     * @param zoomDuration duration, default is 200ms
     */
    public void setZoomDuration(int zoomDuration) {
        this.zoomDuration = zoomDuration;
    }

    public int getRippleDuration() {
        return rippleDuration;
    }

    /**
     * Duration of the ripple animation in ms
     *
     * @param rippleDuration duration, default is 400ms
     */
    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public int getFrameRate() {
        return frameRate;
    }

    /**
     * Set frame rate for ripple animation.
     *
     * @param frameRate new frame rate value, default is 10
     */
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getRippleAlpha() {
        return rippleAlpha;
    }

    /**
     * Set alpha for ripple effect color.
     *
     * @param rippleAlpha alpha value between 0 and 255, default is 90
     */
    public void setRippleAlpha(int rippleAlpha) {
        this.rippleAlpha = rippleAlpha;
    }

    public void setOnRippleCompleteListener(OnRippleCompleteListener listener) {
        this.onCompletionListener = listener;
    }

    /**
     * Defines a callback called at the end of the ripple effect.
     */
    public interface OnRippleCompleteListener {
        /**
         * 完成
         *
         * @param rippleLayout 波纹视图
         */
        void onComplete(RippleLayout rippleLayout);
    }

    public enum RippleType {
        /**
         * 简单
         */
        SIMPLE(0),
        /**
         * 双
         */
        DOUBLE(1),
        /**
         * 矩形
         */
        RECTANGLE(2);
        int type;

        RippleType(int type) {
            this.type = type;
        }
    }
}
