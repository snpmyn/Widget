package com.zsp.library.searchbox.two.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;
import com.zsp.library.searchbox.two.adapter.SearchHistoryAdapter;
import com.zsp.library.searchbox.two.animator.CircularRevealAnimator;
import com.zsp.library.searchbox.two.database.SearchHistoryDataBase;
import com.zsp.library.searchbox.two.listener.OnItemClickOrDeleteClickListener;
import com.zsp.utilone.keyboard.KeyboardUtils;
import com.zsp.utilone.toast.ToastUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @decs: 搜索对话框碎片
 * @author: 郑少鹏
 * @date: 2019/4/23 11:43
 */
public class SearchDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener, ViewTreeObserver.OnPreDrawListener, CircularRevealAnimator.AnimationListener, OnItemClickOrDeleteClickListener, View.OnClickListener {
    public static final String TAG = "SearchDialogFragment";
    private EditText searchDialogFragmentEt;
    private ImageView searchDialogFragmentIvSearch;
    private View searchDialogFragmentViewDivider;
    private View view;
    /**
     * 数据库名
     */
    private String name;
    /**
     * 动画
     */
    private CircularRevealAnimator circularRevealAnimator;
    /**
     * 数据库
     */
    private SearchHistoryDataBase searchHistoryDataBase;
    /**
     * 历史搜索记录
     */
    private ArrayList<String> histories = new ArrayList<>();
    private ArrayList<String> allHistories = new ArrayList<>();
    /**
     * 适配器
     */
    private SearchHistoryAdapter searchHistoryAdapter;
    /**
     * 搜索监听
     */
    private OnSearchClickListener onSearchClickListener;
    /**
     * 搜索对话框隐监听
     */
    private OnSearchDialogHideListener onSearchDialogHideListener;

    /**
     * constructor
     *
     * @param name 数据库名
     *             SearchHistory_db于沙盒看显乱码
     *             SearchHistory.db于沙盒直查
     */
    public SearchDialogFragment(String name) {
        this.name = name;
    }

    /**
     * Remove dialog.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != onSearchClickListener) {
            onSearchClickListener = null;
        }
        if (null != onSearchDialogHideListener) {
            onSearchClickListener = null;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getDialog()) {
            try {
                // 解决内存泄漏
                getDialog().setOnDismissListener(null);
                getDialog().setOnCancelListener(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.SearchDialogFragmentStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        stepDialog();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_dialog, container, false);
        step();
        return view;
    }

    /**
     * 初始
     */
    private void step() {
        ImageView searchDialogFragmentIvTopBack = view.findViewById(R.id.searchDialogFragmentIvTopBack);
        searchDialogFragmentEt = view.findViewById(R.id.searchDialogFragmentEt);
        searchDialogFragmentIvSearch = view.findViewById(R.id.searchDialogFragmentIvSearch);
        RecyclerView searchDialogFragmentRv = view.findViewById(R.id.searchDialogFragmentRv);
        searchDialogFragmentViewDivider = view.findViewById(R.id.searchDialogFragmentViewDivider);
        TextView searchDialogFragmentTvClearSearchHistory = view.findViewById(R.id.searchDialogFragmentTvClearSearchHistory);
        View searchDialogFragmentViewOutside = view.findViewById(R.id.searchDialogFragmentViewOutside);
        // 实例动画效果
        circularRevealAnimator = new CircularRevealAnimator();
        // 监听动画
        circularRevealAnimator.setAnimationListener(this);
        // 监听键盘
        if (getDialog() != null) {
            getDialog().setOnKeyListener(this);
        }
        // 监听绘制
        searchDialogFragmentIvSearch.getViewTreeObserver().addOnPreDrawListener(this);
        // 实例化数据库
        searchHistoryDataBase = new SearchHistoryDataBase(getContext(), name, null, 1);
        allHistories = searchHistoryDataBase.queryAllHistories();
        setAllHistories();
        // 初始RecyclerView
        searchDialogFragmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        searchHistoryAdapter = new SearchHistoryAdapter(Objects.requireNonNull(getContext(), "must not be null"), histories);
        searchDialogFragmentRv.setAdapter(searchHistoryAdapter);
        // 监听单删记录
        searchHistoryAdapter.setOnItemClickListener(this);
        // 监听编辑框文字变化
        searchDialogFragmentEt.addTextChangedListener(new TextWatcherImpl());
        // 监听点击
        searchDialogFragmentIvTopBack.setOnClickListener(this);
        searchDialogFragmentViewOutside.setOnClickListener(this);
        searchDialogFragmentIvSearch.setOnClickListener(this);
        searchDialogFragmentTvClearSearchHistory.setOnClickListener(this);
    }

