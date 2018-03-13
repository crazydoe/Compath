package com.example.michal.compath.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.michal.compath.Adapters.CustomListAdapterFavorite;
import com.example.michal.compath.Database.FavoriteLocationModel;
import com.example.michal.compath.Database.LocationsDatabase;
import com.example.michal.compath.R;

import java.util.List;

public class FavoriteFragment extends Fragment {


    private Button addButton;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<FavoriteLocationModel> array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_favorite_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.favorite_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        addButton = (Button) view.findViewById(R.id.add_new_favorite_location);

        updateListView();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddFavoriteDialog(getContext()).show();
                updateListView();
            }
        });



        return view;
    }

    private void updateListView() {
        array = LocationsDatabase.getInstance().getAllFavoriteLocations();


        RecyclerView.Adapter mAdapter = new CustomListAdapterFavorite(array, new CustomListAdapterFavorite.Callback() {

            @Override
            public void onItemClick(String text, String position, double lati, double longi) {
                createOnClickListDialog(position, text, lati, longi).show();
            }
        });
        recyclerView.setAdapter(mAdapter);
    }


    private Dialog createAddFavoriteDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.new_favorite_dialog);
        dialog.setTitle("Dodaj lokalizację");

        final EditText longitude = (EditText) dialog.findViewById(R.id.latitude_favorite_text_edit_dialog);
        final EditText latitude = (EditText) dialog.findViewById(R.id.longitude_favorite_text_edit_dialog);
        final EditText description = (EditText) dialog.findViewById(R.id.location_description_text_edit_dialog);
        Button cancelBut = (Button) dialog.findViewById(R.id.cancel_button_dialog);
        Button confirmBut = (Button) dialog.findViewById(R.id.save_location_button);

        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    double latitudeDouble = Double.parseDouble(latitude.getText().toString());
                    double longitudeDouble = Double.parseDouble(longitude.getText().toString());
                    String descriptionString = description.getText().toString();

                    LocationsDatabase.getInstance().pushFavoriteLocaion(
                            descriptionString,
                            latitudeDouble,
                            longitudeDouble
                    );
                    dialog.dismiss();
                    updateListView();
                }catch (Exception e) {
                    Log.e("Exception", e + "");
                }



            }
        });

        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        return dialog;
    }


    private Dialog createOnClickListDialog(String name, final String timeAt, final double lati, final double longi) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(name);
        dialogBuilder.setMessage(
                "Wybierz akcję"
        );
        dialogBuilder.setPositiveButton("Przekaż do nawigacji", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("Which element choosed", which + "");

                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(lati);
                location.setLongitude(longi);

                LocationsDatabase.getInstance().setFromFavoriteLocation(location);

                updateListView();
            }
        });
        dialogBuilder.setNegativeButton("Usuń", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocationsDatabase.getInstance().deleteFromFavorite(timeAt);
                updateListView();
            }
        });
        return dialogBuilder.create();
    }
}
