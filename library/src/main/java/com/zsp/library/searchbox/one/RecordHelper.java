package com.zsp.library.searchbox.one;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

/**
 * @decs: RecordHelper
 * @author: 郑少鹏
 * @date: 2019/4/22 11:56
 */
public class RecordHelper extends SQLiteOpenHelper {
    private static final String NAME = "SearchRecord.db";
    private static final Integer VERSION = 1;

    RecordHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        // 创数据库并建record表（仅name列存历史记录）
        db.execSQL("create table record(id integer primary key autoincrement,name varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
