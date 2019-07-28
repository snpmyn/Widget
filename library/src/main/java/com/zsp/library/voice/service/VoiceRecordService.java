package com.zsp.library.voice.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;

import com.zsp.library.R;
import com.zsp.library.voice.value.VoiceConstant;
import com.zsp.library.voice.value.VoiceFolder;
import com.zsp.utilone.datetime.DateUtils;
import com.zsp.utilone.sharedpreferences.SharedPreferencesUtils;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

import timber.log.Timber;

/**
 * @decs: 录音服务
 * @author: 郑少鹏
 * @date: 2018/12/7 12:37
 */
public class VoiceRecordService extends Service {
    private String recordFilePath = null;
    private MediaRecorder mediaRecorder = null;
    private long recordStartTimeMillis = 0;
    private TimerTask timerTask = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    public void startRecording() {
        setFileNameAndPath();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(recordFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(192000);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            recordStartTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    public void setFileNameAndPath() {
        File file;
        do {
            // .mp3 .mp4 .amr
            String recordFileName = getString(R.string.voiceRecord) + DateUtils.timeStamp() + ".mp3";
            File mediaStorageDir = new File(VoiceFolder.VOICE);
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
                if (!mediaStorageDir.mkdirs()) {
                    Timber.d("setFileNameAndPath", "failed to create directory");
                }
            }
            file = new File(mediaStorageDir + File.separator + recordFileName);
            recordFilePath = VoiceFolder.VOICE + File.separator + recordFileName;
            SharedPreferencesUtils.saveString(this, VoiceConstant.VOICE_RECORD_FILE_NAME, getString(R.string.voiceRecord) + DateUtils.timeStamp());
        } while (file.exists() && !file.isDirectory());
    }

    public void stopRecording() {
        mediaRecorder.stop();
        long recordElapsedMillis = (System.currentTimeMillis() - recordStartTimeMillis);
        mediaRecorder.release();
        SharedPreferencesUtils.saveString(this, VoiceConstant.VOICE_RECORD_FILE_PATH, recordFilePath);
        SharedPreferencesUtils.saveString(this, VoiceConstant.VOICE_RECORD_FILE_LENGTH, Long.toString(recordElapsedMillis));
        Timber.d("停止录制" + recordFilePath);
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        mediaRecorder = null;
    }
}
