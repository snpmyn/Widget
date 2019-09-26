package com.zsp.library.screen.listener;

/**
 * Created on 2019/9/26.
 *
 * @author 郑少鹏
 * @desc 互斥点监听
 */
public interface MutuallyExclusiveClickListener {
    /**
     * 点
     *
     * @param classification 类别
     */
    void click(String classification);
}
