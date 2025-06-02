package com.example.hymnapp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Hymn implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("lyrics")
    private String lyrics;

    private boolean isFavorite;

    // Main constructor
    public Hymn(int id, String title, String author, String lyrics) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.lyrics = lyrics;
        this.isFavorite = false;
    }

    // Empty constructor for Firebase and GSON
    public Hymn() {
        this.isFavorite = false;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
