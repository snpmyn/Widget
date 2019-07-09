package com.zsp.library.screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.zsp.library.R;
import com.zsp.library.recyclerview.RecyclerViewKit;

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
     * 筛选
     */
    private ScreenAdapter screenAdapter;
    private Map<List<String>, Map<Integer, Boolean>> map;
    private List<String> canCancelAfterSingleSelectList;
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
        this.map = new LinkedHashMap<>();
        this.canCancelAfterSingleSelectList = new ArrayList<>();
        stepBottomSheetDialog();
    }

    /**
     * 初始BottomSheetDialog
     */
    private void stepBottomSheetDialog() {
        @SuppressLint("InflateParams") View bottomSheetDialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_screen, null);
        MaterialButton bottomSheetDialogMemberScreenMbResetting = bottomSheetDialogView.findViewById(R.id.bottomSheetDialogMemberScreenMbResetting);
        bottomSheetDialogMemberScreenMbResetting.setOnClickListener(this);
        MaterialButton bottomSheetDialogMemberScreenMbEnsure = bottomSheetDialogView.findViewById(R.id.bottomSheetDialogMemberScreenMbEnsure);
        bottomSheetDialogMemberScreenMbEnsure.setOnClickListener(this);
        bottomSheetDialogMemberScreenRv = bottomSheetDialogView.findViewById(R.id.bottomSheetDialogMemberScreenRv);
        RecyclerViewKit recyclerViewKit = new RecyclerViewKit(context, bottomSheetDialogMemberScreenRv);
        recyclerViewKit.linearVerticalLayout(false, 0, false, false);
        bottomSheetDialogMemberScreenRv.setNestedScrollingEnabled(false);
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
    }

    /**
     * 通字符串条件打包
     *
     * @param classification 类别
     * @param spanCount      跨距数
     * @param singleSelect   单选否
     * @param conditions     条件
     */
    public void packByStringConditions(String classification, int spanCount, boolean singleSelect, String... conditions) {
        List<String> list = new ArrayList<>();
        list.add(classification);
        list.addAll(Arrays.asList(conditions));
        Map<Integer, Boolean> map = new LinkedHashMap<>();
        map.put(spanCount, singleSelect);
        this.map.put(list, map);
    }

    /**
     * 通集合条件打包
     *
     * @param classification 类别
     * @param spanCount      跨距数
     * @param singleSelect   单选否
     * @param conditions     条件
     */
    public void packByListConditions(String classification, int spanCount, boolean singleSelect, List<String> conditions) {
        List<String> list = new ArrayList<>();
        list.add(classification);
        list.addAll(conditions);
        Map<Integer, Boolean> map = new LinkedHashMap<>();
        map.put(spanCount, singleSelect);
        this.map.put(list, map);
    }

    /**
     * 支持单选后取消
     *
     * @param classifications 条件
     */
    public void supportCancelAfterSingleSelect(String... classifications) {
        for (String classification : classifications) {
            if (canCancelAfterSingleSelectList.contains(classification)) {
                return;
            }
            canCancelAfterSingleSelectList.add(classification);
        }
    }

    /**
     * 关联
     */
    public void associate() {
        screenAdapter.setOnRecyclerViewItemClickListener((view, classification, condition, selected) -> screenHandleListener.click(view, classification, condition, selected));
        screenAdapter.setScreeningData(map, canCancelAfterSingleSelectList);
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

    /**
     * 重置
     */
    public void resetting() {
        screenAdapter.resetting();
    }

    /**
     * 筛选操作监听
     */
    public interface ScreenHandleListener {
        /**
         * 点
         *
         * @param view           视图
         * @param classification 类别
         * @param condition      条件
         * @param selected       选否
         */
        void click(View view, String classification, String condition, boolean selected);

        /**
         * 重置
         */
        void resetting();

        /**
         * 确定
         */
        void ensure();
    }
}
