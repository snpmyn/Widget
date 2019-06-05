package com.zsp.library.button;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import value.WidgetMagic;

/**
 * Created on 2019/5/7.
 *
 * @author 郑少鹏
 * @desc 拖拽悬浮按钮
 */
public class DragFloatingActionButton extends FloatingActionButton {
    private int parentWidth;
    private int parentHeight;
    private int lastX;
    private int lastY;
    private boolean isDrag;

    public DragFloatingActionButton(Context context) {
        super(context);
    }

    public DragFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public DragFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (parentHeight <= 0 || parentWidth == 0) {
                    isDrag = false;
                    break;
                } else {
                    isDrag = true;
                }
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                // 此处修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0) {
                    isDrag = false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                // 检测到边缘否（左上右下）
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                Log.e("ACTION_MOVE", "isDrag=" + isDrag + "getX=" + getX() + "; getY=" + getY() + "; parentWidth=" + parentWidth);
                break;
            case MotionEvent.ACTION_UP:
                if (isNotDrag()) {
                    // 恢复按压效果
                    setPressed(false);
                    /*Log.e("getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf);*/
                    if (rawX >= parentWidth / WidgetMagic.INT_TWO) {
                        // 靠右吸附
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(300)
                                .xBy(parentWidth - getWidth() - getX())
                                .start();
                    } else {
                        // 靠左吸附
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(300);
                        oa.start();
                    }
                }
                break;
            default:
                break;
        }
        // 拖拽则消耗事件（否正常传递）
        return isNotDrag() || super.onTouchEvent(event);
    }

    private boolean isNotDrag() {
        return isDrag || (getX() != 0 && (getX() != parentWidth - getWidth()));
    }
}
