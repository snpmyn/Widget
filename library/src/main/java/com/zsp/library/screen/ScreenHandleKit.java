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
    /**
     * 筛选操作接口
     */
    private ScreenHandleListener screenHandleListener;

    /**
     * constructor
     *
     * @param context       上下文
     * @param screenAdapter 筛选适配器
     */
    public ScreenHandleKit(Context context, ScreenAdapter screenAdapter) {
        this.context = context;
        this.screenAdapter = screenAdapter;
        this.map = new LinkedHashMap<>();
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
        recyclerViewKit.linearVerticalLayout();
        bottomSheetDialogMemberScreenRv.setNestedScrollingEnabled(false);
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
    }

    /**
     * 通字符串组装数据
     *
     * @param classification 类别
     * @param spanCount      跨距数
     * @param singleSelect   单选否
     * @param conditions     条件
     */
    public void assemblyDataByString(String classification, int spanCount, boolean singleSelect, String... conditions) {
        List<String> list = new ArrayList<>();
        Map<Integer, Boolean> map = new LinkedHashMap<>();
        list.add(classification);
        list.addAll(Arrays.asList(conditions));
        map.put(spanCount, singleSelect);
        this.map.put(list, map);
    }

    /**
     * 通集合组装数据
     *
     * @param classification 类别
     * @param spanCount      跨距数
     * @param singleSelect   单选否
     * @param conditions     条件
     */
    public void assemblyDataByList(String classification, int spanCount, boolean singleSelect, List<String> conditions) {
        List<String> list = new ArrayList<>();
        Map<Integer, Boolean> map = new LinkedHashMap<>();
        list.add(classification);
        list.addAll(conditions);
        map.put(spanCount, singleSelect);
        this.map.put(list, map);
    }

    /**
     * 关联
     */
    public void associate() {
        screenAdapter.setScreeningData(map);
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
         * 重置
         */
        void resetting();

        /**
         * 确定
         */
        void ensure();
    }
}
