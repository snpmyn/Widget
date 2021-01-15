package com.zsp.autosize;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.Locale;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;

/**
 * Created on 2019/7/18.
 *
 * @author 郑少鹏
 * @desc AutoSize 初始化配置
 */
public class AutoSizeInitConfigure {
    public static void initAutoSize(Application application) {
        // 应用多进程且适配所有进程需初始时调 initCompatMultiProcess(Context context)
        AutoSize.initCompatMultiProcess(application);
        // 开启支持 Fragment 自定参数功能
        AutoSizeConfig.getInstance().setCustomFragment(true);
        // 自定 AndroidAutoSize
        AutoSizeConfig.getInstance()
                // 屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        Log.d(this.getClass().getSimpleName(), String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        Log.d(this.getClass().getSimpleName(), String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                });
    }
}
