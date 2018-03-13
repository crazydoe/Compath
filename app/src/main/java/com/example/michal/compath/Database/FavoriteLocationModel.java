package com.example.michal.compath.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Michal on 06.07.2016.
 */

@Table(name = "FavoriteLocations")
public class FavoriteLocationModel extends Model{
    @Column(name = "location_description")
    public String locationDescription;

    @Column(name = "location_coordinate")
    public Coordinate locationCoordiate;

    @Column(name = "created_at")
    public String createdAt;

    public static List<FavoriteLocationModel>getAllLocations() {
        return new Select().from(FavoriteLocationModel.class).execute();
    }
}
