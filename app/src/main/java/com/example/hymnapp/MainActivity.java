package com.example.hymnapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HymnAdapter.OnItemClickListener {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;
    private List<Hymn> originalHymnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);


        // Load hymn data and store in singleton for global use
        List<Hymn> hymnList = loadHymnData();
        originalHymnList = hymnList;
        HymnDataHolder.getInstance().setAllHymns(originalHymnList);

        // Setup ViewPager
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Setup BottomNavigationView
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_all) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (item.getItemId() == R.id.nav_favorites) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (item.getItemId() == R.id.nav_recent) {
                viewPager.setCurrentItem(2);
                return true;
            }
            return false;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNav.getMenu().getItem(position).setChecked(true);
            }
        });

        if (isInternetAvailable()) {
            fetchHymnsFromFirebase();
        } else {
            List<Hymn> localHymns = readHymnsFromFile(); // from internal storage
            if (localHymns.isEmpty()) {
                localHymns = loadHymnData(); // from assets
            }
            HymnDataHolder.getInstance().setAllHymns(localHymns);
        }

    }
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    private void fetchHymnsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("hymns").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Hymn> firebaseHymns = new ArrayList<>();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Hymn hymn = doc.toObject(Hymn.class);
                firebaseHymns.add(hymn);
            }

            // Save to internal file
            writeHymnsToFile(firebaseHymns);
            HymnDataHolder.getInstance().setAllHymns(firebaseHymns);
        }).addOnFailureListener(e -> {
            // fallback to local JSON if fetch fails
            HymnDataHolder.getInstance().setAllHymns(readHymnsFromFile());
        });
    }

    private void writeHymnsToFile(List<Hymn> hymns) {
        File file = new File(getFilesDir(), "hymns.json");
        try (FileWriter writer = new FileWriter(file)) {
            new Gson().toJson(hymns, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Hymn> readHymnsFromFile() {
        File file = new File(getFilesDir(), "hymns.json");
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Hymn>>() {}.getType();
            return new Gson().fromJson(reader, type);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menusearch, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (viewPager.getCurrentItem() == 0) {
                        // Only filter if we're on the All Hymns tab
                        AllHymnFragment fragment = (AllHymnFragment) getSupportFragmentManager()
                                .findFragmentByTag("f" + viewPager.getCurrentItem());
                        if (fragment != null) {
                            fragment.filterHymns(newText);
                        }
                    }
                    return true;
                }
            });
        }

        return true;
    }


    private List<Hymn> loadHymnData() {
        List<Hymn> hymns = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("hymns.json");
            InputStreamReader reader = new InputStreamReader(inputStream);

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
        // Load current recent list
        List<Hymn> recentList = RecentlyViewedManager.loadRecentHymns(this);

        // Avoid duplicates
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recentList.removeIf(h -> h.getId().equals(hymn.getId()));
        }
        recentList.add(0, hymn); // Add to top

        // Limit recent list size (e.g. last 20 hymns)
        if (recentList.size() > 50) {
            recentList = recentList.subList(0, 50);
        }

        // Save updated list
        RecentlyViewedManager.saveRecentHymns(this, recentList);
//        HymnDataHolder.getInstance().addRecentlyViewed(hymn);
        Intent intent = new Intent(this, HymnDetailsActivity.class);
        intent.putExtra("hymn", hymn);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Optional: Override if needed for drawer or fragment back handling
        super.onBackPressed();
    }
}
