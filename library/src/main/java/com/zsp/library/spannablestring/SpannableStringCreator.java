package com.zsp.library.spannablestring;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created on 2019/6/24.
 *
 * @author 郑少鹏
 * @desc SpannableStringCreator
 */
public class SpannableStringCreator {
    public static SpannableStringCreator.Builder with(String source) {
        return new Builder(source);
    }

    public static class Builder {
        private SpannableString spannableString;
        private ClickableSpanListener clickableSpanListener;

        Builder(String source) {
            this.spannableString = new SpannableString(source);
        }

        /**
         * 前景色
         *
         * @param colorRes 颜色资源
         * @param start    起始
         * @param end      终止
         * @param flags    标志
         * @return Builder
         */
        public Builder foregroundColorSpan(int colorRes, int start, int end, int flags) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(colorRes);
            spannableString.setSpan(colorSpan, start, end, flags);
            return this;
        }

        /**
         * 背景色
         *
         * @param colorRes 颜色资源
         * @param start    起始
         * @param end      终止
         * @param flags    标志
         * @return Builder
         */
        public Builder backgroundColorSpan(int colorRes, int start, int end, int flags) {
            BackgroundColorSpan colorSpan = new BackgroundColorSpan(colorRes);
            spannableString.setSpan(colorSpan, start, end, flags);
            return this;
        }

        /**
         * 相对大小
         * <p>
         * 1.0f原状。
         *
         * @param size  大小
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder relativeSizeSpan(float size, int start, int end, int flags) {
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(size);
            spannableString.setSpan(relativeSizeSpan, start, end, flags);
            return this;
        }

        /**
         * 中划线
         *
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder strikethroughSpan(int start, int end, int flags) {
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            spannableString.setSpan(strikethroughSpan, start, end, flags);
            return this;
        }

        /**
         * 下划线
         *
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder underlineSpan(int start, int end, int flags) {
            UnderlineSpan underlineSpan = new UnderlineSpan();
            spannableString.setSpan(underlineSpan, start, end, flags);
            return this;
        }

        /**
         * 上标
         *
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder superscriptSpan(int start, int end, int flags) {
            SuperscriptSpan superscriptSpan = new SuperscriptSpan();
            spannableString.setSpan(superscriptSpan, start, end, flags);
            return this;
        }

        /**
         * 下标
         *
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder subscriptSpan(int start, int end, int flags) {
            SubscriptSpan subscriptSpan = new SubscriptSpan();
            spannableString.setSpan(subscriptSpan, start, end, flags);
            return this;
        }

        /**
         * 粗体
         *
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder bold(int start, int end, int flags) {
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(styleSpan, start, end, flags);
            return this;
        }

        /**
         * 斜体
         *
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder italic(int start, int end, int flags) {
            StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
            spannableString.setSpan(styleSpan, start, end, flags);
            return this;
        }

        /**
         * 图片
         *
         * @param drawable       位图
         * @param drawableLeft   位图左边距
         * @param drawableTop    位图上边距
         * @param drawableRight  位图右边距
         * @param drawableBottom 位图下边距
         * @param start          起始
         * @param end            终止
         * @param flags          标志
         * @return Builder
         */
        public Builder imageSpan(Drawable drawable, int drawableLeft, int drawableTop, int drawableRight, int drawableBottom, int start, int end, int flags) {
            drawable.setBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            ImageSpan imageSpan = new ImageSpan(drawable);
            spannableString.setSpan(imageSpan, start, end, flags);
            return this;
        }

        /**
         * 可点击
         *
         * @param clickableSpanListener 可点击监听
         * @param start                 起始
         * @param end                   终止
         * @param flags                 标志
         * @return Builder
         */
        public Builder clickableSpan(ClickableSpanListener clickableSpanListener, int start, int end, int flags) {
            this.clickableSpanListener = clickableSpanListener;
            MyClickableSpan myClickableSpan = new MyClickableSpan();
            spannableString.setSpan(myClickableSpan, start, end, flags);
            return this;
        }

        class MyClickableSpan extends ClickableSpan {
            MyClickableSpan() {

            }

            @Override
            public void onClick(@NonNull View view) {
                clickableSpanListener.click();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
            }
        }

        public interface ClickableSpanListener {
            /**
             * 点击
             */
            void click();
        }

        /**
         * 超链接
         *
         * @param url   统一资源定位符
         * @param start 起始
         * @param end   终止
         * @param flags 标志
         * @return Builder
         */
        public Builder urlSpan(String url, int start, int end, int flags) {
            URLSpan urlSpan = new URLSpan(url);
            spannableString.setSpan(urlSpan, start, end, flags);
            return this;
        }

        public SpannableString create() {
            return spannableString;
        }
    }
}
