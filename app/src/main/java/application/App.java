package application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created on 2019/7/22.
 *
 * @author 郑少鹏
 * @desc 应用
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
