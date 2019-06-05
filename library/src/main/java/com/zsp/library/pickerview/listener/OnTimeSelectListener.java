package com.zsp.library.pickerview.listener;

import android.view.View;

import java.util.Date;

/**
 * @decs: OnTimeSelectListener
 * @author: 郑少鹏
 * @date: 2018/4/3 17:14
 */
public interface OnTimeSelectListener {
    /**
     * 时间选
     *
     * @param date 日期
     * @param view 视图
     */
    void onTimeSelect(Date date, View view);
}
