package com.zsp.library.sms.kit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
public class SmsKit {
    /**
     * 上下文
     */
    private WeakReference<Context> weakReference;
    /**
     * 短信广播接收器
     */
    private SmsBroadcastReceiver smsSendBroadcastReceiver, smsDeliverBroadcastReceiver;
    /**
     * 意图
     */
    private Intent smsSendIntent;
    private Intent smsDeliverIntent;
    private PendingIntent smsSendPendingIntent;
    private PendingIntent smsDeliverPendingIntent;
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
        registerReceiver();
        setListener();
        smsSendIntent = new Intent(SMS_SEND_ACTION);
        smsDeliverIntent = new Intent(SMS_DELIVER_ACTION);
        smsSendPendingIntent = PendingIntent.getBroadcast(weakReference.get(), 0, smsSendIntent, 0);
        smsDeliverPendingIntent = PendingIntent.getBroadcast(weakReference.get(), 0, smsDeliverIntent, 0);
    }

    /**
     * 注册
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
     * 反注册
     */
    public void unregisterReceiver() {
        PackageManager packageManager = weakReference.get().getPackageManager();
        List<ResolveInfo> smsSendResolveInfos = packageManager.queryBroadcastReceivers(smsSendIntent, 0);
        List<ResolveInfo> smsDeliverResolveInfos = packageManager.queryBroadcastReceivers(smsDeliverIntent, 0);
        if (!smsSendResolveInfos.isEmpty()) {
            weakReference.get().unregisterReceiver(smsSendBroadcastReceiver);
        }
        if (!smsDeliverResolveInfos.isEmpty()) {
            weakReference.get().unregisterReceiver(smsDeliverBroadcastReceiver);
        }
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
        // 发送
        smsSendBroadcastReceiver.setSmsBroadcastReceiverSendListener(new SmsBroadcastReceiver.SmsBroadcastReceiverSendListener() {
            @Override
            public void resultOk() {
                smsKitSendListener.resultOk();
            }

            @Override
            public void resultErrorCenericFailure() {
                smsKitSendListener.resultErrorCenericFailure();
            }
        });
        // 传送
        smsDeliverBroadcastReceiver.setSmsBroadcastReceiverDeliverListener(new SmsBroadcastReceiver.SmsBroadcastReceiverDeliverListener() {
            @Override
            public void resultOk() {
                smsKitDeliverListener.resultOk();
            }

            @Override
            public void resultErrorCenericFailure() {
                smsKitDeliverListener.resultErrorCenericFailure();
            }
        });
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
         * RESULT_OK
         */
        void resultOk();

        /**
         * RESULT_ERROR_GENERIC_FAILURE
         */
        void resultErrorCenericFailure();
    }

    /**
     * SmsKit传送监听
     */
    public interface SmsKitDeliverListener {
        /**
         * RESULT_OK
         */
        void resultOk();

        /**
         * RESULT_ERROR_GENERIC_FAILURE
         */
        void resultErrorCenericFailure();
    }
}
