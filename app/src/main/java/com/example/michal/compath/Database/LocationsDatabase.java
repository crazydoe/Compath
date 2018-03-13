package com.example.michal.compath.Database;

import android.location.Location;
import android.util.Log;

import com.activeandroid.query.Delete;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Michal on 06.07.2016.
 */
public class LocationsDatabase {
    private static LocationsDatabase ourInstance = new LocationsDatabase();

    public static LocationsDatabase getInstance() {
        return ourInstance;
    }

    private FavoriteLocationModel favoriteLocations;
    private HistoryLocationModel historyLocaions;
    private Location newLocationFromFavorite;


    public void setFromFavoriteLocation(Location location) {
        newLocationFromFavorite = location;
    }

    public Location getNewLocationFromFavorite() {
        return newLocationFromFavorite;
    }


    private LocationsDatabase() {
    }

    public void pushFavoriteLocaion(String description, double latitude, double longitude) {
        favoriteLocations = new FavoriteLocationModel();
        SimpleDateFormat sdf = new SimpleDateFormat("  dd - MM - yyyy    HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        favoriteLocations.locationDescription = description;
        favoriteLocations.createdAt = currentDateandTime;
        favoriteLocations.locationCoordiate = new Coordinate();
        favoriteLocations.locationCoordiate.latitude = latitude;
        favoriteLocations.locationCoordiate.longitude = longitude;
        favoriteLocations.locationCoordiate.save();
        favoriteLocations.save();

    }

    public void pushHistoryLocation(Location from, Location where) {
        SimpleDateFormat sdf = new SimpleDateFormat("  dd - MM - yyyy    HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        historyLocaions = new HistoryLocationModel();


        historyLocaions.createdAt = currentDateandTime;

        historyLocaions.coordinateFrom = new Coordinate();

        historyLocaions.coordinateFrom.latitude = from.getLatitude();
        historyLocaions.coordinateFrom.longitude = from.getLongitude();

        historyLocaions.coordinateWhere = new Coordinate();
        historyLocaions.coordinateWhere.latitude = where.getLatitude();
        historyLocaions.coordinateWhere.longitude = where.getLongitude();


        historyLocaions.coordinateWhere.save();
        historyLocaions.coordinateFrom.save();
        historyLocaions.save();

        Log.d("LocationDatabase", "Location  added to history");


    }

    public List<FavoriteLocationModel> getAllFavoriteLocations() {
        return FavoriteLocationModel.getAllLocations();
    }

    public List<HistoryLocationModel> getAllHistoryLocations() {
        return HistoryLocationModel.getAllLocations();
    }


    public void deleteFromHistory(String createdAt) {
        new Delete().from(HistoryLocationModel.class)
                .where("created_at = ?", createdAt)
                .execute();
    }

    public void deleteFromFavorite(String createdAt) {
        new Delete().from(FavoriteLocationModel.class)
                .where("created_at = ?", createdAt)
                .execute();
    }


}
