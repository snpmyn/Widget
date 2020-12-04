package com.zsp.library.sms.kit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.zsp.library.sms.receiver.SmsBroadcastReceiver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/1/11.
 *
 * @author 郑少鹏
 * @desc SmsKit
 */
public class SmsKit implements SmsBroadcastReceiver.SmsBroadcastReceiverSendListener, SmsBroadcastReceiver.SmsBroadcastReceiverDeliverListener {
    /**
     * 上下文
     */
    private final WeakReference<Context> weakReference;
    /**
     * 短信广播接收器
     */
    private SmsBroadcastReceiver smsSendBroadcastReceiver, smsDeliverBroadcastReceiver;
    /**
     * 意图
     */
    private final PendingIntent smsSendPendingIntent;
    private final PendingIntent smsDeliverPendingIntent;
    /**
     * 滤值
     */
    public static String SMS_SEND_ACTION = "SMS_SEND_ACTION";
    public static String SMS_DELIVER_ACTION = "SMS_DELIVERED_ACTION";
    /**
     * SmsKit发/传送监听
     */
    private SmsKitSendListener smsKitSendListener;
    private SmsKitDeliverListener smsKitDeliverListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public SmsKit(Context context) {
        this.weakReference = new WeakReference<>(context);
        // 注册接收器
        registerReceiver();
        // 设监听
        setListener();
        // 意图
        Intent smsSendIntent = new Intent(SMS_SEND_ACTION);
        Intent smsDeliverIntent = new Intent(SMS_DELIVER_ACTION);
        smsSendPendingIntent = PendingIntent.getBroadcast(weakReference.get(), 0, smsSendIntent, 0);
        smsDeliverPendingIntent = PendingIntent.getBroadcast(weakReference.get(), 0, smsDeliverIntent, 0);
    }

    /**
     * 注册接收器
     */
    private void registerReceiver() {
        // 发送
        smsSendBroadcastReceiver = new SmsBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(SMS_SEND_ACTION);
        weakReference.get().registerReceiver(smsSendBroadcastReceiver, intentFilter);
        // 传送
        smsDeliverBroadcastReceiver = new SmsBroadcastReceiver();
        intentFilter = new IntentFilter(SMS_DELIVER_ACTION);
        weakReference.get().registerReceiver(smsDeliverBroadcastReceiver, intentFilter);
    }

    /**
     * 反注册接收器
     */
    public void unregisterReceiver() {
        // 发送
        weakReference.get().unregisterReceiver(smsSendBroadcastReceiver);
        // 传送
        weakReference.get().unregisterReceiver(smsDeliverBroadcastReceiver);
    }

    /**
     * 单发
     *
     * @param address 地址
     * @param content 内容
     */
    public void singleShot(String address, String content) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            List<String> strings = smsManager.divideMessage(content);
            for (String text : strings) {
                smsManager.sendTextMessage(address, null, text, smsSendPendingIntent, smsDeliverPendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单发二
     *
     * @param address 地址
     * @param content 内容
     */
    public void singleShotTwo(String address, String content) {
        ArrayList<PendingIntent> smsSendPendingIntents = new ArrayList<>();
        ArrayList<PendingIntent> smsDeliverPendingIntents = new ArrayList<>();
        SmsManager smsManager = SmsManager.getDefault();
        try {
            ArrayList<String> strings = smsManager.divideMessage(content);
            for (int i = 0; i < strings.size(); i++) {
                smsSendPendingIntents.add(i, smsSendPendingIntent);
                smsDeliverPendingIntents.add(i, smsDeliverPendingIntent);
            }
            smsManager.sendMultipartTextMessage(address, null, strings, smsSendPendingIntents, smsDeliverPendingIntents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发
     *
     * @param addresses 地址
     * @param content   内容
     */
    public void mass(List<String> addresses, String content) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            List<String> strings = smsManager.divideMessage(content);
            for (String address : addresses) {
                for (String text : strings) {
                    smsManager.sendTextMessage(address, null, text, smsSendPendingIntent, smsDeliverPendingIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设监听
     */
    private void setListener() {
        smsSendBroadcastReceiver.setSmsBroadcastReceiverSendListener(this);
        smsDeliverBroadcastReceiver.setSmsBroadcastReceiverDeliverListener(this);
    }

    /**
     * 设SmsKit发送监听
     *
     * @param smsKitSendListener SmsKit发送监听
     */
    public void setSmsKitSendListener(SmsKitSendListener smsKitSendListener) {
        this.smsKitSendListener = smsKitSendListener;
    }

    /**
     * 设SmsKit传送监听
     *
     * @param smsKitDeliverListener SmsKit传送监听
     */
    public void setSmsKitDeliverListener(SmsKitDeliverListener smsKitDeliverListener) {
        this.smsKitDeliverListener = smsKitDeliverListener;
    }

    /**
     * SmsKit发送监听
     */
    public interface SmsKitSendListener {
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
     * SmsKit传送监听
     */
    public interface SmsKitDeliverListener {
        /**
         * 传送（RESULT_OK）
         */
        void deliverResultOk();

        /**
         * 传送（RESULT_ERROR_GENERIC_FAILURE）
         */
        void deliverResultErrorCenericFailure();
    }

    /**
     * 发送（RESULT_OK）
     */
    @Override
    public void sendResultOk() {
        smsKitSendListener.sendResultOk();
    }

    /**
     * 发送（RESULT_ERROR_GENERIC_FAILURE）
     */
    @Override
    public void sendResultErrorCenericFailure() {
        smsKitSendListener.sendResultErrorCenericFailure();
    }

    /**
     * 传送（RESULT_OK）
     */
    @Override
    public void deliverResultOk() {
        smsKitDeliverListener.deliverResultOk();
    }

    /**
     * 传送（RESULT_ERROR_GENERIC_FAILURE）
     */
    @Override
    public void deliverResultErrorCenericFailure() {
        smsKitDeliverListener.deliverResultErrorCenericFailure();
    }
}
