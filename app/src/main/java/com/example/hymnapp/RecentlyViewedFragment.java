package com.example.hymnapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentlyViewedFragment extends Fragment {
    private RecyclerView recyclerView;
    private HymnAdapter adapter;

    public RecentlyViewedFragment() {
        super(R.layout.fragment_recently_viewed);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HymnAdapter(HymnDataHolder.getInstance().getRecentlyViewedHymns());
//        recyclerView.setAdapter(adapter);

//        List<Hymn> recentHymns = HymnDataHolder.getInstance().getRecentlyViewedHymns();
//        adapter.setOnItemClickListener((MainActivity) getActivity());
        List<Hymn> recentHymns = RecentlyViewedManager.loadRecentHymns(getContext());
        adapter = new HymnAdapter(recentHymns);
        recyclerView.setAdapter(adapter);

    }
}
