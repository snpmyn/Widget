package com.zsp.library.pickerview.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.zsp.library.R;
import com.zsp.library.pickerview.configure.PickerOptions;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import value.WidgetMagic;

/**
 * @decs: 时间选择器
 * @author: 郑少鹏
 * @date: 2018/4/3 17:35
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "bill_cancel";
    /**
     * 自定义控件
     */
    private WheelTime wheelTime;

    public TimePickerView(PickerOptions pickerOptions) {
        super(pickerOptions.context);
        mPickerOptions = pickerOptions;
        initView(pickerOptions.context);
    }

    private void initView(Context context) {
        setDialogOutSideCancelable();
        initViews();
        initAnim();
        if (mPickerOptions.customListener == null) {
            LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);
            // 顶标
            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            RelativeLayout rvTopBar = (RelativeLayout) findViewById(R.id.rlTop);
            // 确定/取消
            Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
            Button btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit.setTag(TAG_SUBMIT);
            btnCancel.setTag(TAG_CANCEL);
            btnSubmit.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
            // 文本
            btnSubmit.setText(TextUtils.isEmpty(mPickerOptions.textContentConfirm) ? context.getResources().getString(R.string.ensure) : mPickerOptions.textContentConfirm);
            btnCancel.setText(TextUtils.isEmpty(mPickerOptions.textContentCancel) ? context.getResources().getString(R.string.cancel) : mPickerOptions.textContentCancel);
            // 默空
            tvTitle.setText(TextUtils.isEmpty(mPickerOptions.textContentTitle) ? "" : mPickerOptions.textContentTitle);
            // color
            // 自定
            btnSubmit.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            // 自定
            btnCancel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            // 自定
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.fontInput));
            // 自定
            rvTopBar.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
            // 文字大小
            btnSubmit.setTextSize(mPickerOptions.textSizeSubmitCancel);
            btnCancel.setTextSize(mPickerOptions.textSizeSubmitCancel);
            tvTitle.setTextSize(mPickerOptions.textSizeTitle);
        } else {
            mPickerOptions.customListener.customLayout(LayoutInflater.from(context).inflate(mPickerOptions.layoutRes, contentContainer));
        }
        // 时间转轮（自定义）
        LinearLayout timePicker = (LinearLayout) findViewById(R.id.timePicker);
        timePicker.setBackgroundColor(mPickerOptions.bgColorWheel);
        initWheelTime(context, timePicker);
    }

    private void initWheelTime(Context context, LinearLayout timePickerView) {
        wheelTime = new WheelTime(timePickerView, mPickerOptions.type, mPickerOptions.textGravity, mPickerOptions.textSizeContent);
        if (mPickerOptions.timeSelectChangeListener != null) {
            wheelTime.setSelectChangeCallback(() -> {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    mPickerOptions.timeSelectChangeListener.onTimeSelectChanged(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        wheelTime.setLunarMode(mPickerOptions.isLunarCalendar);
        if (mPickerOptions.startYear != 0 && mPickerOptions.endYear != 0 && mPickerOptions.startYear <= mPickerOptions.endYear) {
            setRange();
        }
        // 手设时间范围限制
        if (mPickerOptions.startDate != null && mPickerOptions.endDate != null) {
            if (mPickerOptions.startDate.getTimeInMillis() > mPickerOptions.endDate.getTimeInMillis()) {
                throw new IllegalArgumentException("startDate can't be later than endDate");
            } else {
                setRangDate();
            }
        } else if (mPickerOptions.startDate != null) {
            if (mPickerOptions.startDate.get(Calendar.YEAR) < WidgetMagic.INT_ONE_THOUSAND_NINE_HUNDRED) {
                throw new IllegalArgumentException("The startDate can not as early as 1900");
            } else {
                setRangDate();
            }
        } else if (mPickerOptions.endDate != null) {
            if (mPickerOptions.endDate.get(Calendar.YEAR) > WidgetMagic.INT_TWO_THOUSAND) {
                throw new IllegalArgumentException("The endDate should not be later than 2100");
            } else {
                setRangDate();
            }
        } else {
            // 没设时间范围限制则用默范围
            setRangDate();
        }
        setTime();
        wheelTime.setLabels(mPickerOptions.labelYear, mPickerOptions.labelMonth, mPickerOptions.labelDay,
                mPickerOptions.labelHours, mPickerOptions.labelMinutes, mPickerOptions.labelSeconds);
        wheelTime.setTextXOffset(mPickerOptions.xOffsetYear, mPickerOptions.xOffsetMonth, mPickerOptions.xOffsetDay,
                mPickerOptions.xOffsetHours, mPickerOptions.xOffsetMinutes, mPickerOptions.xOffsetSeconds);
        setOutSideCancelable(mPickerOptions.cancelable);
        wheelTime.setCyclic(mPickerOptions.cyclic);
        // 自定
        wheelTime.setDividerColor(ContextCompat.getColor(context, R.color.graySelect));
        wheelTime.setDividerType(mPickerOptions.dividerType);
        wheelTime.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        // 自定
        wheelTime.setTextColorOut(ContextCompat.getColor(context, R.color.fontHint));
        // 自定
        wheelTime.setTextColorCenter(ContextCompat.getColor(context, R.color.fontInput));
        wheelTime.isCenterLabel(mPickerOptions.isCenterLabel);
    }

    /**
     * 默时
     */
    public void setDate(Calendar date) {
        mPickerOptions.date = date;
        setTime();
    }

    /**
     * 可选时间范围（setTime前调有效）
     */
    private void setRange() {
        wheelTime.setStartYear(mPickerOptions.startYear);
        wheelTime.setEndYear(mPickerOptions.endYear);

    }

    /**
     * 可选择时间范围（setTime前调有效）
     */
    private void setRangDate() {
        wheelTime.setRangDate(mPickerOptions.startDate, mPickerOptions.endDate);
        initDefaultSelectedDate();
    }

    private void initDefaultSelectedDate() {
        // 手设时间范围
        if (mPickerOptions.startDate != null && mPickerOptions.endDate != null) {
            // 默时未设或所设默时越界，则设默选时为开始时
            if (mPickerOptions.date == null || mPickerOptions.date.getTimeInMillis() < mPickerOptions.startDate.getTimeInMillis()
                    || mPickerOptions.date.getTimeInMillis() > mPickerOptions.endDate.getTimeInMillis()) {
                mPickerOptions.date = mPickerOptions.startDate;
            }
        } else if (mPickerOptions.startDate != null) {
            // 没设默选时则以开始时为默时
            mPickerOptions.date = mPickerOptions.startDate;
        } else if (mPickerOptions.endDate != null) {
            mPickerOptions.date = mPickerOptions.endDate;
        }
    }

    /**
     * 选中时（默选当前时）
     */
    private void setTime() {
        int year, month, day, hours, minute, seconds;
        Calendar calendar = Calendar.getInstance();
        if (mPickerOptions.date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        } else {
            year = mPickerOptions.date.get(Calendar.YEAR);
            month = mPickerOptions.date.get(Calendar.MONTH);
            day = mPickerOptions.date.get(Calendar.DAY_OF_MONTH);
            hours = mPickerOptions.date.get(Calendar.HOUR_OF_DAY);
            minute = mPickerOptions.date.get(Calendar.MINUTE);
            seconds = mPickerOptions.date.get(Calendar.SECOND);
        }
        wheelTime.setPicker(year, month, day, hours, minute, seconds);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            returnData();
        }
        dismiss();
    }

    private void returnData() {
        if (mPickerOptions.timeSelectListener != null) {
            try {
                Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                mPickerOptions.timeSelectListener.onTimeSelect(date, clickView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 动设标题
     *
     * @param text 标题内容
     */
    public void setTitleText(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText(text);
        }
    }

    public boolean isLunarCalendar() {
        return wheelTime.isLunarMode();
    }

    /**
     * 暂仅支持设1900-2100年
     *
     * @param lunar 农历开关
     */
    public void setLunarCalendar(boolean lunar) {
        try {
            int year, month, day, hours, minute, seconds;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(WheelTime.dateFormat.parse(wheelTime.getTime()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
            wheelTime.setLunarMode(lunar);
            wheelTime.setLabels(mPickerOptions.labelYear, mPickerOptions.labelMonth, mPickerOptions.labelDay,
                    mPickerOptions.labelHours, mPickerOptions.labelMinutes, mPickerOptions.labelSeconds);
            wheelTime.setPicker(year, month, day, hours, minute, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDialog() {
        return mPickerOptions.isDialog;
    }
}
