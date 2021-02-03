package com.zsp.library.dialog.basedialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.zsp.library.R;
import com.zsp.utilone.density.DensityUtils;
import com.zsp.utilone.screen.ScreenUtils;

import java.util.Objects;

/**
 * @decs: BaseDialog
 * @author: 郑少鹏
 * @date: 2018/4/4 13:40
 */
public abstract class BaseDialog extends DialogFragment {
    private static final String MARGIN = "margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String CANCEL = "out_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";
    private static final String CENTER = "show_center";
    /**
     * 布局ID
     */
    int layoutId;
    /**
     * 左右边距
     */
    private int margin;
    /**
     * 宽
     */
    private int width;
    /**
     * 高
     */
    private int height;
    /**
     * 灰度
     */
    private float dimAmount = 0.5f;
    /**
     * 底部显
     */
    private boolean showBottom;
    /**
     * 外点取
     */
    private boolean outCancel = true;
    private int animStyle;
    /**
     * 中间显
     */
    private boolean showCenter;

    /**
     * xxx
     *
     * @return 布局ID
     */
    public abstract int intLayoutId();

    /**
     * xxx
     *
     * @param holder viewHolder
     * @param dialog baseDialog
     */
    public abstract void convertView(ViewHolder holder, BaseDialog dialog);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialog);
        layoutId = intLayoutId();
        // 恢复所保存数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN);
            width = savedInstanceState.getInt(WIDTH);
            height = savedInstanceState.getInt(HEIGHT);
            dimAmount = savedInstanceState.getFloat(DIM);
            showBottom = savedInstanceState.getBoolean(BOTTOM);
            outCancel = savedInstanceState.getBoolean(CANCEL);
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);
            showCenter = savedInstanceState.getBoolean(CENTER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        convertView(ViewHolder.create(view), this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    /**
     * 屏幕旋转等致DialogFragment销毁后重建时保存数据
     *
     * @param outState outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MARGIN, margin);
        outState.putInt(WIDTH, width);
        outState.putInt(HEIGHT, height);
        outState.putFloat(DIM, dimAmount);
        outState.putBoolean(BOTTOM, showBottom);
        outState.putBoolean(CANCEL, outCancel);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);
        outState.putBoolean(CENTER, showCenter);
    }

    private void initParams() {
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                // 调节灰色背景透明度[0-1]（默0.5f）
                lp.dimAmount = dimAmount;
                // 底部显
                if (showBottom) {
                    lp.gravity = Gravity.BOTTOM;
                    if (animStyle == 0) {
                        animStyle = R.style.DefaultAnimation;
                    }
                }
                // 中间显示
                if (showCenter) {
                    lp.gravity = Gravity.CENTER;
                    if (animStyle == 0) {
                        animStyle = R.style.DefaultAnimation;
                    }
                }
                // Dialog宽
                if (width == 0) {
                    lp.width = ScreenUtils.screenWidth(Objects.requireNonNull(getContext())) - 2 * DensityUtils.dipToPxByFloat(Objects.requireNonNull(getContext(), "must not be null"), margin);
                } else {
                    lp.width = DensityUtils.dipToPxByFloat(Objects.requireNonNull(getContext(), "must not be null"), width);
                }
                // Dialog高
                if (height == 0) {
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                } else {
                    lp.height = DensityUtils.dipToPxByFloat(getContext(), height);
                }
                // Dialog进/退动画
                window.setWindowAnimations(animStyle);
                window.setAttributes(lp);
            }
        }
        setCancelable(outCancel);
    }

    public BaseDialog setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public BaseDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public BaseDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    public BaseDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public BaseDialog setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    public BaseDialog setShowCenter(boolean showCenter) {
        this.showCenter = showCenter;
        return this;
    }

    public BaseDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public BaseDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public BaseDialog show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }
}
