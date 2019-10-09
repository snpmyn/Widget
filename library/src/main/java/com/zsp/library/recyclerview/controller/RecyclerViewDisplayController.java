package com.zsp.library.recyclerview.controller;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created on 2019/9/3.
 *
 * @author 郑少鹏
 * @desc RecyclerViewDisplayController
 */
public class RecyclerViewDisplayController {
    /**
     * 展示
     *
     * @param recyclerView 控件
     * @param adapter      适配器
     */
    public static void display(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (adapter.hasObservers()) {
            adapter.notifyDataSetChanged();
        } else {
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * 动删
     *
     * @param adapter  适配器
     * @param position 位
     * @param list     数据
     * @param <T>      泛型
     */
    public static <T> void deleteDynamic(RecyclerView.Adapter adapter, int position, List<T> list) {
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, list.size() - position);
        list.remove(position);
    }

    /**
     * 条目视图可见
     *
     * @param itemView 条目视图
     */
    public static void itemViewVisible(View itemView) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (layoutParams.width == LinearLayout.LayoutParams.MATCH_PARENT) {
            return;
        }
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        itemView.setLayoutParams(layoutParams);

        itemView.setVisibility(View.VISIBLE);
    }

    /**
     * 条目视图不可见
     *
     * @param itemView 条目视图
     */
    public static void itemViewGone(View itemView) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (layoutParams.width == 0) {
            return;
        }
        itemView.setVisibility(View.GONE);
        layoutParams.width = 0;
        layoutParams.height = 0;
        itemView.setLayoutParams(layoutParams);
    }
}
