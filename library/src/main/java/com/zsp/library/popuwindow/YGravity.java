package com.zsp.library.popuwindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @decs: YGravity
 * @author: 郑少鹏
 * @date: 2018/6/22 21:25
 */
@IntDef({
        YGravity.center,
        YGravity.above,
        YGravity.below,
        YGravity.alignTop,
        YGravity.alignBottom,
})
@Retention(RetentionPolicy.SOURCE)
public @interface YGravity {
    int center = 0;
    int above = 1;
    int below = 2;
    int alignTop = 3;
    int alignBottom = 4;
}
