package com.zsp.scancode.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.zhihu.matisse.Matisse;
import com.zsp.matisse.kit.MatisseKit;
import com.zsp.scancode.R;
import com.zsp.scancode.listener.ScanCodeListener;
import com.zsp.utilone.vibrator.VibratorUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * @decs: 扫条形码页
 * @author: 郑少鹏
 * @date: 2019/11/6 15:22
 */
public class ScanBarCodeActivity extends AppCompatActivity implements View.OnClickListener, QRCodeView.Delegate {
    /**
     * 控件
     */
    private MaterialToolbar scanBarCodeActivityMt;
    private ZXingView scanBarCodeActivityZxv;
    private ImageButton scanBarCodeActivityIbFlash;
    private ImageButton scanBarCodeActivityIbPhotoLibrary;
    /**
     * 闪光灯开
     */
    private boolean flashOn;
    /**
     * 扫码监听
     */
    public static ScanCodeListener scanCodeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code);
        // 初始化控件
        stepUi();
        // 初始化配置
        initConfiguration();
        // 添监听事件
        setListener();
    }

    /**
     * 初始化控件
     */
    private void stepUi() {
        scanBarCodeActivityMt = findViewById(R.id.scanBarCodeActivityMt);
        scanBarCodeActivityZxv = findViewById(R.id.scanBarCodeActivityZxv);
        scanBarCodeActivityIbFlash = findViewById(R.id.scanBarCodeActivityIbFlash);
        scanBarCodeActivityIbPhotoLibrary = findViewById(R.id.scanBarCodeActivityIbPhotoLibrary);
    }

    /**
     * 初始化配置
     */
    protected void initConfiguration() {
        // MaterialToolbar
        setSupportActionBar(scanBarCodeActivityMt);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // ZXingView
        scanBarCodeActivityZxv.setDelegate(this);
    }

    /**
     * 添监听事件
     */
    private void setListener() {
        // MaterialToolbar
        scanBarCodeActivityMt.setNavigationOnClickListener(view -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        // ImageButton
        scanBarCodeActivityIbFlash.setOnClickListener(this);
        scanBarCodeActivityIbPhotoLibrary.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        // 闪光灯
        if (view.getId() == R.id.scanBarCodeActivityIbFlash) {
            if (flashOn) {
                scanBarCodeActivityZxv.closeFlashlight();
                scanBarCodeActivityIbFlash.setImageResource(R.drawable.ic_flash_off_24dp_blue);
            } else {
                scanBarCodeActivityZxv.openFlashlight();
                scanBarCodeActivityIbFlash.setImageResource(R.drawable.ic_flash_on_24dp_blue);
            }
            flashOn = !flashOn;
        }
        // 图库
        if (view.getId() == R.id.scanBarCodeActivityIbPhotoLibrary) {
            MatisseKit.matisseOperateInActivity(this, 1, getString(R.string.fileProviderAuthorities), false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开后置摄像头预览（未识别）
        scanBarCodeActivityZxv.startCamera();
        // 显扫描框并识别
        scanBarCodeActivityZxv.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        // 关摄像头预览并隐扫描框
        scanBarCodeActivityZxv.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 毁二维码扫描控件
        scanBarCodeActivityZxv.onDestroy();
        // 扫码监听置空避内存泄漏
        scanCodeListener = null;
        super.onDestroy();
    }

    /**
     * 设扫码监听
     *
     * @param scanCodeListener 扫码监听
     */
    public static void setScanCodeListener(ScanCodeListener scanCodeListener) {
        ScanBarCodeActivity.scanCodeListener = scanCodeListener;
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MatisseKit.REQUEST_CODE) {
                if (data != null) {
                    List<String> list = new ArrayList<>(Matisse.obtainPathResult(data));
                    scanBarCodeActivityZxv.decodeQRCode(list.get(0));
                }
            }
        }
    }

    /**
     * 扫码成功
     *
     * @param result 摄像头扫码时回调该法则result定有值，解本地图或Bitmap时result或null。
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        VibratorUtils.oneShotVibration(this, 200, -1);
        scanCodeListener.onScanQrCodeSuccess(result);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 摄像头环境亮度发生变化
     *
     * @param isDark 暗否
     */
    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String tipText = scanBarCodeActivityZxv.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                scanBarCodeActivityZxv.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                scanBarCodeActivityZxv.getScanBoxView().setTipText(tipText);
            }
        }
    }

    /**
     * 扫码开相机错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        scanCodeListener.onScanQrCodeOpenCameraError();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
