package com.example.hymnapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecentlyViewedActivity extends AppCompatActivity {

    private HymnAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed);

        RecyclerView recyclerView = findViewById(R.id.recentlyViewedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Hymn> recentlyViewed = RecentlyViewedHolder.getInstance().getRecentlyViewed();
        adapter = new HymnAdapter(recentlyViewed);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(hymn -> {
            Intent intent = new Intent(this, HymnDetailsActivity.class);
            intent.putExtra("hymn", hymn);
            startActivity(intent);
        });
    }
}
