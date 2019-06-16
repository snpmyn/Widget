package com.zsp.library.pickerview.view;

import android.graphics.Typeface;
import android.view.View;

import com.zsp.library.R;
import com.zsp.library.pickerview.adapter.ArrayWheelAdapter;
import com.zsp.library.pickerview.listener.OnOptionsSelectChangeListener;
import com.zsp.library.wheelview.listener.OnItemSelectedListener;
import com.zsp.library.wheelview.view.WheelView;

import java.util.List;

/**
 * @decs: WheelOptions
 * @author: 郑少鹏
 * @date: 2018/4/3 17:39
 */
public class WheelOptions<T> {
    private View view;
    private WheelView wvOption1;
    private WheelView wvOption2;
    private WheelView wvOption3;
    private List<T> mOptions1Items;
    private List<List<T>> mOptions2Items;
    private List<List<List<T>>> mOptions3Items;
    /**
     * 默联动
     */
    private boolean linkage = true;
    /**
     * 切换时还原第一项
     */
    private boolean isRestoreItem;
    private OnItemSelectedListener wheelListenerOption2;
    private OnOptionsSelectChangeListener optionsSelectChangeListener;
    /**
     * 文本/分割线色
     */
    private int textColorOut;
    private int textColorCenter;
    private int dividerColor;
    private WheelView.DividerType dividerType;
    /**
     * 条目间距倍数
     */
    private float lineSpacingMultiplier;

