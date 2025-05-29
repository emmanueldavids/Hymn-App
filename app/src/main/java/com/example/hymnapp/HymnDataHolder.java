package com.example.hymnapp;

import java.util.ArrayList;
import java.util.List;

public class HymnDataHolder {
    private static HymnDataHolder instance;
    private List<Hymn> allHymns = new ArrayList<>();
    private List<Hymn> favoriteHymns = new ArrayList<>();
    private List<Hymn> recentlyViewedHymns = new ArrayList<>();

    private HymnDataHolder() {}

    public static HymnDataHolder getInstance() {
        if (instance == null) {
            instance = new HymnDataHolder();
        }
        return instance;
    }

    public void setAllHymns(List<Hymn> hymns) {
        this.allHymns = hymns;
    }

    public List<Hymn> getAllHymns() {
        return allHymns;
    }

    public void addFavorite(Hymn hymn) {
        if (!favoriteHymns.contains(hymn)) {
            favoriteHymns.add(hymn);
        }
    }

    public List<Hymn> getFavoriteHymns() {
        return favoriteHymns;
    }

    public void addRecentlyViewed(Hymn hymn) {
        // Optional: Move to top if already exists
        recentlyViewedHymns.remove(hymn);
        recentlyViewedHymns.add(0, hymn);

        // Optional: Limit size
        if (recentlyViewedHymns.size() > 50) {
            recentlyViewedHymns.remove(recentlyViewedHymns.size() - 1);
        }
    }

    public List<Hymn> getRecentlyViewedHymns() {
        return recentlyViewedHymns;
    }
}
