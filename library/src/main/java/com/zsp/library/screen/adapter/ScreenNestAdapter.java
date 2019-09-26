package com.zsp.library.screen.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;
import com.zsp.library.screen.listener.MutuallyExclusiveClickListener;
import com.zsp.library.screen.listener.ScreenNestAdapterItemClickListener;

import java.util.List;

/**
 * Created on 2019/5/24.
 *
 * @author 郑少鹏
 * @desc 筛选嵌套适配器
 */
public class ScreenNestAdapter extends RecyclerView.Adapter<ScreenNestAdapter.ViewHolder> {
    /**
     * 类别
     */
    public String classification;
    /**
     * 选位
     */
    int selectPosition;
    /**
     * SparseBooleanArray
     */
    SparseBooleanArray sparseBooleanArray;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 条件
     */
    private List<String> conditions;
    /**
     * 单选
     */
    private boolean singleSelect;
    /**
     * 单选后可反选
     */
    private boolean canReverseSelectAfterSingleSelect;
    /**
     * 默选下标数据
     */
    private List<Integer> defaultSelectIndexList;
    /**
     * 互斥
     */
    private boolean mutuallyExclusive;
    /**
     * 筛选嵌套适配器条目短点监听
     */
    private ScreenNestAdapterItemClickListener screenNestAdapterItemClickListener;
    /**
     * 互斥点监听
     */
    private MutuallyExclusiveClickListener mutuallyExclusiveClickListener;

    /**
     * constructor
     *
     * @param context                           上下文
     * @param classification                    类别
     * @param conditions                        条件
     * @param singleSelect                      单选
     * @param canReverseSelectAfterSingleSelect 单选后可反选
     * @param defaultSelectIndexList            默选下标数据
     * @param mutuallyExclusive                 互斥
     */
    ScreenNestAdapter(Context context,
                      String classification,
                      List<String> conditions,
                      boolean singleSelect,
                      boolean canReverseSelectAfterSingleSelect,
                      List<Integer> defaultSelectIndexList,
                      boolean mutuallyExclusive) {
        this.context = context;
        this.classification = classification;
        this.conditions = conditions;
        this.singleSelect = singleSelect;
        this.canReverseSelectAfterSingleSelect = canReverseSelectAfterSingleSelect;
        this.defaultSelectIndexList = defaultSelectIndexList;
        this.mutuallyExclusive = mutuallyExclusive;
        selectMark();
    }

    /**
     * 设筛选嵌套适配器条目短点监听
     *
     * @param screenNestAdapterItemClickListener 筛选嵌套适配器条目短点监听
     */
    void setScreenNestAdapterItemClickListener(ScreenNestAdapterItemClickListener screenNestAdapterItemClickListener) {
        this.screenNestAdapterItemClickListener = screenNestAdapterItemClickListener;
        defaultSelectValue();
    }

    /**
     * 设互斥点监听
     *
     * @param mutuallyExclusiveClickListener 互斥点监听
     */
    void setMutuallyExclusiveClickListener(MutuallyExclusiveClickListener mutuallyExclusiveClickListener) {
        this.mutuallyExclusiveClickListener = mutuallyExclusiveClickListener;
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
                    screenNestAdapterItemClickListener.onItemClick(v, classification, conditions.get(position), true);
                } else if (canReverseSelectAfterSingleSelect) {
                    selectPosition = -1;
                    screenNestAdapterItemClickListener.onItemClick(v, classification, conditions.get(position), false);
                }
            } else {
                boolean preSelected = sparseBooleanArray.get(position);
                screenNestAdapterItemClickListener.onItemClick(v, classification, conditions.get(position), !preSelected);
                sparseBooleanArray.put(position, !preSelected);
            }
            notifyDataSetChanged();
            // 互斥
            if (mutuallyExclusive) {
                mutuallyExclusiveClickListener.click(classification);
            }
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

    /**
     * 选标记
     */
    private void selectMark() {
        selectPosition = -1;
        if (null == sparseBooleanArray) {
            sparseBooleanArray = new SparseBooleanArray();
        }
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
            screenNestAdapterItemClickListener.onItemClick(null, classification, conditions.get(defaultSelectIndexList.get(defaultSelectIndexList.size() - 1)), true);
            return;
        }
        for (Integer integer : defaultSelectIndexList) {
            screenNestAdapterItemClickListener.onItemClick(null, classification, conditions.get(integer), true);
        }
    }

    /**
     * 重置
     * <p>
     * 场景一：左上角重置按钮；
     * 场景二：互斥场景选中类别外类别重置。
     */
    void resetting() {
        selectMark();
        notifyDataSetChanged();
        defaultSelectValue();
        if (mutuallyExclusive) {
            screenNestAdapterItemClickListener.onItemClick(null, classification, null, false);
        }
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


