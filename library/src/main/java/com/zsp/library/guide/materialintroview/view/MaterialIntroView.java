package com.zsp.library.guide.materialintroview.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zsp.library.R;
import com.zsp.library.guide.materialintroview.animation.AnimationFactory;
import com.zsp.library.guide.materialintroview.animation.MaterialIntroListener;
import com.zsp.library.guide.materialintroview.configuration.MaterialIntroConfiguration;
import com.zsp.library.guide.materialintroview.prefs.PreferencesManager;
import com.zsp.library.guide.materialintroview.shape.BaseShape;
import com.zsp.library.guide.materialintroview.shape.Circle;
import com.zsp.library.guide.materialintroview.shape.Focus;
import com.zsp.library.guide.materialintroview.shape.FocusGravity;
import com.zsp.library.guide.materialintroview.shape.Rect;
import com.zsp.library.guide.materialintroview.shape.ShapeType;
import com.zsp.library.guide.materialintroview.target.Target;
import com.zsp.library.guide.materialintroview.target.ViewTarget;
import com.zsp.utilone.density.DensityUtils;

import value.WidgetLibraryMagic;

/**
 * @decs: MaterialIntroView
 * @author: 郑少鹏
 * @date: 2019/9/24 11:53
 */
public class MaterialIntroView extends RelativeLayout {
    /**
     * mask color
     */
    private int maskColor;
    /**
     * MaterialIntroView will start showing after delayMillis seconds passed.
     */
    private long delayMillis;
    /**
     * We don't draw MaterialIntroView until isReady field set to true.
     */
    private boolean isReady;
    /**
     * Show/Dismiss MaterialIntroView with fade in/out animation if this is enabled.
     */
    private boolean isFadeAnimationEnabled;
    /**
     * animation duration
     */
    private long fadeAnimationDuration;
    /**
     * TargetBaseShape focus on target and clear circle to focus.
     */
    private BaseShape targetBaseShape;
    /**
     * focus type
     */
    private Focus focusType;
    /**
     * FocusGravity type
     */
    private FocusGravity focusGravity;
    /**
     * target view
     */
    private Target targetView;
    /**
     * eraser
     */
    private Paint eraser;
    /**
     * Handler will be used to delay MaterialIntroView.
     */
    private Handler handler;
    /**
     * All views will be drawn to this bitmap and canvas then bitmap will be drawn to canvas.
     */
    private Bitmap bitmap;
    private Canvas canvas;
    /**
     * circle padding
     */
    private int padding;
    /**
     * layout width/height
     */
    private int width;
    private int height;
    /**
     * dismiss on touch any position
     */
    private boolean dismissOnTouch;
    /**
     * info dialog view
     */
    private View materialIntroViewRl;
    /**
     * info dialog text
     */
    private TextView materialIntroViewTv;
    /**
     * info dialog text color
     */
    private int colorTextViewInfo;
    /**
     * Info dialog will be shown if this value true.
     */
    private boolean isInfoEnabled;
    /**
     * Dot view will appear center of cleared target area.
     */
    private View dotView;
    /**
     * Dot View will be shown if this is true.
     */
    private boolean isDotViewEnabled;
    /**
     * info dialog icon
     */
    private ImageView materialIntroViewIv;
    /**
     * Image View will be shown if this is true.
     */
    private boolean isImageViewEnabled;
    /**
     * Save/Retrieve status of MaterialIntroView if Intro is already learnt then don't show it again.
     */
    private PreferencesManager preferencesManager;
    /**
     * Check using this Id whether user learned or not.
     */
    private String materialIntroViewId;
    /**
     * When layout completed, we set this true Otherwise onGlobalLayoutListener stuck on loop.
     */
    private boolean isLayoutCompleted;
    /**
     * Notify user when MaterialIntroView is dismissed.
     */
    private MaterialIntroListener materialIntroListener;
    /**
     * Perform click operation to target if this is true.
     */
    private boolean isPerformClick;
    /**
     * Disallow this MaterialIntroView from showing up more than once at a time.
     */
    private boolean isIdempotent;
    /**
     * baseShape of target
     */
    private ShapeType shapeType;
    /**
     * use custom shape
     */
    private boolean usesCustomShape = false;

    public MaterialIntroView(Context context) {
        super(context);
        init(context);
    }

