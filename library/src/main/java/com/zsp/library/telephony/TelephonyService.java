package com.zsp.library.telephony;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import timber.log.Timber;

/**
 * Created on 2019/6/11.
 *
 * @author 郑少鹏
 * @desc 电话服务
 */
public class TelephonyService extends Service {
    /**
     * 电话管理器
     */
    private TelephonyManager telephonyManager;
    /**
     * 电话状监听
     */
    private MyPhoneStateListener myPhoneStateListener;
    /**
     * 电话服务监听
     */
    private static TelephonyServiceListener telephonyServiceListener;
    /**
     * 记录上电话状
     */
    private int lastCallState = TelephonyManager.CALL_STATE_IDLE;
    /**
     * 时长
     */
    private long duration;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("开启电话服务");
        // 监听通话状
        listenCallState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("关闭电话服务");
        // 不监听
        listenNone();
    }

    /**
     * 监听通话状
     */
    private void listenCallState() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 不监听
     */
    private void listenNone() {
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * 电话状监听
     */
    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Timber.d(incomingNumber);
            switch (state) {
                /*
                  响铃
                 */
                case TelephonyManager.CALL_STATE_RINGING:
                    Timber.d("响铃");
                    lastCallState = TelephonyManager.CALL_STATE_RINGING;
                    duration = System.currentTimeMillis();
                    telephonyServiceListener.callStateRinging(duration);
                    break;
                /*
                  通话
                 */
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (lastCallState == TelephonyManager.CALL_STATE_RINGING) {
                        Timber.d("呼入");
                        telephonyServiceListener.incomingCall();
                    } else {
                        Timber.d("呼出");
                        duration = System.currentTimeMillis();
                        telephonyServiceListener.callOut(duration);
                    }
                    lastCallState = TelephonyManager.CALL_STATE_OFFHOOK;
                    break;
                /*
                  空闲
                 */
                case TelephonyManager.CALL_STATE_IDLE:
                    if (lastCallState == TelephonyManager.CALL_STATE_IDLE) {
                        Timber.d("无通话");
                        if (telephonyServiceListener != null) {
                            telephonyServiceListener.noCalls();
                        }
                    } else {
                        Timber.d("挂断通话");
                        lastCallState = TelephonyManager.CALL_STATE_IDLE;
                        long endTime = System.currentTimeMillis();
                        telephonyServiceListener.hangUpTheCall(endTime, endTime - duration);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设电话服务监听
     *
     * @param telephonyServiceListener 电话服务监听
     */
    public static void setTelephonyServiceListener(TelephonyServiceListener telephonyServiceListener) {
        TelephonyService.telephonyServiceListener = telephonyServiceListener;
    }

    /**
     * 电话服务监听
     */
    public interface TelephonyServiceListener {
        /**
         * 响铃
         * <p>
         * 呼入调，不调{@link #incomingCall()}。
         *
         * @param startTime 开始时间
         */
        void callStateRinging(long startTime);

        /**
         * 呼入
         * <p>
         * 呼入不调，调{@link #callStateRinging(long)}。
         */
        void incomingCall();

        /**
         * 呼出
         *
         * @param startTime 开始时间
         */
        void callOut(long startTime);

        /**
         * 无通话
         */
        void noCalls();

        /**
         * 挂断通话
         *
         * @param endTime  结束时间
         * @param duration 时长
         */
        void hangUpTheCall(long endTime, long duration);
    }
}

