package com.zsp.library.searchbox.one;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @decs: 记录列表
 * @author: 郑少鹏
 * @date: 2019/4/22 12:00
 */
public class RecordListView extends ListView {
    public RecordListView(Context context) {
        super(context);
    }

    public RecordListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 复写onMeasure适配ScrollView
     *
     * @param widthMeasureSpec  widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
