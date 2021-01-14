package com.zsp.library.layout.percent.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MarginLayoutParamsCompat;

import com.zsp.library.R;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2019/8/15.
 *
 * @author 郑少鹏
 * @desc PercentLayoutHelper
 */
public class PercentLayoutHelper {
    private final ViewGroup mHost;
    private static int mWidthScreen;
    private static int mHeightScreen;

    public PercentLayoutHelper(ViewGroup host) {
        mHost = host;
        getScreenSize();
    }

    private void getScreenSize() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        mHost.getContext().getDisplay().getMetrics(outMetrics);
        mWidthScreen = outMetrics.widthPixels;
        mHeightScreen = outMetrics.heightPixels;
    }

    /**
     * Helper method to be called from {@link ViewGroup.LayoutParams#setBaseAttributes(TypedArray, int, int)}
     * override that reads layout_width and layout_height attribute values without throwing an exception if they aren't present.
     *
     * @param params     ViewGroup.LayoutParams
     * @param array      TypedArray
     * @param widthAttr  int
     * @param heightAttr int
     */
    public static void fetchWidthAndHeight(@NonNull ViewGroup.LayoutParams params, @NonNull TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    private static PercentLayoutInfo setWidthAndHeightVal(TypedArray array, PercentLayoutInfo percentLayoutInfo) {
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_widthPercent, true);
        if (percentVal != null) {
            percentLayoutInfo = checkForInfoExists(percentLayoutInfo);
            percentLayoutInfo.widthPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_heightPercent, false);
        if (percentVal != null) {
            percentLayoutInfo = checkForInfoExists(percentLayoutInfo);
            percentLayoutInfo.heightPercent = percentVal;
        }
        return percentLayoutInfo;
    }

    private void supportPadding(int widthHint, int heightHint, @NonNull View view, @NonNull PercentLayoutInfo info) {
        int left = view.getPaddingLeft(), right = view.getPaddingRight(), top = view.getPaddingTop(), bottom = view.getPaddingBottom();
        PercentLayoutInfo.PercentVal percentVal = info.paddingLeftPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.baseMode);
            left = (int) (base * percentVal.percent);
        }
        percentVal = info.paddingRightPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.baseMode);
            right = (int) (base * percentVal.percent);
        }
        percentVal = info.paddingTopPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.baseMode);
            top = (int) (base * percentVal.percent);
        }
        percentVal = info.paddingBottomPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.baseMode);
            bottom = (int) (base * percentVal.percent);
        }
        view.setPadding(left, top, right, bottom);
    }

    /**
     * 获PercentVal
     * <p>
     * eg:
     * 35%w => new PercentVal(35, true)
     *
     * @param percentStr String
     * @param isOnWidth  boolean
     * @return PercentLayoutInfo.PercentVal
     */
    private static PercentLayoutInfo.PercentVal getPercentVal(String percentStr, boolean isOnWidth) {
        // valid param
        if (percentStr == null) {
            return null;
        }
        Pattern p = Pattern.compile(REGEX_PERCENT);
        Matcher matcher = p.matcher(percentStr);
        if (!matcher.matches()) {
            throw new RuntimeException("the value of layout_xxxPercent invalid! ==>" + percentStr);
        }
        // extract the float value
        String floatVal = matcher.group(1);
        float percent = 0;
        if (floatVal != null) {
            percent = Float.parseFloat(floatVal) / 100.0f;
        }
        PercentLayoutInfo.PercentVal percentVal = new PercentLayoutInfo.PercentVal();
        percentVal.percent = percent;
        if (percentStr.endsWith(PercentLayoutInfo.BaseMode.SW)) {
            percentVal.baseMode = PercentLayoutInfo.BaseMode.BASE_SCREEN_WIDTH;
        } else if (percentStr.endsWith(PercentLayoutInfo.BaseMode.SH)) {
            percentVal.baseMode = PercentLayoutInfo.BaseMode.BASE_SCREEN_HEIGHT;
        } else if (percentStr.endsWith(PercentLayoutInfo.BaseMode.PERCENT)) {
            if (isOnWidth) {
                percentVal.baseMode = PercentLayoutInfo.BaseMode.BASE_WIDTH;
            } else {
                percentVal.baseMode = PercentLayoutInfo.BaseMode.BASE_HEIGHT;
            }
        } else if (percentStr.endsWith(PercentLayoutInfo.BaseMode.W)) {
            percentVal.baseMode = PercentLayoutInfo.BaseMode.BASE_WIDTH;
        } else if (percentStr.endsWith(PercentLayoutInfo.BaseMode.H)) {
            percentVal.baseMode = PercentLayoutInfo.BaseMode.BASE_HEIGHT;
        } else {
            throw new IllegalArgumentException("the " + percentStr + " must be endWith [%|w|h|sw|sh]");
        }
        return percentVal;
    }

    private void invokeMethod(String methodName, int widthHint, int heightHint, View view, Class clazz, PercentLayoutInfo.PercentVal percentVal) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (percentVal != null) {
            Method setMaxWidthMethod = clazz.getMethod(methodName, int.class);
            setMaxWidthMethod.setAccessible(true);
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.baseMode);
            setMaxWidthMethod.invoke(view, (int) (base * percentVal.percent));
        }
    }

    private void supportTextSize(int widthHint, int heightHint, View view, @NonNull PercentLayoutInfo info) {
        // textSize percent support
        PercentLayoutInfo.PercentVal textSizePercent = info.textSizePercent;
        if (textSizePercent == null) {
            return;
        }
        int base = getBaseByModeAndVal(widthHint, heightHint, textSizePercent.baseMode);
        float textSize = (int) (base * textSizePercent.percent);
        // Button和EditText是TextView子类
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    @Contract(pure = true)
    private static int getBaseByModeAndVal(int widthHint, int heightHint, @NonNull PercentLayoutInfo.BaseMode baseMode) {
        switch (baseMode) {
            case BASE_HEIGHT:
                return heightHint;
            case BASE_WIDTH:
                return widthHint;
            case BASE_SCREEN_WIDTH:
                return mWidthScreen;
            case BASE_SCREEN_HEIGHT:
                return mHeightScreen;
            default:
                break;
        }
        return 0;
    }

    /**
     * Constructs a PercentLayoutInfo from attributes associated with a View.
     *
     * @param context Context
     * @param attrs   AttributeSet
     * @return PercentLayoutInfo
     */
    public static PercentLayoutInfo getPercentLayoutInfo(@NonNull Context context, AttributeSet attrs) {
        PercentLayoutInfo info;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
        info = setWidthAndHeightVal(array, null);
        info = setMarginRelatedVal(array, info);
        info = setTextSizeSupportVal(array, info);
        info = setMinMaxWidthHeightRelatedVal(array, info);
        info = setPaddingRelatedVal(array, info);
        array.recycle();
        return info;
    }

    /**
     * Iterates over children and changes their width and height to one calculated from percentage values.
     *
     * @param widthMeasureSpec  Width MeasureSpec of the parent ViewGroup.
     * @param heightMeasureSpec Height MeasureSpec of the parent ViewGroup.
     */
    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    supportTextSize(widthHint, heightHint, view, info);
                    supportPadding(widthHint, heightHint, view, info);
                    supportMinOrMaxDimension(widthHint, heightHint, view, info);
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.fillMarginLayoutParams((ViewGroup.MarginLayoutParams) params, widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }
    }

    private static PercentLayoutInfo setTextSizeSupportVal(TypedArray array, PercentLayoutInfo info) {
        // textSizePercent默以高作基准
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_textSizePercent, false);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.textSizePercent = percentVal;
        }
        return info;
    }

    private static PercentLayoutInfo setMinMaxWidthHeightRelatedVal(TypedArray array, PercentLayoutInfo info) {
        // maxWidth
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_maxWidthPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.maxWidthPercent = percentVal;
        }
        // maxHeight
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_maxHeightPercent, false);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.maxHeightPercent = percentVal;
        }
        // minWidth
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_minWidthPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.minWidthPercent = percentVal;
        }
        // minHeight
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_minHeightPercent, false);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.minHeightPercent = percentVal;
        }
        return info;
    }

    private static PercentLayoutInfo setMarginRelatedVal(TypedArray array, PercentLayoutInfo info) {
        // 默margin参考宽
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal;
            info.topMarginPercent = percentVal;
            info.rightMarginPercent = percentVal;
            info.bottomMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginLeftPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginTopPercent, false);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.topMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginRightPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.rightMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginBottomPercent, false);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.bottomMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginStartPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.startMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginEndPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.endMarginPercent = percentVal;
        }
        return info;
    }

    /**
     * 设paddingPercent相关属性
     *
     * @param array TypedArray
     * @param info  PercentLayoutInfo
     * @return PercentLayoutInfo
     */
    private static PercentLayoutInfo setPaddingRelatedVal(TypedArray array, PercentLayoutInfo info) {
        // 默padding以宽作标准
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_paddingPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal;
            info.paddingRightPercent = percentVal;
            info.paddingBottomPercent = percentVal;
            info.paddingTopPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_paddingLeftPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_paddingRightPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingRightPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_paddingTopPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingTopPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_paddingBottomPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingBottomPercent = percentVal;
        }
        return info;
    }

    private static PercentLayoutInfo.PercentVal getPercentVal(@NonNull TypedArray array, int index, boolean baseWidth) {
        String sizeStr = array.getString(index);
        return getPercentVal(sizeStr, baseWidth);
    }

    @NonNull
    private static PercentLayoutInfo checkForInfoExists(PercentLayoutInfo info) {
        info = info != null ? info : new PercentLayoutInfo();
        return info;
    }

    private static final String REGEX_PERCENT = "^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)%([s]?[wh]?)$";

    private void supportMinOrMaxDimension(int widthHint, int heightHint, @NonNull View view, @NonNull PercentLayoutInfo info) {
        try {
            Class clazz = view.getClass();
            invokeMethod("setMaxWidth", widthHint, heightHint, view, clazz, info.maxWidthPercent);
            invokeMethod("setMaxHeight", widthHint, heightHint, view, clazz, info.maxHeightPercent);
            invokeMethod("setMinWidth", widthHint, heightHint, view, clazz, info.minWidthPercent);
            invokeMethod("setMinHeight", widthHint, heightHint, view, clazz, info.minHeightPercent);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Iterates over children and restores their original dimensions that were changed for percentage values.
     * Calling this method only makes sense if you previously called {@link PercentLayoutHelper#adjustChildren(int, int)}.
     */
    public void restoreOriginalParams() {
        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.restoreMarginLayoutParams((ViewGroup.MarginLayoutParams) params);
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }
    }

    /**
     * Iterates over children and checks if any of them would like to get more space than it
     * received through the percentage dimension.
     * <p/>
     * If you are building a layout that supports percentage dimensions you are encouraged to take
     * advantage of this method. The developer should be able to specify that a child should be
     * remeasured by adding normal dimension attribute with {@code wrap_content} value. For example
     * he might specify child's attributes as {@code app:layout_widthPercent="60%p"} and
     * {@code android:layout_width="wrap_content"}. In this case if the child receives too little
     * space, it will be remeasured with width set to {@code WRAP_CONTENT}.
     *
     * @return True if the measure phase needs to be rerun because one of the children would like
     * to receive more space.
     */
    public boolean handleMeasuredStateTooSmall() {
        boolean needsSecondMeasure = false;
        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info =
                        ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
            }
        }
        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(@NonNull View view, PercentLayoutInfo info) {
        int state = view.getMeasuredWidthAndState() & View.MEASURED_STATE_MASK;
        if (info == null || info.widthPercent == null) {
            return false;
        }
        return state == View.MEASURED_STATE_TOO_SMALL && info.widthPercent.percent >= 0 && info.mPreservedParams.width == ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(@NonNull View view, PercentLayoutInfo info) {
        int state = view.getMeasuredHeightAndState() & View.MEASURED_STATE_MASK;
        if (info == null || info.heightPercent == null) {
            return false;
        }
        return state == View.MEASURED_STATE_TOO_SMALL && info.heightPercent.percent >= 0 && info.mPreservedParams.height == ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Container for information about percentage dimensions and margins.
     * It acts as an extension for {@code LayoutParams}.
     */
    public static class PercentLayoutInfo {
        private enum BaseMode {
            BASE_WIDTH, BASE_HEIGHT, BASE_SCREEN_WIDTH, BASE_SCREEN_HEIGHT;
            /**
             * width_parent
             */
            public static final String PERCENT = "%";
            /**
             * width_parent
             */
            public static final String W = "w";
            /**
             * height_parent
             */
            public static final String H = "h";
            /**
             * width_screen
             */
            public static final String SW = "sw";
            /**
             * height_screen
             */
            public static final String SH = "sh";
        }

        @NonNull
        @Override
        public String toString() {
            return "PercentLayoutInfo{" +
                    "widthPercent=" + widthPercent +
                    ", heightPercent=" + heightPercent +
                    ", leftMarginPercent=" + leftMarginPercent +
                    ", topMarginPercent=" + topMarginPercent +
                    ", rightMarginPercent=" + rightMarginPercent +
                    ", bottomMarginPercent=" + bottomMarginPercent +
                    ", startMarginPercent=" + startMarginPercent +
                    ", endMarginPercent=" + endMarginPercent +
                    ", textSizePercent=" + textSizePercent +
                    ", maxWidthPercent=" + maxWidthPercent +
                    ", maxHeightPercent=" + maxHeightPercent +
                    ", minWidthPercent=" + minWidthPercent +
                    ", minHeightPercent=" + minHeightPercent +
                    ", paddingLeftPercent=" + paddingLeftPercent +
                    ", paddingRightPercent=" + paddingRightPercent +
                    ", paddingTopPercent=" + paddingTopPercent +
                    ", paddingBottomPercent=" + paddingBottomPercent +
                    ", mPreservedParams=" + mPreservedParams +
                    '}';
        }

        PercentVal widthPercent;
        PercentVal heightPercent;
        PercentVal leftMarginPercent;
        PercentVal topMarginPercent;
        PercentVal rightMarginPercent;
        PercentVal bottomMarginPercent;
        PercentVal startMarginPercent;
        PercentVal endMarginPercent;
        PercentVal textSizePercent;
        // 1.0.4 those attr for some views' setMax/min Height/Width method
        PercentVal maxWidthPercent;
        PercentVal maxHeightPercent;
        PercentVal minWidthPercent;
        PercentVal minHeightPercent;
        // 1.0.6 add padding support
        PercentVal paddingLeftPercent;
        PercentVal paddingRightPercent;
        PercentVal paddingTopPercent;
        PercentVal paddingBottomPercent;
        // package
        final ViewGroup.MarginLayoutParams mPreservedParams;

        PercentLayoutInfo() {
            mPreservedParams = new ViewGroup.MarginLayoutParams(0, 0);
        }

        /**
         * Fills {@code ViewGroup.LayoutParams} dimensions based on percentage values.
         *
         * @param params     ViewGroup.LayoutParams
         * @param widthHint  int
         * @param heightHint int
         */
        void fillLayoutParams(@NonNull ViewGroup.LayoutParams params, int widthHint, int heightHint) {
            // Preserve the original layout params, so we can restore them after the measure step.
            mPreservedParams.width = params.width;
            mPreservedParams.height = params.height;
            if (widthPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, widthPercent.baseMode);
                params.width = (int) (base * widthPercent.percent);
            }
            if (heightPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, heightPercent.baseMode);
                params.height = (int) (base * heightPercent.percent);
            }
        }

        /**
         * Fills {@code ViewGroup.MarginLayoutParams} dimensions and margins based on percentage values.
         *
         * @param params     ViewGroup.MarginLayoutParams
         * @param widthHint  int
         * @param heightHint int
         */
        void fillMarginLayoutParams(ViewGroup.MarginLayoutParams params, int widthHint, int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);
            // Preserver the original margins, so we can restore them after the measure step.
            mPreservedParams.leftMargin = params.leftMargin;
            mPreservedParams.topMargin = params.topMargin;
            mPreservedParams.rightMargin = params.rightMargin;
            mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(mPreservedParams, MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(params));
            if (leftMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, leftMarginPercent.baseMode);
                params.leftMargin = (int) (base * leftMarginPercent.percent);
            }
            if (topMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, topMarginPercent.baseMode);
                params.topMargin = (int) (base * topMarginPercent.percent);
            }
            if (rightMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, rightMarginPercent.baseMode);
                params.rightMargin = (int) (base * rightMarginPercent.percent);
            }
            if (bottomMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, bottomMarginPercent.baseMode);
                params.bottomMargin = (int) (base * bottomMarginPercent.percent);
            }
            if (startMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, startMarginPercent.baseMode);
                MarginLayoutParamsCompat.setMarginStart(params, (int) (base * startMarginPercent.percent));
            }
            if (endMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, endMarginPercent.baseMode);
                MarginLayoutParamsCompat.setMarginEnd(params, (int) (base * endMarginPercent.percent));
            }
        }

        public static class PercentVal {
            float percent = -1;
            BaseMode baseMode;

            PercentVal() {

            }

            public PercentVal(float percent, BaseMode baseMode) {
                this.percent = percent;
                this.baseMode = baseMode;
            }

            @NonNull
            @Override
            public String toString() {
                return "PercentVal{" +
                        "percent=" + percent +
                        ", baseMode=" + baseMode.name() +
                        '}';
            }
        }

        /**
         * Restores original dimensions and margins after they were changed for percentage based values.
         * Calling this method only makes sense if you previously called {@link PercentLayoutHelper.PercentLayoutInfo#fillMarginLayoutParams}.
         *
         * @param params ViewGroup.MarginLayoutParams
         */
        void restoreMarginLayoutParams(ViewGroup.MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = mPreservedParams.leftMargin;
            params.topMargin = mPreservedParams.topMargin;
            params.rightMargin = mPreservedParams.rightMargin;
            params.bottomMargin = mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params, MarginLayoutParamsCompat.getMarginStart(mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params, MarginLayoutParamsCompat.getMarginEnd(mPreservedParams));
        }

        /**
         * Restores original dimensions after they were changed for percentage based values.
         * Calling this method only makes sense if you previously called
         *
         * @param params ViewGroup.LayoutParams
         */
        void restoreLayoutParams(@NonNull ViewGroup.LayoutParams params) {
            params.width = mPreservedParams.width;
            params.height = mPreservedParams.height;
        }
    }

    /**
     * If a layout wants to support percentage based dimensions and use this helper class, its {@code LayoutParams} subclass must implement this interface.
     * <p>
     * Your {@code LayoutParams} subclass should contain an instance of {@code PercentLayoutInfo} and the implementation of this interface should be a simple accessor.
     */
    public interface PercentLayoutParams {
        /**
         * 获PercentLayoutInfo
         *
         * @return PercentLayoutInfo
         */
        PercentLayoutInfo getPercentLayoutInfo();
    }
}
