package com.zsp.library.voice.value;

import android.os.Environment;

/**
 * Created on 2018/11/14.
 *
 * @author 郑少鹏
 * @desc 语音文件夹
 */
public class VoiceFolder {
    public static String VOICE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
}
