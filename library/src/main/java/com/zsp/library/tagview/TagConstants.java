package com.zsp.library.tagview;

import android.graphics.Color;

/**
 * @decs: 标签常量
 * Use dp and sp, not px.
 * @author: 郑少鹏
 * @date: 2019/11/12 15:28
 */
class TagConstants {
    /**
     * separator TagView
     */
    static final float DEFAULT_LINE_MARGIN = 5;
    static final float DEFAULT_TAG_MARGIN = 5;
    static final float DEFAULT_TAG_TEXT_PADDING_LEFT = 8;
    static final float DEFAULT_TAG_TEXT_PADDING_TOP = 5;
    static final float DEFAULT_TAG_TEXT_PADDING_RIGHT = 8;
    static final float DEFAULT_TAG_TEXT_PADDING_BOTTOM = 5;
    static final float LAYOUT_WIDTH_OFFSET = 2;
    /**
     * separator Tag item
     */
    static final float DEFAULT_TAG_TEXT_SIZE = 14f;
    static final float DEFAULT_TAG_DELETE_INDICATOR_SIZE = 14f;
    static final float DEFAULT_TAG_LAYOUT_BORDER_SIZE = 0f;
    static final float DEFAULT_TAG_RADIUS = 100;
    static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#AED374");
    static final int DEFAULT_TAG_LAYOUT_COLOR_PRESS = Color.parseColor("#88363636");
    static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#ffffff");
    static final int DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff");
    static final int DEFAULT_TAG_LAYOUT_BORDER_COLOR = Color.parseColor("#ffffff");
    static final String DEFAULT_TAG_DELETE_ICON = "×";
    static final boolean DEFAULT_TAG_IS_DELETABLE = false;

    private TagConstants() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation.");
    }
}
