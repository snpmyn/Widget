package com.zsp.library.sidedrag.adapter;

/**
 * @decs: ItemTouchHelperAdapter
 * @author: 郑少鹏
 * @date: 2019/8/16 12:11
 */
public interface ItemTouchHelperAdapter {
    /**
     * 移动
     *
     * @param fromPosition 起始位
     * @param toPosition   终止位
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * 侧滑结束
     *
     * @param position 位置
     */
    void onItemSlideSlipEnd(int position);
}
