package com.zsp.scancode.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
 * @decs: 扫二维码页
 * @author: 郑少鹏
 * @date: 2019/11/7 10:56
 */
public class ScanQrCodeActivity extends AppCompatActivity implements View.OnClickListener, QRCodeView.Delegate {
    /**
     * 控件
     */
    private MaterialToolbar scanQrCodeActivityMt;
    private ZXingView scanQrCodeActivityZxv;
    private ImageButton scanQrCodeActivityIbFlash;
    private ImageButton scanQrCodeActivityIbPhotoLibrary;
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
        setContentView(R.layout.activity_scan_qr_code);
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
        scanQrCodeActivityMt = findViewById(R.id.scanQrCodeActivityMt);
        scanQrCodeActivityZxv = findViewById(R.id.scanQrCodeActivityZxv);
        scanQrCodeActivityIbFlash = findViewById(R.id.scanQrCodeActivityIbFlash);
        scanQrCodeActivityIbPhotoLibrary = findViewById(R.id.scanQrCodeActivityIbPhotoLibrary);
    }

    /**
     * 初始化配置
     */
    protected void initConfiguration() {
        // MaterialToolbar
        setSupportActionBar(scanQrCodeActivityMt);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // ZXingView
        scanQrCodeActivityZxv.setDelegate(this);
    }

    /**
     * 添监听事件
     */
    private void setListener() {
        // MaterialToolbar
        scanQrCodeActivityMt.setNavigationOnClickListener(view -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        // ImageButton
        scanQrCodeActivityIbFlash.setOnClickListener(this);
        scanQrCodeActivityIbPhotoLibrary.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // 闪光灯
        if (view.getId() == R.id.scanQrCodeActivityIbFlash) {
            if (flashOn) {
                scanQrCodeActivityZxv.closeFlashlight();
                scanQrCodeActivityIbFlash.setImageResource(R.drawable.ic_flash_off_24dp_blue);
            } else {
                scanQrCodeActivityZxv.openFlashlight();
                scanQrCodeActivityIbFlash.setImageResource(R.drawable.ic_flash_on_24dp_blue);
            }
            flashOn = !flashOn;
        }
        // 图库
        if (view.getId() == R.id.scanQrCodeActivityIbPhotoLibrary) {
            MatisseKit.matisseOperateInActivity(this, 1, getString(R.string.fileProviderAuthorities));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开后置摄像头预览（未识别）
        scanQrCodeActivityZxv.startCamera();
        // 显扫描框并识别
        scanQrCodeActivityZxv.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        // 关摄像头预览并隐扫描框
        scanQrCodeActivityZxv.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 毁二维码扫描控件
        scanQrCodeActivityZxv.onDestroy();
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
                    scanQrCodeActivityZxv.decodeQRCode(list.get(0));
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
        String tipText = scanQrCodeActivityZxv.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                scanQrCodeActivityZxv.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                scanQrCodeActivityZxv.getScanBoxView().setTipText(tipText);
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
