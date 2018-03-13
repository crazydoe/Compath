package com.example.michal.compath.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Michal on 06.07.2016.
 */

@Table(name = "HistoryLocations")
public class HistoryLocationModel extends Model{

    @Column(name = "from_coordinate")
    public Coordinate coordinateFrom;

    @Column(name = "where_coordinate")
    public Coordinate coordinateWhere;

    @Column(name = "CreatedAt")
    public String createdAt;


    public static List<HistoryLocationModel> getAllLocations() {
        return new Select().from(HistoryLocationModel.class).execute();
    }

}
