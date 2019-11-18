package com.zsp.autosize;

import android.app.Activity;
import android.app.Application;

import java.util.Locale;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.LogUtils;

/**
 * Created on 2019/7/18.
 *
 * @author 郑少鹏
 * @desc AutoSize初始化配置
 */
public class AutoSizeInitConfigure {
    public static void initAutoSize(Application application) {
        // 应用多进程且适配所有进程需初始时调initCompatMultiProcess(Context context)
        AutoSize.initCompatMultiProcess(application);
        // 自定AndroidAutoSize
        AutoSizeConfig.getInstance()
                // 屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                });
    }
}
