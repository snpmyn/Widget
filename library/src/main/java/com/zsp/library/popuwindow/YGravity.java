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
        YGravity.CENTER,
        YGravity.ABOVE,
        YGravity.BELOW,
        YGravity.ALIGN_TOP,
        YGravity.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface YGravity {
    int CENTER = 0;
    int ABOVE = 1;
    int BELOW = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
