package com.zsp.library.screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;
import com.zsp.library.recyclerview.configure.RecyclerViewConfigure;

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
    /**
     * 上下文
     */
    private Context context;
    /**
     * 主体数据键值数据
     */
    private List<List<String>> subjectMapKeyList;
    private List<Map<Integer, Boolean>> subjectMapValueList;
    /**
     * 单选后可反选数据
     */
    private List<String> canReverseSelectAfterSingleSelectList;
    /**
     * 默选数据、默选数据键数据
     */
    private Map<String, List<String>> defaultSelectMap;
    private List<String> defaultSelectMapKeyList;
    /**
     * 短点监听
     */
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    /**
     * 筛选嵌套适配器数据
     */
    private List<ScreenNestAdapter> screenNestAdapters;

    /**
     * constructor
     *
     * @param context 上下文
     */
    ScreenAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设筛选数据
     *
     * @param subjectMap                            主体数据
     * @param canReverseSelectAfterSingleSelectList 单选后可反选数据
     * @param defaultSelectMap                      默选数据
     */
    void setScreeningData(Map<List<String>, Map<Integer, Boolean>> subjectMap,
                          List<String> canReverseSelectAfterSingleSelectList,
                          Map<String, List<String>> defaultSelectMap) {
        this.subjectMapKeyList = new ArrayList<>(subjectMap.keySet());
        this.subjectMapValueList = new ArrayList<>(subjectMap.values());
        this.canReverseSelectAfterSingleSelectList = canReverseSelectAfterSingleSelectList;
        this.defaultSelectMap = defaultSelectMap;
        this.defaultSelectMapKeyList = new ArrayList<>(defaultSelectMap.keySet());
        this.screenNestAdapters = new ArrayList<>(subjectMap.size());
    }

    void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
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
        List<String> leftList = subjectMapKeyList.get(position);
        Map<Integer, Boolean> rightMap = subjectMapValueList.get(position);
        List<Integer> integerList = new ArrayList<>(rightMap.keySet());
        List<Boolean> booleanList = new ArrayList<>(rightMap.values());
        // 类别
        String classification = leftList.get(0);
        holder.screenItemTv.setText(classification);
        // 嵌套（控件）
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(context, holder.screenItemRv);
        recyclerViewConfigure.gridLayout(integerList.get(0), 36, true, false, false);
        // 嵌套（适配器）
        List<String> conditions = leftList.subList(1, leftList.size());
        ScreenNestAdapter screenNestAdapter = new ScreenNestAdapter(context, classification, conditions,
                booleanList.get(0), canReverseSelectAfterSingleSelectList.contains(classification),
                defaultSelectMapKeyList.contains(classification) ? indexExtract(conditions, defaultSelectMap.get(classification)) : null);
        screenNestAdapters.add(screenNestAdapter);
        // 嵌套（控件关联适配器）
        holder.screenItemRv.setAdapter(screenNestAdapter);
        // 嵌套（监听）
        screenNestAdapter.setOnRecyclerViewItemClickListener((view, classification1, condition, selected) -> {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onItemClick(view, classification1, condition, selected);
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
     * 下标提取
     *
     * @param comparedList      被比对数据
     * @param needToCompareList 需比对数据
     * @return 下标数据
     */
    private List<Integer> indexExtract(List<String> comparedList, List<String> needToCompareList) {
        if (null == comparedList || null == needToCompareList) {
            return null;
        }
        int size = needToCompareList.size();
        List<Integer> indexList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String element = needToCompareList.get(i);
            if (comparedList.contains(element)) {
                indexList.add(comparedList.indexOf(element));
            }
        }
        return indexList;
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
        if (null != subjectMapKeyList && subjectMapKeyList.size() != 0) {
            return subjectMapKeyList.size();
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

