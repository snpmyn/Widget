package com.zsp.library.pickerview.adapter;

import com.zsp.library.wheelview.adapter.WheelAdapter;

/**
 * @decs: NumericWheelAdapter
 * @author: 郑少鹏
 * @date: 2018/4/3 16:57
 */
public class NumericWheelAdapter implements WheelAdapter {
    private final int minValue;
    private final int maxValue;

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            return minValue + index;
        }
        return 0;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    @Override
    public int indexOf(Object o) {
        try {
            return (int) o - minValue;
        } catch (Exception e) {
            return -1;
        }
    }
}
