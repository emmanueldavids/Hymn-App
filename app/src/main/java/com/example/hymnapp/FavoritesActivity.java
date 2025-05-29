package com.example.hymnapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private HymnAdapter adapter;
    private List<Hymn> favoriteHymns = new ArrayList<>(); // temporary list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        RecyclerView recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // For now we mock the data — ideally fetch from shared preferences/database
        if (HymnDataHolder.getInstance().getAllHymns() != null) {
            for (Hymn hymn : HymnDataHolder.getInstance().getAllHymns()) {
                if (hymn.isFavorite()) {
                    favoriteHymns.add(hymn);
                }
            }
        }

        adapter = new HymnAdapter(favoriteHymns);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(hymn -> {
            // Go to hymn details
            Intent intent = new Intent(this, HymnDetailsActivity.class);
            intent.putExtra("hymn", hymn);
            startActivity(intent);
        });
    }
}
