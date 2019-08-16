package com.zsp.library.sidedrag.listener;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @decs: ItemDragListener
 * @author: 郑少鹏
 * @date: 2019/8/16 12:11
 */
public interface ItemDragListener {
    /**
     * 拖拽
     *
     * @param viewHolder viewHolder
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
