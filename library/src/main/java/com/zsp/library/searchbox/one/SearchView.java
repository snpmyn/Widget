package com.zsp.library.searchbox.one;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.zsp.library.R;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.utilone.view.ViewUtils;

/**
 * @decs: 搜索视图
 * @author: 郑少鹏
 * @date: 2019/4/22 12:02
 */
public class SearchView extends LinearLayout {
    private final Context context;
    /**
     * 搜索键
     */
    private EditText searchViewEt;
    /**
     * 清除键
     */
    private TextView searchViewTvClear;
    /**
     * 搜索框属性（高、色）
     */
    private int searchBlockHeight;
    private int searchBlockColor;
    /**
     * 搜索字体属性（大小、色、默提）
     */
    private Float textSizeSearch;
    private int textColorSearch;
    private String textHintSearch;
    /**
     * 搜索键回调接口
     */
    private SearchCallBack searchCallBack;
    /**
     * 列表
     */
    private RecordListView searchViewSlv;
    /**
     * 数据库
     */
    private RecordHelper recordHelper;
    private SQLiteDatabase sqLiteDatabase;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public SearchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        stepAttributeSet(context, attrs);
        init();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        stepAttributeSet(context, attrs);
        init();
    }

    /**
     * 初始自定属性
     *
     * @param context 上下文
     * @param attrs   自定属性
     */
    private void stepAttributeSet(@NonNull Context context, AttributeSet attrs) {
        // 控件资源名
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        // 搜索框高（px）、色
        searchBlockHeight = typedArray.getInteger(R.styleable.SearchView_searchBlockHeight, 0);
        searchBlockColor = typedArray.getColor(R.styleable.SearchView_searchBlockColor, ContextCompat.getColor(context, R.color.colorPrimary));
        // 搜索框字体大小（dp）、色（十六进制。如#333、#8e8e8e）
        textSizeSearch = typedArray.getDimension(R.styleable.SearchView_textSizeSearch, 0);
        textColorSearch = typedArray.getColor(R.styleable.SearchView_textColorSearch, ContextCompat.getColor(context, R.color.white));
        // 搜索框提示（String）
        textHintSearch = typedArray.getString(R.styleable.SearchView_textHintSearch);
        // 释放资源
        typedArray.recycle();
    }

    private void init() {
        initView();
        recordHelper = new RecordHelper(context);
        searchViewTvClear.setOnClickListener(v -> {
            // 清数据库
            clearDataBase();
            // 模糊搜索空字符（此时无搜索记录）
            queryData("");
        });
        searchViewEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                queryData("");
            }
        });
        searchViewEt.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                ViewUtils.hideView(searchViewSlv, View.GONE);
                // 点搜索键后据所输搜索字段查
                // 此处需求据场景定。开发者自实现具体逻辑（此处仅留接口）
                if (searchCallBack != null) {
                    searchCallBack.search(searchViewEt.getText().toString());
                }
                String temporaryName = searchViewEt.getText().toString();
                if ("".equals(temporaryName)) {
                    ToastUtils.shortShow(context, context.getString(R.string.enterSearchKeyword));
                } else {
                    // 点搜索键后查数据库存该搜索字段否
                    boolean hasData = hasData(searchViewEt.getText().toString());
                    // 无时存该搜索字段（插入）至数据库
                    if (!hasData) {
                        insertData(searchViewEt.getText().toString());
                    }
                }
            }
            return false;
        });
        searchViewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 每输后模糊查数据库
                // 搜索框空则模糊搜索空字符（显全搜索历史）
                String tempName = searchViewEt.getText().toString();
                queryData(tempName);
            }
        });
        searchViewSlv.setOnItemClickListener((parent, view, position, id) -> {
            // 获用户所点列表文本并自动填充搜索框
            TextView textView = view.findViewById(android.R.id.text1);
            String temporaryName = textView.getText().toString();
            searchViewEt.setText(temporaryName);
            searchViewEt.setSelection(temporaryName.length());
        });
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.search_view, this);
        // 搜索框EditText
        searchViewEt = findViewById(R.id.searchViewEt);
        searchViewEt.setHint(textHintSearch);
        searchViewEt.setTextSize(textSizeSearch);
        searchViewEt.setTextColor(textColorSearch);
        // 搜索框背景色、高
        LinearLayout searchViewLlBlock = findViewById(R.id.searchViewLlBlock);
        searchViewLlBlock.setBackgroundColor(searchBlockColor);
        LayoutParams layoutParams = (LayoutParams) searchViewLlBlock.getLayoutParams();
        layoutParams.height = searchBlockHeight;
        searchViewLlBlock.setLayoutParams(layoutParams);
        // 历史搜索记录
        searchViewSlv = findViewById(R.id.searchViewSlv);
        // 清除搜索历史按钮
        searchViewTvClear = findViewById(R.id.searchViewTvClear);
    }

    /**
     * 查数据
     *
     * @param temporaryName 临名
     */
    private void queryData(String temporaryName) {
        // 模糊搜索
        Cursor cursor = recordHelper.getReadableDatabase().rawQuery("select id as _id,name from record where name like '%" + temporaryName + "%' order by id desc ", null);
        // 初始适配器
        BaseAdapter baseAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, cursor, new String[]{"name"},
                new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设适配器
        searchViewSlv.setAdapter(baseAdapter);
        ViewUtils.showView(searchViewSlv);
        // 输框空且数据库有搜索记录时显"清除搜索历史"按钮
        if ("".equals(temporaryName) && cursor.getCount() != 0) {
            ViewUtils.showView(searchViewTvClear);
        } else {
            ViewUtils.hideView(searchViewTvClear, View.GONE);
        }
    }

    /**
     * 清数据库
     */
    private void clearDataBase() {
        sqLiteDatabase = recordHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from record");
        sqLiteDatabase.close();
        ViewUtils.hideView(searchViewTvClear, View.GONE);
    }

    /**
     * 查数据库已有该搜索记录否
     *
     * @param temporaryName 临名
     * @return 已有该搜索记录否
     */
    private boolean hasData(String temporaryName) {
        // 从数据库Record表找到name=tempName之id
        @SuppressLint("Recycle") Cursor cursor = recordHelper.getReadableDatabase().rawQuery("select id as _id,name from record where name =?", new String[]{temporaryName});
        // 判有下一个否
        return cursor.moveToNext();
    }

    /**
     * 插数据至数据库
     * <p>
     * 写搜索字段至历史搜索记录。
     *
     * @param temporaryName 临名
     */
    private void insertData(String temporaryName) {
        sqLiteDatabase = recordHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into record(name) values('" + temporaryName + "')");
        sqLiteDatabase.close();
    }

    /**
     * 点软键盘搜索键后操作
     * <p>
     * 用于接口回调。
     *
     * @param searchCallBack SearchCallBack
     */
    public void setOnClickSearch(SearchCallBack searchCallBack) {
        this.searchCallBack = searchCallBack;
    }
}
