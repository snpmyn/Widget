package com.zsp.library.focusresize;

import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import timber.log.Timber;

/**
 * @decs: FocusResizeScrollListener
 * @author: 郑少鹏
 * @date: 2019/9/5 17:16
 */
public class FocusResizeScrollListener<T extends BaseFocusResizeAdapter> extends RecyclerView.OnScrollListener {
    private final int heightCollapsedItem;
    private final int heightExpandedItem;
    private int itemToResize;
    private boolean init = false;
    private final LinearLayoutManager mLinearLayoutManager;
    private int dyAbs;
    private final T adapter;

    /**
     * constructor
     *
     * @param adapter             T
     * @param linearLayoutManager LinearLayoutManager
     */
    public FocusResizeScrollListener(@NonNull T adapter, LinearLayoutManager linearLayoutManager) {
        this.adapter = adapter;
        heightCollapsedItem = adapter.getHeight();
        heightExpandedItem = heightCollapsedItem * 3;
        mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        try {
            if (mLinearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                dyAbs = Math.abs(dy);
                int totalItemCount = mLinearLayoutManager.getItemCount();
                itemToResize = dy > 0 ? 1 : 0;
                initFocusResize(recyclerView);
                calculateScrolledPosition(totalItemCount, recyclerView);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void calculateScrolledPosition(int totalItemCount, RecyclerView recyclerView) {
        for (int j = 0; j < totalItemCount - 1; j++) {
            View view = recyclerView.getChildAt(j);
            if (view != null) {
                if (!(recyclerView.getChildViewHolder(view) instanceof BaseFocusResizeAdapter.FooterViewHolder)) {
                    if (j == itemToResize) {
                        onItemBigResize(view, recyclerView);
                    } else {
                        onItemSmallResize(view, recyclerView);
                    }
                    view.requestLayout();
                }
            }
        }
    }

    private void onItemSmallResize(@NonNull View view, RecyclerView recyclerView) {
        if (view.getLayoutParams().height - dyAbs <= heightCollapsedItem) {
            view.getLayoutParams().height = heightCollapsedItem;
        } else if (view.getLayoutParams().height >= heightCollapsedItem) {
            view.getLayoutParams().height -= (dyAbs * 2);
        }
        adapter.onItemSmallResize(recyclerView.getChildViewHolder(view), itemToResize, dyAbs);
    }

    private void onItemBigResize(@NonNull View view, RecyclerView recyclerView) {
        if (view.getLayoutParams().height + dyAbs >= heightExpandedItem) {
            view.getLayoutParams().height = heightExpandedItem;
        } else {
            view.getLayoutParams().height += (dyAbs * 2);
        }
        adapter.onItemBigResize(recyclerView.getChildViewHolder(view), itemToResize, dyAbs);
    }

    private void initFocusResize(RecyclerView recyclerView) {
        if (!init) {
            init = true;
            View view = recyclerView.getChildAt(0);
            view.getLayoutParams().height = heightExpandedItem;
            adapter.onItemInit(recyclerView.getChildViewHolder(view));
        }
    }

    private int calculatePositionScrolledDown(RecyclerView recyclerView) {
        int positionScrolled;
        if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == mLinearLayoutManager.getItemCount() - 1) {
            positionScrolled = itemToResize - 1;
            mLinearLayoutManager.scrollToPositionWithOffset(mLinearLayoutManager.findFirstVisibleItemPosition(), 0);
        } else {
            if (recyclerView.getChildAt(itemToResize).getHeight() > recyclerView.getChildAt(itemToResize - 1).getHeight()) {
                positionScrolled = itemToResize;
                mLinearLayoutManager.scrollToPositionWithOffset(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition(), 0);
            } else {
                positionScrolled = itemToResize - 1;
                mLinearLayoutManager.scrollToPositionWithOffset(mLinearLayoutManager.findFirstVisibleItemPosition(), 0);
            }
        }
        return positionScrolled;
    }

    private int calculatePositionScrolledUp(@NonNull RecyclerView recyclerView) {
        int positionScrolled;
        if (recyclerView.getChildAt(itemToResize).getHeight() > recyclerView.getChildAt(itemToResize + 1).getHeight()) {
            positionScrolled = itemToResize;
            mLinearLayoutManager.scrollToPositionWithOffset(mLinearLayoutManager.findFirstVisibleItemPosition(), 0);
        } else {
            positionScrolled = itemToResize + 1;
            mLinearLayoutManager.scrollToPositionWithOffset(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition(), 0);
        }
        return positionScrolled;
    }

    private void forceScrollItem(@NonNull RecyclerView recyclerView, View view, int j, int positionScrolled) {
        if (!(recyclerView.getChildViewHolder(view) instanceof BaseFocusResizeAdapter.FooterViewHolder)) {
            if (j == positionScrolled) {
                view.getLayoutParams().height = heightExpandedItem;
                adapter.onItemBigResizeScrolled(recyclerView.getChildViewHolder(view), itemToResize, dyAbs);
            } else {
                if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == mLinearLayoutManager.getItemCount() - 1
                        || mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == -1) {
                    view.getLayoutParams().height = heightExpandedItem;
                    adapter.onItemBigResizeScrolled(recyclerView.getChildViewHolder(view), itemToResize, dyAbs);
                } else {
                    view.getLayoutParams().height = heightCollapsedItem;
                    adapter.onItemSmallResizeScrolled(recyclerView.getChildViewHolder(view), itemToResize, dyAbs);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        try {
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (mLinearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    int positionScrolled = (itemToResize == 1) ? calculatePositionScrolledDown(recyclerView) : calculatePositionScrolledUp(recyclerView);
                    for (int j = 0; j < mLinearLayoutManager.getItemCount() - 1; j++) {
                        View view = recyclerView.getChildAt(j);
                        if (view != null) {
                            forceScrollItem(recyclerView, view, j, positionScrolled);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}