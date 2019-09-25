package com.zsp.library.guide.materialintroview.configuration;

import com.zsp.library.guide.materialintroview.shape.Focus;
import com.zsp.library.guide.materialintroview.shape.FocusGravity;

import value.WidgetLibraryMagic;

/**
 * @decs: MaterialIntroConfiguration
 * @author: 郑少鹏
 * @date: 2019/9/24 11:55
 */
public class MaterialIntroConfiguration {
    private int maskColor;
    private long delayMillis;
    private boolean isFadeAnimationEnabled;
    private Focus focusType;
    private FocusGravity focusGravity;
    private int padding;
    private boolean dismissOnTouch;
    private int colorTextViewInfo;
    private boolean isDotViewEnabled;
    private boolean isImageViewEnabled;

    public MaterialIntroConfiguration() {
        maskColor = 0x70000000;
        delayMillis = 0L;
        padding = WidgetLibraryMagic.INT_TEN;
        colorTextViewInfo = 0xFF000000;
        focusType = Focus.ALL;
        focusGravity = FocusGravity.CENTER;
        isFadeAnimationEnabled = false;
        dismissOnTouch = false;
        isDotViewEnabled = false;
        isImageViewEnabled = true;
    }

    public int getMaskColor() {
        return maskColor;
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public boolean isFadeAnimationEnabled() {
        return isFadeAnimationEnabled;
    }

    public void setFadeAnimationEnabled(boolean fadeAnimationEnabled) {
        isFadeAnimationEnabled = fadeAnimationEnabled;
    }

    public Focus getFocusType() {
        return focusType;
    }

    public void setFocusType(Focus focusType) {
        this.focusType = focusType;
    }

    public FocusGravity getFocusGravity() {
        return focusGravity;
    }

    public void setFocusGravity(FocusGravity focusGravity) {
        this.focusGravity = focusGravity;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public boolean isDismissOnTouch() {
        return dismissOnTouch;
    }

    public void setDismissOnTouch(boolean dismissOnTouch) {
        this.dismissOnTouch = dismissOnTouch;
    }

    public int getColorTextViewInfo() {
        return colorTextViewInfo;
    }

    public void setColorTextViewInfo(int colorTextViewInfo) {
        this.colorTextViewInfo = colorTextViewInfo;
    }

    public boolean isDotViewEnabled() {
        return isDotViewEnabled;
    }

    public void setDotViewEnabled(boolean dotViewEnabled) {
        isDotViewEnabled = dotViewEnabled;
    }

    public boolean isImageViewEnabled() {
        return isImageViewEnabled;
    }
}