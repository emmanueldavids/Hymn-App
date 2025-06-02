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
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements HymnAdapter.OnItemClickListener {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;
    private List<Hymn> originalHymnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Load hymn data (internal or assets)
        List<Hymn> hymnList = HymnUtils.readHymnsFromFile(this);
        if (hymnList.isEmpty()) {
            hymnList = HymnUtils.readHymnsFromAssets(this, "hymns.json");
        }

        originalHymnList = hymnList;
        HymnDataHolder.getInstance().setAllHymns(originalHymnList);

        // ViewPager and BottomNavigationView setup
        setupNavigation();

        // Sync Firebase data if online
        if (isInternetAvailable()) {
            syncWithFirebase();
        }

        // Periodic sync worker
        PeriodicWorkRequest syncRequest = new PeriodicWorkRequest.Builder(
                SyncWorker.class, 6, TimeUnit.HOURS
        ).build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "hymnSyncWork", ExistingPeriodicWorkPolicy.KEEP, syncRequest
        );
    }

    private void setupNavigation() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(this));

        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_all) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.nav_recent) {
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
    }

    private void syncWithFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Hymn> localHymns = HymnUtils.readHymnsFromFile(this);
        if (localHymns.isEmpty()) {
            localHymns = HymnUtils.readHymnsFromAssets(this, "hymns.json");
        }

        List<Hymn> finalLocalHymns = localHymns; // effectively final for use in lambda

        db.collection("hymns").get()
                .addOnSuccessListener(querySnapshots -> {
                    if (!querySnapshots.isEmpty()) {
                        List<Hymn> remoteHymns = new ArrayList<>();
                        for (DocumentSnapshot doc : querySnapshots) {
                            Hymn hymn = doc.toObject(Hymn.class);
                            if (hymn != null) {
                                remoteHymns.add(hymn);
                            }
                        }

                        List<Hymn> merged = HymnUtils.mergeHymns(finalLocalHymns, remoteHymns);
                        HymnUtils.saveHymnsToJson(this, merged);
                        HymnDataHolder.getInstance().setAllHymns(merged);
                    } else {
                        // Firebase returned nothing
                        HymnDataHolder.getInstance().setAllHymns(finalLocalHymns);
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    HymnDataHolder.getInstance().setAllHymns(finalLocalHymns);
                });
    }


    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onItemClick(Hymn hymn) {
        List<Hymn> recent = RecentlyViewedManager.loadRecentHymns(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recent.removeIf(h -> h.getId().equals(hymn.getId()));
        }
        recent.add(0, hymn);
        if (recent.size() > 50) {
            recent = recent.subList(0, 50);
        }
        RecentlyViewedManager.saveRecentHymns(this, recent);

        Intent intent = new Intent(this, HymnDetailsActivity.class);
        intent.putExtra("hymn", hymn);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
