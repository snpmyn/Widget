package com.zsp.library.picture.kit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import androidx.annotation.FloatRange;
import androidx.core.app.ActivityOptionsCompat;

import com.zsp.library.R;
import com.zsp.library.picture.activity.PicturePreviewActivity;

import timber.log.Timber;

/**
 * @decs: DragKit
 * @author: 郑少鹏
 * @date: 2019/6/6 19:26
 */
public class DragKit {
    /**
     * 动画执行时长
     */
    private final static long DURATION = 100;
    /**
     * 滑动边界距离
     */
    private final static int MAX_EXIT_Y = 500;
    /**
     * 最小缩放尺寸
     */
    private static final float MIN_SCALE_SIZE = 0.4F;
    /**
     * 数据
     */
    public static Object[] data;
    /**
     * 索引
     */
    public static int index;
    /**
     * 共享元素名
     */
    public String sharedElementName = "share_picture";
    /**
     * ViewConfiguration
     */
    private ViewConfiguration viewConfiguration;
    private int yMaxExit = MAX_EXIT_Y;
    private float minScaleSize = MIN_SCALE_SIZE;
    /**
     * 滑动关闭中否（手指触摸中）
     */
    private boolean isSwipingToClose;
    /**
     * 上次触摸坐标
     */
    private float yLast, yLastRaw, xLast, xLastRaw;
    /**
     * 上次触摸手指ID
     */
    private int lastPointerId;
    /**
     * 当前位移距离
     */
    private float yCurrentTranslation, xCurrentTranslation;
    /**
     * 上次位移距离
     */
    private float yLastTranslation, xLastTranslation;
    /**
     * 恢复原位中否
     */
    private boolean isResettingAnimate = false;
    /**
     * 共享元素模式
     */
    private boolean isShareElementMode = false;
    private View vParent, vChild;
    private DragCloseListener dragCloseListener;
    private Context mContext;

    public DragKit(Context mContext) {
        this.mContext = mContext;
        viewConfiguration = ViewConfiguration.get(mContext);
    }

    public void setDragCloseListener(DragCloseListener dragCloseListener) {
        this.dragCloseListener = dragCloseListener;
    }

    /**
     * 共享元素模式
     *
     * @param shareElementMode 共享元素模式否
     */
    public void setShareElementMode(boolean shareElementMode) {
        isShareElementMode = shareElementMode;
    }

    /**
     * 拖拽关闭的视图
     *
     * @param parentView 父视图
     * @param childView  子视图
     */
    public void setDragCloseView(View parentView, View childView) {
        this.vParent = parentView;
        this.vChild = childView;
    }

    /**
     * 最大退出距离
     *
     * @param yMaxExit 最大退出距离
     */
    public void setyMaxExit(int yMaxExit) {
        this.yMaxExit = yMaxExit;
    }

    /**
     * 最小缩放尺寸
     *
     * @param minScaleSize 最小缩放尺寸
     */
    public void setMinScaleSize(@FloatRange(from = 0.1f, to = 1.0f) float minScaleSize) {
        this.minScaleSize = minScaleSize;
    }

