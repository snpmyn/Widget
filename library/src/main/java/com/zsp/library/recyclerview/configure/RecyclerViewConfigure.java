package com.zsp.library.recyclerview.configure;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtreeapps.spruce.sort.LinearSort;
import com.zsp.library.recyclerview.decoration.GridLayoutSpaceItemDecoration;
import com.zsp.library.recyclerview.decoration.LinearLayoutHorizontalSpaceItemDecoration;
import com.zsp.library.recyclerview.decoration.LinearLayoutVerticalSpaceItemDecoration;
import com.zsp.library.recyclerview.manager.MyGridLayoutManager;
import com.zsp.library.recyclerview.manager.MyLinearLayoutManager;
import com.zsp.library.spruce.SpruceKit;

/**
 * Created on 2019/5/22.
 *
 * @author 郑少鹏
 * @desc RecyclerViewConfigure
 */
public class RecyclerViewConfigure {
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
    public RecyclerViewConfigure(Context context, RecyclerView recyclerView) {
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
     * {@link #linearHorizontalLayout(boolean, int, boolean, boolean)}前调。
     * {@link #linearVerticalLayout(boolean, int, boolean, boolean, boolean)}前调。
     * {@link #gridLayout(int, int, boolean, boolean, boolean)}前调。
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
     * 线性水平布局
     * <p>
     * {@link #spruceKitConfigure(long, long, boolean, LinearSort.Direction)}后调。
     *
     * @param needSpace    需间距
     * @param space        间距
     * @param hasFixedSize 已固定大小
     * @param spruce       spruce否
     */
    public void linearHorizontalLayout(boolean needSpace, int space, boolean hasFixedSize, boolean spruce) {
        // false头至尾/true尾至头（默false）
        recyclerView.setLayoutManager(new MyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false, (recycler, state) -> {
            if (spruce) {
                spruceKit.defaultSort(recyclerView, interObjectDelay, duration);
            }
        }));
        // 固定RecyclerView高（避RecyclerView重measure）
        recyclerView.setHasFixedSize(hasFixedSize);
        if (needSpace) {
            recyclerView.addItemDecoration(new LinearLayoutHorizontalSpaceItemDecoration(space));
        }
    }

    /**
     * 线性垂直布局
     * <p>
     * {@link #spruceKitConfigure(long, long, boolean, LinearSort.Direction)}后调。
     *
     * @param needSpace          需间距
     * @param space              间距
     * @param leftAndRightOffset 左右偏移
     * @param hasFixedSize       已固定大小
     * @param spruce             spruce否
     */
    public void linearVerticalLayout(boolean needSpace, int space, boolean leftAndRightOffset, boolean hasFixedSize, boolean spruce) {
        recyclerView.setLayoutManager(new MyLinearLayoutManager(context, (recycler, state) -> {
            if (spruce) {
                spruceKit.defaultSort(recyclerView, interObjectDelay, duration);
            }
        }));
        // 固定RecyclerView高（避RecyclerView重measure）
        recyclerView.setHasFixedSize(hasFixedSize);
        if (needSpace) {
            recyclerView.addItemDecoration(new LinearLayoutVerticalSpaceItemDecoration(space, leftAndRightOffset));
        }
    }

    /**
     * 表格布局
     * <p>
     * {@link #spruceKitConfigure(long, long, boolean, LinearSort.Direction)}后调。
     *
     * @param spanCount                      跨距数
     * @param spacing                        间距
     * @param firstRowHaveTopSpaceDecoration 头行有上间距装饰否
     * @param hasFixedSize                   已固定大小
     * @param spruce                         spruce否
     */
    public void gridLayout(int spanCount, int spacing, boolean firstRowHaveTopSpaceDecoration, boolean hasFixedSize, boolean spruce) {
        recyclerView.setLayoutManager(new MyGridLayoutManager(context, spanCount, (recycler, state) -> {
            if (spruce) {
                spruceKit.linearSort(recyclerView, interObjectDelay, reversed, direction, duration);
            }
        }));
        // 固定RecyclerView高（避RecyclerView重measure）
        recyclerView.setHasFixedSize(hasFixedSize);
        recyclerView.addItemDecoration(new GridLayoutSpaceItemDecoration(spanCount, spacing, firstRowHaveTopSpaceDecoration, true));
    }
}
