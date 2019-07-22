package example.sms;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.sms.kit.SmsKit;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 短信页
 * @author: 郑少鹏
 * @date: 2019/7/22 17:09
 */
public class SmsActivity extends AppCompatActivity {
    /**
     * SoulPermissionUtils
     */
    private SoulPermissionUtils soulPermissionUtils;
    /**
     * SmsKit
     */
    private SmsKit smsKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
        initConfiguration();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smsKit.unregisterReceiver();
    }

    private void initConfiguration() {
        // SoulPermissionUtils
        soulPermissionUtils = new SoulPermissionUtils();
        // SmsKit
        smsKit = new SmsKit(this);
    }

    private void setListener() {
        // 设SmsKit发送监听
        smsKit.setSmsKitSendListener(new SmsKit.SmsKitSendListener() {
            @Override
            public void resultOk() {
                ToastUtils.shortShow(SmsActivity.this, getString(R.string.smsSendSuccess));
            }

            @Override
            public void resultErrorCenericFailure() {
                ToastUtils.shortShow(SmsActivity.this, getString(R.string.smsSendFail));
            }
        });
        // 设SmsKit传送监听
        smsKit.setSmsKitDeliverListener(new SmsKit.SmsKitDeliverListener() {
            @Override
            public void resultOk() {
                ToastUtils.shortShow(SmsActivity.this, getString(R.string.smsDeliverSuccess));
            }

            @Override
            public void resultErrorCenericFailure() {
                ToastUtils.shortShow(SmsActivity.this, getString(R.string.smsDeliverFail));
            }
        });
    }

    @OnClick({R.id.smsActivitySingleShot, R.id.smsActivityMass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 单发
            case R.id.smsActivitySingleShot:
                singleShot();
                break;
            // 群发
            case R.id.smsActivityMass:
                mass();
                break;
            default:
                break;
        }
    }

    /**
     * 单发
     */
    private void singleShot() {
        soulPermissionUtils.checkAndRequestPermission(this, Manifest.permission.SEND_SMS, soulPermissionUtils,
                true, new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {
                        smsKit.singleShot("13673541527", "单发测试");
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleInMiUi(String s) {
                        ToastUtils.shortShow(SmsActivity.this, s);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleWithoutLoopHint(String s) {

                    }
                });
    }

    /**
     * 群发
     */
    private void mass() {
        soulPermissionUtils.checkAndRequestPermission(this, Manifest.permission.SEND_SMS, soulPermissionUtils,
                true, new SoulPermissionUtils.CheckAndRequestPermissionCallBack() {
                    @Override
                    public void onPermissionOk() {
                        List<String> stringList = new ArrayList<>();
                        stringList.add("13673541527");
                        stringList.add("15686216273");
                        smsKit.mass(stringList, "群发测试");
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleInMiUi(String s) {
                        ToastUtils.shortShow(SmsActivity.this, s);
                    }

                    @Override
                    public void onPermissionDeniedNotRationaleWithoutLoopHint(String s) {

                    }
                });
    }
}
