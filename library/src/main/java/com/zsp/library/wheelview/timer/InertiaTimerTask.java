package com.zsp.library.wheelview.timer;

import com.zsp.library.wheelview.view.WheelView;

import java.util.TimerTask;

import value.WidgetMagic;

/**
 * Created on 2018/4/3.
 *
 * @author 郑少鹏
 * @desc InertiaTimerTask
 */
public final class InertiaTimerTask extends TimerTask {
    /**
     * 手指离屏时初始速
     */
    private final float mFirstVelocityY;
    private final WheelView mWheelView;
    /**
     * 当前滑速
     */
    private float mCurrentVelocityY;

    /**
     * @param wheelView 滚轮对象
     * @param velocityY Y轴滑速
     */
    public InertiaTimerTask(WheelView wheelView, float velocityY) {
        super();
        this.mWheelView = wheelView;
        this.mFirstVelocityY = velocityY;
        mCurrentVelocityY = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        // 防闪动（对速度限制）
        if (mCurrentVelocityY == Integer.MAX_VALUE) {
            if (Math.abs(mFirstVelocityY) > WidgetMagic.FLOAT_TWO_THOUSAND) {
                mCurrentVelocityY = mFirstVelocityY > 0 ? 2000F : -2000F;
            } else {
                mCurrentVelocityY = mFirstVelocityY;
            }
        }
        // 发handler消息，处理平顺停止滚动逻辑
        if (Math.abs(mCurrentVelocityY) >= WidgetMagic.FLOAT_LDL && Math.abs(mCurrentVelocityY) <= WidgetMagic.FLOAT_TWENTY) {
            mWheelView.cancelFuture();
            mWheelView.getHandler().sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int dy = (int) (mCurrentVelocityY / 100F);
        mWheelView.setTotalScrollY(mWheelView.getTotalScrollY() - dy);
        if (!mWheelView.isLoop()) {
            float itemHeight = mWheelView.getItemHeight();
            float top = (-mWheelView.getInitPosition()) * itemHeight;
            float bottom = (mWheelView.getItemsCount() - 1 - mWheelView.getInitPosition()) * itemHeight;
            if (mWheelView.getTotalScrollY() - itemHeight * WidgetMagic.FLOAT_LDEW < top) {
                top = mWheelView.getTotalScrollY() + dy;
            } else if (mWheelView.getTotalScrollY() + itemHeight * WidgetMagic.FLOAT_LDEW > bottom) {
                bottom = mWheelView.getTotalScrollY() + dy;
            }
            if (mWheelView.getTotalScrollY() <= top) {
                mCurrentVelocityY = 40F;
                mWheelView.setTotalScrollY((int) top);
            } else if (mWheelView.getTotalScrollY() >= bottom) {
                mWheelView.setTotalScrollY((int) bottom);
                mCurrentVelocityY = -40F;
            }
        }
        if (mCurrentVelocityY < WidgetMagic.FLOAT_LDL) {
            mCurrentVelocityY = mCurrentVelocityY + 20F;
        } else {
            mCurrentVelocityY = mCurrentVelocityY - 20F;
        }
        // 刷UI
        mWheelView.getHandler().sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}

