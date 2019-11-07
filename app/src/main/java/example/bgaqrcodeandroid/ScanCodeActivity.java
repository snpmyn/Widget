package example.bgaqrcodeandroid;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.scancode.activity.ScanBarCodeActivity;
import com.zsp.scancode.listener.ScanCodeListener;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 扫码页
 * @author: 郑少鹏
 * @date: 2019/11/6 16:26
 */
public class ScanCodeActivity extends AppCompatActivity implements ScanCodeListener {
    /**
     * SoulPermissionUtils
     */
    private SoulPermissionUtils soulPermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        // 初始化配置
        initConfiguration();
    }

    /**
     * 初始化配置
     */
    private void initConfiguration() {
        soulPermissionUtils = new SoulPermissionUtils();
    }

    @OnClick({R.id.scanCodeActivityMbScanQrCode, R.id.scanCodeActivityMbScanBarCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 扫二维码
            case R.id.scanCodeActivityMbScanQrCode:
                scanQrCode();
                break;
            // 扫条形码
            case R.id.scanCodeActivityMbScanBarCode:
                break;
            default:
                break;
        }
    }

    /**
     * 扫二维码
     */
    private void scanQrCode() {
        soulPermissionUtils.checkAndRequestPermission(this, Manifest.permission.CAMERA, soulPermissionUtils,
                false, new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {
                        ScanBarCodeActivity.setScanCodeListener(ScanCodeActivity.this);
                        Intent intent = new Intent(ScanCodeActivity.this, ScanBarCodeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleInMiUi(String s) {
                        ToastUtils.shortShow(ScanCodeActivity.this, s);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleWithoutLoopHint(String s) {
                        ToastUtils.shortShow(ScanCodeActivity.this, s);
                    }
                });
    }

    /**
     * 扫码成功
     *
     * @param result 摄像头扫码回调该法时result定有值，解本地图或Bitmap时result或null。
     */
    @Override
    public void onScanQrCodeSuccess(String result) {
        ToastUtils.shortShow(this, result);
    }

    /**
     * 扫码开相机错
     */
    @Override
    public void onScanQrCodeOpenCameraError() {
        ToastUtils.shortShow(this, getString(R.string.errorOpeningTheCamera));
    }
}
