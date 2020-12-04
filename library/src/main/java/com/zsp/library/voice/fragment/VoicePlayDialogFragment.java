package com.zsp.library.voice.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zsp.library.R;
import com.zsp.library.voice.value.VoiceConstant;
import com.zsp.utilone.storage.sharedpreferences.SharedPreferencesUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * @decs: 语音播放对话框
 * @author: 郑少鹏
 * @date: 2018/12/7 12:26
 */
public class VoicePlayDialogFragment extends DialogFragment {
    /**
     * 控件
     */
    private SeekBar acsbVoicePlayDialogFragment;
    private TextView tvVoicePlayDialogFragmentProgressLength;
    private FloatingActionButton fabVoicePlayDialogFragmentClick;
    private TextView tvVoicePlayDialogFragmentFileLength;
    /**
     * handler
     */
    private final Handler mHandler = new Handler();
    /**
     * 播放器
     */
    private MediaPlayer mMediaPlayer;
    /**
     * 播放否
     */
    private boolean isPlaying;
    /**
     * 路径
     */
    private String path;
    /**
     * 分、秒
     */
    private long minutes = 0;
    private long seconds = 0;
    /**
     * 更新进度条
     */
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null) {
                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                acsbVoicePlayDialogFragment.setProgress(mCurrentPosition);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition) - TimeUnit.MINUTES.toSeconds(minutes);
                // 进度时长
                tvVoicePlayDialogFragmentProgressLength.setText(String.format(Locale.CHINA, "%02d:%02d", minutes, seconds));
                updateSeekBar();
            }
        }
    };

    /**
     * 实例
     *
     * @param voiceRecordFilePath   录音文件时长
     * @param voiceRecordFileLength 录音文件路径
     * @return 实例
     */
    public VoicePlayDialogFragment newInstance(String voiceRecordFilePath, long voiceRecordFileLength) {
        VoicePlayDialogFragment f = new VoicePlayDialogFragment();
        Bundle b = new Bundle();
        b.putString(VoiceConstant.VOICE_RECORD_FILE_PATH, voiceRecordFilePath);
        b.putString(VoiceConstant.VOICE_RECORD_FILE_LENGTH, Long.toString(voiceRecordFileLength));
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 文件路径
        if (getArguments() != null) {
            path = getArguments().getString(VoiceConstant.VOICE_RECORD_FILE_PATH);
        }
        // 文件时长
        Bundle bundle = getArguments();
        if (null != bundle) {
            long itemDuration = Long.parseLong(Objects.requireNonNull(bundle.getString(VoiceConstant.VOICE_RECORD_FILE_LENGTH), "must not be null"));
            minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
            seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes);
            Timber.d("时长：%s-%s", minutes, seconds);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_voice_play_dialog, null);
        // 初始控件
        TextView tvVoicePlayDialogFragmentFileName = view.findViewById(R.id.tvVoicePlayDialogFragmentFileName);
        acsbVoicePlayDialogFragment = view.findViewById(R.id.acsbVoicePlayDialogFragment);
        tvVoicePlayDialogFragmentProgressLength = view.findViewById(R.id.tvVoicePlayDialogFragmentProgressLength);
        fabVoicePlayDialogFragmentClick = view.findViewById(R.id.fabVoicePlayDialogFragmentClick);
        tvVoicePlayDialogFragmentFileLength = view.findViewById(R.id.tvVoicePlayDialogFragmentFileLength);
        // 文件名
        tvVoicePlayDialogFragmentFileName.setText(SharedPreferencesUtils.getString(Objects.requireNonNull(getContext(), "must not be null"),
                VoiceConstant.VOICE_RECORD_FILE_NAME, null));
        // 进度
        ColorFilter filter = new LightingColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), ContextCompat.getColor(getContext(), R.color.colorPrimary));
        acsbVoicePlayDialogFragment.getProgressDrawable().setColorFilter(filter);
        acsbVoicePlayDialogFragment.getThumb().setColorFilter(filter);
        acsbVoicePlayDialogFragment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mHandler.removeCallbacks(mRunnable);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition()) - TimeUnit.MINUTES.toSeconds(minutes);
                    // 进度时长
                    tvVoicePlayDialogFragmentProgressLength.setText(String.format(Locale.CHINA, "%02d:%02d", minutes, seconds));
                    updateSeekBar();
                } else if (mMediaPlayer == null && fromUser) {
                    prepareMediaPlayerFromPoint(progress);
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null) {
                    // remove message Handler from updating progress bar
                    mHandler.removeCallbacks(mRunnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null) {
                    mHandler.removeCallbacks(mRunnable);
                    mMediaPlayer.seekTo(seekBar.getProgress());
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition()) - TimeUnit.MINUTES.toSeconds(minutes);
                    // 进度时长
                    tvVoicePlayDialogFragmentProgressLength.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
                    updateSeekBar();
                }
            }
        });
        // 按钮
        fabVoicePlayDialogFragmentClick.setOnClickListener(v -> {
            play(isPlaying);
            isPlaying = !isPlaying;
        });
        // 文件时长
        tvVoicePlayDialogFragmentFileLength.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                // set transparent background
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
            // disable buttons from widget.dialog
            AlertDialog alertDialog = (AlertDialog) getDialog();
            alertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
            alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
            alertDialog.getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    /**
     * 播放
     *
     * @param playing 播放否
     */
    private void play(boolean playing) {
        if (!playing) {
            // currently MediaPlayer is not playing audio
            if (mMediaPlayer == null) {
                // start from beginning
                startPlaying();
            } else {
                // resume the currently paused MediaPlayer
                resumePlaying();
            }
        } else {
            // pause the MediaPlayer
            pausePlaying();
        }
    }

    private void startPlaying() {
        fabVoicePlayDialogFragmentClick.setImageResource(R.drawable.voice_play_pause);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            acsbVoicePlayDialogFragment.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.setOnPreparedListener(mp -> mMediaPlayer.start());
        } catch (IOException e) {
            Timber.d("prepare() failed");
        }
        mMediaPlayer.setOnCompletionListener(mp -> stopPlaying());
        updateSeekBar();
        // keep screen on while playing audio
        if (getActivity() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void prepareMediaPlayerFromPoint(int progress) {
        // set mediaPlayer to start from middle of the audio file
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            acsbVoicePlayDialogFragment.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(progress);
            mMediaPlayer.setOnCompletionListener(mp -> stopPlaying());
        } catch (IOException e) {
            Timber.d("prepare fail");
        }
        // keep screen on while playing audio
        if (getActivity() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void pausePlaying() {
        fabVoicePlayDialogFragmentClick.setImageResource(R.drawable.voice_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();
    }

    private void resumePlaying() {
        fabVoicePlayDialogFragmentClick.setImageResource(R.drawable.voice_play_pause);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        updateSeekBar();
    }

    private void stopPlaying() {
        fabVoicePlayDialogFragmentClick.setImageResource(R.drawable.voice_play);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
        acsbVoicePlayDialogFragment.setProgress(acsbVoicePlayDialogFragment.getMax());
        isPlaying = !isPlaying;
        // 进度时长
        tvVoicePlayDialogFragmentProgressLength.setText(tvVoicePlayDialogFragmentFileLength.getText());
        acsbVoicePlayDialogFragment.setProgress(acsbVoicePlayDialogFragment.getMax());
        // allow the screen to turn off again once audio is finished playing
        if (getActivity() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
    }
}
