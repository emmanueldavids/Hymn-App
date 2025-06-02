package com.example.hymnapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HymnUtils {
    private static final String FILE_NAME = "hymns.json";

    // Read hymns from internal storage JSON
    public static List<Hymn> readHymnsFromFile(Context context) {
        List<Hymn> hymns = new ArrayList<>();
        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                Type listType = new TypeToken<List<Hymn>>() {}.getType();
                hymns = new Gson().fromJson(reader, listType);
                reader.close();
            } else {
                // Fallback: Read from assets
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(context.getAssets().open(FILE_NAME)));
                Type listType = new TypeToken<List<Hymn>>() {}.getType();
                hymns = new Gson().fromJson(reader, listType);
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hymns;
    }

    public static List<Hymn> readHymnsFromAssets(Context context, String assetFileName) {
        List<Hymn> hymns = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(assetFileName)));
            Type listType = new TypeToken<List<Hymn>>() {}.getType();
            hymns = new Gson().fromJson(reader, listType);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hymns;
    }

    // Save updated hymns list to internal storage JSON
    public static void saveHymnsToJson(Context context, List<Hymn> hymns) {
        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            new Gson().toJson(hymns, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Merge remote hymns with local hymns (based on hymn number)
    public static List<Hymn> mergeHymns(List<Hymn> local, List<Hymn> remote) {
        Map<Integer, Hymn> hymnMap = new HashMap<>();

        // Add local hymns first
        for (Hymn hymn : local) {
            hymnMap.put(hymn.getId(), hymn);
        }

        // Overwrite with remote hymns (assumed to be latest)
        for (Hymn hymn : remote) {
            hymnMap.put(hymn.getId(), hymn);
        }

        return new ArrayList<>(hymnMap.values());
    }
}
