package com.zsp.library.conection;

import android.content.Context;
import android.util.AttributeSet;

import com.zsp.library.R;

/**
 * @decs: 默连状图
 * @author: 郑少鹏
 * @date: 2019/10/18 17:02
 */
public class DefaultConnectionConnectionStatusView extends ConnectionStatusView {
    public DefaultConnectionConnectionStatusView(Context context) {
        super(context, R.layout.default_connection_status_view_complete, R.layout.default_connection_statue_view_error, R.layout.default_connection_status_view_loading);
    }

    public DefaultConnectionConnectionStatusView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.default_connection_status_view_complete, R.layout.default_connection_statue_view_error, R.layout.default_connection_status_view_loading);
    }

    public DefaultConnectionConnectionStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.layout.default_connection_status_view_complete, R.layout.default_connection_statue_view_error, R.layout.default_connection_status_view_loading);
    }
}
