package com.zsp.library.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtreeapps.spruce.sort.LinearSort;
import com.zsp.library.spruce.SpruceKit;

/**
 * Created on 2019/5/22.
 *
 * @author 郑少鹏
 * @desc RecyclerViewKit
 */
public class RecyclerViewKit {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 控件
     */
    private RecyclerView recyclerView;
    /**
     * SpruceKit
     */
    private SpruceKit spruceKit;
    private long interObjectDelay;
    private long duration;
    private boolean reversed;
    private LinearSort.Direction direction;

    /**
     * constructor
     *
     * @param context      控件
     * @param recyclerView 控件
     */
    public RecyclerViewKit(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.spruceKit = new SpruceKit();
        this.interObjectDelay = 100L;
        this.duration = 800L;
        this.reversed = false;
        this.direction = LinearSort.Direction.TOP_TO_BOTTOM;
    }

    /**
     * SpruceKit配置
     * <p>
     * {@link #linearVerticalLayout()}前调。
     * {@link #linearHorizontalLayout()}前调。
     * {@link #gridLayout(int, int, boolean)}前调。
     *
     * @param interObjectDelay 实体整型延迟
     * @param duration         时长
     * @param reversed         反转
     * @param direction        时长
     */
    public void spruceKitConfigure(long interObjectDelay, long duration, boolean reversed, LinearSort.Direction direction) {
        this.interObjectDelay = interObjectDelay;
        this.duration = duration;
        this.reversed = reversed;
        this.direction = direction;
    }

    /**
     * 线性垂直布局
     * <p>
     * {@link #spruceKitConfigure(long, long, boolean, LinearSort.Direction)}后调。
     */
    public void linearVerticalLayout() {
        recyclerView.setLayoutManager(new MyLinearLayoutManager(context, (recycler, state) -> spruceKit.defaultSort(recyclerView, interObjectDelay, duration)));
        // 每item内容不改RecyclerView大小，此设提性能
        recyclerView.setHasFixedSize(true);
    }

    /**
     * 线性水平布局
     * <p>
     * {@link #spruceKitConfigure(long, long, boolean, LinearSort.Direction)}后调。
     */
    public void linearHorizontalLayout() {
        // false头至尾/true尾至头（默false）
        recyclerView.setLayoutManager(new MyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false, (recycler, state) -> spruceKit.defaultSort(recyclerView, interObjectDelay, duration)));
        // 每item内容不改RecyclerView大小，此设提性能
        recyclerView.setHasFixedSize(true);
    }

    /**
     * 表格布局
     * <p>
     * {@link #spruceKitConfigure(long, long, boolean, LinearSort.Direction)}后调。
     *
     * @param spanCount                      spanCount
     * @param spacing                        spacing
     * @param firstRowHaveTopSpaceDecoration firstRowHaveTopSpaceDecoration
     */
    public void gridLayout(int spanCount, int spacing, boolean firstRowHaveTopSpaceDecoration) {
        recyclerView.setLayoutManager(new MyGridLayoutManager(context, spanCount, (recycler, state) -> spruceKit.linearSort(recyclerView, interObjectDelay, reversed, direction, duration)));
        // 每item内容不改RecyclerView大小，此设提性能
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridLayoutSpacingItemDecoration(spanCount, spacing, firstRowHaveTopSpaceDecoration, true));
    }
}
