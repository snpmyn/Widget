package com.zsp.library.screen.kit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.zsp.library.R;
import com.zsp.library.recyclerview.configure.RecyclerViewConfigure;
import com.zsp.library.screen.adapter.ScreenAdapter;
import com.zsp.library.screen.bean.MutuallyExclusiveBean;
import com.zsp.library.screen.listener.ScreenHandleListener;
import com.zsp.utilone.data.IntUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2019/6/10.
 *
 * @author 郑少鹏
 * @desc ScreenHandleKit
 */
public class ScreenHandleKit implements View.OnClickListener {
    /**
     * 上下文
     */
    private Context context;
    /**
     * BottomSheetDialog
     */
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView bottomSheetDialogMemberScreenRv;
    /**
     * 筛选适配器
     */
    private ScreenAdapter screenAdapter;
    /**
     * 主体数据
     */
    private Map<List<String>, Map<Integer, Boolean>> subjectMap;
    /**
     * 单选后可反选数据
     */
    private List<String> canCancelAfterSingleSelectList;
    /**
     * 默选数据
     */
    private Map<String, List<String>> defaultSelectMap;
    /**
     * 互斥数据、互斥数据类别数据
     */
    private List<MutuallyExclusiveBean> mutuallyExclusiveBeanList;
    private List<String> mutuallyExclusiveBeanListClassificationList;
    /**
     * 筛选操作接口
     */
    private ScreenHandleListener screenHandleListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public ScreenHandleKit(Context context) {
        this.context = context;
        this.screenAdapter = new ScreenAdapter(context);
        this.subjectMap = new LinkedHashMap<>();
        stepBottomSheetDialog();
    }

    /**
     * 初始BottomSheetDialog
     */
    private void stepBottomSheetDialog() {
        @SuppressLint("InflateParams") View bottomSheetDialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_screen, null);
        // 重置
        MaterialButton bottomSheetDialogMemberScreenMbResetting = bottomSheetDialogView.findViewById(R.id.bottomSheetDialogMemberScreenMbResetting);
        bottomSheetDialogMemberScreenMbResetting.setOnClickListener(this);
        // 确定
        MaterialButton bottomSheetDialogMemberScreenMbEnsure = bottomSheetDialogView.findViewById(R.id.bottomSheetDialogMemberScreenMbEnsure);
        bottomSheetDialogMemberScreenMbEnsure.setOnClickListener(this);
        // RecyclerView
        bottomSheetDialogMemberScreenRv = bottomSheetDialogView.findViewById(R.id.bottomSheetDialogMemberScreenRv);
        RecyclerViewConfigure recyclerViewConfigure = new RecyclerViewConfigure(context, bottomSheetDialogMemberScreenRv);
        recyclerViewConfigure.linearVerticalLayout(false, 0, false, false);
//        bottomSheetDialogMemberScreenRv.setNestedScrollingEnabled(false);
        // BottomSheetDialog
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
    }

    /**
     * 打包字符串条件
     *
     * @param classification 类别
     * @param spanCount      跨距数
     * @param singleSelect   单选否
     * @param conditions     条件
     */
    public void packStringConditions(String classification, int spanCount, boolean singleSelect, String... conditions) {
        List<String> list = new ArrayList<>();
        list.add(classification);
        list.addAll(Arrays.asList(conditions));
        Map<Integer, Boolean> map = new LinkedHashMap<>();
        map.put(spanCount, singleSelect);
        this.subjectMap.put(list, map);
    }

    /**
     * 打包集合条件
     *
     * @param classification 类别
     * @param spanCount      跨距数
     * @param singleSelect   单选否
     * @param conditions     条件
     */
    public void packListConditions(String classification, int spanCount, boolean singleSelect, List<String> conditions) {
        List<String> list = new ArrayList<>();
        list.add(classification);
        list.addAll(conditions);
        Map<Integer, Boolean> map = new LinkedHashMap<>();
        map.put(spanCount, singleSelect);
        this.subjectMap.put(list, map);
    }

    /**
     * 单选后可反选
     *
     * @param classifications 类别
     */
    public void canReverseSelectAfterSingleSelect(String... classifications) {
        canCancelAfterSingleSelectList = new ArrayList<>();
        for (String classification : classifications) {
            if (canCancelAfterSingleSelectList.contains(classification)) {
                return;
            }
            canCancelAfterSingleSelectList.add(classification);
        }
    }

    /**
     * 默选
     * <p>
     * 单选仅呈现默选一条件。
     * 多选可呈现默选多条件。
     *
     * @param classification 类别
     * @param conditions     条件
     */
    public void defaultSelect(String classification, String... conditions) {
        if (null == defaultSelectMap) {
            defaultSelectMap = new LinkedHashMap<>();
        }
        defaultSelectMap.put(classification, Arrays.asList(conditions));
    }

    /**
     * 互斥
     *
     * @param strings 数据（组ID，类别、组ID，类别...）
     */
    public void mutuallyExclusive(String... strings) {
        mutuallyExclusiveBeanList = new ArrayList<>();
        mutuallyExclusiveBeanListClassificationList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (IntUtils.even(i)) {
                String classification = strings[i + 1];
                mutuallyExclusiveBeanList.add(new MutuallyExclusiveBean(strings[i], classification));
                mutuallyExclusiveBeanListClassificationList.add(classification);
            }
        }
    }

    /**
     * 关联
     */
    public void associate() {
        screenAdapter.setScreenAdapterItemClickListener((view, classification, condition, selected) -> screenHandleListener.click(view, classification, condition, selected));
        screenAdapter.setScreeningData(subjectMap, canCancelAfterSingleSelectList, defaultSelectMap, mutuallyExclusiveBeanList, mutuallyExclusiveBeanListClassificationList);
        bottomSheetDialogMemberScreenRv.setAdapter(screenAdapter);
    }

    /**
     * 显
     */
    public void show() {
        bottomSheetDialog.show();
    }

    /**
     * 消
     */
    public void dismiss() {
        bottomSheetDialog.dismiss();
    }

    /**
     * 重置
     */
    public void resetting() {
        screenAdapter.resetting();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bottomSheetDialogMemberScreenMbResetting) {
            // 重置
            screenHandleListener.resetting();
        } else if (v.getId() == R.id.bottomSheetDialogMemberScreenMbEnsure) {
            // 确定
            screenHandleListener.ensure();
        }
    }

    /**
     * 设筛选操作监听
     *
     * @param screenHandleListener 筛选操作监听
     */
    public void setScreenHandleListener(ScreenHandleListener screenHandleListener) {
        this.screenHandleListener = screenHandleListener;
    }
}
