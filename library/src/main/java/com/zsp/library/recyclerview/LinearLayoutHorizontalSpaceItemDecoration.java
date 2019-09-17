package com.zsp.library.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2019/7/1.
 *
 * @author 郑少鹏
 * @desc 线性布局水平条目间距装饰
 * 头条目左间距，其后都右间距。
 */
public class LinearLayoutHorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    LinearLayoutHorizontalSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = space;
        // Add left margin only for the first item to avoid double space between items.
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = space;
        } else {
            outRect.left = 0;
        }
    }
}
