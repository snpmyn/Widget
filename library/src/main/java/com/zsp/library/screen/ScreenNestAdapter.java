package com.zsp.library.screen;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;

import java.util.List;

/**
 * Created on 2019/5/24.
 *
 * @author 郑少鹏
 * @desc 筛选嵌套适配器
 */
public class ScreenNestAdapter extends RecyclerView.Adapter<ScreenNestAdapter.ViewHolder> {
    private Context context;
    private String classification;
    private List<String> strings;
    private boolean singleSelect;
    private boolean canCancelAfterSingleSelect;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private int selectPosition = -1;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    /**
     * constructor
     *
     * @param context                    上下文
     * @param classification             类别
     * @param strings                    数据
     * @param singleSelect               单选否
     * @param canCancelAfterSingleSelect 单选后可取消
     */
    ScreenNestAdapter(Context context, String classification, List<String> strings, boolean singleSelect, boolean canCancelAfterSingleSelect) {
        this.context = context;
        this.classification = classification;
        this.strings = strings;
        this.singleSelect = singleSelect;
        this.canCancelAfterSingleSelect = canCancelAfterSingleSelect;
    }

    void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.screen_nest_item, viewGroup, false);
        view.setOnClickListener(v -> {
            int position = (Integer) v.getTag();
            if (singleSelect) {
                if (selectPosition != position) {
                    selectPosition = position;
                    onRecyclerViewItemClickListener.onItemClick(v, classification, strings.get(position), true);
                } else if (canCancelAfterSingleSelect) {
                    selectPosition = -1;
                    onRecyclerViewItemClickListener.onItemClick(v, classification, strings.get(position), false);
                }
            } else {
                boolean preSelected = sparseBooleanArray.get(position);
                onRecyclerViewItemClickListener.onItemClick(v, classification, strings.get(position), !preSelected);
                sparseBooleanArray.put(position, !preSelected);
            }
            notifyDataSetChanged();
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        // 条件
        holder.screenNestItemTv.setText(strings.get(position));
        // 选否
        if (singleSelect) {
            holder.screenNestItemTv.setSelected(selectPosition == position);
        } else {
            holder.screenNestItemTv.setSelected(sparseBooleanArray.get(position));
        }
    }

    public interface OnRecyclerViewItemClickListener {
        /**
         * 短点
         *
         * @param view           视图
         * @param classification 类别
         * @param condition      条件
         * @param selected       选否
         */
        void onItemClick(View view, String classification, String condition, boolean selected);
    }

    /**
     * 重置
     */
    void resetting() {
        selectPosition = -1;
        if (sparseBooleanArray.size() > 0) {
            sparseBooleanArray.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null != strings && strings.size() != 0) {
            return strings.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView screenNestItemTv;

        private ViewHolder(@NonNull View view) {
            super(view);
            screenNestItemTv = view.findViewById(R.id.screenNestItemTv);
        }
    }
}


