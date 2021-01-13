package com.zsp.library.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @decs: 计时器
 * @author: 郑少鹏
 * @date: 2019/4/23 17:20
 */
public class TimerTextView extends AppCompatTextView implements LifecycleObserver, View.OnClickListener {
    private static final String SHARED_PREFERENCES_FILE = "TimerTextView";
    private static final String SHARED_PREFERENCES_FIELD_TIME = "LastCountTime";
    private static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "LastCountTimestamp";
    private static final String SHARED_PREFERENCES_FIELD_INTERVAL = "CountInterval";
    private static final String SHARED_PREFERENCES_FIELD_COUNTDOWN = "IsCountdown";
    private CountDownTimer mCountDownTimer;
    private OnCountDownStartListener mOnCountDownStartListener;
    private OnCountDownTickListener mOnCountDownTickListener;
    private OnCountDownFinishListener mOnCountDownFinishListener;
    private String mNormalText;
    private String mCountDownText;
    private View.OnClickListener mOnClickListener;
    /**
     * 倒计时期间允点否
     */
    private boolean mClickable = false;
    /**
     * 关页后保倒计时否（再开启继倒计时）
     */
    private boolean mCloseKeepCountDown = false;
    /**
     * 格式化时间为时分秒否
     */
    private boolean mShowFormatTime = false;
    /**
     * 倒计时间隔
     */
    private TimeUnit mIntervalUnit = TimeUnit.SECONDS;

    public TimerTextView(Context context) {
        this(context, null);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 转毫秒为时分秒
     */
    @NonNull
    @SuppressLint("DefaultLocale")
    public static String generateTime(long time) {
        String format;
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            format = String.format("%02d时%02d分%02d秒", hours, minutes, seconds);
        } else if (minutes > 0) {
            format = String.format("%02d分%02d秒", minutes, seconds);
        } else {
            format = String.format("%2d秒", seconds);
        }
        return format;
    }

    private void init(Context context) {
        autoBindLifecycle(context);
    }

