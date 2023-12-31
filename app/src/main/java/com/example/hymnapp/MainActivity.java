package com.example.hymnapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements HymnAdapter.OnItemClickListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private HymnAdapter adapter;
    private List<Hymn> originalHymnList;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menusearch, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.hymnRecyclerView);
        recyclerView.setAdapter(adapter);
        List<Hymn> hymnList = loadHymnData();
        originalHymnList = loadHymnData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HymnAdapter(originalHymnList);
        adapter.setOnItemClickListener(this);

        recyclerView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home)
            {
                // Handle home item click
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_LONG).show();
            }
            else if (itemId == R.id.share)
            {
                // Handle share item click
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"This is my app");
                intent.putExtra(Intent.EXTRA_SUBJECT,"App link here");
                startActivity(Intent.createChooser(intent, "share via"));
                Toast.makeText(MainActivity.this, "Share Selected", Toast.LENGTH_LONG).show();

            }
            else if (itemId == R.id.rateus)
            {
                // Handle about item click
                RateUs rateUs = new RateUs(MainActivity.this);
                rateUs.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                rateUs.setCancelable(false);
                rateUs.show();
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(MainActivity.this,"Rate Selected", Toast.LENGTH_LONG).show();
            }

            else if (itemId == R.id.exit)
            {
                Toast.makeText(MainActivity.this, "Good Bye!", Toast.LENGTH_LONG).show();
                finish();
            }
            else if (itemId == R.id.about)
            {
                {
                    Intent i = new Intent(MainActivity.this, AboutUs.class);
                    startActivity(i);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(MainActivity.this,"About Selected", Toast.LENGTH_LONG).show();
                }
            }
            return false;
        });



    }
    private void filter(String query) {
        List<Hymn> filteredList = new ArrayList<>();
        if (originalHymnList != null) {
            for (Hymn hymn : originalHymnList) {
                if (hymn.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(hymn);
                }
            }
        }
        if (adapter != null) {
            adapter.updateList(filteredList);
        }
    }



    private List<Hymn> loadHymnData() {
        List<Hymn> hymns = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("hymns.json");
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Using Gson for JSON parsing
            Gson gson = new Gson();
            Hymn[] hymnArray = gson.fromJson(reader, Hymn[].class);

            hymns.addAll(Arrays.asList(hymnArray));

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

        // Example: Start the HymnDetailsActivity to display the hymn details
        Intent intent = new Intent(this, HymnDetailsActivity.class);
        intent.putExtra("hymn", hymn);
        startActivity(intent);
    }

    // Handle back press to close the navigation drawer
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}
