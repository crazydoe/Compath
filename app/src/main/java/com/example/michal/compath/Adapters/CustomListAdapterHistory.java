package com.example.michal.compath.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michal.compath.Database.FavoriteLocationModel;
import com.example.michal.compath.Database.HistoryLocationModel;
import com.example.michal.compath.R;

import java.util.List;

/**
 * Created by Michal on 07.07.2016.
 */


public class CustomListAdapterHistory extends RecyclerView.Adapter<CustomListAdapterHistory.ViewHolder>{

    private List<HistoryLocationModel> mDataset;

    public interface Callback {
        void onItemClick(String text, int position);
    }

    public Callback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView latitudeFrom;
        public TextView longitudeFrom;
        public TextView latitudeWhere;
        public TextView longitudeWhere;
        public TextView timeAtText;


        public ViewHolder(View v) {
            super(v);
            latitudeFrom = (TextView) v.findViewById(R.id.latitude_from);
            longitudeFrom = (TextView) v.findViewById(R.id.longitude_from);
            latitudeWhere = (TextView) v.findViewById(R.id.latitude_where);
            longitudeWhere = (TextView) v.findViewById(R.id.longitude_where);
            timeAtText = (TextView) v.findViewById(R.id.created_at_history);


        }
    }

    public CustomListAdapterHistory(List<HistoryLocationModel> myDataset, Callback callback) {
        mDataset = myDataset;
        this.callback = callback;
    }

    @Override
    public CustomListAdapterHistory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_custom_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String timeAndDate = mDataset.get(position).createdAt;
        final Double latitudeFrom = mDataset.get(position).coordinateFrom.latitude;
        final Double longitudeFrom = mDataset.get(position).coordinateFrom.longitude;
        final Double latitudeWhere = mDataset.get(position).coordinateWhere.latitude;
        final Double longitudeWhere = mDataset.get(position).coordinateWhere.longitude;



        holder.timeAtText.setText(timeAndDate);
        holder.latitudeFrom.setText(latitudeFrom + "");
        holder.longitudeFrom.setText(longitudeFrom + "");
        holder.latitudeWhere.setText(latitudeWhere + "");
        holder.longitudeWhere.setText(latitudeWhere + "");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(timeAndDate, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
