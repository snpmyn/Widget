package com.zsp.library.searchbox.one;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @decs: RecordHelper
 * @author: 郑少鹏
 * @date: 2019/4/22 11:56
 */
public class RecordHelper extends SQLiteOpenHelper {
    private static String name = "SearchRecord.db";
    private static Integer version = 1;

    RecordHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创数据库并建record表（仅name列存历史记录）
        db.execSQL("create table record(id integer primary key autoincrement,name varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
