package com.zsp.library.searchbox.listener;

/**
 * @decs: 条目点或点删监听
 * @author: 郑少鹏
 * @date: 2019/4/23 11:37
 */
public interface OnItemClickOrDeleteClickListener {
    /**
     * 点
     *
     * @param keyword 关键字
     */
    void onItemClick(String keyword);

    /**
     * 点删
     *
     * @param position 位置
     * @param keyword  关键字
     */
    void onItemDeleteClick(int position, String keyword);
}
