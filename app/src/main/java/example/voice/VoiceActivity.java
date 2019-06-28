package example.voice;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.zsp.library.voice.kit.VoiceKit;
import com.zsp.library.voice.value.VoiceFolder;
import com.zsp.utilone.file.FileUtils;
import com.zsp.utilone.permission.SoulPermissionUtils;
import com.zsp.widget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @decs: 语音页
 * @author: 郑少鹏
 * @date: 2019/6/28 16:36
 */
public class VoiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);
        execute();
    }

    private void execute() {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO),
                // if you want do noting or no need all the callbacks you may use SimplePermissionsAdapter instead
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        FileUtils.createFolder(VoiceFolder.VOICE, true);
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                        SoulPermissionUtils soulPermissionUtils = new SoulPermissionUtils();
                        soulPermissionUtils.multiPermissionsDenied(VoiceActivity.this, refusedPermissions);
                    }
                });
    }

    @OnClick({R.id.voiceActivityMbRecord, R.id.voiceActivityMbPlay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 录制
            case R.id.voiceActivityMbRecord:
                VoiceKit.startRecord(this);
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
