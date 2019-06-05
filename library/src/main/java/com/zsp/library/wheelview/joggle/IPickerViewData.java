package com.zsp.library.wheelview.joggle;

/**
 * Created on 2018/4/3.
 *
 * @author 郑少鹏
 * @desc IPickerViewData
 */
public interface IPickerViewData {
    /**
     * 实现IPickerViewData接口
     * 用于显PickerView上字符串
     * PickerView通IPickerViewData获getPickerViewText并显
     *
     * @return String
     */
    String getPickerViewText();
}