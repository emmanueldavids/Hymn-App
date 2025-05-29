package com.example.hymnapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        HymnDataHolder.getInstance().addRecentlyViewed(hymn);
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
