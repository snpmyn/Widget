package com.zsp.library.tagview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zsp.library.R;
import com.zsp.utilone.density.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @decs: 标签视图
 * @author: 郑少鹏
 * @date: 2019/11/12 14:45
 */
public class TagView extends RelativeLayout {
    /**
     * Tag list
     */
    private List<Tag> tags = new ArrayList<>();
    /**
     * System Service
     */
    private LayoutInflater layoutInflater;
    /**
     * listeners
     */
    private OnTagClickListener onTagClickListener;
    private OnTagDeleteListener onTagDeleteListener;
    private OnTagLongClickListener onTagLongClickListener;
    /**
     * view size param
     */
    private int width;
    /**
     * layout initialize flag
     */
    private boolean initialized = false;
    /**
     * custom layout param
     */
    private int lineMargin;
    private int tagMargin;
    private int textPaddingLeft;
    private int textPaddingRight;
    private int textPaddingTop;
    private int textPaddingBottom;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public TagView(Context context) {
        super(context, null);
        initialize(context, null, 0);
    }

    /**
     * constructor
     *
     * @param context      上下文
     * @param attributeSet AttributeSet
     */
    public TagView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context, attributeSet, 0);
    }

    /**
     * constructor
     *
     * @param context      上下文
     * @param attributeSet AttributeSet
     * @param defaultStyle 默样式
     */
    public TagView(Context context, AttributeSet attributeSet, int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(context, attributeSet, defaultStyle);
    }

    /**
     * Initialize instance.
     *
     * @param context      上下文
     * @param attributeSet AttributeSet
     * @param defaultStyle 默样式
     */
    private void initialize(@NonNull Context context, AttributeSet attributeSet, int defaultStyle) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewTreeObserver mViewTreeObserver = getViewTreeObserver();
        mViewTreeObserver.addOnGlobalLayoutListener(() -> {
            if (!initialized) {
                initialized = true;
                drawTags();
            }
        });
        // get AttributeSet
        TypedArray typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.TagView, defaultStyle, defaultStyle);
        this.lineMargin = (int) typeArray.getDimension(R.styleable.TagView_lineMargin, DensityUtils.dipToPxByFloat(this.getContext(), TagConstants.DEFAULT_LINE_MARGIN));
        this.tagMargin = (int) typeArray.getDimension(R.styleable.TagView_tagMargin, DensityUtils.dipToPxByFloat(this.getContext(), TagConstants.DEFAULT_TAG_MARGIN));
        this.textPaddingLeft = (int) typeArray.getDimension(R.styleable.TagView_textPaddingLeft, DensityUtils.dipToPxByFloat(this.getContext(), TagConstants.DEFAULT_TAG_TEXT_PADDING_LEFT));
        this.textPaddingRight = (int) typeArray.getDimension(R.styleable.TagView_textPaddingRight, DensityUtils.dipToPxByFloat(this.getContext(), TagConstants.DEFAULT_TAG_TEXT_PADDING_RIGHT));
        this.textPaddingTop = (int) typeArray.getDimension(R.styleable.TagView_textPaddingTop, DensityUtils.dipToPxByFloat(this.getContext(), TagConstants.DEFAULT_TAG_TEXT_PADDING_TOP));
        this.textPaddingBottom = (int) typeArray.getDimension(R.styleable.TagView_textPaddingBottom, DensityUtils.dipToPxByFloat(this.getContext(), TagConstants.DEFAULT_TAG_TEXT_PADDING_BOTTOM));
        typeArray.recycle();
    }

    /**
     * 尺寸变
     *
     * @param width     宽
     * @param height    高
     * @param oldWidth  旧宽
     * @param oldHeight 旧高
     */
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.width = width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        if (width <= 0) {
            return;
        }
        this.width = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTags();
    }

    private void drawTags() {
        if (!initialized) {
            return;
        }
        // clear all tag
        removeAllViews();
        // layout padding left & layout padding right
        float total = getPaddingLeft() + getPaddingRight();
        // List Index
        int listIndex = 1;
        // The Tag to add below
        int indexBottom = 1;
        // The header tag of this line
        int indexHeader = 1;
        Tag tagPre = null;
        for (Tag item : tags) {
            final int position = listIndex - 1;
            final Tag tag = item;
            // inflate tag layout
            @SuppressLint("InflateParams") View tagLayout = layoutInflater.inflate(R.layout.tag_view, null);
            tagLayout.setId(listIndex);
            tagLayout.setBackground(getSelector(tag));
            // tag text
            TextView tagView = tagLayout.findViewById(R.id.tagViewTvContain);
            tagView.setText(tag.getText());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tagView.getLayoutParams();
            params.setMargins(textPaddingLeft, textPaddingTop, textPaddingRight, textPaddingBottom);
            tagView.setLayoutParams(params);
            tagView.setTextColor(tag.getTagTextColor());
            tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tag.getTagTextSize());
            tagLayout.setOnClickListener(v -> {
                if (onTagClickListener != null) {
                    onTagClickListener.onTagClick(tag, position);
                }
            });
            tagLayout.setOnLongClickListener(v -> {
                if (onTagLongClickListener != null) {
                    onTagLongClickListener.onTagLongClick(tag, position);
                }
                return true;
            });
            // calculate　of tag layout width
            float tagWidth = tagView.getPaint().measureText(tag.getText()) + textPaddingLeft + textPaddingRight;
            // tagView padding (left & right)
            // deletable text
            TextView deletableView = tagLayout.findViewById(R.id.tagViewTvDelete);
            if (tag.isDeletable()) {
                deletableView.setVisibility(View.VISIBLE);
                deletableView.setText(tag.getDeleteIcon());
                int offset = DensityUtils.dipToPxByFloat(getContext(), 2f);
                deletableView.setPadding(offset, textPaddingTop, textPaddingRight + offset, textPaddingBottom);
                deletableView.setTextColor(tag.getDeleteIndicatorColor());
                deletableView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tag.getDeleteIndicatorSize());
                deletableView.setOnClickListener(v -> {
                    if (onTagDeleteListener != null) {
                        onTagDeleteListener.onTagDeleted(TagView.this, tag, position);
                    }
                });
                tagWidth += deletableView.getPaint().measureText(tag.getDeleteIcon()) + textPaddingLeft + textPaddingRight;
                // deletableView Padding (left & right)
            } else {
                deletableView.setVisibility(View.GONE);
            }
            LayoutParams tagParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // add margin of each line
            tagParams.bottomMargin = lineMargin;
            if (width <= total + tagWidth + DensityUtils.dipToPxByFloat(this.getContext(), TagConstants.LAYOUT_WIDTH_OFFSET)) {
                // need to add in new line
                if (tagPre != null) {
                    tagParams.addRule(RelativeLayout.BELOW, indexBottom);
                }
                // initialize total param (layout padding left & layout padding right)
                total = getPaddingLeft() + getPaddingRight();
                indexBottom = listIndex;
                indexHeader = listIndex;
            } else {
                // no need to new line
                tagParams.addRule(RelativeLayout.ALIGN_TOP, indexHeader);
                // not header of the line
                if (listIndex != indexHeader) {
                    tagParams.addRule(RelativeLayout.RIGHT_OF, listIndex - 1);
                    tagParams.leftMargin = tagMargin;
                    total += tagMargin;
                    if (tagPre.getTagTextSize() < tag.getTagTextSize()) {
                        indexBottom = listIndex;
                    }
                }
            }
            total += tagWidth;
            addView(tagLayout, tagParams);
            tagPre = tag;
            listIndex++;
        }
    }

    private Drawable getSelector(@NonNull Tag tag) {
        if (tag.getBackground() != null) {
            return tag.getBackground();
        }
        StateListDrawable states = new StateListDrawable();
        GradientDrawable gdNormal = new GradientDrawable();
        gdNormal.setColor(tag.getLayoutColor());
        gdNormal.setCornerRadius(tag.getRadius());
        if (tag.getLayoutBorderSize() > 0) {
            gdNormal.setStroke(DensityUtils.dipToPxByFloat(getContext(), tag.getLayoutBorderSize()), tag.getLayoutBorderColor());
        }
        GradientDrawable gdPress = new GradientDrawable();
        gdPress.setColor(tag.getLayoutColorPress());
        gdPress.setCornerRadius(tag.getRadius());
        states.addState(new int[]{android.R.attr.state_pressed}, gdPress);
        // must add state_pressed first
        // or state_pressed will not take effect
        states.addState(new int[]{}, gdNormal);
        return states;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        drawTags();
    }

    public void addTags(List<Tag> tags) {
        if (tags == null) {
            return;
        }
        this.tags = new ArrayList<>();
        if (tags.isEmpty()) {
            drawTags();
        }
        this.tags.addAll(tags);
        drawTags();
    }

    public void addTags(String[] tags) {
        if (tags == null) {
            return;
        }
        for (String item : tags) {
            Tag tag = new Tag(item);
            this.tags.add(tag);
        }
        drawTags();
    }

    /**
     * Get TagObject list.
     *
     * @return tags TagObject List
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Remove tag.
     *
     * @param position 位
     */
    public void remove(int position) {
        if (position < tags.size()) {
            tags.remove(position);
            drawTags();
        }
    }

    /**
     * Remove all views.
     */
    public void removeAll() {
        tags.clear();
        removeAllViews();
    }

    public int getLineMargin() {
        return lineMargin;
    }

    public void setLineMargin(float lineMargin) {
        this.lineMargin = DensityUtils.dipToPxByFloat(getContext(), lineMargin);
    }

    public int getTagMargin() {
        return tagMargin;
    }

    public void setTagMargin(float tagMargin) {
        this.tagMargin = DensityUtils.dipToPxByFloat(getContext(), tagMargin);
    }

    public int getTextPaddingLeft() {
        return textPaddingLeft;
    }

    public void setTextPaddingLeft(float textPaddingLeft) {
        this.textPaddingLeft = DensityUtils.dipToPxByFloat(getContext(), textPaddingLeft);
    }

    public int getTextPaddingRight() {
        return textPaddingRight;
    }

    public void setTextPaddingRight(float textPaddingRight) {
        this.textPaddingRight = DensityUtils.dipToPxByFloat(getContext(), textPaddingRight);
    }

    public int getTextPaddingTop() {
        return textPaddingTop;
    }

    public void setTextPaddingTop(float textPaddingTop) {
        this.textPaddingTop = DensityUtils.dipToPxByFloat(getContext(), textPaddingTop);
    }

    public int getTextPaddingBottom() {
        return textPaddingBottom;
    }

    public void setTextPaddingBottom(float textPaddingBottom) {
        this.textPaddingBottom = DensityUtils.dipToPxByFloat(getContext(), textPaddingBottom);
    }

    /**
     * 设标签点监听
     *
     * @param onTagClickListener 标签点监听
     */
    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    /**
     * 设标签长点监听
     *
     * @param onTagLongClickListener 标签长点监听
     */
    public void setOnTagLongClickListener(OnTagLongClickListener onTagLongClickListener) {
        this.onTagLongClickListener = onTagLongClickListener;
    }

    /**
     * 设标签删监听
     *
     * @param onTagDeleteListener 标签删监听
     */
    public void setOnTagDeleteListener(OnTagDeleteListener onTagDeleteListener) {
        this.onTagDeleteListener = onTagDeleteListener;
    }

    /**
     * 标签点监听
     */
    public interface OnTagClickListener {
        /**
         * 标签点
         *
         * @param tag      标签
         * @param position 位
         */
        void onTagClick(Tag tag, int position);
    }

    /**
     * 标签长点监听
     */
    public interface OnTagLongClickListener {
        /**
         * 标签长点
         *
         * @param tag      标签
         * @param position 位
         */
        void onTagLongClick(Tag tag, int position);
    }

    /**
     * 标签删监听
     */
    public interface OnTagDeleteListener {
        /**
         * 标签删
         *
         * @param view     视图
         * @param tag      标签
         * @param position 位
         */
        void onTagDeleted(TagView view, Tag tag, int position);
    }
}
