package com.zsp.library.searchbox.one;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.zsp.library.R;

/**
 * @decs: 搜索输框
 * @author: 郑少鹏
 * @date: 2019/4/22 11:47
 */
public class SearchEditText extends AppCompatEditText {
    /**
     * 搜索图、清除图
     */
    private Drawable searchDrawable, clearDrawable;

    public SearchEditText(Context context) {
        super(context);
        step();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        step();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        step();
    }

    /**
     * 初始
     */
    private void step() {
        searchDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_search_to_search_edit_text_and_search_view);
        clearDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_to_search_edit_text_and_search_history_item);
        // setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom)
        // 作用：EditText上、下、左、右设图（相当android:drawableLeft=""、android:drawableRight=""）
        // 注1：setCompoundDrawablesWithIntrinsicBounds（）所传Drawable宽高等固有宽高（自动通getIntrinsicWidth（）、getIntrinsicHeight（）获）
        // 注2：不于某地显设null
        // 此处设左搜索图
        // 另相似法：setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)
        // 同setCompoundDrawablesWithIntrinsicBounds（）区别：可设图大小
        // 所传Drawable须已setBounds(x, y, width, height)（即须设过初始位、宽、高等信息）
        // x：组件于容器X轴起点；y：组件于容器Y轴起点；width：组件长；height：组件高
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setClearIconVisible(hasFocus() && text.length() > 0);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setClearIconVisible(focused && length() > 0);
    }

    private void setClearIconVisible(boolean visible) {
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, visible ? clearDrawable : null, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 手指抬起位于清除图区域即视为点清除图
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable drawable = clearDrawable;
            if (drawable != null && event.getX() <= (getWidth() - getPaddingRight()) &&
                    event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                setText("");
            }
            // 判断条件说明
            // event.getX()：抬起位坐标
            // getWidth()：控件宽
            // getPaddingRight()：清除图右至EditText控件右距
            // 即getWidth() - getPaddingRight() = 清除图右坐标 = X1
            // getWidth() - getPaddingRight() - drawable.getBounds().width() = 清除图左坐标 = X2
            // 故X1与X2间区域 = 清除图区域
            // 手抬起位于清除图区域（X2 =< event.getX() <= X1）（即视为点清除图）
        }
        return super.onTouchEvent(event);
    }
}

