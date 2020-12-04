package com.zsp.library.guide.guideview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.zsp.library.R;

import timber.log.Timber;

/**
 * @decs: 引导视图
 * @author: 郑少鹏
 * @date: 2019/9/11 16:29
 */
public class GuideView extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    private final String TAG = getClass().getSimpleName();
    /**
     * 上下文
     */
    private final Context context;
    /**
     * 目标图前缀（SHOW_GUIDE_PREFIX + targetView.getId()作为存于SP文件key）
     */
    private static final String SHOW_GUIDE_PREFIX = "show_guide_on_view";
    private boolean firstShow = true;
    private boolean onlyOnce;
    boolean needDraw = true;
    /**
     * 引导图偏移量
     */
    private int xOffset, yOffset;
    /**
     * 目标图外切圆半径
     */
    private int radius;
    /**
     * 显提示信息的View
     */
    private View targetView;
    /**
     * 自定View
     */
    private View customGuideView;
    /**
     * 透明圆形画笔
     */
    private Paint mCirclePaint;
    /**
     * 目标图已测否
     */
    private boolean isMeasured;
    /**
     * 目标图圆心
     */
    private int[] center;
    /**
     * 绘图层叠模式
     */
    private PorterDuffXfermode porterDuffXfermode;
    /**
     * 绘制前景Bitmap
     */
    private Bitmap bitmap;
    /**
     * 背景色和透明度（格式#aarrggbb）
     */
    private int backgroundColor;
    /**
     * 画布（绘Bitmap）
     */
    private Canvas temp;
    /**
     * 相对目标图位（target方向）
     */
    private Direction direction;
    /**
     * 形状
     */
    private MyShape myShape;
    /**
     * 目标图左上角坐标
     */
    private int[] location;
    private boolean onClickExit;
    private OnClickCallback onclickListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public GuideView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void restoreState() {
        Timber.d("restoreState");
        xOffset = yOffset = 0;
        radius = 0;
        mCirclePaint = null;
        isMeasured = false;
        center = null;
        porterDuffXfermode = null;
        bitmap = null;
        needDraw = true;
        temp = null;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setShape(MyShape shape) {
        this.myShape = shape;
    }

    /**
     * TextView引导图
     *
     * @return TextView
     */
    public static TextView guideTextView(Context context, int hintRes, int colorRes) {
        TextView textView = new TextView(context);
        textView.setText(hintRes);
        textView.setTextColor(colorRes);
        return textView;
    }

    public void setBgColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setCustomGuideView(View customGuideView) {
        this.customGuideView = customGuideView;
        if (!firstShow) {
            restoreState();
        }
    }

    private void init() {

    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }

    public void showOnce() {
        if (targetView != null) {
            onlyOnce = true;
        }
    }

    private void saveShowState() {
        if (targetView != null) {
            context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().putBoolean(generateUniqueId(targetView), true).apply();
        }
    }

    private String generateUniqueId(View v) {
        return SHOW_GUIDE_PREFIX + v.getId();
    }

    public int[] getCenter() {
        return center;
    }

    public void setCenter(int[] center) {
        this.center = center;
    }

    public void hide() {
        Timber.d("hide");
        if (customGuideView != null) {
            targetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            this.removeAllViews();
            ((FrameLayout) ((Activity) context).getWindow().getDecorView()).removeView(this);
            restoreState();
        }
    }

    public boolean hasShown() {
        return targetView != null && context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getBoolean(generateUniqueId(targetView), false);
    }

    /**
     * 目标图下添提示文本
     * <p>
     * 屏幕窗口添蒙层，蒙层绘总背景和透明圆形，圆形下绘说明文本。
     */
    private void createGuideView() {
        Timber.d("createGuideView");
        // Tips布局参数
        LayoutParams guideViewParams;
        guideViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        guideViewParams.setMargins(0, center[1] + radius + 10, 0, 0);
        if (customGuideView != null) {
            if (direction != null) {
                int width = this.getWidth();
                int height = this.getHeight();
                int left = center[0] - radius;
                int right = center[0] + radius;
                int top = center[1] - radius;
                int bottom = center[1] + radius;
                switch (direction) {
                    case TOP:
                        this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                        guideViewParams.setMargins(xOffset, yOffset - height + top, -xOffset, height - top - yOffset);
                        break;
                    case LEFT:
                        this.setGravity(Gravity.END);
                        guideViewParams.setMargins(xOffset - width + left, top + yOffset, width - left - xOffset, -top - yOffset);
                        break;
                    case BOTTOM:
                        this.setGravity(Gravity.CENTER_HORIZONTAL);
                        guideViewParams.setMargins(xOffset, bottom + yOffset, -xOffset, -bottom - yOffset);
                        break;
                    case RIGHT:
                        guideViewParams.setMargins(right + xOffset, top + yOffset, -right - xOffset, -top - yOffset);
                        break;
                    case LEFT_TOP:
                        this.setGravity(Gravity.END | Gravity.BOTTOM);
                        guideViewParams.setMargins(xOffset - width + left, yOffset - height + top, width - left - xOffset, height - top - yOffset);
                        break;
                    case LEFT_BOTTOM:
                        this.setGravity(Gravity.END);
                        guideViewParams.setMargins(xOffset - width + left, bottom + yOffset, width - left - xOffset, -bottom - yOffset);
                        break;
                    case RIGHT_TOP:
                        this.setGravity(Gravity.BOTTOM);
                        guideViewParams.setMargins(right + xOffset, yOffset - height + top, -right - xOffset, height - top - yOffset);
                        break;
                    case RIGHT_BOTTOM:
                        guideViewParams.setMargins(right + xOffset, bottom + yOffset, -right - xOffset, -top - yOffset);
                        break;
                    default:
                        break;
                }
            } else {
                guideViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                guideViewParams.setMargins(xOffset, yOffset, -xOffset, -yOffset);
            }
            this.addView(customGuideView, guideViewParams);
        }
    }

    /**
     * 目标图宽高
     * <p>
     * 未测返-1、-1。
     *
     * @return 宽高
     */
    private int[] getTargetViewSize() {
        int[] location = {-1, -1};
        if (isMeasured) {
            location[0] = targetView.getWidth();
            location[1] = targetView.getHeight();
        }
        return location;
    }

    /**
     * 目标图半径
     *
     * @return 半径
     */
    private int getTargetViewRadius() {
        if (isMeasured) {
            int[] size = getTargetViewSize();
            int x = size[0];
            int y = size[1];

            return (int) (Math.sqrt(x * x + y * y) / 2);
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Timber.d("onDraw");
        if (!isMeasured) {
            return;
        }
        if (targetView == null) {
            return;
        }
        drawBackground(canvas);
    }

    private void drawBackground(Canvas canvas) {
        Timber.d("drawBackground");
        needDraw = false;
        // 绘Bitmap后绘Bitmap至屏幕
        bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        temp = new Canvas(bitmap);
        // 背景画笔
        Paint bgPaint = new Paint();
        if (backgroundColor != 0) {
            bgPaint.setColor(backgroundColor);
        } else {
            bgPaint.setColor(ContextCompat.getColor(context, R.color.blackCC));
        }
        // 绘屏幕背景
        temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), bgPaint);
        // 目标图透明圆形画笔
        if (mCirclePaint == null) {
            mCirclePaint = new Paint();
        }
        // 或CLEAR
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        mCirclePaint.setXfermode(porterDuffXfermode);
        mCirclePaint.setAntiAlias(true);
        if (myShape != null) {
            RectF oval = new RectF();
            switch (myShape) {
                /*
                  圆形
                 */
                case CIRCULAR:
                    temp.drawCircle(center[0], center[1], radius, mCirclePaint);
                    break;
                /*
                  椭圆
                 */
                case ELLIPSE:
                    // RectF对象
                    // 左、上、右、下
                    oval.left = center[0] - 150;
                    oval.top = center[1] - 50;
                    oval.right = center[0] + 150;
                    oval.bottom = center[1] + 50;
                    // 绘椭圆
                    temp.drawOval(oval, mCirclePaint);
                    break;
                /*
                  圆角矩形
                 */
                case RECTANGULAR:
                    // RectF对象
                    // 左、上、右、下
                    oval.left = center[0] - 150;
                    oval.top = center[1] - 50;
                    oval.right = center[0] + 150;
                    oval.bottom = center[1] + 50;
                    // 绘圆角矩形
                    temp.drawRoundRect(oval, radius, radius, mCirclePaint);
                    break;
                default:
                    break;
            }
        } else {
            // 绘圆形
            temp.drawCircle(center[0], center[1], radius, mCirclePaint);
        }
        // 绘至屏幕
        canvas.drawBitmap(bitmap, 0, 0, bgPaint);
        bitmap.recycle();
    }

    public void setOnClickExit(boolean onClickExit) {
        this.onClickExit = onClickExit;
    }

    public void setOnclickListener(OnClickCallback onclickListener) {
        this.onclickListener = onclickListener;
    }

    private void setClickInfo() {
        final boolean exit = onClickExit;
        setOnClickListener(v -> {
            if (onclickListener != null) {
                onclickListener.onClickedGuideView();
            }
            if (exit) {
                hide();
            }
        });
    }

    @Override
    public void onGlobalLayout() {
        if (isMeasured) {
            return;
        }
        if (targetView.getHeight() > 0 && targetView.getWidth() > 0) {
            isMeasured = true;
        }
        // 目标图中心坐标
        if (center == null) {
            // 右上角坐标
            location = new int[2];
            targetView.getLocationInWindow(location);
            center = new int[2];
            // 中心坐标
            center[0] = location[0] + targetView.getWidth() / 2;
            center[1] = location[1] + targetView.getHeight() / 2;
        }
        // 目标图外切圆半径
        if (radius == 0) {
            radius = getTargetViewRadius();
        }
        // 添GuideView
        createGuideView();
    }

    /**
     * 引导图相对目标图方位
     * <p>
     * 不设默目标图下方。
     */
    public enum Direction {
        // 左、上、右、下、左上、左下、右上、右下
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM
    }

    /**
     * 目标图状。
     * <p>
     * 圆形、椭圆、圆角矩形。
     * 可设圆角大小，不设默圆形。
     */
    public enum MyShape {
        // 圆形、椭圆、圆角矩形
        CIRCULAR,
        ELLIPSE,
        RECTANGULAR
    }

    /**
     * 引导图点回调
     */
    public interface OnClickCallback {
        /**
         * 引导图点
         */
        void onClickedGuideView();
    }

    /**
     * Builder
     * <p>
     * 建造者模式。
     */
    public static class Builder {
        GuideView guiderView;

        public Builder(Context context) {
            guiderView = new GuideView(context);
        }

        public Builder setTargetView(View target) {
            guiderView.setTargetView(target);
            return this;
        }

        public Builder setBgColor(int color) {
            guiderView.setBgColor(color);
            return this;
        }

        public Builder setDirection(Direction dir) {
            guiderView.setDirection(dir);
            return this;
        }

        public Builder setShape(MyShape shape) {
            guiderView.setShape(shape);
            return this;
        }

        public Builder setOffset(int x, int y) {
            guiderView.setxOffset(x);
            guiderView.setyOffset(y);
            return this;
        }

        public Builder setRadius(int radius) {
            guiderView.setRadius(radius);
            return this;
        }

        public Builder setCustomGuideView(View view) {
            guiderView.setCustomGuideView(view);
            return this;
        }

        public Builder setCenter(int x, int y) {
            guiderView.setCenter(new int[]{x, y});
            return this;
        }

        public Builder showOnce() {
            guiderView.showOnce();
            return this;
        }

        public GuideView build() {
            guiderView.setClickInfo();
            return guiderView;
        }

        public Builder setOnClickExit(boolean onclickExit) {
            guiderView.setOnClickExit(onclickExit);
            return this;
        }

        public Builder setOnClickListener(final OnClickCallback callback) {
            guiderView.setOnclickListener(callback);
            return this;
        }
    }

    public void show() {
        Timber.d("show");
        if (hasShown()) {
            return;
        }
        if (targetView != null) {
            targetView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
        this.setBackgroundResource(R.color.transparent);
        ((FrameLayout) ((Activity) context).getWindow().getDecorView()).addView(this);
        firstShow = false;
        if (onlyOnce) {
            saveShowState();
        }
    }
}
