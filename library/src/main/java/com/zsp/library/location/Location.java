package com.zsp.library.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import java.util.List;

import timber.log.Timber;
import value.WidgetLibraryMagic;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created on 2018/6/4.
 *
 * @author 郑少鹏
 * @desc 定位
 */
public class Location {
    private static Context context;
    private LocationManager locationManager;
    private android.location.Location location;
    /**
     * 定位监听
     */
    private LocationListener locationListener = new LocationListener() {
        /**
         * provider于可用、暂时不可用和无服务三状态直切触发此函数
         * @param provider 提供者
         * @param status 状态
         * @param arg2 provider可选包
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Timber.d("onStatusChanged: %s", "当前GPS可见");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Timber.d("onStatusChanged: %s", "当前GPS服务区外");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Timber.d("onStatusChanged: %s", "当前GPS暂停服务");
                    break;
                default:
                    break;
            }
        }

        /**
         * provider被enable触发此函数（如开GPS）
         * @param provider provider
         */
        @Override
        public void onProviderEnabled(String provider) {
            // 需查权限（否编译错）
            // 抽成方法仍错（只能如下重复）
            if (Build.VERSION.SDK_INT >= WidgetLibraryMagic.INT_TWENTY_THREE &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
            setLocation(location);
        }

        /**
         * provider被disable触发此函数（如关GPS）
         * @param provider provider
         */
        @Override
        public void onProviderDisabled(String provider) {
            setLocation(null);
        }

        /**
         * 坐标变触发此函数（Provider传同坐标不触发）
         * @param location 坐标
         */
        @Override
        public void onLocationChanged(android.location.Location location) {
            // 精确度
            location.getAccuracy();
            setLocation(location);
            Timber.d("onLocationChanged 时间：%s", location.getTime());
            Timber.d("onLocationChanged 经度：%s", location.getLongitude());
            Timber.d("onLocationChanged 纬度：%s", location.getLatitude());
            Timber.d("onLocationChanged 海拔：%s", location.getAltitude());
        }
    };

    public Location(Context context) {
        Location.context = context.getApplicationContext();
        getLocation();
    }

    /**
     * 定位可用否
     *
     * @return 是true 否false
     */
    private static boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (lm != null) {
            return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            return false;
        }
    }

    /**
     * GPS可用否
     *
     * @return 是true 否false
     */
    static boolean isGpsEnabled() {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (lm != null) {
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            return false;
        }
    }

    /**
     * 网络可用否
     *
     * @return 是true 否false
     */
    static boolean isNetWorkEnabled() {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (lm != null) {
            return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } else {
            return false;
        }
    }

    /**
     * 设置页
     */
    static void openGpsSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void setLocation(android.location.Location location) {
        this.location = location;
        if (location != null) {
            String address = "经度：" + location.getLongitude() + " " + "纬度：" + location.getLatitude();
            Timber.d("坐标：%s", address);
        }
    }

    /**
     * 坐标
     *
     * @return 坐标
     */
    android.location.Location showLocation() {
        return location;
    }

    private void getLocation() {
        // 位置管理器
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        // 位置提供器（GPS或NetWork）
        List<String> providers = locationManager.getProviders(true);
        String locationProvider;
        // 下if-else语句确定先GPS定位
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            // GPS提供器
            locationProvider = LocationManager.GPS_PROVIDER;
            Timber.d("位置提供器：%s", "gps");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            // 网络提供器
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Timber.d("位置提供器：%s", "network");
        } else {
            Timber.d("位置提供器：%s", "无可用位置提供器");
            return;
        }
        // 需查权限（否编译错）
        // 抽成方法仍错，只能如下重复
        if (Build.VERSION.SDK_INT >= WidgetLibraryMagic.INT_TWENTY_THREE &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 获上次位（通头次运行null）
        android.location.Location location = locationManager.getLastKnownLocation(locationProvider);
        setLocation(location);
        // 监视地理位变（二/三参为更新最短时minTime/最短距minDistance）
        locationManager.requestLocationUpdates(locationProvider, 3000, 0, locationListener);
    }

    /**
     * 移定位监听
     */
    public void removeLocationUpdatesListener() {
        if (locationManager != null) {
            if (locationListener != null) {
                locationManager.removeUpdates(locationListener);
            }
            locationManager = null;
        }
    }
}
