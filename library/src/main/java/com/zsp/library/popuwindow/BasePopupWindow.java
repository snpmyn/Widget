package com.zsp.library.popuwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.Transition;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.core.widget.PopupWindowCompat;

import com.zsp.utilone.log.LogManager;

/**
 * @decs: BasePopupWindow
 * @author: 郑少鹏
 * @date: 2018/6/22 21:37
 */
public abstract class BasePopupWindow<T extends BasePopupWindow> implements PopupWindow.OnDismissListener {
    private static final String TAG = "BasePopupWindow";
    private static final float DEFAULT_DIM = 0.7f;
    /**
     * PopupWindow对象
     */
    private PopupWindow mPopupWindow;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 内容布局
     */
    private View mContentView;
    /**
     * 布局ID
     */
    private int mLayoutId;
    /**
     * 获焦
     */
    private boolean mFocusable = true;
    /**
     * 外触消否
     */
    private boolean mOutsideTouchable = true;
    /**
     * 宽高
     */
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mAnimationStyle;
    private PopupWindow.OnDismissListener mOnDismissListener;
    /**
     * 弹背景变暗否
     */
    private boolean isBackgroundDim;
    /**
     * 背景变暗透明度
     */
    private float mDimValue = DEFAULT_DIM;
    /**
     * 背景变暗颜色
     */
    @ColorInt
    private int mDimColor = Color.BLACK;
    /**
     * 背景变暗view
     */
    private ViewGroup mDimView;
    private Transition mEnterTransition;
    private Transition mExitTransition;
    private boolean mFocusAndOutsideEnable = true;
    private View mAnchorView;
    @YGravity
    private int mYGravity = YGravity.below;
    @XGravity
    private int mXGravity = XGravity.left;
    private int mOffsetX;
    private int mOffsetY;
    private int mInputMethodMode = PopupWindow.INPUT_METHOD_FROM_FOCUSABLE;
    private int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
    /**
     * 重测宽高否
     */
    private boolean isNeedReMeasureWH = false;
    /**
     * 真实宽高备好否
     */
    private boolean isRealWHAlready = false;
    private boolean isAtAnchorViewMethod = false;
    private OnRealWidthHeightAlreadyListener mOnRealWidthHeightAlreadyListener;

    private T self() {
        // noinspection unchecked
        return (T) this;
    }

