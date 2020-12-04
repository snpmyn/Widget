package com.zsp.library.sms.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.zsp.library.sms.kit.SmsKit;
import com.zsp.utilone.toast.ToastUtils;

/**
 * Created on 2018/1/11.
 *
 * @author 郑少鹏
 * @desc 短信广播接收器
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private SmsBroadcastReceiverSendListener smsBroadcastReceiverSendListener;
    private SmsBroadcastReceiverDeliverListener smsBroadcastReceiverDeliverListener;

    public void setSmsBroadcastReceiverSendListener(SmsBroadcastReceiverSendListener smsBroadcastReceiverSendListener) {
        this.smsBroadcastReceiverSendListener = smsBroadcastReceiverSendListener;
    }

    public void setSmsBroadcastReceiverDeliverListener(SmsBroadcastReceiverDeliverListener smsBroadcastReceiverDeliverListener) {
        this.smsBroadcastReceiverDeliverListener = smsBroadcastReceiverDeliverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent.getAction()) {
            if (intent.getAction().equals(SmsKit.SMS_SEND_ACTION)) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        smsBroadcastReceiverSendListener.sendResultOk();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        smsBroadcastReceiverSendListener.sendResultErrorCenericFailure();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        ToastUtils.shortShow(context, "RESULT_ERROR_RADIO_OFF");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        ToastUtils.shortShow(context, "RESULT_ERROR_NULL_PDU");
                        break;
                    default:
                        break;
                }
            } else if (intent.getAction().equals(SmsKit.SMS_DELIVER_ACTION)) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        smsBroadcastReceiverDeliverListener.deliverResultOk();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        smsBroadcastReceiverDeliverListener.deliverResultErrorCenericFailure();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        ToastUtils.shortShow(context, "RESULT_ERROR_RADIO_OFF");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        ToastUtils.shortShow(context, "RESULT_ERROR_NULL_PDU");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 短信广播接收器发送监听
     */
    public interface SmsBroadcastReceiverSendListener {
        /**
         * 发送（RESULT_OK）
         */
        void sendResultOk();

        /**
         * 发送（RESULT_ERROR_GENERIC_FAILURE）
         */
        void sendResultErrorCenericFailure();
    }

    /**
     * 短信广播接收器传送监听
     */
    public interface SmsBroadcastReceiverDeliverListener {
        /**
         * 传送（RESULT_OK）
         */
        void deliverResultOk();

        /**
         * 传送（RESULT_ERROR_GENERIC_FAILURE）
         */
        void deliverResultErrorCenericFailure();
    }
}