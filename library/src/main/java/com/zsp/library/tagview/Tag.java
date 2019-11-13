package com.zsp.library.tagview;

import android.graphics.drawable.Drawable;

/**
 * @decs: 标签
 * @author: 郑少鹏
 * @date: 2019/11/12 14:44
 */
public class Tag {
    private String text;
    private int tagTextColor;
    private float tagTextSize;
    private int layoutColor;
    private int layoutColorPress;
    private boolean isDeletable;
    private int deleteIndicatorColor;
    private float deleteIndicatorSize;
    private float radius;
    private String deleteIcon;
    private float layoutBorderSize;
    private int layoutBorderColor;
    private Drawable background;

    /**
     * constructor
     *
     * @param text 文本
     */
    public Tag(String text) {
        // 初始
        init(text);
    }

    /**
     * 初始
     *
     * @param text 文本
     */
    private void init(String text) {
        this.text = text;
        this.tagTextColor = TagConstants.DEFAULT_TAG_TEXT_COLOR;
        this.tagTextSize = TagConstants.DEFAULT_TAG_TEXT_SIZE;
        this.layoutColor = TagConstants.DEFAULT_TAG_LAYOUT_COLOR;
        this.layoutColorPress = TagConstants.DEFAULT_TAG_LAYOUT_COLOR_PRESS;
        this.isDeletable = TagConstants.DEFAULT_TAG_IS_DELETABLE;
        this.deleteIndicatorColor = TagConstants.DEFAULT_TAG_DELETE_INDICATOR_COLOR;
        this.deleteIndicatorSize = TagConstants.DEFAULT_TAG_DELETE_INDICATOR_SIZE;
        this.radius = TagConstants.DEFAULT_TAG_RADIUS;
        this.deleteIcon = TagConstants.DEFAULT_TAG_DELETE_ICON;
        this.layoutBorderSize = TagConstants.DEFAULT_TAG_LAYOUT_BORDER_SIZE;
        this.layoutBorderColor = TagConstants.DEFAULT_TAG_LAYOUT_BORDER_COLOR;
    }

    public void setTagTextColor(int tagTextColor) {
        this.tagTextColor = tagTextColor;
    }

    public void setTagTextSize(float tagTextSize) {
        this.tagTextSize = tagTextSize;
    }

    public void setLayoutColor(int layoutColor) {
        this.layoutColor = layoutColor;
    }

    public void setLayoutColorPress(int layoutColorPress) {
        this.layoutColorPress = layoutColorPress;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    public void setDeleteIndicatorColor(int deleteIndicatorColor) {
        this.deleteIndicatorColor = deleteIndicatorColor;
    }

    public void setDeleteIndicatorSize(float deleteIndicatorSize) {
        this.deleteIndicatorSize = deleteIndicatorSize;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDeleteIcon(String deleteIcon) {
        this.deleteIcon = deleteIcon;
    }

    public void setLayoutBorderSize(float layoutBorderSize) {
        this.layoutBorderSize = layoutBorderSize;
    }

    public void setLayoutBorderColor(int layoutBorderColor) {
        this.layoutBorderColor = layoutBorderColor;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public String getText() {
        return text;
    }

    int getTagTextColor() {
        return tagTextColor;
    }

    float getTagTextSize() {
        return tagTextSize;
    }

    int getLayoutColor() {
        return layoutColor;
    }

    int getLayoutColorPress() {
        return layoutColorPress;
    }

    boolean isDeletable() {
        return isDeletable;
    }

    int getDeleteIndicatorColor() {
        return deleteIndicatorColor;
    }

    float getDeleteIndicatorSize() {
        return deleteIndicatorSize;
    }

    public float getRadius() {
        return radius;
    }

    String getDeleteIcon() {
        return deleteIcon;
    }

    float getLayoutBorderSize() {
        return layoutBorderSize;
    }

    int getLayoutBorderColor() {
        return layoutBorderColor;
    }

    public Drawable getBackground() {
        return background;
    }
}
