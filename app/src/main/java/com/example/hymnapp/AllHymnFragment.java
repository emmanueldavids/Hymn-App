package com.example.hymnapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AllHymnFragment extends Fragment {

    private RecyclerView recyclerView;
    private HymnAdapter adapter;

    // Constructor sets the layout for this fragment
    public AllHymnFragment() {
        super(R.layout.fragment_all_hymn); // This should match the layout XML file name
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.hymnRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HymnAdapter(HymnDataHolder.getInstance().getAllHymns());

        if (getActivity() instanceof HymnAdapter.OnItemClickListener) {
            adapter.setOnItemClickListener((HymnAdapter.OnItemClickListener) getActivity());
        }

        recyclerView.setAdapter(adapter);
    }


    public void filterHymns(String query) {
        List<Hymn> filtered = new ArrayList<>();
        for (Hymn hymn : HymnDataHolder.getInstance().getAllHymns()) {
            if (hymn.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(hymn);
            }
        }
        adapter.updateList(filtered);
    }

}
