package com.zsp.library.pickerview.listener;

import android.view.View;

/**
 * @decs: OnOptionsSelectListener
 * @author: 郑少鹏
 * @date: 2018/4/3 17:14
 */
public interface OnOptionsSelectListener {
    /**
     * 选项选
     *
     * @param options1 options1
     * @param options2 options2
     * @param options3 options3
     * @param view     视图
     */
    void onOptionsSelect(int options1, int options2, int options3, View view);
}