    /**
     * 处理触摸事件
     *
     * @param event 触摸事件
     * @return 处理触摸事件否
     */
    public boolean handleMotionEvent(MotionEvent event) {
        if (dragCloseListener != null && dragCloseListener.intercept()) {
            // 拦截
            Timber.d("action dispatch--->");
            isSwipingToClose = false;
            return false;
        } else {
            // 不拦截
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Timber.d("action down--->");
                // 初始数据
                lastPointerId = event.getPointerId(0);
                reset(event);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                Timber.d("action move--->%s---%s", event.getPointerCount(), isSwipingToClose);
                if (event.getPointerCount() > 1) {
                    // 有多手指
                    if (isSwipingToClose) {
                        // 已开始滑动关闭，恢复原状（否需派发事件）
                        isSwipingToClose = false;
                        resetCallBackAnimation();
                        return true;
                    }
                    reset(event);
                    return false;
                }
                if (lastPointerId != event.getPointerId(0)) {
                    // 手指不一致，恢复原状
                    if (isSwipingToClose) {
                        resetCallBackAnimation();
                    }
                    reset(event);
                    return true;
                }
                float yCurrent = event.getY();
                float xCurrent = event.getX();
                boolean flag = isSwipingToClose || (Math.abs(yCurrent - yLast) > 2 * viewConfiguration.getScaledTouchSlop() && Math.abs(yCurrent - yLast) > Math.abs(xCurrent - xLast) * 1.5);
                if (flag) {
                    // 已触发或开始触发，更新视图
                    yLast = yCurrent;
                    xLast = xCurrent;
                    Timber.d("action move--->%s", "start close");
                    float yCurrentRaw = event.getRawY();
                    float xCurrentRaw = event.getRawX();
                    if (!isSwipingToClose) {
                        // 准备开始
                        isSwipingToClose = true;
                        if (dragCloseListener != null) {
                            dragCloseListener.dragStart();
                        }
                    }
                    // 已开始，更新视图
                    yCurrentTranslation = yCurrentRaw - yLastRaw + yLastTranslation;
                    xCurrentTranslation = xCurrentRaw - xLastRaw + xLastTranslation;
                    float percent = 1 - Math.abs(yCurrentTranslation / (yMaxExit + vChild.getHeight()));
                    if (percent > 1) {
                        percent = 1;
                    } else if (percent < 0) {
                        percent = 0;
                    }
                    vParent.getBackground().mutate().setAlpha((int) (percent * 255));
                    if (dragCloseListener != null) {
                        dragCloseListener.dragging(percent);
                    }
                    vChild.setTranslationY(yCurrentTranslation);
                    vChild.setTranslationX(xCurrentTranslation);
                    if (percent < minScaleSize) {
                        percent = minScaleSize;
                    }
                    vChild.setScaleX(percent);
                    vChild.setScaleY(percent);
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Timber.d("action up--->%s", isSwipingToClose);
                // 手指抬起事件
                if (isSwipingToClose) {
                    if (yCurrentTranslation > yMaxExit) {
                        if (isShareElementMode) {
                            // 执行共享元素退出动画
                            if (dragCloseListener != null) {
                                dragCloseListener.dragClose(true);
                            }
                        } else {
                            // 执行定制退出动画
                            exitWithTranslation(yCurrentTranslation);
                        }
                    } else {
                        resetCallBackAnimation();
                    }
                    isSwipingToClose = false;
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                // 取消事件
                if (isSwipingToClose) {
                    resetCallBackAnimation();
                    isSwipingToClose = false;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出动画
     *
     * @param yCurrent 当前Y坐标
     */
    private void exitWithTranslation(float yCurrent) {
        int targetValue = yCurrent > 0 ? vChild.getHeight() : -vChild.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(yCurrentTranslation, targetValue);
        valueAnimator.addUpdateListener(animation -> updateChildView(xCurrentTranslation, (float) animation.getAnimatedValue()));
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (dragCloseListener != null) {
                    dragCloseListener.dragClose(false);
                }
                ((Activity) mContext).finish();
                ((Activity) mContext).overridePendingTransition(R.anim.drag_no, R.anim.drag_out);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(DURATION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    /**
     * 重置
     *
     * @param event 触摸事件
     */
    private void reset(MotionEvent event) {
        isSwipingToClose = false;
        yLast = event.getY();
        xLast = event.getX();
        yLastRaw = event.getRawY();
        xLastRaw = event.getRawX();
        yLastTranslation = 0;
        xLastTranslation = 0;
    }

    /**
     * 更新子视图
     */
    private void updateChildView(float xTrans, float yTrans) {
        vChild.setTranslationY(yTrans);
        vChild.setTranslationX(xTrans);
        float percent = Math.abs(yTrans / (yMaxExit + vChild.getHeight()));
        float scale = 1 - percent;
        if (scale < minScaleSize) {
            scale = minScaleSize;
        }
        vChild.setScaleX(scale);
        vChild.setScaleY(scale);
    }

    /**
     * 重置至原位动画
     */
    private void resetCallBackAnimation() {
        if (isResettingAnimate || yCurrentTranslation == 0) {
            return;
        }
        float ratio = xCurrentTranslation / yCurrentTranslation;
        ValueAnimator yAnimator = ValueAnimator.ofFloat(yCurrentTranslation, 0);
        yAnimator.addUpdateListener(valueAnimator -> {
            if (isResettingAnimate) {
                yCurrentTranslation = (float) valueAnimator.getAnimatedValue();
                xCurrentTranslation = ratio * yCurrentTranslation;
                yLastTranslation = yCurrentTranslation;
                xLastTranslation = xCurrentTranslation;
                updateChildView(xLastTranslation, yCurrentTranslation);
            }
        });
        yAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isResettingAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isResettingAnimate) {
                    vParent.getBackground().mutate().setAlpha(255);
                    yCurrentTranslation = 0;
                    xCurrentTranslation = 0;
                    isResettingAnimate = false;
                    if (dragCloseListener != null) {
                        dragCloseListener.dragCancel();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        yAnimator.setDuration(DURATION).start();
    }

    /**
     * 跳转
     *
     * @param activity 活动
     * @param view     视图
     * @param data     数据
     * @param index    索引
     */
    public void jump(Activity activity, View view, Object[] data, int index) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, sharedElementName);
        DragKit.data = data;
        DragKit.index = index;
        Intent intent = new Intent();
        intent.setClass(activity, PicturePreviewActivity.class);
        activity.startActivity(intent, compat.toBundle());
    }

    public interface DragCloseListener {
        /**
         * 拦截
         *
         * @return 拦截否
         */
        boolean intercept();

        /**
         * 开始拖拽
         */
        void dragStart();

        /**
         * 拖拽中
         *
         * @param percent 百分比
         */
        void dragging(float percent);

        /**
         * 取消拖拽
         */
        void dragCancel();

        /**
         * 拖拽结束且关
         *
         * @param isShareElementMode 共享元素模式否
         */
        void dragClose(boolean isShareElementMode);
    }
}
