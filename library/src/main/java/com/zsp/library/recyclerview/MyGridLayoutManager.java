package com.zsp.library.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * Created on 2017/11/19 0019.
 *
 * @author 郑少鹏
 * @desc custom gridLayoutManager
 */
public class MyGridLayoutManager extends GridLayoutManager {
    private boolean scrollEnable = true;

    MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnable(boolean flag) {
        this.scrollEnable = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return scrollEnable && super.canScrollVertically();
    }
}
