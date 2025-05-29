package com.example.hymnapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedManager {
    private static final String PREF_NAME = "HymnPrefs";
    private static final String KEY_RECENT_HYMNS = "recentHymns";

    public static void saveRecentHymns(Context context, List<Hymn> hymns) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = new Gson().toJson(hymns);
        editor.putString(KEY_RECENT_HYMNS, json);
        editor.apply();
    }

    public static List<Hymn> loadRecentHymns(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_RECENT_HYMNS, null);

        if (json != null) {
            Type type = new TypeToken<List<Hymn>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }
}
