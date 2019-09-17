package com.zsp.library.status.listener;

import android.view.View;

import com.zsp.library.status.manager.StatusManager;

/**
 * @decs: 状态监听
 * @author: 郑少鹏
 * @date: 2018/10/23 18:54
 */
public abstract class BaseStatusListener {
    /**
     * 加载
     *
     * @param loadingView loadingView
     */
    public void setLoadingEvent(View loadingView) {

    }

    /**
     * 空
     *
     * @param emptyView emptyView
     */
    public void setEmptyEvent(View emptyView) {

    }

    /**
     * 重试
     *
     * @param retryView retryView
     */
    public abstract void setRetryEvent(View retryView);

    public int generateLoadingLayoutId() {
        return StatusManager.NO_LAYOUT_ID;
    }

    public int generateEmptyLayoutId() {
        return StatusManager.NO_LAYOUT_ID;
    }

    public int generateRetryLayoutId() {
        return StatusManager.NO_LAYOUT_ID;
    }

    public View generateLoadingLayout() {
        return null;
    }

    public View generateEmptyLayout() {
        return null;
    }

    public View generateRetryLayout() {
        return null;
    }

    public boolean isSetLoadingLayout() {
        return generateLoadingLayoutId() != StatusManager.NO_LAYOUT_ID || generateLoadingLayout() != null;
    }

    public boolean isSetEmptyLayout() {
        return generateEmptyLayoutId() != StatusManager.NO_LAYOUT_ID || generateEmptyLayout() != null;
    }

    public boolean isSetRetryLayout() {
        return generateRetryLayoutId() != StatusManager.NO_LAYOUT_ID || generateRetryLayout() != null;
    }
}