package com.example.hymnapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class HymnDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn_details);

        // Get the hymn data from the intent
        if (getIntent().hasExtra("hymn")) {
            Hymn hymn = (Hymn) getIntent().getSerializableExtra("hymn");
            displayHymnDetails(hymn);
        } else {
            // Handle the case when no hymn data is provided
            // For example, show an error message or go back to the previous activity
            finish();
        }
    }


    private void displayHymnDetails(Hymn hymn) {
        TextView titleTextView = findViewById(R.id.hymnTitleTextView);
        TextView lyricsTextView = findViewById(R.id.hymnLyricsTextView);

        titleTextView.setText(hymn.getTitle());
        lyricsTextView.setText(hymn.getLyrics());
    }
}
