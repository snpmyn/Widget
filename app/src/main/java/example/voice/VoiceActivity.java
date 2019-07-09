package example.voice;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zsp.library.voice.kit.VoiceKit;
import com.zsp.library.voice.value.VoiceFolder;
import com.zsp.utilone.file.FileUtils;
import com.zsp.utilone.miui.MiuiUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.utilone.toast.ToastUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 语音页
 * @author: 郑少鹏
 * @date: 2019/6/28 16:36
 */
public class VoiceActivity extends AppCompatActivity {
    private SoulPermissionUtils soulPermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);
        initConfiguration();
    }

    private void initConfiguration() {
        soulPermissionUtils = new SoulPermissionUtils();
    }

    @OnClick({R.id.voiceActivityMbRecord, R.id.voiceActivityMbPlay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 录制
            case R.id.voiceActivityMbRecord:
                soulPermissionUtils.checkAndRequestPermissions(
                        soulPermissionUtils,
                        true,
                        new SoulPermissionUtils.CheckAndRequestPermissionsCallBack() {
                            @Override
                            public void onAllPermissionOk() {
                                FileUtils.createFolder(VoiceFolder.VOICE, true);
                                VoiceKit.startRecord(VoiceActivity.this);
                            }

                            @Override
                            public void onPermissionDenied(String s) {
                                if (MiuiUtils.isMiUi()) {
                                    ToastUtils.shortShow(VoiceActivity.this, s);
                                    finish();
                                }
                            }
                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
                break;
            // 播放
            case R.id.voiceActivityMbPlay:
                VoiceKit.stopRecord(this);
                new Handler().postDelayed(() -> VoiceKit.play(VoiceActivity.this, getSupportFragmentManager()), 1000);
                break;
            default:
                break;
        }
    }
}
