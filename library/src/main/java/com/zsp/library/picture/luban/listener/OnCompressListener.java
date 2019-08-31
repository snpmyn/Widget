package com.zsp.library.picture.luban.listener;

import java.io.File;

/**
 * @decs: OnCompressListener
 * @author: 郑少鹏
 * @date: 2019/8/28 19:14
 */
public interface OnCompressListener {
    /**
     * Fired when the compression is started, override to handle in your own code.
     */
    void onStart();

    /**
     * Fired when a compression returns successfully, override to handle in your own code.
     *
     * @param file File
     */
    void onSuccess(File file);

    /**
     * Fired when a compression fails to complete, override to handle in your own code.
     *
     * @param e Throwable
     */
    void onError(Throwable e);
}
