package com.zsp.library.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2019/3/22.
 *
 * @author 郑少鹏
 * @desc 线性布局垂直条目间距装饰
 * 头条目上间距，其后都下间距。
 */
public class LinearLayoutVerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    LinearLayoutVerticalSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = space;
        // Add top margin only for the first item to avoid double space between items.
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}