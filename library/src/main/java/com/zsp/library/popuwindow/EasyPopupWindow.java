package com.zsp.library.popuwindow;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

/**
 * @decs: EasyPopupWindow
 * @author: 郑少鹏
 * @date: 2018/6/22 21:16
 */
public class EasyPopupWindow extends BasePopupWindow<EasyPopupWindow> {
    private OnViewListener mOnViewListener;

    private EasyPopupWindow() {

    }

    private EasyPopupWindow(Context context) {
        setContext(context);
    }

    @NonNull
    @Contract(" -> new")
    public static EasyPopupWindow create() {
        return new EasyPopupWindow();
    }

    @NonNull
    @Contract("_ -> new")
    public static EasyPopupWindow create(Context context) {
        return new EasyPopupWindow(context);
    }

    @Override
    protected void initAttributes() {

    }

    @Override
    protected void initViews(View view) {
        if (mOnViewListener != null) {
            mOnViewListener.initViews(view);
        }
    }

    public EasyPopupWindow setOnViewListener(OnViewListener listener) {
        this.mOnViewListener = listener;
        return this;
    }

    public interface OnViewListener {
        /**
         * 视图初始化
         *
         * @param view 视图
         */
        void initViews(View view);
    }
}
