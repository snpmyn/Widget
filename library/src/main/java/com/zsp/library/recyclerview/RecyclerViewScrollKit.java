package com.zsp.library.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2018/6/28.
 *
 * @author 郑少鹏
 * @desc RecyclerViewScrollKit
 */
public class RecyclerViewScrollKit {
    private boolean move;
    private int index;

    /**
     * 平缓滑至目标位
     *
     * @param recyclerView 控件
     * @param position     位
     */
    public void flatSlidToTargetPosition(RecyclerView recyclerView, int position) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (manager != null) {
            // 滑至指定位
            // 记录（第三种情况用到）
            this.index = position;
            // 当前屏幕可见头项与末项
            int firstItem = manager.findFirstVisibleItemPosition();
            int lastItem = manager.findLastVisibleItemPosition();
            // 区分
            if (position <= firstItem) {
                // 所置顶项于当前显示头项前
                recyclerView.smoothScrollToPosition(position);
            } else if (position <= lastItem) {
                // 所置顶项已于屏幕上
                int top = recyclerView.getChildAt(position - firstItem).getTop();
                recyclerView.smoothScrollBy(0, top);
            } else {
                // 所置顶项于当前显示末项后
                recyclerView.smoothScrollToPosition(position);
                move = true;
            }
            // 滑动监听
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    // 此处第二次滚动
                    if (move) {
                        move = false;
                        // 所置顶项于当前屏幕位，mIndex记录所置顶于RecyclerView中位
                        int n = index - manager.findFirstVisibleItemPosition();
                        if (0 <= n && n < recyclerView.getChildCount()) {
                            // 所置顶项顶距RecyclerView顶距离
                            int top = recyclerView.getChildAt(n).getTop();
                            // 最后移动
                            recyclerView.smoothScrollBy(0, top);
                        }
                    }
                }
            });
        }
    }

    /**
     * 条目滑至居中
     *
     * @param recyclerView 控件
     * @param position     位
     */
    public void itemSlidToCenter(RecyclerView recyclerView, int position) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (linearLayoutManager != null) {
            int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
            int left = recyclerView.getChildAt(position - firstPosition).getLeft();
            int right = recyclerView.getChildAt(lastPosition - position).getLeft();
            recyclerView.scrollBy((left - right) / 2, 0);
        }
    }
}
