package com.zsp.library.guide.materialintroview.target;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * @decs: 目标
 * @author: 郑少鹏
 * @date: 2019/9/24 14:52
 */
public interface Target {
    /**
     * Returns center point of target.
     * <p>
     * We can get x and y coordinates using point object.
     *
     * @return the center point of target
     */
    Point getPoint();

    /**
     * Returns rectangle points of target view.
     *
     * @return rectangle points of target view
     */
    Rect getRect();

    /**
     * Return target view.
     *
     * @return target view
     */
    View getView();
}
