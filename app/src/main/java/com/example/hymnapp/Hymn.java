package com.example.hymnapp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
public class Hymn  implements Serializable {
    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("lyrics")
    private String lyrics;

    private boolean isFavorite;



    public Hymn(String title, String author, String lyrics) {
        this.title = title;
        this.author = author;
        this.lyrics = lyrics;
        this.isFavorite = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLyrics() {
        return lyrics;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void toggleFavorite() {
        isFavorite = !isFavorite;
    }


    public Hymn(/* constructor parameters */) {
        // ...
        isFavorite = false;
    }

}
