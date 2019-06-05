package com.zsp.library.pickerview.listener;

import java.util.Date;

/**
 * @decs: OnTimeSelectChangeListener
 * @author: 郑少鹏
 * @date: 2018/4/3 17:14
 */
public interface OnTimeSelectChangeListener {
    /**
     * 时间选变
     *
     * @param date 日期
     */
    void onTimeSelectChanged(Date date);
}
