package com.zsp.library.focusresize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;
import com.zsp.utilone.screen.ScreenUtils;

/**
 * @decs: BaseFocusResizeAdapter
 * @author: 郑少鹏
 * @date: 2019/9/5 17:12
 */
public abstract class BaseFocusResizeAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    private static final int VIEW_TYPE_FOOTER = 1;
    private Context context;
    private int height;

    public BaseFocusResizeAdapter(Context context, int height) {
        this.context = context;
        this.height = height;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            return (T) new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.focus_resize, parent, false));
        } else {
            return onCreateFooterViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        // Loader ViewHolder
        if (position != getFooterItemCount()) {
            onBindFooterViewHolder((T) viewHolder, position);
        }
    }

    @Override
    public final int getItemCount() {
        if (getFooterItemCount() == 0) {
            return 0;
        }
        return getFooterItemCount() + 1;
    }

    @Override
    public final int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }
        return getItemFooterViewType();
    }

    /**
     * onItemBigResize
     *
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   int
     * @param dyAbs      int
     */
    public abstract void onItemBigResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs);

    /**
     * onItemBigResizeScrolled
     *
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   int
     * @param dyAbs      int
     */
    public abstract void onItemBigResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs);

    /**
     * onItemSmallResizeScrolled
     *
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   int
     * @param dyAbs      int
     */
    public abstract void onItemSmallResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs);

    /**
     * onItemSmallResize
     *
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   int
     * @param dyAbs      int
     */
    public abstract void onItemSmallResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs);

    /**
     * onItemInit
     *
     * @param viewHolder RecyclerView.ViewHolder
     */
    public abstract void onItemInit(RecyclerView.ViewHolder viewHolder);

    /**
     * getItemFooterViewType
     *
     * @return int
     */
    private int getItemFooterViewType() {
        return 0;
    }

    /**
     * getFooterItemCount
     *
     * @return int
     */
    public abstract int getFooterItemCount();

    /**
     * onCreateFooterViewHolder
     *
     * @param parent   ViewGroup
     * @param viewType int
     * @return T
     */
    public abstract T onCreateFooterViewHolder(ViewGroup parent, int viewType);

    /**
     * onBindFooterViewHolder
     *
     * @param holder   T
     * @param position int
     */
    public abstract void onBindFooterViewHolder(T holder, int position);

    public int getHeight() {
        return height;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View v) {
            super(v);
            v.getLayoutParams().height = (ScreenUtils.screenHeight(context) - (height * 3));
        }
    }
}