    WheelOptions(View view, boolean isRestoreItem) {
        super();
        this.isRestoreItem = isRestoreItem;
        this.view = view;
        // 初始化显示数据
        wvOption1 = view.findViewById(R.id.options1);
        wvOption2 = view.findViewById(R.id.options2);
        wvOption3 = view.findViewById(R.id.options3);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    void setPicker(List<T> options1Items, List<List<T>> options2Items, List<List<List<T>>> options3Items) {
        this.mOptions1Items = options1Items;
        this.mOptions2Items = options2Items;
        this.mOptions3Items = options3Items;
        // 选项1显示数据
        wvOption1.setAdapter(new ArrayWheelAdapter(mOptions1Items));
        // 初始化显示数据
        wvOption1.setCurrentItem(0);
        // 选项2
        if (mOptions2Items != null) {
            // 显示数据
            wvOption2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(0)));
        }
        // 初始化显示数据
        wvOption2.setCurrentItem(wvOption2.getCurrentItem());
        // 选项3
        if (mOptions3Items != null) {
            // 显示数据
            wvOption3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(0).get(0)));
        }
        wvOption3.setCurrentItem(wvOption3.getCurrentItem());
        wvOption1.setIsOptions(true);
        wvOption2.setIsOptions(true);
        wvOption3.setIsOptions(true);
        if (this.mOptions2Items == null) {
            wvOption2.setVisibility(View.GONE);
        } else {
            wvOption2.setVisibility(View.VISIBLE);
        }
        if (this.mOptions3Items == null) {
            wvOption3.setVisibility(View.GONE);
        } else {
            wvOption3.setVisibility(View.VISIBLE);
        }
        // 联动监听器
        // 仅1级联动数据
        // 上一opt2选中位
        // 新opt2位，旧位没超数据范围则沿用旧位，否选最后一项
        // 仅2级联动数据，滑动第1项回调
        OnItemSelectedListener wheelListenerOption1 = index -> {
            int opt2Select = 0;
            if (mOptions2Items == null) {
                // 仅1级联动数据
                if (optionsSelectChangeListener != null) {
                    optionsSelectChangeListener.onOptionsSelectChanged(wvOption1.getCurrentItem(), 0, 0);
                }
            } else {
                if (!isRestoreItem) {
                    // 上一opt2选中位
                    opt2Select = wvOption2.getCurrentItem();
                    // 新opt2位，旧位没超数据范围则沿用旧位，否选最后一项
                    opt2Select = opt2Select >= mOptions2Items.get(index).size() - 1 ? mOptions2Items.get(index).size() - 1 : opt2Select;
                }
                wvOption2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(index)));
                wvOption2.setCurrentItem(opt2Select);
                if (mOptions3Items != null) {
                    wheelListenerOption2.onItemSelected(opt2Select);
                } else {
                    // 仅2级联动数据，滑动第1项回调
                    if (optionsSelectChangeListener != null) {
                        optionsSelectChangeListener.onOptionsSelectChanged(index, opt2Select, 0);
                    }
                }
            }
        };
        wheelListenerOption2 = index -> {
            if (mOptions3Items != null) {
                int opt1Select = wvOption1.getCurrentItem();
                opt1Select = opt1Select >= mOptions3Items.size() - 1 ? mOptions3Items.size() - 1 : opt1Select;
                index = index >= mOptions2Items.get(opt1Select).size() - 1 ? mOptions2Items.get(opt1Select).size() - 1 : index;
                int opt3 = 0;
                if (!isRestoreItem) {
                    // wv_option3.getCurrentItem()上一opt3选中位
                    // 新opt3位，旧位没超数据范围则用旧位，否选最后一项
                    opt3 = wvOption3.getCurrentItem() >= mOptions3Items.get(opt1Select).get(index).size() - 1 ?
                            mOptions3Items.get(opt1Select).get(index).size() - 1 : wvOption3.getCurrentItem();
                }
                wvOption3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(wvOption1.getCurrentItem()).get(index)));
                wvOption3.setCurrentItem(opt3);
                // 3级联动数据实时回调
                if (optionsSelectChangeListener != null) {
                    optionsSelectChangeListener.onOptionsSelectChanged(wvOption1.getCurrentItem(), index, opt3);
                }
            } else {
                // 仅2级联动数据，滑动第2项回调
                if (optionsSelectChangeListener != null) {
                    optionsSelectChangeListener.onOptionsSelectChanged(wvOption1.getCurrentItem(), index, 0);
                }
            }
        };
        // 添联动监听
        if (options1Items != null && linkage) {
            wvOption1.setOnItemSelectedListener(wheelListenerOption1);
        }
        if (options2Items != null && linkage) {
            wvOption2.setOnItemSelectedListener(wheelListenerOption2);
        }
        if (options3Items != null && linkage && optionsSelectChangeListener != null) {
            wvOption3.setOnItemSelectedListener(index -> optionsSelectChangeListener.onOptionsSelectChanged(wvOption1.getCurrentItem(), wvOption2.getCurrentItem(), index));
        }
    }

    /**
     * 不联动
     *
     * @param options1Items options1Items
     * @param options2Items options2Items
     * @param options3Items options3Items
     */
    void setNnPicker(List<T> options1Items, List<T> options2Items, List<T> options3Items) {
        // 选项1显示数据
        wvOption1.setAdapter(new ArrayWheelAdapter(options1Items));
        // 初始化显示数据
        wvOption1.setCurrentItem(0);
        // 选项2
        if (options2Items != null) {
            // 显示数据
            wvOption2.setAdapter(new ArrayWheelAdapter(options2Items));
        }
        // 初始化显示数据
        wvOption2.setCurrentItem(wvOption2.getCurrentItem());
        // 选项3
        if (options3Items != null) {
            // 显示数据
            wvOption3.setAdapter(new ArrayWheelAdapter(options3Items));
        }
        wvOption3.setCurrentItem(wvOption3.getCurrentItem());
        wvOption1.setIsOptions(true);
        wvOption2.setIsOptions(true);
        wvOption3.setIsOptions(true);
        if (optionsSelectChangeListener != null) {
            wvOption1.setOnItemSelectedListener(index -> optionsSelectChangeListener.onOptionsSelectChanged(index, wvOption2.getCurrentItem(), wvOption3.getCurrentItem()));
        }
        if (options2Items == null) {
            wvOption2.setVisibility(View.GONE);
        } else {
            wvOption2.setVisibility(View.VISIBLE);
            if (optionsSelectChangeListener != null) {
                wvOption2.setOnItemSelectedListener(index -> optionsSelectChangeListener.onOptionsSelectChanged(wvOption1.getCurrentItem(), index, wvOption3.getCurrentItem()));
            }
        }
        if (options3Items == null) {
            wvOption3.setVisibility(View.GONE);
        } else {
            wvOption3.setVisibility(View.VISIBLE);
            if (optionsSelectChangeListener != null) {
                wvOption3.setOnItemSelectedListener(index -> optionsSelectChangeListener.onOptionsSelectChanged(wvOption1.getCurrentItem(), wvOption2.getCurrentItem(), index));
            }
        }
    }

    void setTextContentSize(int textSize) {
        wvOption1.setTextSize(textSize);
        wvOption2.setTextSize(textSize);
        wvOption3.setTextSize(textSize);
    }

    private void setTextColorOut() {
        wvOption1.setTextColorOut(textColorOut);
        wvOption2.setTextColorOut(textColorOut);
        wvOption3.setTextColorOut(textColorOut);
    }

    private void setTextColorCenter() {
        wvOption1.setTextColorCenter(textColorCenter);
        wvOption2.setTextColorCenter(textColorCenter);
        wvOption3.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
        wvOption1.setDividerColor(dividerColor);
        wvOption2.setDividerColor(dividerColor);
        wvOption3.setDividerColor(dividerColor);
    }

    private void setDividerType() {
        wvOption1.setDividerType(dividerType);
        wvOption2.setDividerType(dividerType);
        wvOption3.setDividerType(dividerType);
    }

    private void setLineSpacingMultiplier() {
        wvOption1.setLineSpacingMultiplier(lineSpacingMultiplier);
        wvOption2.setLineSpacingMultiplier(lineSpacingMultiplier);
        wvOption3.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    /**
     * 选项单位
     *
     * @param label1 单位
     * @param label2 单位
     * @param label3 单位
     */
    void setLabels(String label1, String label2, String label3) {
        if (label1 != null) {
            wvOption1.setLabel(label1);
        }
        if (label2 != null) {
            wvOption2.setLabel(label2);
        }
        if (label3 != null) {
            wvOption3.setLabel(label3);
        }
    }

    /**
     * x轴偏移量
     */
    void setxOffsetOfText(int xOffsetOne, int xOffsetTwo, int xOffsetThree) {
        wvOption1.setxOffsetOfText(xOffsetOne);
        wvOption2.setxOffsetOfText(xOffsetTwo);
        wvOption3.setxOffsetOfText(xOffsetThree);
    }

    /**
     * 循环滚动
     *
     * @param cyclic 循环
     */
    public void setCyclic(boolean cyclic) {
        wvOption1.setCyclic(cyclic);
        wvOption2.setCyclic(cyclic);
        wvOption3.setCyclic(cyclic);
    }

    /**
     * 字体样式
     *
     * @param font 系统提供样式
     */
    void setTypeface(Typeface font) {
        wvOption1.setTypeface(font);
        wvOption2.setTypeface(font);
        wvOption3.setTypeface(font);
    }

    /**
     * 分设一二三级循环滚动
     *
     * @param cyclic1,cyclic2,cyclic3 循环
     */
    void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
        wvOption1.setCyclic(cyclic1);
        wvOption2.setCyclic(cyclic2);
        wvOption3.setCyclic(cyclic3);
    }

    /**
     * 返当前选中结果对应位置数组
     * 因支持三级联动，分三个级别索引 0，1，2
     * 快滑未停点确定，进行判断，匹配数据越界设0，防index出错致崩溃
     *
     * @return 索引数组
     */
    int[] getCurrentItems() {
        int[] currentItems = new int[3];
        currentItems[0] = wvOption1.getCurrentItem();
        if (mOptions2Items != null && mOptions2Items.size() > 0) {
            // 非空判断
            currentItems[1] = wvOption2.getCurrentItem() > (mOptions2Items.get(currentItems[0]).size() - 1) ? 0 : wvOption2.getCurrentItem();
        } else {
            currentItems[1] = wvOption2.getCurrentItem();
        }
        if (mOptions3Items != null && mOptions3Items.size() > 0) {
            // 非空判断
            currentItems[2] = wvOption3.getCurrentItem() > (mOptions3Items.get(currentItems[0]).get(currentItems[1]).size() - 1) ? 0 : wvOption3.getCurrentItem();
        } else {
            currentItems[2] = wvOption3.getCurrentItem();
        }
        return currentItems;
    }

    void setCurrentItems(int option1, int option2, int option3) {
        if (linkage) {
            itemSelected(option1, option2, option3);
        } else {
            wvOption1.setCurrentItem(option1);
            wvOption2.setCurrentItem(option2);
            wvOption3.setCurrentItem(option3);
        }
    }

    private void itemSelected(int opt1Select, int opt2Select, int opt3Select) {
        if (mOptions1Items != null) {
            wvOption1.setCurrentItem(opt1Select);
        }
        if (mOptions2Items != null) {
            wvOption2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(opt1Select)));
            wvOption2.setCurrentItem(opt2Select);
        }
        if (mOptions3Items != null) {
            wvOption3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(opt1Select).get(opt2Select)));
            wvOption3.setCurrentItem(opt3Select);
        }
    }

    /**
     * 间距倍数（1.2-4.0f）
     *
     * @param lineSpacingMultiplier 间距倍数
     */
    void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    /**
     * 分割线色
     *
     * @param dividerColor 分割线色
     */
    void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    /**
     * 分割线类型
     *
     * @param dividerType 分割线类型
     */
    void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    /**
     * 分割线间文本色
     *
     * @param textColorCenter 分割线间文本色
     */
    void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**
     * 分割线外文本色
     *
     * @param textColorOut 分割线外文本色
     */
    void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    /**
     * 仅显中间选中项Label
     *
     * @param isCenterLabel 仅显中间选中项Label否
     */
    void isCenterLabel(boolean isCenterLabel) {
        wvOption1.isCenterLabel(isCenterLabel);
        wvOption2.isCenterLabel(isCenterLabel);
        wvOption3.isCenterLabel(isCenterLabel);
    }

    void setOptionsSelectChangeListener(OnOptionsSelectChangeListener optionsSelectChangeListener) {
        this.optionsSelectChangeListener = optionsSelectChangeListener;
    }

    void setLinkage(boolean linkage) {
        this.linkage = linkage;
    }
}
