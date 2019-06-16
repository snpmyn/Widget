package com.zsp.library.pickerview.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.zsp.library.R;
import com.zsp.library.pickerview.configure.PickerOptions;

import java.util.List;

/**
 * @decs: 条件选择器
 * @author: 郑少鹏
 * @date: 2018/4/3 17:34
 */
public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "bill_cancel";
    private WheelOptions wheelOptions;

    public OptionsPickerView(PickerOptions pickerOptions) {
        super(pickerOptions.context);
        mPickerOptions = pickerOptions;
        initView(pickerOptions.context);
    }

    private void initView(Context context) {
        setDialogOutSideCancelable();
        initViews();
        initAnim();
        initEvents();
        if (mPickerOptions.customListener == null) {
            LayoutInflater.from(context).inflate(mPickerOptions.layoutRes, contentContainer);
            // 顶标
            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            RelativeLayout rlTop = (RelativeLayout) findViewById(R.id.rlTop);
            // 确定/取消按钮
            MaterialButton btnSubmit = (MaterialButton) findViewById(R.id.btnSubmit);
            MaterialButton btnCancel = (MaterialButton) findViewById(R.id.btnCancel);
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
            rlTop.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
            // 文本大小
            btnSubmit.setTextSize(mPickerOptions.textSizeSubmitCancel);
            btnCancel.setTextSize(mPickerOptions.textSizeSubmitCancel);
            tvTitle.setTextSize(mPickerOptions.textSizeTitle);
        } else {
            mPickerOptions.customListener.customLayout(LayoutInflater.from(context).inflate(mPickerOptions.layoutRes, contentContainer));
        }
        // 滚轮布局
        final LinearLayout optionsPicker = (LinearLayout) findViewById(R.id.optionsPicker);
        optionsPicker.setBackgroundColor(mPickerOptions.bgColorWheel);
        wheelOptions = new WheelOptions(optionsPicker, mPickerOptions.isRestoreItem);
        if (mPickerOptions.optionsSelectChangeListener != null) {
            wheelOptions.setOptionsSelectChangeListener(mPickerOptions.optionsSelectChangeListener);
        }
        wheelOptions.setTextContentSize(mPickerOptions.textSizeContent);
        wheelOptions.setLabels(mPickerOptions.label1, mPickerOptions.label2, mPickerOptions.label3);
        wheelOptions.setxOffsetOfText(mPickerOptions.xOffsetOne, mPickerOptions.xOffsetTwo, mPickerOptions.xOffsetThree);
        wheelOptions.setCyclic(mPickerOptions.cyclic1, mPickerOptions.cyclic2, mPickerOptions.cyclic3);
        wheelOptions.setTypeface(mPickerOptions.font);
        setOutSideCancelable(mPickerOptions.cancelable);
        // 自定
        wheelOptions.setDividerColor(ContextCompat.getColor(context, R.color.graySelect));
        wheelOptions.setDividerType(mPickerOptions.dividerType);
        wheelOptions.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        // 自定
        wheelOptions.setTextColorOut(ContextCompat.getColor(context, R.color.fontHint));
        // 自定
        wheelOptions.setTextColorCenter(ContextCompat.getColor(context, R.color.fontInput));
        wheelOptions.isCenterLabel(mPickerOptions.isCenterLabel);
    }

    /**
     * 动设标题
     *
     * @param text 标题
     */
    public void setTitleText(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText(text);
        }
    }

    /**
     * 默选中项
     *
     * @param option1 默选中项
     */
    public void setSelectOptions(int option1) {
        mPickerOptions.option1 = option1;
        reSetCurrentItems();
    }

    public void setSelectOptions(int option1, int option2) {
        mPickerOptions.option1 = option1;
        mPickerOptions.option2 = option2;
        reSetCurrentItems();
    }

    public void setSelectOptions(int option1, int option2, int option3) {
        mPickerOptions.option1 = option1;
        mPickerOptions.option2 = option2;
        mPickerOptions.option3 = option3;
        reSetCurrentItems();
    }

    private void reSetCurrentItems() {
        if (wheelOptions != null) {
            wheelOptions.setCurrentItems(mPickerOptions.option1, mPickerOptions.option2, mPickerOptions.option3);
        }
    }

    public void setPicker(List<T> optionsItems) {
        this.setPicker(optionsItems, null, null);
    }

    public void setPicker(List<T> options1Items, List<List<T>> options2Items) {
        this.setPicker(options1Items, options2Items, null);
    }

    private void setPicker(List<T> options1Items, List<List<T>> options2Items, List<List<List<T>>> options3Items) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items);
        reSetCurrentItems();
    }

    /**
     * 不联动调
     *
     * @param options1Items options1Items
     * @param options2Items options2Items
     * @param options3Items options3Items
     */
    public void setNnPicker(List<T> options1Items, List<T> options2Items, List<T> options3Items) {
        wheelOptions.setLinkage(false);
        wheelOptions.setNnPicker(options1Items, options2Items, options3Items);
        reSetCurrentItems();
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            returnData();
        }
        dismiss();
    }

    /**
     * 抽离接口回调方法
     */
    private void returnData() {
        if (mPickerOptions.optionsSelectListener != null) {
            int[] optionsCurrentItems = wheelOptions.getCurrentItems();
            mPickerOptions.optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2], clickView);
        }
    }

    @Override
    public boolean isDialog() {
        return mPickerOptions.isDialog;
    }
}
