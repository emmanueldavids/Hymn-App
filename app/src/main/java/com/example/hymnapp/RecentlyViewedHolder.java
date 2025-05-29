package com.example.hymnapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecentlyViewedHolder {
    private static final int MAX_SIZE = 20;
    private static RecentlyViewedHolder instance;
    private LinkedList<Hymn> recentlyViewed = new LinkedList<>();

    private RecentlyViewedHolder() {}

    public static RecentlyViewedHolder getInstance() {
        if (instance == null) {
            instance = new RecentlyViewedHolder();
        }
        return instance;
    }

    public void addHymn(Hymn hymn) {
        recentlyViewed.remove(hymn); // Avoid duplicates
        recentlyViewed.addFirst(hymn);
        if (recentlyViewed.size() > MAX_SIZE) {
            recentlyViewed.removeLast();
        }
    }

    public List<Hymn> getRecentlyViewed() {
        return new ArrayList<>(recentlyViewed);
    }
}
