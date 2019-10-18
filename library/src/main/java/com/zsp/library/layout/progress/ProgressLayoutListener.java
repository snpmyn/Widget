package com.zsp.library.layout.progress;

/**
 * @decs: 进度布局监听
 * @author: 郑少鹏
 * @date: 2019/10/18 10:24
 */
public interface ProgressLayoutListener {
    /**
     * 进度完成
     */
    void onProgressCompleted();

    /**
     * 进度变
     *
     * @param seconds 秒
     */
    void onProgressChanged(int seconds);
}
