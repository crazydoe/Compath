package com.example.michal.compath.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Michal on 06.07.2016.
 */
@Table(name = "coordinate")
public class Coordinate extends Model{
    @Column(name = "latitude")
    public double latitude;
    @Column(name = "longitude")
    public double longitude;

}