    public MaterialIntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MaterialIntroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public static void removeOnGlobalLayoutListener(@NonNull View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

    @SuppressLint("InflateParams")
    private void init(@NonNull Context context) {
        setWillNotDraw(false);
        setVisibility(INVISIBLE);
        // set default values
        maskColor = 0x70000000;
        delayMillis = 0L;
        fadeAnimationDuration = 700L;
        padding = WidgetLibraryMagic.INT_TEN;
        colorTextViewInfo = 0xFF000000;
        focusType = Focus.ALL;
        focusGravity = FocusGravity.CENTER;
        shapeType = ShapeType.CIRCLE;
        isReady = false;
        isFadeAnimationEnabled = true;
        dismissOnTouch = false;
        isLayoutCompleted = false;
        isInfoEnabled = false;
        isDotViewEnabled = false;
        isPerformClick = false;
        isImageViewEnabled = true;
        isIdempotent = false;
        // initialize objects
        handler = new Handler(context.getMainLooper());
        preferencesManager = new PreferencesManager(context);
        eraser = new Paint();
        eraser.setColor(0xFFFFFFFF);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setFlags(Paint.ANTI_ALIAS_FLAG);
        View layoutInfo = LayoutInflater.from(getContext()).inflate(R.layout.material_intro_view, null);
        materialIntroViewRl = layoutInfo.findViewById(R.id.materialIntroViewRl);
        materialIntroViewTv = layoutInfo.findViewById(R.id.materialIntroViewTv);
        materialIntroViewTv.setTextColor(colorTextViewInfo);
        materialIntroViewIv = layoutInfo.findViewById(R.id.materialIntroViewIv);
        dotView = LayoutInflater.from(getContext()).inflate(R.layout.material_intro_view_dot, null);
        dotView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                targetBaseShape.reCalculateAll();
                if (targetBaseShape != null && targetBaseShape.getPoint().y != 0 && !isLayoutCompleted) {
                    if (isInfoEnabled) {
                        setInfoLayout();
                    }
                    if (isDotViewEnabled) {
                        setDotViewLayout();
                    }
                    removeOnGlobalLayoutListener(MaterialIntroView.this, this);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isReady) {
            return;
        }
        if (bitmap == null || canvas == null) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.canvas = new Canvas(bitmap);
        }
        // draw mask
        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        this.canvas.drawColor(maskColor);
        // clear focus area
        targetBaseShape.draw(this.canvas, eraser, padding);
        if (canvas != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        }
    }

