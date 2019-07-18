package com.zsp.library.pickerview.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.zsp.library.R;
import com.zsp.library.pickerview.configure.PickerOptions;
import com.zsp.library.pickerview.listener.OnDismissListener;
import com.zsp.library.pickerview.util.PickerViewAnimateUtil;

/**
 * @decs: 精仿iOS之PickerViewController控件
 * @author: 郑少鹏
 * @date: 2018/4/3 19:18
 */
public class BasePickerView {
    ViewGroup contentContainer;
    PickerOptions pickerOptions;
    /**
     * 通哪View弹出
     */
    View clickView;
    private Context context;
    /**
     * 附加View之根View
     */
    private ViewGroup rootView;
    /**
     * 附加Dialog之根View
     */
    private ViewGroup dialogView;
    private OnDismissListener onDismissListener;
    private boolean dismissing;
    private Animation outAnim;
    private Animation inAnim;
    private boolean isShowing;
    private Dialog mDialog;
    private boolean isAnim = true;
    /**
     * Called when the user touch on black overlay, in order to dismiss the widget.dialog.
     */
    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener onCancelableTouchListener = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dismiss();
        }
        return false;
    };
    private int animGravity = Gravity.BOTTOM;
    private View.OnKeyListener onKeyBackListener = (v, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN && isShowing()) {
            dismiss();
            return true;
        }
        return false;
    };

    BasePickerView(Context context) {
        this.context = context;
    }

    @SuppressLint("InflateParams")
    void initViews() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (isDialog()) {
            // 对话框模式
            dialogView = (ViewGroup) layoutInflater.inflate(R.layout.base_picker_view, null, false);
            // 界面背景透明
            dialogView.setBackgroundColor(Color.TRANSPARENT);
            // 真正加载选择器之父布局
            contentContainer = dialogView.findViewById(R.id.basePickerViewFlTwo);
            // 默左右距屏30
            // 自定（注销并布局设）
            /*params.leftMargin = 30;
            params.rightMargin = 30;*/
            contentContainer.setLayoutParams(params);
            // 创对话框
            createDialog();
            // 背景设点击事件（点内容外关界面）
            dialogView.setOnClickListener(view -> dismiss());
        } else {
            // 仅显屏幕下方
            // decorView是activity之根View（含contentView和titleView）
            if (pickerOptions.decorView == null) {
                pickerOptions.decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            }
            // 控件添到decorView中
            rootView = (ViewGroup) layoutInflater.inflate(R.layout.base_picker_view, pickerOptions.decorView, false);
            rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (pickerOptions.backgroundId != -1) {
                rootView.setBackgroundColor(pickerOptions.backgroundId);
            }
            // 真正加载时间选取器之父布局
            contentContainer = rootView.findViewById(R.id.basePickerViewFlTwo);
            contentContainer.setLayoutParams(params);
        }
        setKeyBackCancelable(true);
    }

    void initAnim() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    void initEvents() {
    }

    /**
     * @param v      通哪View弹出
     * @param isAnim 动画效果
     */
    public void show(View v, boolean isAnim) {
        this.clickView = v;
        this.isAnim = isAnim;
        show();
    }

    public void show(boolean isAnim) {
        this.isAnim = isAnim;
        show();
    }

    public void show(View v) {
        this.clickView = v;
        show();
    }

    /**
     * 添View到根视图
     */
    public void show() {
        if (isDialog()) {
            showDialog();
        } else {
            if (isShowing()) {
                return;
            }
            isShowing = true;
            onAttached(rootView);
            rootView.requestFocus();
        }
    }

    /**
     * show时调
     *
     * @param view 该View
     */
    private void onAttached(View view) {
        pickerOptions.decorView.addView(view);
        if (isAnim) {
            contentContainer.startAnimation(inAnim);
        }
    }

    /**
     * 检测该View添到根视图
     *
     * @return 视图已存该View返true
     */
    public boolean isShowing() {
        return !isDialog() && (rootView.getParent() != null || isShowing);
    }

    public void dismiss() {
        if (isDialog()) {
            dismissDialog();
        } else {
            if (dismissing) {
                return;
            }
            if (isAnim) {
                // 消失动画
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dismissImmediately();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                contentContainer.startAnimation(outAnim);
            } else {
                dismissImmediately();
            }
            dismissing = true;
        }
    }

    private void dismissImmediately() {
        pickerOptions.decorView.post(() -> {
            // 从根视图移除
            pickerOptions.decorView.removeView(rootView);
            isShowing = false;
            dismissing = false;
            if (onDismissListener != null) {
                onDismissListener.onDismiss(BasePickerView.this);
            }
        });
    }

    private Animation getInAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.animGravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    private Animation getOutAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.animGravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    private void setKeyBackCancelable(boolean isCancelable) {
        ViewGroup view;
        if (isDialog()) {
            view = dialogView;
        } else {
            view = rootView;
        }
        view.setFocusable(isCancelable);
        view.setFocusableInTouchMode(isCancelable);
        if (isCancelable) {
            view.setOnKeyListener(onKeyBackListener);
        } else {
            view.setOnKeyListener(null);
        }
    }

    BasePickerView setOutSideCancelable(boolean isCancelable) {
        if (rootView != null) {
            View view = rootView.findViewById(R.id.basePickerViewFl);
            if (isCancelable) {
                view.setOnTouchListener(onCancelableTouchListener);
            } else {
                view.setOnTouchListener(null);
            }
        }
        return this;
    }

    /**
     * 对话框模式可点外部取消
     */
    void setDialogOutSideCancelable() {
        if (mDialog != null) {
            mDialog.setCancelable(pickerOptions.cancelable);
        }
    }

    public View findViewById(int id) {
        return contentContainer.findViewById(id);
    }

    private void createDialog() {
        if (dialogView != null) {
            mDialog = new Dialog(context, R.style.PickerViewStyle);
            // 不可点外/back取消
            mDialog.setCancelable(pickerOptions.cancelable);
            mDialog.setContentView(dialogView);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.PickerViewAnimationStyle);
                // 可改Bottom
                dialogWindow.setGravity(Gravity.CENTER);
            }
            mDialog.setOnDismissListener(dialog -> {
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(BasePickerView.this);
                }
            });
        }
    }

    private void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public boolean isDialog() {
        return false;
    }
}
