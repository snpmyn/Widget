package example.onepartylibrary.sidedrag.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.zsp.library.sidedrag.adapter.ItemTouchHelperAdapter;
import com.zsp.library.sidedrag.listener.ItemDragListener;
import com.zsp.library.sidedrag.viewholder.ItemTouchHelperViewHolder;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2019/8/16.
 *
 * @author 郑少鹏
 * @desc 侧拖适配器
 */
public class SideDragAdapter extends RecyclerView.Adapter<SideDragAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private List<String> list;
    private ItemDragListener dragStartListener;

    /**
     * constructor
     *
     * @param context           Context
     * @param dragStartListener ItemDragListener
     */
    public SideDragAdapter(Context context, ItemDragListener dragStartListener) {
        this.context = context;
        this.dragStartListener = dragStartListener;
        this.list = new ArrayList<>();
    }

    public void setSideDragData(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.side_drag_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.sideDragItemTv.setText(list.get(position));
        holder.sideDragItemIv.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dragStartListener.onStartDrag(holder);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.sideDragItemTv)
        TextView sideDragItemTv;
        @BindView(R.id.sideDragItemIv)
        ImageView sideDragItemIv;
        @BindView(R.id.sideDragItemMcv)
        MaterialCardView sideDragItemMcv;

        private ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * 侧滑开始
         *
         * @param context 上下文
         */
        @Override
        public void onItemSlideSlipStart(Context context) {
            sideDragItemMcv.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            sideDragItemTv.setTextColor(ContextCompat.getColor(context, R.color.background));
            sideDragItemIv.setColorFilter(ContextCompat.getColor(context, R.color.background), PorterDuff.Mode.SRC_IN);
        }

        /**
         * 侧滑停止
         *
         * @param context 上下文
         */
        @Override
        public void onItemSlideSlipStop(Context context) {
            sideDragItemMcv.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
            sideDragItemTv.setTextColor(ContextCompat.getColor(context, R.color.fontInput));
            sideDragItemIv.setColorFilter(ContextCompat.getColor(context, R.color.gray), PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * 移动
     *
     * @param fromPosition 起始位
     * @param toPosition   终止位
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 侧滑结束
     *
     * @param position 位置
     */
    @Override
    public void onItemSlideSlipEnd(int position) {
        notifyItemRangeChanged(0, getItemCount());
    }
}
