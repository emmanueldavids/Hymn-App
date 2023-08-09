package com.example.hymnapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HymnAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<Hymn> hymnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.hymnRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hymnList = loadHymnData(this);
        HymnAdapter adapter = new HymnAdapter(hymnList);

        // Set the click listener to this activity
        adapter.setOnItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    private List<Hymn> loadHymnData(MainActivity context) {
        List<Hymn> hymns = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("hymns.json");
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Using Gson for JSON parsing
            Gson gson = new Gson();
            Hymn[] hymnArray = gson.fromJson(reader, Hymn[].class);

            for (Hymn hymn : hymnArray) {
                hymns.add(hymn);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hymns;
    }

    @Override
    public void onItemClick(Hymn hymn) {
        // Handle the click event here, e.g., open a new activity or dialog to display the full hymn text
        // You can use the 'hymn' object to get the title, author, and other hymn details

        // Example: Show hymn details in a dialog
        // Start the HymnDetailsActivity to display the hymn details
        Intent intent = new Intent(this, HymnDetailsActivity.class);
        intent.putExtra("hymn", (Serializable) hymn);
        startActivity(intent);
    }
}
