package com.zsp.library.screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;
import com.zsp.library.recyclerview.RecyclerViewKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 2019/5/24.
 *
 * @author 郑少鹏
 * @desc 筛选适配器
 */
public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.ViewHolder> {
    private Context context;
    private List<List<String>> keyList;
    private List<Map<Integer, Boolean>> valueList;
    private List<String> canCancelAfterSingleSelectList;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<ScreenNestAdapter> screenNestAdapters;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public ScreenAdapter(Context context) {
        this.context = context;
    }

    void setScreeningData(Map<List<String>, Map<Integer, Boolean>> map, List<String> list) {
        this.keyList = new ArrayList<>(map.keySet());
        this.valueList = new ArrayList<>(map.values());
        this.canCancelAfterSingleSelectList = list;
        this.screenNestAdapters = new ArrayList<>(map.size());
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.screen_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        List<String> leftList = keyList.get(position);
        Map<Integer, Boolean> rightList = valueList.get(position);
        List<Integer> integerList = new ArrayList<>(rightList.keySet());
        List<Boolean> booleanList = new ArrayList<>(rightList.values());
        // 类别
        holder.screenItemTv.setText(leftList.get(0));
        // 嵌套（控件）
        RecyclerViewKit recyclerViewKit = new RecyclerViewKit(context, holder.screenItemRv);
        recyclerViewKit.gridLayout(integerList.get(0), 36, true, false, false);
        // 嵌套（适配器）
        ScreenNestAdapter screenNestAdapter = new ScreenNestAdapter(context,
                leftList.subList(1, leftList.size()), leftList.get(0), booleanList.get(0), canCancelAfterSingleSelectList.contains(leftList.get(0)));
        screenNestAdapters.add(screenNestAdapter);
        // 嵌套（控件关联适配器）
        holder.screenItemRv.setAdapter(screenNestAdapter);
        // 嵌套（监听）
        screenNestAdapter.setOnRecyclerViewItemClickListener((view, classification, condition, selected) -> {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onItemClick(view, classification, condition, selected);
            }
        });
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
        for (ScreenNestAdapter screenNestAdapter : screenNestAdapters) {
            screenNestAdapter.resetting();
        }
    }

    @Override
    public int getItemCount() {
        if (null != keyList && keyList.size() != 0) {
            return keyList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView screenItemTv;
        private RecyclerView screenItemRv;

        private ViewHolder(@NonNull View view) {
            super(view);
            screenItemTv = view.findViewById(R.id.screenItemTv);
            screenItemRv = view.findViewById(R.id.screenItemRv);
        }
    }
}

