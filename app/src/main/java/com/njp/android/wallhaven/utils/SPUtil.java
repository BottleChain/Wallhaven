package com.njp.android.wallhaven.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 */

public class SPUtil {

    private static SharedPreferences sPreferences;

    public static final String FILE_NAME = "settings";

    public static void init(Context context) {
        sPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void putInt(String key, int values) {
        sPreferences.edit().putInt(key, values).apply();
    }

    public static void putLong(String key, long values) {
        sPreferences.edit().putLong(key, values).apply();
    }

    public static void putFloat(String key, float values) {
        sPreferences.edit().putFloat(key, values).apply();
    }

    public static void putString(String key, String values) {
        sPreferences.edit().putString(key, values).apply();
    }

    public static void putBoolean(String key, boolean values) {
        sPreferences.edit().putBoolean(key, values).apply();
    }

    public static int getInt(String key, int defValues) {
        return sPreferences.getInt(key, defValues);
    }

    public static long getLong(String key, long defValues) {
        return sPreferences.getLong(key, defValues);
    }

    public static float getFloat(String key, float defValues) {
        return sPreferences.getFloat(key, defValues);
    }

    public static boolean getBoolean(String key, boolean defValues) {
        return sPreferences.getBoolean(key, defValues);
    }

    public static String getString(String key, String defValues) {
        return sPreferences.getString(key, defValues);
    }

}
