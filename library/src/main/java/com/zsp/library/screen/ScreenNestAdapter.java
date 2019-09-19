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
    private List<String> conditions;
    private boolean singleSelect;
    private boolean canReverseSelectAfterSingleSelect;
    private List<Integer> defaultSelectIndexList;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private int selectPosition;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    /**
     * constructor
     *
     * @param context                           上下文
     * @param classification                    类别
     * @param conditions                        条件
     * @param singleSelect                      单选否
     * @param canReverseSelectAfterSingleSelect 单选后可反选
     * @param defaultSelectIndexList            默选下标数据
     */
    ScreenNestAdapter(Context context,
                      String classification,
                      List<String> conditions,
                      boolean singleSelect,
                      boolean canReverseSelectAfterSingleSelect,
                      List<Integer> defaultSelectIndexList) {
        this.context = context;
        this.classification = classification;
        this.conditions = conditions;
        this.singleSelect = singleSelect;
        this.canReverseSelectAfterSingleSelect = canReverseSelectAfterSingleSelect;
        this.defaultSelectIndexList = defaultSelectIndexList;
        selectMark();
    }

    void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        defaultSelectValue();
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
                    onRecyclerViewItemClickListener.onItemClick(v, classification, conditions.get(position), true);
                } else if (canReverseSelectAfterSingleSelect) {
                    selectPosition = -1;
                    onRecyclerViewItemClickListener.onItemClick(v, classification, conditions.get(position), false);
                }
            } else {
                boolean preSelected = sparseBooleanArray.get(position);
                onRecyclerViewItemClickListener.onItemClick(v, classification, conditions.get(position), !preSelected);
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
        holder.screenNestItemTv.setText(conditions.get(position));
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
     * 选标记
     */
    private void selectMark() {
        selectPosition = -1;
        if (sparseBooleanArray.size() > 0) {
            sparseBooleanArray.clear();
        }
        // 无默选
        boolean flag = (null != defaultSelectIndexList) && defaultSelectIndexList.size() > 0;
        if (!flag) {
            return;
        }
        // 有默选
        if (singleSelect) {
            selectPosition = (defaultSelectIndexList.get(defaultSelectIndexList.size() - 1));
            return;
        }
        for (Integer integer : defaultSelectIndexList) {
            sparseBooleanArray.append(integer, true);
        }
    }

    /**
     * 默选值
     */
    private void defaultSelectValue() {
        boolean flag = (null != defaultSelectIndexList) && defaultSelectIndexList.size() > 0;
        if (!flag) {
            return;
        }
        if (singleSelect) {
            onRecyclerViewItemClickListener.onItemClick(null, classification, conditions.get(defaultSelectIndexList.get(defaultSelectIndexList.size() - 1)), true);
            return;
        }
        for (Integer integer : defaultSelectIndexList) {
            onRecyclerViewItemClickListener.onItemClick(null, classification, conditions.get(integer), true);
        }
    }

    /**
     * 重置
     */
    void resetting() {
        selectMark();
        notifyDataSetChanged();
        defaultSelectValue();
    }

    @Override
    public int getItemCount() {
        if (null != conditions && conditions.size() != 0) {
            return conditions.size();
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


