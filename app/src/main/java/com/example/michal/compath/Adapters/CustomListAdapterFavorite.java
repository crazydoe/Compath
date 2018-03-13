package com.example.michal.compath.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michal.compath.Database.FavoriteLocationModel;
import com.example.michal.compath.R;

import java.util.List;

/*
 * Created by michal on 06.07.16.
 */

public class CustomListAdapterFavorite extends RecyclerView.Adapter<CustomListAdapterFavorite.ViewHolder>{

    private List<FavoriteLocationModel> mDataset;

    public interface Callback {
        void onItemClick(String text, String position, double lati, double longi);
    }

    public Callback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView latitudeText;
        public TextView longitudeText;
        public TextView descriprionText;
        public TextView timeAtText;


        public ViewHolder(View v) {
            super(v);
            latitudeText = (TextView) v.findViewById(R.id.latitude_text);
            longitudeText = (TextView) v.findViewById(R.id.longitude_text);
            descriprionText = (TextView) v.findViewById(R.id.description_text);
            timeAtText = (TextView) v.findViewById(R.id.created_at_text);


        }
    }

    public CustomListAdapterFavorite(List<FavoriteLocationModel> myDataset, Callback callback) {
        mDataset = myDataset;
        this.callback = callback;
    }

    @Override
    public CustomListAdapterFavorite.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_custom_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String description = mDataset.get(position).locationDescription;
        final String timeAndDate = mDataset.get(position).createdAt;
        final Double latitude = mDataset.get(position).locationCoordiate.latitude;
        final Double longitude = mDataset.get(position).locationCoordiate.longitude;


        holder.latitudeText.setText(latitude + "");
        holder.longitudeText.setText(longitude + "");
        holder.timeAtText.setText(timeAndDate);
        holder.descriprionText.setText(description);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(timeAndDate, description, latitude, longitude);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