    /**
     * 控件自动绑定生命周期（宿主可Activity或Fragment）
     */
    private void autoBindLifecycle(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            for (Fragment fragment : fragments) {
                View parent = fragment.getView();
                if (parent != null) {
                    View find = parent.findViewById(getId());
                    if (find == this) {
                        fragment.getLifecycle().addObserver(this);
                        return;
                    }
                }
            }
        }
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        if (mCountDownTimer == null) {
            checkLastCountTimestamp();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    /**
     * 非倒计时状文本
     *
     * @param normalText 文本
     */
    public TimerTextView setNormalText(String normalText) {
        mNormalText = normalText;
        setText(normalText);
        return this;
    }

    /**
     * 倒计时文本内容
     *
     * @param front  倒计时文本前部分
     * @param latter 倒计时文本后部分
     */
    public TimerTextView setCountDownText(String front, String latter) {
        mCountDownText = front + "%1$s" + latter;
        return this;
    }

    /**
     * 倒计时间隔
     *
     * @param timeUnit TimeUnit
     */
    public TimerTextView setIntervalUnit(TimeUnit timeUnit) {
        mIntervalUnit = timeUnit;
        return this;
    }

    /**
     * 顺序计时
     * <p>
     * 非倒计时。
     *
     * @param second 计时时间秒
     */
    public void startCount(long second) {
        startCount(second, TimeUnit.SECONDS);
    }

    public void startCount(long time, final TimeUnit timeUnit) {
        if (mCloseKeepCountDown && checkLastCountTimestamp()) {
            return;
        }
        count(time, 0, timeUnit, false);
    }

    /**
     * 默秒倒计时
     *
     * @param second 多少秒
     */
    public void startCountDown(long second) {
        startCountDown(second, TimeUnit.SECONDS);
    }

    public void startCountDown(long time, final TimeUnit timeUnit) {
        if (mCloseKeepCountDown && checkLastCountTimestamp()) {
            return;
        }
        count(time, 0, timeUnit, true);
    }

    /**
     * 计时方案
     *
     * @param time        计时时长
     * @param timeUnit    时间单位
     * @param isCountDown 倒计时否（false正计时）
     */
    private void count(final long time, final long offset, final TimeUnit timeUnit, final boolean isCountDown) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        setEnabled(mClickable);
        final long millisInFuture = timeUnit.toMillis(time) + 500;
        long interval = TimeUnit.MILLISECONDS.convert(1, mIntervalUnit);
        if (mCloseKeepCountDown && offset == 0) {
            setLastCountTimestamp(millisInFuture, interval, isCountDown);
        }
        if (offset == 0 && mOnCountDownStartListener != null) {
            mOnCountDownStartListener.onStart();
        }
        if (TextUtils.isEmpty(mCountDownText)) {
            mCountDownText = getText().toString();
        }
        mCountDownTimer = new CountDownTimer(millisInFuture, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                long count = isCountDown ? millisUntilFinished : (millisInFuture - millisUntilFinished + offset);
                long l = timeUnit.convert(count, TimeUnit.MILLISECONDS);
                String showTime;
                if (mShowFormatTime) {
                    showTime = generateTime(count);
                } else {
                    showTime = String.valueOf(l);
                }
                setText(String.format(mCountDownText, showTime));
                if (mOnCountDownTickListener != null) {
                    mOnCountDownTickListener.onTick(l);
                }
            }

            @Override
            public void onFinish() {
                setEnabled(true);
                mCountDownTimer = null;
                setText(mNormalText);
                if (mOnCountDownFinishListener != null) {
                    mOnCountDownFinishListener.onFinish();
                }
            }
        };
        mCountDownTimer.start();
    }

    public TimerTextView setOnCountDownStartListener(OnCountDownStartListener onCountDownStartListener) {
        mOnCountDownStartListener = onCountDownStartListener;
        return this;
    }

    public TimerTextView setOnCountDownTickListener(OnCountDownTickListener onCountDownTickListener) {
        mOnCountDownTickListener = onCountDownTickListener;
        return this;
    }

    public TimerTextView setOnCountDownFinishListener(OnCountDownFinishListener onCountDownFinishListener) {
        mOnCountDownFinishListener = onCountDownFinishListener;
        return this;
    }

    /**
     * 倒计时期间点击事件响应否
     *
     * @param clickable 响应否
     */
    public TimerTextView setCountDownClickable(boolean clickable) {
        mClickable = clickable;
        return this;
    }

    /**
     * 关页保倒计时否
     *
     * @param keep 保否
     */
    public TimerTextView setCloseKeepCountDown(boolean keep) {
        mCloseKeepCountDown = keep;
        return this;
    }

    /**
     * 格式化时间否
     *
     * @param formatTime 格式化否
     */
    public TimerTextView setShowFormatTime(boolean formatTime) {
        mShowFormatTime = formatTime;
        return this;
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        mOnClickListener = l;
        super.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mCountDownTimer != null && !mClickable) {
            return;
        }
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    /**
     * 持久化
     *
     * @param time        倒计时时长
     * @param interval    倒计时间隔
     * @param isCountDown 倒计时否
     */
    private void setLastCountTimestamp(long time, long interval, boolean isCountDown) {
        getContext()
                .getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit()
                .putLong(SHARED_PREFERENCES_FIELD_TIME + getId(), time)
                .putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP + getId(), Calendar.getInstance().getTimeInMillis() + time)
                .putLong(SHARED_PREFERENCES_FIELD_INTERVAL + getId(), interval)
                .putBoolean(SHARED_PREFERENCES_FIELD_COUNTDOWN + getId(), isCountDown)
                .apply();

    }

    /**
     * 查持久化参数
     *
     * @return 保持久化计时否
     */
    private boolean checkLastCountTimestamp() {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        long lastCountTimestamp = sp.getLong(SHARED_PREFERENCES_FIELD_TIMESTAMP + getId(), -1);
        long nowTimeMillis = Calendar.getInstance().getTimeInMillis();
        long diff = lastCountTimestamp - nowTimeMillis;
        if (diff <= 0) {
            return false;
        }
        long time = sp.getLong(SHARED_PREFERENCES_FIELD_TIME + getId(), -1);
        long interval = sp.getLong(SHARED_PREFERENCES_FIELD_INTERVAL + getId(), -1);
        boolean isCountDown = sp.getBoolean(SHARED_PREFERENCES_FIELD_COUNTDOWN + getId(), true);
        for (TimeUnit timeUnit : TimeUnit.values()) {
            long convert = timeUnit.convert(interval, TimeUnit.MILLISECONDS);
            if (convert == 1) {
                long last = timeUnit.convert(diff, TimeUnit.MILLISECONDS);
                long offset = time - diff;
                count(last, offset, timeUnit, isCountDown);
                return true;
            }
        }
        return false;
    }

    public interface OnCountDownStartListener {
        /**
         * 计时开始回调
         * <p>
         * 反序列化时不回调。
         */
        void onStart();
    }

    public interface OnCountDownTickListener {
        /**
         * 计时回调
         *
         * @param untilFinished 剩余时间（单位为开始计时传入单位）
         */
        void onTick(long untilFinished);
    }

    public interface OnCountDownFinishListener {
        /**
         * 计时结束回调
         */
        void onFinish();
    }
}
