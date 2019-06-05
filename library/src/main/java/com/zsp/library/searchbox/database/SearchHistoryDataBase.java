package com.zsp.library.searchbox.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @decs: 搜索历史记录数据库
 * @author: 郑少鹏
 * @date: 2019/4/23 11:39
 */
public class SearchHistoryDataBase extends SQLiteOpenHelper {
    /**
     * 数据库名
     * SearchHistory_db于沙盒看显乱码
     * SearchHistory.db于沙盒直查
     */
    public static final String DATA_BASE_NAME = "SearchHistory.db";
    /**
     * 表名
     */
    private static final String TABLE_NAME = "SearchHistory";
    private static final String CREATE_TABLE_SQL = "create table if not exists " + TABLE_NAME +
            "(" + "id integer primary key autoincrement, " + "history text)";

    public SearchHistoryDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创数据库建表
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 查全历史记录
     */
    public ArrayList<String> queryAllHistories() {
        ArrayList<String> histories = new ArrayList<>();
        // 数据库对象
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // 查表数据
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, "id desc");
        // 获name列索引
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String history = cursor.getString(1);
            histories.add(history);
        }
        // 关游标
        cursor.close();
        // 关数据库
        sqLiteDatabase.close();
        return histories;
    }

    /**
     * 插
     */
    public void insert(String keyword) {
        List<String> stringList = queryAllHistories();
        if (stringList.size() == 0 || !stringList.contains(keyword)) {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            // ContentValues对象
            ContentValues contentValues = new ContentValues();
            // 键值对存数据至ContentValues对象
            contentValues.put("history", keyword);
            // 插数据至数据库
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            // 关数据库
            sqLiteDatabase.close();
        }
    }

    /**
     * 删
     */
    public void delete(String keyword) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        // ContentValues对象
        sqLiteDatabase.delete(TABLE_NAME, "history=?", new String[]{keyword});
        // 关数据库
        sqLiteDatabase.close();
    }

    /**
     * 全删
     */
    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        // 删全数据
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME);
        // 关数据库
        sqLiteDatabase.close();
    }
}
