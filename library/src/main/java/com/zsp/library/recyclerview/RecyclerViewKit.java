package com.zsp.library.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2019/5/22.
 *
 * @author 郑少鹏
 * @desc RecyclerViewKit
 */
public class RecyclerViewKit {
    private Context context;
    private RecyclerView recyclerView;

    /**
     * constructor
     *
     * @param recyclerView 控件
     */
    public RecyclerViewKit(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    /**
     * 线性垂直布局
     */
    public void linearVerticalLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // 每item内容不改RecyclerView大小，此设提性能
        recyclerView.setHasFixedSize(true);
    }

    /**
     * 表格布局
     *
     * @param spanCount                      spanCount
     * @param spacing                        spacing
     * @param firstRowHaveTopSpaceDecoration firstRowHaveTopSpaceDecoration
     */
    public void gridLayout(int spanCount, int spacing, boolean firstRowHaveTopSpaceDecoration) {
        recyclerView.setLayoutManager(new MyGridLayoutManager(context, spanCount));
        // 每item内容不改RecyclerView大小，此设提性能
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridLayoutSpacingItemDecoration(spanCount, spacing, firstRowHaveTopSpaceDecoration, true));
    }
}
