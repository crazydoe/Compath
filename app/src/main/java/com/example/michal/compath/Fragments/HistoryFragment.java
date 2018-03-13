package com.example.michal.compath.Fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.michal.compath.Adapters.CustomListAdapterFavorite;
import com.example.michal.compath.Adapters.CustomListAdapterHistory;
import com.example.michal.compath.Database.FavoriteLocationModel;
import com.example.michal.compath.Database.HistoryLocationModel;
import com.example.michal.compath.Database.LocationsDatabase;
import com.example.michal.compath.R;

import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_history_fragment, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.history_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        updateListView();
        return view;

    }

    private void updateListView() {
        List<HistoryLocationModel> array = LocationsDatabase.getInstance().getAllHistoryLocations();


        RecyclerView.Adapter mAdapter = new CustomListAdapterHistory(array, new CustomListAdapterHistory.Callback() {
            @Override
            public void onItemClick(String text, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);
    }

}
