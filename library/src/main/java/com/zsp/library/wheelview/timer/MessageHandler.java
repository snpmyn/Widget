package com.zsp.library.wheelview.timer;

import android.os.Handler;
import android.os.Message;

import com.zsp.library.wheelview.view.WheelView;

/**
 * Created on 2018/4/3.
 *
 * @author 郑少鹏
 * @desc MessageHandler
 */
public final class MessageHandler extends Handler {
    static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    static final int WHAT_SMOOTH_SCROLL = 2000;
    static final int WHAT_ITEM_SELECTED = 3000;
    private final WheelView wheelView;

    public MessageHandler(WheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                wheelView.invalidate();
                break;
            case WHAT_SMOOTH_SCROLL:
                wheelView.smoothScroll(WheelView.ACTION.FLING);
                break;
            case WHAT_ITEM_SELECTED:
                wheelView.onItemSelected();
                break;
            default:
                break;
        }
    }
}

