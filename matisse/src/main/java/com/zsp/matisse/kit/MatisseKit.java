package com.zsp.matisse.kit;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import androidx.fragment.app.Fragment;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zsp.matisse.R;
import com.zsp.matisse.engine.Glide4Engine;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/**
 * Created on 2018/12/6.
 *
 * @author 郑少鹏
 * @desc MatisseKit
 */
public class MatisseKit {
    /**
     * 请求码
     */
    public static final int REQUEST_CODE = 0x001;

    /**
     * 活动中Matisse准备
     *
     * @param activity  活动
     * @param maxCount  最大数
     * @param authority 权威（如${applicationId}.fileprovider）
     */
    public static void matisseOperateInActivity(Activity activity, int maxCount, String authority) {
        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        Activity activityUse = weakReference.get();
        Matisse.from(activityUse)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, authority))
                .maxSelectable(maxCount)
                .gridExpectedSize(activityUse.getResources().getDimensionPixelSize(R.dimen.dp_130))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                // for glide-V3
                // .imageEngine(new GlideEngine())
                // for glide-V4
                .imageEngine(new Glide4Engine())
                .setOnSelectedListener((uriList, pathList) -> {
                    // DO SOMETHING IMMEDIATELY HERE
                    Timber.d("onSelected: pathList = %s", pathList);
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    // DO SOMETHING IMMEDIATELY HERE
                    Timber.d("onCheck: isChecked = %s", isChecked);
                })
                .forResult(REQUEST_CODE);
    }

    /**
     * 碎片中Matisse准备
     *
     * @param fragment  碎片
     * @param maxCount  最大数
     * @param authority 权威（如${applicationId}.fileprovider）
     */
    public static void matisseOperateInFragment(Fragment fragment, int maxCount, String authority) {
        Matisse.from(fragment)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, authority))
                .maxSelectable(maxCount)
                .gridExpectedSize(fragment.getResources().getDimensionPixelSize(R.dimen.dp_130))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                // for glide-V3
                // .imageEngine(new GlideEngine())
                // for glide-V4
                .imageEngine(new Glide4Engine())
                .setOnSelectedListener((uriList, pathList) -> {
                    // DO SOMETHING IMMEDIATELY HERE
                    Timber.d("onSelected: pathList = %s", pathList);
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    // DO SOMETHING IMMEDIATELY HERE
                    Timber.d("onCheck: isChecked = %s", isChecked);
                })
                .forResult(REQUEST_CODE);
    }
}
