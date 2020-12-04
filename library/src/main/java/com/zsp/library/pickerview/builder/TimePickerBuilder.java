package com.zsp.library.pickerview.builder;

import android.content.Context;
import android.view.ViewGroup;

import com.zsp.library.pickerview.configure.PickerOptions;
import com.zsp.library.pickerview.listener.CustomListener;
import com.zsp.library.pickerview.listener.OnTimeSelectChangeListener;
import com.zsp.library.pickerview.listener.OnTimeSelectListener;
import com.zsp.library.pickerview.view.TimePickerView;
import com.zsp.library.wheelview.view.WheelView;

import java.util.Calendar;

/**
 * @decs: TimePickerBuilder
 * @author: 郑少鹏
 * @date: 2018/4/3 17:01
 */
public class TimePickerBuilder {
    /**
     * 配置类
     */
    private final PickerOptions mPickerOptions;

    /**
     * Required
     *
     * @param context  上下文
     * @param listener 监听
     */
    public TimePickerBuilder(Context context, OnTimeSelectListener listener) {
        mPickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_TIME);
        mPickerOptions.context = context;
        mPickerOptions.timeSelectListener = listener;
    }

    /**
     * Option
     *
     * @param gravity 位
     * @return timePickerBuilder
     */
    public TimePickerBuilder setGravity(int gravity) {
        mPickerOptions.textGravity = gravity;
        return this;
    }

    /**
     * new boolean[]{true, true, true, false, false, false}
     * control the "year","month","day","hours","minutes","seconds" display or hide
     * 分控“年”“月”“日”“时”“分”“秒”显隐
     *
     * @param type 布尔型数组（长需设6）
     * @return TimePickerBuilder
     */
    public TimePickerBuilder setType(boolean[] type) {
        mPickerOptions.type = type;
        return this;
    }

    public TimePickerBuilder setSubmitText(String textContentConfirm) {
        mPickerOptions.textContentConfirm = textContentConfirm;
        return this;
    }

    public TimePickerBuilder isDialog(boolean isDialog) {
        mPickerOptions.isDialog = isDialog;
        return this;
    }

    public TimePickerBuilder setCancelText(String textContentCancel) {
        mPickerOptions.textContentCancel = textContentCancel;
        return this;
    }

    public TimePickerBuilder setTitleText(String textContentTitle) {
        mPickerOptions.textContentTitle = textContentTitle;
        return this;
    }

    public TimePickerBuilder setSubmitColor(int textColorConfirm) {
        mPickerOptions.textColorConfirm = textColorConfirm;
        return this;
    }

    public TimePickerBuilder setCancelColor(int textColorCancel) {
        mPickerOptions.textColorCancel = textColorCancel;
        return this;
    }

    /**
     * ViewGroup类型容器
     *
     * @param decorView 选择器被添到此容器
     * @return TimePickerBuilder
     */
    public TimePickerBuilder setDecorView(ViewGroup decorView) {
        mPickerOptions.decorView = decorView;
        return this;
    }

    public TimePickerBuilder setBgColor(int bgColorWheel) {
        mPickerOptions.bgColorWheel = bgColorWheel;
        return this;
    }

    public TimePickerBuilder setTitleBgColor(int bgColorTitle) {
        mPickerOptions.bgColorTitle = bgColorTitle;
        return this;
    }

    public TimePickerBuilder setTitleColor(int textColorTitle) {
        mPickerOptions.textColorTitle = textColorTitle;
        return this;
    }

    public TimePickerBuilder setSubCalSize(int textSizeSubmitCancel) {
        mPickerOptions.textSizeSubmitCancel = textSizeSubmitCancel;
        return this;
    }

    public TimePickerBuilder setTitleSize(int textSizeTitle) {
        mPickerOptions.textSizeTitle = textSizeTitle;
        return this;
    }

    public TimePickerBuilder setContentTextSize(int textSizeContent) {
        mPickerOptions.textSizeContent = textSizeContent;
        return this;
    }

    /**
     * 系统Calendar月0-11
     * 故调Calendar之set设时月范围亦0-11
     *
     * @param date 日期
     * @return TimePickerBuilder
     */
    public TimePickerBuilder setDate(Calendar date) {
        mPickerOptions.date = date;
        return this;
    }

    public TimePickerBuilder setLayoutRes(int res, CustomListener customListener) {
        mPickerOptions.layoutRes = res;
        mPickerOptions.customListener = customListener;
        return this;
    }

    /**
     * 起始时
     * 系统Calendar月0-11
     * 故调Calendar之set设时月范围亦0-11
     *
     * @param startDate 开始日期
     * @param endDate   终止日期
     * @return timePickerBuilder
     */
    public TimePickerBuilder setRangDate(Calendar startDate, Calendar endDate) {
        mPickerOptions.startDate = startDate;
        mPickerOptions.endDate = endDate;
        return this;
    }

    /**
     * 间距倍数（1.0-4.0f）
     *
     * @param lineSpacingMultiplier 间距倍数
     */
    public TimePickerBuilder setLineSpacingMultiplier(float lineSpacingMultiplier) {
        mPickerOptions.lineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    /**
     * 分割线色
     *
     * @param dividerColor 分割线色
     */
    public TimePickerBuilder setDividerColor(int dividerColor) {
        mPickerOptions.dividerColor = dividerColor;
        return this;
    }

    /**
     * 分割线类型
     *
     * @param dividerType 分割线类型
     */
    public TimePickerBuilder setDividerType(WheelView.DividerType dividerType) {
        mPickerOptions.dividerType = dividerType;
        return this;
    }

    /**
     * 显外背景色（默灰）
     *
     * @param backgroundId 显外背景色
     */
    public TimePickerBuilder setBackgroundId(int backgroundId) {
        mPickerOptions.backgroundId = backgroundId;
        return this;
    }

    /**
     * 分割线间文本色
     *
     * @param textColorCenter 分割线间文本色
     */
    public TimePickerBuilder setTextColorCenter(int textColorCenter) {
        mPickerOptions.textColorCenter = textColorCenter;
        return this;
    }

    /**
     * 分割线外文本色
     *
     * @param textColorOut 分割线外文本色
     */
    public TimePickerBuilder setTextColorOut(int textColorOut) {
        mPickerOptions.textColorOut = textColorOut;
        return this;
    }

    public TimePickerBuilder isCyclic(boolean cyclic) {
        mPickerOptions.cyclic = cyclic;
        return this;
    }

    public TimePickerBuilder setOutSideCancelable(boolean cancelable) {
        mPickerOptions.cancelable = cancelable;
        return this;
    }

    public TimePickerBuilder setLunarCalendar(boolean lunarCalendar) {
        mPickerOptions.isLunarCalendar = lunarCalendar;
        return this;
    }

    public TimePickerBuilder setLabel(String labelYear, String labelMonth, String labelDay, String labelHours, String labelMins, String labelSeconds) {
        mPickerOptions.labelYear = labelYear;
        mPickerOptions.labelMonth = labelMonth;
        mPickerOptions.labelDay = labelDay;
        mPickerOptions.labelHours = labelHours;
        mPickerOptions.labelMinutes = labelMins;
        mPickerOptions.labelSeconds = labelSeconds;
        return this;
    }

    /**
     * X轴倾斜角度[ -90 , 90°]
     *
     * @param xOffsetYear    年
     * @param xOffsetMonth   月
     * @param xOffsetDay     日
     * @param xOffsetHours   时
     * @param xOffsetMinutes 分
     * @param xOffsetSeconds 秒
     * @return timePickerBuilder
     */
    public TimePickerBuilder setxOffsetOfText(int xOffsetYear, int xOffsetMonth, int xOffsetDay, int xOffsetHours, int xOffsetMinutes, int xOffsetSeconds) {
        mPickerOptions.xOffsetYear = xOffsetYear;
        mPickerOptions.xOffsetMonth = xOffsetMonth;
        mPickerOptions.xOffsetDay = xOffsetDay;
        mPickerOptions.xOffsetHours = xOffsetHours;
        mPickerOptions.xOffsetMinutes = xOffsetMinutes;
        mPickerOptions.xOffsetSeconds = xOffsetSeconds;
        return this;
    }

    public TimePickerBuilder isCenterLabel(boolean isCenterLabel) {
        mPickerOptions.isCenterLabel = isCenterLabel;
        return this;
    }

    /**
     * @param listener 切item项滚动停止时实时回调监听
     * @return timePickerBuilder
     */
    public TimePickerBuilder setTimeSelectChangeListener(OnTimeSelectChangeListener listener) {
        mPickerOptions.timeSelectChangeListener = listener;
        return this;
    }

    public TimePickerView build() {
        return new TimePickerView(mPickerOptions);
    }
}
