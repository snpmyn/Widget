package com.zsp.library.pickerview.configure;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;

import com.zsp.library.R;
import com.zsp.library.pickerview.listener.CustomListener;
import com.zsp.library.pickerview.listener.OnOptionsSelectChangeListener;
import com.zsp.library.pickerview.listener.OnOptionsSelectListener;
import com.zsp.library.pickerview.listener.OnTimeSelectChangeListener;
import com.zsp.library.pickerview.listener.OnTimeSelectListener;
import com.zsp.library.wheelview.view.WheelView;

import java.util.Calendar;

/**
 * @decs: 配置类
 * @author: 郑少鹏
 * @date: 2018/4/3 17:05
 */
public class PickerOptions {
    public static final int TYPE_PICKER_OPTIONS = 1;
    public static final int TYPE_PICKER_TIME = 2;
    /**
     * 常量
     */
    private static final int PICKER_VIEW_BTN_COLOR_NORMAL = 0xFF057dff;
    private static final int PICKER_VIEW_BG_COLOR_TITLE = 0xFFf5f5f5;
    private static final int PICKER_VIEW_COLOR_TITLE = 0xFF000000;
    private static final int PICKER_VIEW_BG_COLOR_DEFAULT = 0xFFFFFFFF;
    public OnOptionsSelectListener optionsSelectListener;
    public OnTimeSelectListener timeSelectListener;
    public OnTimeSelectChangeListener timeSelectChangeListener;
    public OnOptionsSelectChangeListener optionsSelectChangeListener;
    public CustomListener customListener;
    /**
     * 单位字符
     */
    public String label1, label2, label3;
    /**
     * 默选中项
     */
    public int option1, option2, option3;
    /**
     * x轴偏移量
     */
    public int xOffsetOne, xOffsetTwo, xOffsetThree;
    /**
     * 循环（默否）
     */
    public boolean cyclic1 = false;
    public boolean cyclic2 = false;
    public boolean cyclic3 = false;
    /**
     * 切换时还原第一项
     */
    public boolean isRestoreItem = false;
    /**
     * 显类型（默显年、月、日）
     */
    public boolean[] type = new boolean[]{true, true, true, false, false, false};
    /**
     * 当前选中时
     */
    public Calendar date;
    /**
     * 开始时
     */
    public Calendar startDate;
    /**
     * 终止时
     */
    public Calendar endDate;
    /**
     * 开始年份
     */
    public int startYear;
    /**
     * 结尾年份
     */
    public int endYear;
    /**
     * 循环
     */
    public boolean cyclic = false;
    /**
     * 显农历
     */
    public boolean isLunarCalendar = false;
    /**
     * 单位
     */
    public String labelYear, labelMonth, labelDay, labelHours, labelMinutes, labelSeconds;
    /**
     * 单位
     */
    public int xOffsetYear, xOffsetMonth, xOffsetDay, xOffsetHours, xOffsetMinutes, xOffsetSeconds;
    /**
     * 公有字段
     */
    public int layoutRes;
    public ViewGroup decorView;
    public int textGravity = Gravity.CENTER;
    public Context context;
    /**
     * 确定文本
     */
    public String textContentConfirm;
    /**
     * 取消文本
     */
    public String textContentCancel;
    /**
     * 标题文本
     */
    public String textContentTitle;
    /**
     * 确定色
     */
    public int textColorConfirm = PICKER_VIEW_BTN_COLOR_NORMAL;
    /**
     * 取消色
     */
    public int textColorCancel = PICKER_VIEW_BTN_COLOR_NORMAL;
    /**
     * 标题色
     */
    public int textColorTitle = PICKER_VIEW_COLOR_TITLE;
    /**
     * 滚轮背景色
     */
    public int bgColorWheel = PICKER_VIEW_BG_COLOR_DEFAULT;
    /**
     * 标题背景色
     */
    public int bgColorTitle = PICKER_VIEW_BG_COLOR_TITLE;
    /**
     * 确定/取消大小
     * 自定
     */
    public int textSizeSubmitCancel = 15;
    /**
     * 标题文本大小
     * 自定
     */
    public int textSizeTitle = 15;
    /**
     * 内容文本大小
     * 自定
     */
    public int textSizeContent = 15;
    /**
     * 分割线外文本色
     */
    public int textColorOut = 0xFFa8a8a8;
    /**
     * 分割线间文本色
     */
    public int textColorCenter = 0xFF2a2a2a;
    /**
     * 分割线色
     */
    public int dividerColor = Color.BLUE;
    /**
     * 显示时外部背景色（默灰）
     */
    public int backgroundId = -1;
    /**
     * 条目间距倍数（默1.6）
     */
    public float lineSpacingMultiplier = 2.6f;
    /**
     * 对话框模式
     */
    public boolean isDialog;
    /**
     * 可取消
     */
    public boolean cancelable = true;
    /**
     * 仅显中间Label（默每item都显）
     */
    public boolean isCenterLabel = false;
    /**
     * 字体样式
     */
    public Typeface font = Typeface.DEFAULT;
    /**
     * 分隔线类型
     */
    public WheelView.DividerType dividerType = WheelView.DividerType.WRAP;

    public PickerOptions(int buildType) {
        if (buildType == TYPE_PICKER_OPTIONS) {
            layoutRes = R.layout.pickerview_options;
        } else {
            layoutRes = R.layout.pickerview_time;
        }
    }
}
