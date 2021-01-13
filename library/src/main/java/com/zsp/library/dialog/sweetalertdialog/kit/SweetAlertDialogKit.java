package com.zsp.library.dialog.sweetalertdialog.kit;

import android.content.Context;

import androidx.annotation.NonNull;

import com.zsp.library.dialog.sweetalertdialog.SweetAlertDialog;
import com.zsp.library.dialog.sweetalertdialog.listener.SweetAlertDialogValueListener;

/**
 * Created on 2018/12/4.
 *
 * @author 郑少鹏
 * @desc SweetAlertDialogKit
 */
public class SweetAlertDialogKit {
    /**
     * 创建
     *
     * @param context                       上下文
     * @param type                          NORMAL_TYPE 0 ERROR_TYPE = 1 SUCCESS_TYPE = 2 WARNING_TYPE = 3 CUSTOM_IMAGE_TYPE = 4 PROGRESS_TYPE = 5
     * @param title                         标题
     * @param cancel                        取消否
     * @param sweetAlertDialogValueListener SweetAlertDialog值监听
     * @return widget.dialog
     */
    @NonNull
    public static SweetAlertDialog dialogCreate(Context context, int type, String title, boolean cancel, SweetAlertDialogValueListener sweetAlertDialogValueListener) {
        SweetAlertDialog hintDialog = new SweetAlertDialog(context, type).setTitleText(title);
        hintDialog.setCancelable(cancel);
        if (sweetAlertDialogValueListener != null) {
            hintDialog.setListener(sweetAlertDialogValueListener);
        }
        return hintDialog;
    }

    /**
     * 创建（点）
     *
     * @param context     上下文
     * @param type        NORMAL_TYPE 0 ERROR_TYPE = 1 SUCCESS_TYPE = 2 WARNING_TYPE = 3 CUSTOM_IMAGE_TYPE = 4 PROGRESS_TYPE = 5
     * @param title       标题
     * @param content     内容
     * @param confirmText 确定提示
     * @param cancelText  取消提示
     * @param cancel      取消否
     * @return widget.dialog
     */
    public static SweetAlertDialog dialogWithClickCreate(Context context, int type, String title, String content, String confirmText, String cancelText, boolean cancel) {
        return new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText(confirmText)
                .setCancelText(cancelText)
                .showCancelButton(cancel);
    }

    /**
     * 创建（自定图）
     *
     * @param context                       上下文
     * @param title                         标题
     * @param content                       内容
     * @param confirmText                   确定提示
     * @param customRes                     自定资源
     * @param cancel                        取消否
     * @param sweetAlertDialogValueListener SweetAlertDialog值监听
     * @return widget.dialog
     */
    public static SweetAlertDialog dialogCustomImageCreate(Context context, String title, String content, String confirmText, int customRes, boolean cancel, SweetAlertDialogValueListener sweetAlertDialogValueListener) {
        SweetAlertDialog hintDialog = new SweetAlertDialog(context, 4)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText(confirmText)
                .setCustomImage(customRes)
                .showCancelButton(cancel);
        if (sweetAlertDialogValueListener != null) {
            hintDialog.setListener(sweetAlertDialogValueListener);
        }
        return hintDialog;
    }

    /**
     * 变化
     *
     * @param baseHintDialog 基础提示框
     * @param title          标题
     * @param buttonHint     按钮提示
     * @param type           NORMAL_TYPE 0 ERROR_TYPE = 1 SUCCESS_TYPE = 2 WARNING_TYPE = 3 CUSTOM_IMAGE_TYPE = 4 PROGRESS_TYPE = 5
     */
    public static void dialogChange(SweetAlertDialog baseHintDialog, String title, String buttonHint, int type) {
        if (baseHintDialog != null) {
            if (buttonHint != null) {
                baseHintDialog.setTitleText(title).setConfirmText(buttonHint).changeAlertType(type);
            } else {
                baseHintDialog.setTitleText(title).changeAlertType(type);
            }
            baseHintDialog.show();
        }
    }

    /**
     * 销毁
     *
     * @param baseHintDialog 基础提示框
     */
    public static void dialogDestroy(SweetAlertDialog baseHintDialog) {
        if (baseHintDialog != null && baseHintDialog.isShowing()) {
            baseHintDialog.dismiss();
        }
    }
}
