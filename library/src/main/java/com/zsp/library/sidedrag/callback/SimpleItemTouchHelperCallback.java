package com.zsp.library.sidedrag.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.sidedrag.adapter.ItemTouchHelperAdapter;
import com.zsp.library.sidedrag.viewholder.ItemTouchHelperViewHolder;
import com.zsp.utilone.density.DensityUtils;

/**
 * @decs: SimpleItemTouchHelperCallback
 * @author: 郑少鹏
 * @date: 2019/8/16 12:10
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;
    private Context context;
    private int color;
    private int drawable;
    private Paint paint = new Paint();

    /**
     * constructor
     *
     * @param context                Context
     * @param itemTouchHelperAdapter ItemTouchHelperAdapter
     * @param color                  int
     * @param drawable               int
     */
    public SimpleItemTouchHelperCallback(Context context, ItemTouchHelperAdapter itemTouchHelperAdapter, int color, int drawable) {
        this.context = context;
        mAdapter = itemTouchHelperAdapter;
        this.color = color;
        this.drawable = drawable;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // Enable drag up and down and right swipe in right direction
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.END;
        // final int swipeFlags =  ItemTouchHelper.END | ItemTouchHelper.START; Enable swipe in both direction
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        // return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION : DEFAULT_SWIPE_ANIMATION_DURATION;
        return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION : 350;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        // Notify the adapter of the move
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the dismissal
        mAdapter.onItemSlideSlipEnd(viewHolder.getAdapterPosition());
    }

    @Override
    public int getBoundingBoxMargin() {
        return super.getBoundingBoxMargin();
    }

    @Override
    public void onChildDraw(@NonNull final Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
        // Fade out the view as it is swiped out of the parent's bounds
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            Bitmap icon;
            if (dx > 0) {
                icon = BitmapFactory.decodeResource(context.getResources(), drawable);
                // Set color for right swipe
                paint.setColor(ContextCompat.getColor(context, color));
                // Draw Rect with varying right side, equal to displacement dX
                c.drawRect((float) itemView.getLeft() + DensityUtils.dipToPxByInt(0), (float) itemView.getTop(), dx + DensityUtils.dipToPxByInt(0),
                        (float) itemView.getBottom(), paint);
                // Set the image icon for right swipe
                c.drawBitmap(icon, (float) itemView.getLeft() + DensityUtils.dipToPxByInt(16), (float) itemView.getTop() +
                        ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2, paint);
                icon.recycle();
            }
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // Let the view holder know that this item is being moved or dragged
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSlideSlipStart(context);
        }
        super.onSelectedChanged(viewHolder, actionState);
        /*final boolean swiping = actionState == ItemTouchHelper.ACTION_STATE_SWIPE;
        swipeRefreshLayout.setEnabled(!swiping);*/
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // Tell the view holder it's time to restore the idle state
        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemSlideSlipStop(context);

    }

    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        /*return super.getMoveThreshold(viewHolder);*/
        return 0.1f;
        /*return super.getMoveThreshold(0.5f);*/
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        /*if (viewHolder instanceof RecyclerView.ViewHolder) {
            return 1f;
        }
        return super.getSwipeThreshold(viewHolder);*/
        return 0.9f;
    }
}
