package com.zsp.library.popuwindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @decs: XGravity
 * @author: 郑少鹏
 * @date: 2018/6/22 21:15
 */
@IntDef({
        XGravity.center,
        XGravity.left,
        XGravity.right,
        XGravity.alignLeft,
        XGravity.alignRight,
})
@Retention(RetentionPolicy.SOURCE)
public @interface XGravity {
    int center = 0;
    int left = 1;
    int right = 2;
    int alignLeft = 3;
    int alignRight = 4;
}