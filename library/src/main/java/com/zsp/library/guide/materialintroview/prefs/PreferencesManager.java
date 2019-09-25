package com.zsp.library.guide.materialintroview.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @decs: PreferencesManager
 * @author: 郑少鹏
 * @date: 2019/9/24 11:53
 */
public class PreferencesManager {
    private static final String PREFERENCES_NAME = "material_intro_view_preferences";
    private SharedPreferences sharedPreferences;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public PreferencesManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isDisplayed(String id) {
        return sharedPreferences.getBoolean(id, false);
    }

    public void setDisplayed(String id) {
        sharedPreferences.edit().putBoolean(id, true).apply();
    }

    public void reset(String id) {
        sharedPreferences.edit().putBoolean(id, false).apply();
    }

    public void resetAll() {
        sharedPreferences.edit().clear().apply();
    }
}
