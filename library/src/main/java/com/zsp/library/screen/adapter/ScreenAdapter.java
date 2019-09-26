package com.zsp.library.screen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;
import com.zsp.library.recyclerview.configure.RecyclerViewConfigure;
import com.zsp.library.screen.bean.MutuallyExclusiveBean;
import com.zsp.library.screen.listener.ScreenAdapterItemClickListener;

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
     * 互斥数据、互斥数据类别数据
     */
    private List<MutuallyExclusiveBean> mutuallyExclusiveBeanList;
    private List<String> mutuallyExclusiveBeanListClassificationList;
    /**
     * 筛选适配器条目短点监听
     */
    private ScreenAdapterItemClickListener screenAdapterItemClickListener;
    /**
     * 筛选嵌套适配器数据
     */
    private List<ScreenNestAdapter> screenNestAdapterList;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public ScreenAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设筛选数据
     *
     * @param subjectMap                                  主体数据
     * @param canReverseSelectAfterSingleSelectList       单选后可反选数据
     * @param defaultSelectMap                            默选数据
     * @param mutuallyExclusiveBeanList                   互斥数据
     * @param mutuallyExclusiveBeanListClassificationList 互斥数据类别数据
     */
    public void setScreeningData(Map<List<String>, Map<Integer, Boolean>> subjectMap,
                                 List<String> canReverseSelectAfterSingleSelectList,
                                 Map<String, List<String>> defaultSelectMap,
                                 List<MutuallyExclusiveBean> mutuallyExclusiveBeanList,
                                 List<String> mutuallyExclusiveBeanListClassificationList) {
        this.subjectMapKeyList = new ArrayList<>(subjectMap.keySet());
        this.subjectMapValueList = new ArrayList<>(subjectMap.values());
        this.canReverseSelectAfterSingleSelectList = canReverseSelectAfterSingleSelectList;
        this.defaultSelectMap = defaultSelectMap;
        this.mutuallyExclusiveBeanList = mutuallyExclusiveBeanList;
        this.mutuallyExclusiveBeanListClassificationList = mutuallyExclusiveBeanListClassificationList;
        this.defaultSelectMapKeyList = new ArrayList<>(defaultSelectMap.keySet());
        this.screenNestAdapterList = new ArrayList<>(subjectMap.size());
    }

    /**
     * 设筛选适配器条目短点监听
     *
     * @param screenAdapterItemClickListener 筛选适配器条目短点监听
     */
    public void setScreenAdapterItemClickListener(ScreenAdapterItemClickListener screenAdapterItemClickListener) {
        this.screenAdapterItemClickListener = screenAdapterItemClickListener;
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
                defaultSelectMapKeyList.contains(classification) ? indexExtract(conditions, defaultSelectMap.get(classification)) : null,
                mutuallyExclusiveBeanListClassificationList.contains(classification));
        screenNestAdapterList.add(screenNestAdapter);
        // 嵌套（控件关联适配器）
        holder.screenItemRv.setAdapter(screenNestAdapter);
        // 嵌套（监听）
        screenNestAdapter.setScreenNestAdapterItemClickListener((view, classification1, condition, selected) -> {
            if (screenAdapterItemClickListener != null) {
                screenAdapterItemClickListener.onItemClick(view, classification1, condition, selected);
            }
        });
        screenNestAdapter.setMutuallyExclusiveClickListener(this::mutuallyExclusive);
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
     * 互斥
     *
     * @param classification 类别
     */
    private void mutuallyExclusive(String classification) {
        String groupId = null;
        List<String> classifications = new ArrayList<>();
        // 互斥组ID
        for (MutuallyExclusiveBean mutuallyExclusiveBean : mutuallyExclusiveBeanList) {
            if (classification.equals(mutuallyExclusiveBean.getClassification())) {
                groupId = mutuallyExclusiveBean.getGroupId();
                break;
            }
        }
        // 互斥类别
        for (MutuallyExclusiveBean mutuallyExclusive : mutuallyExclusiveBeanList) {
            if (groupId != null && groupId.equals(mutuallyExclusive.getGroupId())) {
                classifications.add(mutuallyExclusive.getClassification());
            }
        }
        // 除自身
        classifications.remove(classification);
        // 剩余互斥类别重置
        for (ScreenNestAdapter otherScreenNestAdapter : screenNestAdapterList) {
            boolean flag = classifications.contains(otherScreenNestAdapter.classification) &&
                    (otherScreenNestAdapter.selectPosition != -1 || otherScreenNestAdapter.sparseBooleanArray.size() > 0);
            if (flag) {
                otherScreenNestAdapter.resetting();
            }
        }
    }

    /**
     * 重置
     */
    public void resetting() {
        for (ScreenNestAdapter screenNestAdapter : screenNestAdapterList) {
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