    /**
     * 显Fragment避多次打开致崩溃
     *
     * @param fragmentManager 碎片管理器
     * @param tag             tag
     */
    public void showFragment(FragmentManager fragmentManager, String tag) {
        if (!this.isAdded()) {
            this.show(fragmentManager, tag);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.searchDialogFragmentIvTopBack || view.getId() == R.id.searchDialogFragmentViewOutside) {
            hideAnimator();
        } else if (view.getId() == R.id.searchDialogFragmentIvSearch) {
            search();
        } else if (view.getId() == R.id.searchDialogFragmentTvClearSearchHistory) {
            if (histories.size() > 0) {
                histories.clear();
                searchHistoryAdapter.notifyDataSetChanged();
                searchDialogFragmentViewDivider.setVisibility(View.GONE);
                searchHistoryDataBase.deleteAll();
            } else {
                ToastUtils.shortShow(Objects.requireNonNull(getContext(), "must not be null"), getString(R.string.noHistory));
            }
        }
    }

    /**
     * 初始对话框
     */
    private void stepDialog() {
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            // 宽
            int width = (int) (metrics.widthPixels * 0.98);
            if (window != null) {
                window.setLayout(width, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.TOP);
                // 取消过渡动画以更平滑
                window.setWindowAnimations(R.style.SearchDialogFragmentAnimation);
            }
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            hideAnimator();
        } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            search();
        }
        return false;
    }

    /**
     * 监听搜索键绘制
     *
     * @return boolean
     */
    @Override
    public boolean onPreDraw() {
        searchDialogFragmentIvSearch.getViewTreeObserver().removeOnPreDrawListener(this);
        circularRevealAnimator.show(searchDialogFragmentIvSearch, view);
        return true;
    }

    /**
     * 搜索框动画隐完调
     */
    @Override
    public void onAnimationHideEnd() {
        onSearchDialogHideListener.searchDialogHide();
        dismiss();
    }

    /**
     * 搜索框动画显完调
     */
    @Override
    public void onAnimationShowEnd() {
        if (isVisible()) {
            KeyboardUtils.openKeyboardThree(Objects.requireNonNull(getContext(), "must not be null"), searchDialogFragmentEt);
        }
    }

    /**
     * 隐动画
     */
    private void hideAnimator() {
        KeyboardUtils.closeKeyboard(Objects.requireNonNull(getContext(), "must not be null"), searchDialogFragmentEt);
        circularRevealAnimator.hide(searchDialogFragmentIvSearch, view);
    }

    private void search() {
        String keyword = searchDialogFragmentEt.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            ToastUtils.shortShow(Objects.requireNonNull(getContext(), "must not be null"), getString(R.string.enterSearchKeyword));
        } else {
            // 接口回调
            onSearchClickListener.onSearchClick(keyword);
            // 隐动画
            hideAnimator();
            // 插
            searchHistoryDataBase.insert(keyword);
            // 置空
            searchDialogFragmentEt.setText("");
        }
    }

    private void dividerVisibleOrGone() {
        if (histories.size() < 1) {
            searchDialogFragmentViewDivider.setVisibility(View.GONE);
        } else {
            searchDialogFragmentViewDivider.setVisibility(View.VISIBLE);
        }
    }

    private void setAllHistories() {
        if (histories.size() > 0) {
            histories.clear();
        }
        histories.addAll(allHistories);
        dividerVisibleOrGone();
    }

    private void setKeywordHistories(String keyword) {
        histories.clear();
        for (String string : allHistories) {
            if (string.contains(keyword)) {
                histories.add(string);
            }
        }
        searchHistoryAdapter.notifyDataSetChanged();
        dividerVisibleOrGone();
    }

    @Override
    public void onItemClick(String keyword) {
        onSearchClickListener.onSearchClick(keyword);
        hideAnimator();
    }

    @Override
    public void onItemDeleteClick(int position, String keyword) {
        searchHistoryAdapter.notifyItemRemoved(position);
        searchHistoryAdapter.notifyItemRangeChanged(position, histories.size() - position);
        histories.remove(keyword);
        dividerVisibleOrGone();
        searchHistoryDataBase.delete(keyword);
    }

    public void setOnSearchClickListener(OnSearchClickListener listener) {
        this.onSearchClickListener = listener;
    }

    public void setOnSearchDialogHideListener(OnSearchDialogHideListener listener) {
        this.onSearchDialogHideListener = listener;
    }

    public interface OnSearchClickListener {
        /**
         * 搜索
         *
         * @param keyword 关键字
         */
        void onSearchClick(String keyword);
    }

    public interface OnSearchDialogHideListener {
        /**
         * 隐
         */
        void searchDialogHide();
    }

    /**
     * 监听编辑框文字变化
     */
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String keyword = editable.toString();
            if (TextUtils.isEmpty(keyword)) {
                setAllHistories();
                searchHistoryAdapter.notifyDataSetChanged();
            } else {
                setKeywordHistories(editable.toString());
            }
        }
    }
}
