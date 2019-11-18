package application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.zsp.autosize.AutoSizeInitConfigure;
import com.zsp.library.status.manager.StatusManager;
import com.zsp.utilone.activity.ActivitySuperviseManager;
import com.zsp.utilone.timber.configure.TimberInitConfigure;
import com.zsp.widget.BuildConfig;
import com.zsp.widget.R;

/**
 * Created on 2019/7/22.
 *
 * @author 郑少鹏
 * @desc 应用
 */
public class WidgetApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StatusManager.BASE_LOADING_LAYOUT_ID = R.layout.status_loading;
        StatusManager.BASE_EMPTY_LAYOUT_ID = R.layout.status_empty;
        StatusManager.BASE_RETRY_LAYOUT_ID = R.layout.status_retry;
        // 初始化配置
        initConfiguration();
    }

    /**
     * 初始化配置
     */
    private void initConfiguration() {
        // timber
        TimberInitConfigure.initTimber(BuildConfig.DEBUG);
        // AutoSize
        AutoSizeInitConfigure.initAutoSize(this);
        // 全局监听Activity生命周期
        registerActivityListener();
    }

    /**
     * Activity全局监听
     */
    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                // 添监听到创事件Activity至集合
                ActivitySuperviseManager.pushActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                // 移监听到销事件Activity出集合
                ActivitySuperviseManager.removeActivity(activity);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