    public T apply() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow();
        }
        onPopupWindowCreated();
        initContentViewAndWH();
        onPopupWindowViewCreated(mContentView);
        if (mAnimationStyle != 0) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }
        initFocusAndBack();
        mPopupWindow.setOnDismissListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mEnterTransition != null) {
                mPopupWindow.setEnterTransition(mEnterTransition);
            }
            if (mExitTransition != null) {
                mPopupWindow.setExitTransition(mExitTransition);
            }
        }
        return self();
    }

    private void initContentViewAndWH() {
        if (mContentView == null) {
            if (mLayoutId != 0 && mContext != null) {
                mContentView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
            } else {
                throw new IllegalArgumentException("The content view is null,the layoutId=" + mLayoutId + ",context=" + mContext);
            }
        }
        mPopupWindow.setContentView(mContentView);
        if (mWidth > 0 || mWidth == ViewGroup.LayoutParams.WRAP_CONTENT || mWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
            mPopupWindow.setWidth(mWidth);
        } else {
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mHeight > 0 || mHeight == ViewGroup.LayoutParams.WRAP_CONTENT || mHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
            mPopupWindow.setHeight(mHeight);
        } else {
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 测contentView大小（或不准）
        measureContentView();
        // contentView精准大小
        registerOnGlobalLayoutListener();
        mPopupWindow.setInputMethodMode(mInputMethodMode);
        mPopupWindow.setSoftInputMode(mSoftInputMode);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initFocusAndBack() {
        if (!mFocusAndOutsideEnable) {
            // from https://github.com/pinguo-zhouwei/CustomPopwindow
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            // 下三个为contentView非PopupWindow（响应返按钮事件）
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            });
            // 6.0+仅通拦截事件解决
            mPopupWindow.setTouchInterceptor((v, event) -> {
                final int x = (int) event.getX();
                final int y = (int) event.getY();
                boolean flag = (event.getAction() == MotionEvent.ACTION_DOWN) && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight));
                if (flag) {
                    // outside
                    LogManager.e(TAG, "onTouch outside:mWidth=" + mWidth + "，mHeight=" + mHeight);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    // outside
                    LogManager.e(TAG, "onTouch outside event:mWidth=" + mWidth + "，mHeight=" + mHeight);
                    return true;
                }
                return false;
            });
        } else {
            mPopupWindow.setFocusable(mFocusable);
            mPopupWindow.setOutsideTouchable(mOutsideTouchable);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
    /*自定生命周期法*/

    /**
     * PopupWindow对象创完
     */
    private void onPopupWindowCreated() {
        // PopupWindow属性也可通Builder设
        /*setContentView(x, x, x);*/
        initAttributes();
    }

    private void onPopupWindowViewCreated(View contentView) {
        initViews(contentView);
    }

    private void onPopupWindowDismiss() {

    }

    /**
     * 可此法设PopupWindow所需属性
     */
    protected abstract void initAttributes();

    /**
     * 初始化view{@see getView()}
     *
     * @param view 视图
     */
    protected abstract void initViews(View view);

    /**
     * 需测contentView大小否（如需重测并赋宽高）
     * 此法所获宽高或不准（MATCH_PARENT时无法获准宽高）
     */
    private void measureContentView() {
        final View contentView = getContentView();
        if (mWidth <= 0 || mHeight <= 0) {
            if (contentView != null) {
                // 测大小
                contentView.measure(0, View.MeasureSpec.UNSPECIFIED);
                if (mWidth <= 0) {
                    mWidth = contentView.getMeasuredWidth();
                }
                if (mHeight <= 0) {
                    mHeight = contentView.getMeasuredHeight();
                }
            }
        }
    }

    /**
     * 注册GlobalLayoutListener获精准宽高
     */
    private void registerOnGlobalLayoutListener() {
        if (getContentView() != null) {
            getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mWidth = getContentView().getWidth();
                    mHeight = getContentView().getHeight();
                    isRealWHAlready = true;
                    isNeedReMeasureWH = false;
                    if (mOnRealWidthHeightAlreadyListener != null) {
                        mOnRealWidthHeightAlreadyListener.onRealWHAlready(BasePopupWindow.this, mWidth, mHeight, mAnchorView == null ?
                                0 : mAnchorView.getWidth(), mAnchorView == null ? 0 : mAnchorView.getHeight());
                    }
                    LogManager.e("onGlobalLayout finished. isShowing=" + isShowing());
                    if (isShowing() && isAtAnchorViewMethod) {
                        updateLocation(mWidth, mHeight, mAnchorView, mYGravity, mXGravity, mOffsetX, mOffsetY);
                    }
                }
            });
        }
    }

    /**
     * 更新PopupWindow至精准位
     *
     * @param width    宽
     * @param height   高
     * @param anchor   锚点
     * @param yGravity y位
     * @param xGravity x位
     * @param x        x
     * @param y        y
     */
    private void updateLocation(int width, int height, @NonNull View anchor, @YGravity final int yGravity, @XGravity int xGravity, int x, int y) {
        if (mPopupWindow == null) {
            return;
        }
        x = calculateX(anchor, xGravity, width, x);
        y = calculateY(anchor, yGravity, height, y);
        mPopupWindow.update(anchor, x, y, width, height);
    }

    /****属性****/
    public T setContext(Context context) {
        this.mContext = context;
        return self();
    }

    public T setContentView(Context context, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mContentView = null;
        this.mLayoutId = layoutId;
        return self();
    }

    public T setContentView(View contentView, int width, int height) {
        this.mContentView = contentView;
        this.mLayoutId = 0;
        this.mWidth = width;
        this.mHeight = height;
        return self();
    }

    public T setContentView(@LayoutRes int layoutId, int width, int height) {
        this.mContentView = null;
        this.mLayoutId = layoutId;
        this.mWidth = width;
        this.mHeight = height;
        return self();
    }

    public T setContentView(Context context, @LayoutRes int layoutId, int width, int height) {
        this.mContext = context;
        this.mContentView = null;
        this.mLayoutId = layoutId;
        this.mWidth = width;
        this.mHeight = height;
        return self();
    }

    public T setAnchorView(View view) {
        this.mAnchorView = view;
        return self();
    }

    public T setAnimationStyle(@StyleRes int animationStyle) {
        this.mAnimationStyle = animationStyle;
        return self();
    }

    public T setFocusable(boolean focusable) {
        this.mFocusable = focusable;
        return self();
    }

    public T setOutsideTouchable(boolean outsideTouchable) {
        this.mOutsideTouchable = outsideTouchable;
        return self();
    }

    /**
     * PopupWindow外点消否
     *
     * @param focusAndOutsideEnable 外点消否
     * @return T
     */
    public T setFocusAndOutsideEnable(boolean focusAndOutsideEnable) {
        this.mFocusAndOutsideEnable = focusAndOutsideEnable;
        return self();
    }

    /**
     * 背景变暗支持（大等18）
     *
     * @param isDim 变暗支持否
     * @return T
     */
    public T setBackgroundDimEnable(boolean isDim) {
        this.isBackgroundDim = isDim;
        return self();
    }

    public T setDimValue(@FloatRange(from = 0.0f, to = 1.0f) float dimValue) {
        this.mDimValue = dimValue;
        return self();
    }

    public T setDimColor(@ColorInt int color) {
        this.mDimColor = color;
        return self();
    }

    public T setDimView(@NonNull ViewGroup dimView) {
        this.mDimView = dimView;
        return self();
    }

    public T setEnterTransition(Transition enterTransition) {
        this.mEnterTransition = enterTransition;
        return self();
    }

    public T setExitTransition(Transition exitTransition) {
        this.mExitTransition = exitTransition;
        return self();
    }

    public T setInputMethodMode(int mode) {
        this.mInputMethodMode = mode;
        return self();
    }

    public T setSoftInputMode(int mode) {
        this.mSoftInputMode = mode;
        return self();
    }

    /**
     * 需重获宽高否
     *
     * @param needReMeasureWH 需重获宽高否
     * @return T
     */
    public T setNeedReMeasureWH(boolean needReMeasureWH) {
        this.isNeedReMeasureWH = needReMeasureWH;
        return self();
    }

    /**
     * 调apply()否
     *
     * @param isAtAnchorView showAt否
     */
    private void checkIsApply(boolean isAtAnchorView) {
        if (this.isAtAnchorViewMethod != isAtAnchorView) {
            this.isAtAnchorViewMethod = isAtAnchorView;
        }
        if (mPopupWindow == null) {
            apply();
        }
    }

    /**
     * 此法需创时调setAnchorView()等属性设置{@see setAnchorView()}
     */
    public void showAsDropDown() {
        if (mAnchorView == null) {
            return;
        }
        showAsDropDown(mAnchorView, mOffsetX, mOffsetY);
    }

    /**
     * PopupWindow自带显法
     *
     * @param anchor  锚点
     * @param offsetX X偏移量
     * @param offsetY Y偏移量
     */
    private void showAsDropDown(View anchor, int offsetX, int offsetY) {
        // 避忘调apply()
        checkIsApply(false);
        handleBackgroundDim();
        mAnchorView = anchor;
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        // 重获宽高否
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        mPopupWindow.showAsDropDown(anchor, mOffsetX, mOffsetY);
    }

    public void showAsDropDown(View anchor) {
        // 避忘调apply()
        checkIsApply(false);
        handleBackgroundDim();
        mAnchorView = anchor;
        // 重获宽高否
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        mPopupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int offsetX, int offsetY, int gravity) {
        // 避忘调apply()
        checkIsApply(false);
        handleBackgroundDim();
        mAnchorView = anchor;
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        // 重获宽高否
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        PopupWindowCompat.showAsDropDown(mPopupWindow, anchor, mOffsetX, mOffsetY, gravity);
    }

    public void showAtLocation(View parent, int gravity, int offsetX, int offsetY) {
        // 避忘调apply()
        checkIsApply(false);
        handleBackgroundDim();
        mAnchorView = parent;
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        // 重获宽高否
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        mPopupWindow.showAtLocation(parent, gravity, mOffsetX, mOffsetY);
    }

    /**
     * 相对anchor view显
     * <p>
     * 此法需创时调setAnchorView()等属性设置{@see setAnchorView()}
     * <p>
     * 用VerticalGravity和HorizontalGravity保使用后PopupWindow没超屏边
     * 超屏边则VerticalGravity和HorizontalGravity或无效，达不到理想效果
     */
    public void showAtAnchorView() {
        if (mAnchorView == null) {
            return;
        }
        showAtAnchorView(mAnchorView, mYGravity, mXGravity);
    }

    /**
     * 相对anchor view显
     * <p>
     * 用VerticalGravity和HorizontalGravity保使用后PopupWindow没超屏边
     * 超屏边则VerticalGravity和HorizontalGravity或无效，达不到理想效果
     *
     * @param anchor            锚点
     * @param verticalGravity   垂直对齐方式
     * @param horizontalGravity 水平对齐方式
     */
    private void showAtAnchorView(@NonNull View anchor, @YGravity int verticalGravity, @XGravity int horizontalGravity) {
        showAtAnchorView(anchor, verticalGravity, horizontalGravity, 0, 0);
    }

    /**
     * 相对anchor view显
     * <p>
     * 用VerticalGravity和HorizontalGravity保使用后PopupWindow没超屏边
     * 超屏边则VerticalGravity和HorizontalGravity或无效，达不到理想效果
     *
     * @param anchor            锚点
     * @param verticalGravity   垂直对齐方式
     * @param horizontalGravity 水平对齐方式
     * @param x                 水平偏移
     * @param y                 垂直偏移
     */
    public void showAtAnchorView(@NonNull View anchor, @YGravity final int verticalGravity, @XGravity int horizontalGravity, int x, int y) {
        // 避忘调apply()
        checkIsApply(true);
        mAnchorView = anchor;
        mOffsetX = x;
        mOffsetY = y;
        mYGravity = verticalGravity;
        mXGravity = horizontalGravity;
        // 处理背景变暗
        handleBackgroundDim();
        x = calculateX(anchor, horizontalGravity, mWidth, mOffsetX);
        y = calculateY(anchor, verticalGravity, mHeight, mOffsetY);
        // 重获宽高否
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        PopupWindowCompat.showAsDropDown(mPopupWindow, anchor, x, y, Gravity.NO_GRAVITY);
    }

    /**
     * 据垂直gravity算y偏移
     *
     * @param anchor          锚点
     * @param verticalGravity 垂直对齐方式
     * @param measuredH       测高
     * @param y               y
     * @return y偏移
     */
    private int calculateY(View anchor, int verticalGravity, int measuredH, int y) {
        switch (verticalGravity) {
            case YGravity.above:
                // anchor view上
                y -= measuredH + anchor.getHeight();
                break;
            case YGravity.alignBottom:
                // anchor view底对齐
                y -= measuredH;
                break;
            case YGravity.center:
                // anchor view垂直居中
                y -= anchor.getHeight() / 2 + measuredH / 2;
                break;
            case YGravity.alignTop:
                // anchor view顶对齐
                y -= anchor.getHeight();
                break;
            case YGravity.below:
                // anchor view下
                // default position
                break;
            default:
                break;
        }
        return y;
    }

    /**
     * 据水平gravity算x偏移
     *
     * @param anchor            锚点
     * @param horizontalGravity 水平对齐方式
     * @param measuredW         测宽
     * @param x                 x
     * @return x偏移
     */
    private int calculateX(View anchor, int horizontalGravity, int measuredW, int x) {
        switch (horizontalGravity) {
            case XGravity.left:
                // anchor view左侧
                x -= measuredW;
                break;
            case XGravity.alignRight:
                // 与anchor view右对齐
                x -= measuredW - anchor.getWidth();
                break;
            case XGravity.center:
                // anchor view水平居中
                x += anchor.getWidth() / 2 - measuredW / 2;
                break;
            case XGravity.alignLeft:
                // 与anchor view左对齐
                // default position
                break;
            case XGravity.right:
                // anchor view右侧
                x += anchor.getWidth();
                break;
            default:
                break;
        }
        return x;
    }

    /**
     * 监听
     *
     * @param listener 监听
     */
    public T setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
        return self();
    }

    public T setOnRealWHAlreadyListener(OnRealWidthHeightAlreadyListener listener) {
        this.mOnRealWidthHeightAlreadyListener = listener;
        return self();
    }

    /**
     * 处理背景变暗
     * https://blog.nex3z.com/2016/12/04/%E5%BC%B9%E5%87%BApopupwindow%E5%90%8E%E8%AE%A9%E8%83%8C%E6%99%AF%E5%8F%98%E6%9A%97%E7%9A%84%E6%96%B9%E6%B3%95/
     */
    private void handleBackgroundDim() {
        if (!isBackgroundDim) {
            return;
        }
        applyDim(mDimView);
    }

    private void applyDim(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        // activity根布局
        /*ViewGroup parent = (ViewGroup) parent1.getChildAt(0);*/
        Drawable dimDrawable = new ColorDrawable(mDimColor);
        dimDrawable.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dimDrawable.setAlpha((int) (255 * mDimValue));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dimDrawable);
    }

    private void applyDim(ViewGroup dimView) {
        Drawable dimDrawable = new ColorDrawable(mDimColor);
        dimDrawable.setBounds(0, 0, dimView.getWidth(), dimView.getHeight());
        dimDrawable.setAlpha((int) (255 * mDimValue));
        ViewGroupOverlay overlay = dimView.getOverlay();
        overlay.add(dimDrawable);
    }

    /**
     * 清背景变暗
     */
    private void clearBackgroundDim() {
        if (isBackgroundDim) {
            clearDim(mDimView);
        }
    }

    private void clearDim(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        // activity根布局
        /*ViewGroup parent = (ViewGroup) parent1.getChildAt(0);*/
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    private void clearDim(ViewGroup dimView) {
        ViewGroupOverlay overlay = dimView.getOverlay();
        overlay.clear();
    }

    /**
     * 获PopupWindow加载view
     *
     * @return 加载view
     */
    private View getContentView() {
        if (mPopupWindow != null) {
            return mPopupWindow.getContentView();
        } else {
            return null;
        }
    }

    public T setContentView(View contentView) {
        this.mContentView = contentView;
        this.mLayoutId = 0;
        return self();
    }

    public T setContentView(@LayoutRes int layoutId) {
        this.mContentView = null;
        this.mLayoutId = layoutId;
        return self();
    }

    /**
     * PopupWindow对象
     *
     * @return PopupWindow
     */
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    /**
     * PopupWindow宽
     *
     * @return 宽
     */
    public int getWidth() {
        return mWidth;
    }

    public T setWidth(int width) {
        this.mWidth = width;
        return self();
    }

    /**
     * PopupWindow高
     *
     * @return 高
     */
    public int getHeight() {
        return mHeight;
    }

    public T setHeight(int height) {
        this.mHeight = height;
        return self();
    }

    /**
     * 横Gravity
     *
     * @return 横Gravity
     */
    public int getXGravity() {
        return mXGravity;
    }

    public T setXGravity(@XGravity int xGravity) {
        this.mXGravity = xGravity;
        return self();
    }

    /**
     * 纵Gravity
     *
     * @return 纵Gravity
     */
    public int getYGravity() {
        return mYGravity;
    }

    public T setYGravity(@YGravity int yGravity) {
        this.mYGravity = yGravity;
        return self();
    }

    /**
     * X轴偏移
     *
     * @return x轴偏移
     */
    public int getOffsetX() {
        return mOffsetX;
    }

    public T setOffsetX(int offsetX) {
        this.mOffsetX = offsetX;
        return self();
    }

    /**
     * Y轴偏移
     *
     * @return y轴偏移
     */
    public int getOffsetY() {
        return mOffsetY;
    }

    public T setOffsetY(int offsetY) {
        this.mOffsetY = offsetY;
        return self();
    }

    /**
     * 正显否
     *
     * @return 正显否
     */
    private boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    /**
     * 精准宽高获完否
     *
     * @return 精准宽高获完否
     */
    public boolean isRealWHAlready() {
        return isRealWHAlready;
    }

    /**
     * view
     *
     * @param viewId 视图ID
     * @param <T>    <T></>
     * @return T
     */
    public <T extends View> T findViewById(@IdRes int viewId) {
        View view = null;
        if (getContentView() != null) {
            view = getContentView().findViewById(viewId);
        }
        return (T) view;
    }

    /**
     * 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onDismiss() {
        handleDismiss();
    }

    /**
     * PopupWindow消后处理逻辑
     */
    private void handleDismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
        // 清背景变暗
        clearBackgroundDim();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        onPopupWindowDismiss();
    }

    /**
     * PopupWindow是否window中显
     * 获准PopupWindow宽高（可重设偏移量）
     */
    public interface OnRealWidthHeightAlreadyListener {
        /**
         * show后updateLocation前执行
         *
         * @param basePopupWindow basePopupWindow
         * @param popWidth        PopupWindow准宽
         * @param popHeight       PopupWindow准高
         * @param anchorW         锚点宽
         * @param anchorH         锚点高
         */
        void onRealWHAlready(BasePopupWindow basePopupWindow, int popWidth, int popHeight, int anchorW, int anchorH);
    }
}
