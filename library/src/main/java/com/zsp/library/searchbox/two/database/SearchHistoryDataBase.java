package com.zsp.library.searchbox.two.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zsp.utilone.data.StringUtils;

import java.util.ArrayList;
import java.util.List;

import value.WidgetLibraryMagic;

/**
 * @decs: 搜索历史记录数据库
 * @author: 郑少鹏
 * @date: 2019/4/23 11:39
 */
public class SearchHistoryDataBase extends SQLiteOpenHelper {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 创表SQL
     */
    private final String createTableSql;

    /**
     * constructor
     *
     * @param context 上下文
     * @param name    数据库名
     *                SearchHistory_db于沙盒看显乱码
     *                SearchHistory.db于沙盒直查
     * @param factory 游标工厂
     * @param version 版本号
     */
    public SearchHistoryDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        if (name.contains(WidgetLibraryMagic.STRING_DOT_DB)) {
            this.tableName = StringUtils.appointForward(name, ".db");
        } else if (name.contains(WidgetLibraryMagic.STRING_UNDERLINE_DB)) {
            this.tableName = StringUtils.appointForward(name, "_db");
        }
        this.createTableSql = "create table if not exists " + tableName + "(" + "id integer primary key autoincrement, " + "history text)";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创数据库建表
        sqLiteDatabase.execSQL(createTableSql);
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
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, "id desc");
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
            sqLiteDatabase.insert(tableName, null, contentValues);
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
        sqLiteDatabase.delete(tableName, "history=?", new String[]{keyword});
        // 关数据库
        sqLiteDatabase.close();
    }

    /**
     * 全删
     */
    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        // 删全数据
        sqLiteDatabase.execSQL("delete from " + tableName);
        // 关数据库
        sqLiteDatabase.close();
    }
}
