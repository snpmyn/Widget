package com.zsp.library.sidedrag.viewholder;

import android.content.Context;

/**
 * @decs: ItemTouchHelperViewHolder
 * @author: 郑少鹏
 * @date: 2019/8/16 12:11
 */
public interface ItemTouchHelperViewHolder {
    /**
     * 侧滑开始
     *
     * @param context 上下文
     */
    void onItemSlideSlipStart(Context context);

    /**
     * 侧滑停止
     *
     * @param context 上下文
     */
    void onItemSlideSlipStop(Context context);
}
