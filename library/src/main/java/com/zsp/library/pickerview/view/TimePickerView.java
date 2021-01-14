package com.zsp.library.pickerview.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.zsp.library.R;
import com.zsp.library.pickerview.configure.PickerOptions;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import value.WidgetLibraryMagic;

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

    public TimePickerView(@NonNull PickerOptions pickerOptions) {
        super(pickerOptions.context);
        this.pickerOptions = pickerOptions;
        initView(pickerOptions.context);
    }

    private void initView(Context context) {
        setDialogOutSideCancelable();
        initViews();
        initAnim();
        if (pickerOptions.customListener == null) {
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
            btnSubmit.setText(TextUtils.isEmpty(pickerOptions.textContentConfirm) ? context.getResources().getString(R.string.ensure) : pickerOptions.textContentConfirm);
            btnCancel.setText(TextUtils.isEmpty(pickerOptions.textContentCancel) ? context.getResources().getString(R.string.cancel) : pickerOptions.textContentCancel);
            // 默空
            tvTitle.setText(TextUtils.isEmpty(pickerOptions.textContentTitle) ? "" : pickerOptions.textContentTitle);
            // color
            // 自定
            btnSubmit.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            // 自定
            btnCancel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            // 自定
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.fontInput));
            // 自定
            rvTopBar.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            // 文字大小
            btnSubmit.setTextSize(pickerOptions.textSizeSubmitCancel);
            btnCancel.setTextSize(pickerOptions.textSizeSubmitCancel);
            tvTitle.setTextSize(pickerOptions.textSizeTitle);
        } else {
            pickerOptions.customListener.customLayout(LayoutInflater.from(context).inflate(pickerOptions.layoutRes, contentContainer));
        }
        // 时间转轮（自定义）
        LinearLayout timePicker = (LinearLayout) findViewById(R.id.timePicker);
        timePicker.setBackgroundColor(pickerOptions.bgColorWheel);
        initWheelTime(context, timePicker);
    }

    private void initWheelTime(Context context, LinearLayout timePickerView) {
        wheelTime = new WheelTime(timePickerView, pickerOptions.type, pickerOptions.textGravity, pickerOptions.textSizeContent);
        if (pickerOptions.timeSelectChangeListener != null) {
            wheelTime.setSelectChangeCallback(() -> {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    pickerOptions.timeSelectChangeListener.onTimeSelectChanged(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        wheelTime.setLunarMode(pickerOptions.isLunarCalendar);
        if (pickerOptions.startYear != 0 && pickerOptions.endYear != 0 && pickerOptions.startYear <= pickerOptions.endYear) {
            setRange();
        }
        // 手设时间范围限制
        if (pickerOptions.startDate != null && pickerOptions.endDate != null) {
            if (pickerOptions.startDate.getTimeInMillis() > pickerOptions.endDate.getTimeInMillis()) {
                throw new IllegalArgumentException("startDate can't be later than endDate");
            } else {
                setRangDate();
            }
        } else if (pickerOptions.startDate != null) {
            if (pickerOptions.startDate.get(Calendar.YEAR) < WidgetLibraryMagic.INT_ONE_THOUSAND_NINE_HUNDRED) {
                throw new IllegalArgumentException("The startDate can not as early as 1900");
            } else {
                setRangDate();
            }
        } else if (pickerOptions.endDate != null) {
            if (pickerOptions.endDate.get(Calendar.YEAR) > WidgetLibraryMagic.INT_TWO_THOUSAND) {
                throw new IllegalArgumentException("The endDate should not be later than 2100");
            } else {
                setRangDate();
            }
        } else {
            // 没设时间范围限制则用默范围
            setRangDate();
        }
        setTime();
        wheelTime.setLabels(pickerOptions.labelYear, pickerOptions.labelMonth, pickerOptions.labelDay,
                pickerOptions.labelHours, pickerOptions.labelMinutes, pickerOptions.labelSeconds);
        wheelTime.setxOffsetOfText(pickerOptions.xOffsetYear, pickerOptions.xOffsetMonth, pickerOptions.xOffsetDay,
                pickerOptions.xOffsetHours, pickerOptions.xOffsetMinutes, pickerOptions.xOffsetSeconds);
        setOutSideCancelable(pickerOptions.cancelable);
        wheelTime.setCyclic(pickerOptions.cyclic);
        // 自定
        wheelTime.setDividerColor(ContextCompat.getColor(context, R.color.gray));
        wheelTime.setDividerType(pickerOptions.dividerType);
        wheelTime.setLineSpacingMultiplier(pickerOptions.lineSpacingMultiplier);
        // 自定
        wheelTime.setTextColorOut(ContextCompat.getColor(context, R.color.fontHint));
        // 自定
        wheelTime.setTextColorCenter(ContextCompat.getColor(context, R.color.fontInput));
        wheelTime.isCenterLabel(pickerOptions.isCenterLabel);
    }

    /**
     * 默时
     */
    public void setDate(Calendar date) {
        pickerOptions.date = date;
        setTime();
    }

    /**
     * 可选时间范围（setTime前调有效）
     */
    private void setRange() {
        wheelTime.setStartYear(pickerOptions.startYear);
        wheelTime.setEndYear(pickerOptions.endYear);

    }

    /**
     * 可选择时间范围（setTime前调有效）
     */
    private void setRangDate() {
        wheelTime.setRangDate(pickerOptions.startDate, pickerOptions.endDate);
        initDefaultSelectedDate();
    }

    private void initDefaultSelectedDate() {
        // 手设时间范围
        if (pickerOptions.startDate != null && pickerOptions.endDate != null) {
            // 默时未设或所设默时越界，则设默选时为开始时
            if (pickerOptions.date == null || pickerOptions.date.getTimeInMillis() < pickerOptions.startDate.getTimeInMillis()
                    || pickerOptions.date.getTimeInMillis() > pickerOptions.endDate.getTimeInMillis()) {
                pickerOptions.date = pickerOptions.startDate;
            }
        } else if (pickerOptions.startDate != null) {
            // 没设默选时则以开始时为默时
            pickerOptions.date = pickerOptions.startDate;
        } else if (pickerOptions.endDate != null) {
            pickerOptions.date = pickerOptions.endDate;
        }
    }

    /**
     * 选中时（默选当前时）
     */
    private void setTime() {
        int year, month, day, hours, minute, seconds;
        Calendar calendar = Calendar.getInstance();
        if (pickerOptions.date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        } else {
            year = pickerOptions.date.get(Calendar.YEAR);
            month = pickerOptions.date.get(Calendar.MONTH);
            day = pickerOptions.date.get(Calendar.DAY_OF_MONTH);
            hours = pickerOptions.date.get(Calendar.HOUR_OF_DAY);
            minute = pickerOptions.date.get(Calendar.MINUTE);
            seconds = pickerOptions.date.get(Calendar.SECOND);
        }
        wheelTime.setPicker(year, month, day, hours, minute, seconds);
    }

    @Override
    public void onClick(@NonNull View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            returnData();
        }
        dismiss();
    }

    private void returnData() {
        if (pickerOptions.timeSelectListener != null) {
            try {
                Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                pickerOptions.timeSelectListener.onTimeSelect(date, clickView);
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
            Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
            if (date != null) {
                calendar.setTime(date);
            }
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
            wheelTime.setLunarMode(lunar);
            wheelTime.setLabels(pickerOptions.labelYear, pickerOptions.labelMonth, pickerOptions.labelDay,
                    pickerOptions.labelHours, pickerOptions.labelMinutes, pickerOptions.labelSeconds);
            wheelTime.setPicker(year, month, day, hours, minute, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDialog() {
        return pickerOptions.isDialog;
    }
}
