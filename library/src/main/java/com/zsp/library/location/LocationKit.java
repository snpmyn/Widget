package com.zsp.library.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Looper;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zsp.library.R;
import com.zsp.utilone.thread.ThreadManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created on 2019/4/24.
 *
 * @author 郑少鹏
 * @desc LocationKit
 */
public class LocationKit {
    private final WeakReference<Context> weakReference;
    public android.location.Location location;
    public double longitude;
    public double latitude;

    public LocationKit(Context context) {
        this.weakReference = new WeakReference<>(context);
    }

    /**
     * 模式检测
     */
    public void modeCheck() {
        if (!Location.isGpsEnabled() && !Location.isNetWorkEnabled()) {
            // 无定位
            new MaterialAlertDialogBuilder(weakReference.get())
                    .setMessage(R.string.locationHintContent)
                    .setPositiveButton(R.string.ok, (dialog, which) -> Location.openGpsSettings())
                    .show();
        } else if (!Location.isGpsEnabled() && Location.isNetWorkEnabled()) {
            // 网络定位
            new MaterialAlertDialogBuilder(weakReference.get())
                    .setMessage(R.string.gpsHintContent)
                    .setNegativeButton(R.string.goToSet, (dialog, which) -> Location.openGpsSettings())
                    .setPositiveButton(R.string.tryFirst, (dialog, which) -> startLocation())
                    .show();
        } else if (Location.isGpsEnabled() && !Location.isNetWorkEnabled()) {
            // GPS定位
            new MaterialAlertDialogBuilder(weakReference.get())
                    .setMessage(R.string.netWorkHintContent)
                    .setNegativeButton(R.string.goToSet, (dialog, which) -> Location.openGpsSettings())
                    .setPositiveButton(R.string.tryFirst, (dialog, which) -> startLocation())
                    .show();
        } else {
            startLocation();
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        ScheduledExecutorService scheduledExecutorService = ThreadManager.stepScheduledExecutorService();
        scheduledExecutorService.execute(() -> {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            Location locationUse = new Location(weakReference.get());
            location = locationUse.showLocation();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Looper.loop();
        });
    }

    /**
     * 地址
     *
     * @return Address
     */
    public Address address() {
        if (location != null) {
            Geocoder geocoder = new Geocoder(weakReference.get(), Locale.getDefault());
            List<Address> result = null;
            try {
                result = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result != null && result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
