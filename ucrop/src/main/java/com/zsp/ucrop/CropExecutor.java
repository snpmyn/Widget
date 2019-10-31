package com.zsp.ucrop;

import android.content.Context;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created on 2019/3/19.
 *
 * @author 郑少鹏
 * @desc 裁剪执行者
 */
public class CropExecutor {
    /**
     * 活动中裁剪
     *
     * @param appCompatActivity 上下文
     * @param sourcePath        需裁剪图路径
     * @param destinationPath   目标路径
     * @param maxWidth          最大宽
     * @param maxHeight         最大高
     */
    public static void uCropInActivity(AppCompatActivity appCompatActivity, String sourcePath, String destinationPath, int maxWidth, int maxHeight) {
        File outDir = new File(destinationPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".png");
        Uri destinationUri = Uri.fromFile(outFile);
        UCrop.of(Uri.fromFile(new File(sourcePath)), destinationUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(maxWidth, maxHeight)
                .withOptions(options(appCompatActivity))
                .start(appCompatActivity);
    }

    /**
     * 碎片中裁剪
     *
     * @param context         上下文
     * @param fragment        碎片
     * @param sourcePath      需裁剪图路径
     * @param destinationPath 目标路径
     * @param maxWidth        最大宽
     * @param maxHeight       最大高
     */
    public static void uCropInFragment(Context context, Fragment fragment, String sourcePath, String destinationPath, int maxWidth, int maxHeight) {
        File outDir = new File(destinationPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".png");
        Uri destinationUri = Uri.fromFile(outFile);
        UCrop.of(Uri.fromFile(new File(sourcePath)), destinationUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(maxWidth, maxHeight)
                .withOptions(options(context))
                .start(context, fragment);
    }

    /**
     * 设置
     * <p>
     * 裁剪图操作手势
     * options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
     * 隐底部容器否（默显）
     * options.setHideBottomControls(false);
     * 调裁剪框否
     * options.setFreeStyleCropEnabled(true);
     * 显网格否
     * options.setShowCropGrid(true);
     * 网格色
     * options.setCropGridColor(ContextCompat.getColor(context, R.color.colorPrimary));
     *
     * @param context 上下文
     * @return UCrop.Options
     */
    private static UCrop.Options options(Context context) {
        // 初始UCrop配置
        UCrop.Options options = new UCrop.Options();
        // Toolbar色
        options.setToolbarColor(ActivityCompat.getColor(context, R.color.colorPrimary));
        // 状态栏色
        options.setStatusBarColor(ActivityCompat.getColor(context, R.color.colorPrimary));
        return options;
    }
}
