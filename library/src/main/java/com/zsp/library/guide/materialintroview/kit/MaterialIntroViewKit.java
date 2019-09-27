package com.zsp.library.guide.materialintroview.kit;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.zsp.library.guide.materialintroview.animation.MaterialIntroListener;
import com.zsp.library.guide.materialintroview.prefs.PreferencesManager;
import com.zsp.library.guide.materialintroview.shape.Focus;
import com.zsp.library.guide.materialintroview.shape.FocusGravity;
import com.zsp.library.guide.materialintroview.shape.ShapeType;
import com.zsp.library.guide.materialintroview.view.MaterialIntroView;

/**
 * Created on 2019/9/24.
 *
 * @author 郑少鹏
 * @desc MaterialIntroViewKit
 */
public class MaterialIntroViewKit {
    /**
     * Show MaterialIntroView.
     *
     * @param activity              活动
     * @param view                  视图
     * @param materialIntroViewId   MaterialIntroView ID
     * @param infoText              信息文本
     * @param textColor             文本色
     * @param focusGravity          聚焦位
     * @param focus                 聚焦
     * @param shapeType             形状类型
     * @param isPerformClick        点操作
     * @param isDotAnimationEnabled 点动画
     * @param materialIntroListener MaterialIntroListener
     */
    public static void showMaterialIntroView(Activity activity,
                                             View view,
                                             String materialIntroViewId,
                                             String infoText,
                                             int textColor,
                                             FocusGravity focusGravity,
                                             Focus focus,
                                             ShapeType shapeType,
                                             boolean isPerformClick,
                                             boolean isDotAnimationEnabled,
                                             MaterialIntroListener materialIntroListener) {
        new MaterialIntroView.Builder(activity)
                // Trigger click operation when user click focused area.
                .performClick(isPerformClick)
                // Shows dot animation center of focus area.
                // Default false.
                .enableDotAnimation(isDotAnimationEnabled)
                // You can focus on left of RecyclerView list item.
                .setFocusGravity(focusGravity)
                // If your button's width has MATCH_PARENT.
                // Focus.ALL is not a good option.
                // You can use Focus.MINIMUM or Focus.NORMAL.
                .setFocusType(focus)
                // Starts after 3 seconds passed.
                .setDelayMillis(200)
                // Setting text will enable info dialog.
                .setInfoText(infoText)
                // Info dialog's text color is set to black
                .setTextColor(textColor)
                // Focus view.
                .setTarget(view)
                // Change shape of focus area.
                .setShape(shapeType)
                // Store intro view status whether it is learnt or not.
                .setUsageId(materialIntroViewId)
                // If you don't want to perform click automatically, you can disable perform click and handle it yourself.
                .setListener(materialIntroListener)
                .show();
    }

    /**
     * 重置
     *
     * @param context 上下文
     */
    public static void reset(Context context, String id) {
        new PreferencesManager(context).reset(id);
    }

    /**
     * 重置全部
     *
     * @param context 上下文
     */
    public static void resetAll(Context context) {
        new PreferencesManager(context).resetAll();
    }
}