    /**
     * Perform click operation when user touches on target circle.
     *
     * @param event 触摸事件
     * @return boolean
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float xEvent = event.getX();
        float yEvent = event.getY();
        boolean isTouchOnFocus = targetBaseShape.isTouchOnFocus(xEvent, yEvent);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchOnFocus && isPerformClick) {
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (isTouchOnFocus || dismissOnTouch) {
                    dismiss();
                }
                if (isTouchOnFocus && isPerformClick) {
                    targetView.getView().performClick();
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                    targetView.getView().setPressed(false);
                    targetView.getView().invalidate();
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Shows material view with fade in animation
     *
     * @param activity Activity
     */
    private void show(Activity activity) {
        if (preferencesManager.isDisplayed(materialIntroViewId)) {
            return;
        }
        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);
        setReady(true);
        handler.postDelayed(() -> {
            if (isFadeAnimationEnabled) {
                AnimationFactory.animateFadeIn(MaterialIntroView.this, fadeAnimationDuration, () -> setVisibility(VISIBLE));
            } else {
                setVisibility(VISIBLE);
            }
        }, delayMillis);
        if (isIdempotent) {
            preferencesManager.setDisplayed(materialIntroViewId);
        }
    }

    /**
     * Dismiss Material Intro View.
     */
    public void dismiss() {
        if (!isIdempotent) {
            preferencesManager.setDisplayed(materialIntroViewId);
        }
        AnimationFactory.animateFadeOut(this, fadeAnimationDuration, () -> {
            setVisibility(GONE);
            removeMaterialView();
            if (materialIntroListener != null) {
                materialIntroListener.onUserClick(materialIntroViewId);
            }
        });
    }

    private void removeMaterialView() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    /**
     * Locate info card view above/below the circle.
     * <p>
     * If circle's Y coordiante is bigger than Y coordinate of root view, then locate cardView above the circle.
     * Otherwise locate below.
     */
    private void setInfoLayout() {
        handler.post(() -> {
            isLayoutCompleted = true;
            if (materialIntroViewRl.getParent() != null) {
                ((ViewGroup) materialIntroViewRl.getParent()).removeView(materialIntroViewRl);
            }
            LayoutParams infoDialogParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            if (targetBaseShape.getPoint().y < height / WidgetLibraryMagic.INT_TWO) {
                ((RelativeLayout) materialIntroViewRl).setGravity(Gravity.TOP);
                infoDialogParams.setMargins(
                        0,
                        targetBaseShape.getPoint().y + targetBaseShape.getHeight() / 2,
                        0,
                        0);
            } else {
                ((RelativeLayout) materialIntroViewRl).setGravity(Gravity.BOTTOM);
                infoDialogParams.setMargins(
                        0,
                        0,
                        0,
                        height - (targetBaseShape.getPoint().y + targetBaseShape.getHeight() / 2) + 2 * targetBaseShape.getHeight() / 2);
            }
            materialIntroViewRl.setLayoutParams(infoDialogParams);
            materialIntroViewRl.postInvalidate();
            addView(materialIntroViewRl);
            if (!isImageViewEnabled) {
                materialIntroViewIv.setVisibility(GONE);
            }
            materialIntroViewRl.setVisibility(VISIBLE);
        });
    }

    private void setDotViewLayout() {
        handler.post(() -> {
            if (dotView.getParent() != null) {
                ((ViewGroup) dotView.getParent()).removeView(dotView);
            }
            LayoutParams dotViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dotViewLayoutParams.height = DensityUtils.dipToPxByInt(56);
            dotViewLayoutParams.width = DensityUtils.dipToPxByInt(56);
            dotViewLayoutParams.setMargins(
                    targetBaseShape.getPoint().x - (dotViewLayoutParams.width / 2),
                    targetBaseShape.getPoint().y - (dotViewLayoutParams.height / 2),
                    0,
                    0);
            dotView.setLayoutParams(dotViewLayoutParams);
            dotView.postInvalidate();
            addView(dotView);
            dotView.setVisibility(VISIBLE);
            AnimationFactory.performAnimation(dotView);
        });
    }

    /**
     * SETTERS
     */
    private void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    private void setDelay(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    private void enableFadeAnimation(boolean isFadeAnimationEnabled) {
        this.isFadeAnimationEnabled = isFadeAnimationEnabled;
    }

    private void setShapeType(ShapeType shape) {
        this.shapeType = shape;
    }

    private void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    private void setTarget(Target target) {
        targetView = target;
    }

    private void setFocusType(Focus focusType) {
        this.focusType = focusType;
    }

    private void setShape(BaseShape baseShape) {
        this.targetBaseShape = baseShape;
    }

    private void setPadding(int padding) {
        this.padding = padding;
    }

    private void setDismissOnTouch(boolean dismissOnTouch) {
        this.dismissOnTouch = dismissOnTouch;
    }

    private void setFocusGravity(FocusGravity focusGravity) {
        this.focusGravity = focusGravity;
    }

    private void setColorTextViewInfo(int colorTextViewInfo) {
        this.colorTextViewInfo = colorTextViewInfo;
        materialIntroViewTv.setTextColor(this.colorTextViewInfo);
    }

    private void setMaterialIntroViewTv(CharSequence materialIntroViewTv) {
        this.materialIntroViewTv.setText(materialIntroViewTv);
    }

    private void setTextViewInfoSize(int textViewInfoSize) {
        this.materialIntroViewTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textViewInfoSize);
    }

    private void enableInfoDialog(boolean isInfoEnabled) {
        this.isInfoEnabled = isInfoEnabled;
    }

    private void enableImageViewIcon(boolean isImageViewEnabled) {
        this.isImageViewEnabled = isImageViewEnabled;
    }

    private void setIdempotent(boolean idempotent) {
        this.isIdempotent = idempotent;
    }

    private void enableDotView(boolean isDotViewEnabled) {
        this.isDotViewEnabled = isDotViewEnabled;
    }

    public void setConfiguration(MaterialIntroConfiguration configuration) {
        if (configuration != null) {
            this.maskColor = configuration.getMaskColor();
            this.delayMillis = configuration.getDelayMillis();
            this.isFadeAnimationEnabled = configuration.isFadeAnimationEnabled();
            this.colorTextViewInfo = configuration.getColorTextViewInfo();
            this.isDotViewEnabled = configuration.isDotViewEnabled();
            this.dismissOnTouch = configuration.isDismissOnTouch();
            this.colorTextViewInfo = configuration.getColorTextViewInfo();
            this.focusType = configuration.getFocusType();
            this.focusGravity = configuration.getFocusGravity();
        }
    }

    private void setUsageId(String materialIntroViewId) {
        this.materialIntroViewId = materialIntroViewId;
    }

    private void setListener(MaterialIntroListener materialIntroListener) {
        this.materialIntroListener = materialIntroListener;
    }

    private void setPerformClick(boolean isPerformClick) {
        this.isPerformClick = isPerformClick;
    }

    /**
     * Builder
     * <p>
     * 建造者模式。
     */
    public static class Builder {
        private final Activity activity;
        private final MaterialIntroView materialIntroView;

        public Builder(Activity activity) {
            this.activity = activity;
            materialIntroView = new MaterialIntroView(activity);
        }

        public Builder setMaskColor(int maskColor) {
            materialIntroView.setMaskColor(maskColor);
            return this;
        }

        public Builder setDelayMillis(int delayMillis) {
            materialIntroView.setDelay(delayMillis);
            return this;
        }

        public Builder enableFadeAnimation(boolean isFadeAnimationEnabled) {
            materialIntroView.enableFadeAnimation(isFadeAnimationEnabled);
            return this;
        }

        public Builder setShape(ShapeType shape) {
            materialIntroView.setShapeType(shape);
            return this;
        }

        public Builder setFocusType(Focus focusType) {
            materialIntroView.setFocusType(focusType);
            return this;
        }

        public Builder setFocusGravity(FocusGravity focusGravity) {
            materialIntroView.setFocusGravity(focusGravity);
            return this;
        }

        public Builder setTarget(View view) {
            materialIntroView.setTarget(new ViewTarget(view));
            return this;
        }

        public Builder setTargetPadding(int padding) {
            materialIntroView.setPadding(padding);
            return this;
        }

        public Builder setTextColor(int textColor) {
            materialIntroView.setColorTextViewInfo(textColor);
            return this;
        }

        public Builder setInfoText(CharSequence infoText) {
            materialIntroView.enableInfoDialog(true);
            materialIntroView.setMaterialIntroViewTv(infoText);
            return this;
        }

        public Builder setInfoTextSize(int textSize) {
            materialIntroView.setTextViewInfoSize(textSize);
            return this;
        }

        public Builder dismissOnTouch(boolean dismissOnTouch) {
            materialIntroView.setDismissOnTouch(dismissOnTouch);
            return this;
        }

        public Builder setUsageId(String materialIntroViewId) {
            materialIntroView.setUsageId(materialIntroViewId);
            return this;
        }

        public Builder enableDotAnimation(boolean isDotAnimationEnabled) {
            materialIntroView.enableDotView(isDotAnimationEnabled);
            return this;
        }

        public Builder enableIcon(boolean isImageViewIconEnabled) {
            materialIntroView.enableImageViewIcon(isImageViewIconEnabled);
            return this;
        }

        public Builder setIdempotent(boolean idempotent) {
            materialIntroView.setIdempotent(idempotent);
            return this;
        }

        public Builder setConfiguration(MaterialIntroConfiguration configuration) {
            materialIntroView.setConfiguration(configuration);
            return this;
        }

        public Builder setListener(MaterialIntroListener materialIntroListener) {
            materialIntroView.setListener(materialIntroListener);
            return this;
        }

        public Builder setCustomShape(BaseShape baseShape) {
            materialIntroView.usesCustomShape = true;
            materialIntroView.setShape(baseShape);
            return this;
        }

        public Builder performClick(boolean isPerformClick) {
            materialIntroView.setPerformClick(isPerformClick);
            return this;
        }

        public MaterialIntroView build() {
            if (materialIntroView.usesCustomShape) {
                return materialIntroView;
            }
            // No custom baseShape supplied, build our own.
            BaseShape baseShape;
            if (materialIntroView.shapeType == ShapeType.CIRCLE) {
                baseShape = new Circle(
                        materialIntroView.targetView,
                        materialIntroView.focusType,
                        materialIntroView.focusGravity,
                        materialIntroView.padding);
            } else {
                baseShape = new Rect(
                        materialIntroView.targetView,
                        materialIntroView.focusType,
                        materialIntroView.focusGravity,
                        materialIntroView.padding);
            }
            materialIntroView.setShape(baseShape);
            return materialIntroView;
        }

        public MaterialIntroView show() {
            build().show(activity);
            return materialIntroView;
        }
    }
}
