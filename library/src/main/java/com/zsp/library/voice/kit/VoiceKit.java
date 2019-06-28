package com.zsp.library.voice.kit;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zsp.library.R;
import com.zsp.library.voice.fragment.VoicePlayDialogFragment;
import com.zsp.library.voice.service.VoiceRecordService;
import com.zsp.library.voice.value.VoiceConstant;
import com.zsp.utilone.sharedpreferences.SharedPreferencesUtils;

/**
 * Created on 2018/12/7.
 *
 * @author 郑少鹏
 * @desc VoiceKit
 */
public class VoiceKit {
    private static final String TAG = "VoiceKit";

    /**
     * 开始录制
     *
     * @param activity 活动
     */
    public static void startRecord(Activity activity) {
        // start RecordService
        activity.startService(new Intent(activity, VoiceRecordService.class));
        // keep screen on while recording
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 停止录制
     *
     * @param activity 活动
     */
    public static void stopRecord(Activity activity) {
        // stop RecordService
        activity.stopService(new Intent(activity, VoiceRecordService.class));
        // allow the screen to turn off again once recording is finished
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 播放
     *
     * @param activity        上下文
     * @param fragmentManager 碎片管理器
     */
    public static void play(Activity activity, FragmentManager fragmentManager) {
        String voicePath = SharedPreferencesUtils.getString(activity, VoiceConstant.VOICE_RECORD_FILE_PATH, null);
        Log.e(TAG, "播放" + voicePath);
        if (voicePath != null) {
            VoicePlayDialogFragment voicePlayDialogFragment = new VoicePlayDialogFragment().
                    newInstance(voicePath, Long.parseLong(SharedPreferencesUtils.getString(activity, VoiceConstant.VOICE_RECORD_FILE_LENGTH, null)));
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            voicePlayDialogFragment.show(fragmentTransaction, "RecordPlayDialogFragment");
        } else {
            Toast.makeText(activity, activity.getString(R.string.noVoiceThisMoment), Toast.LENGTH_SHORT).show();
        }
    }
}
