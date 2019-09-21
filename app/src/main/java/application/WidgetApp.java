package application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.zsp.library.status.manager.StatusManager;
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
        // timber
        TimberInitConfigure.initTimber(BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
