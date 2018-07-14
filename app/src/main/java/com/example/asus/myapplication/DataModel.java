package com.example.asus.myapplication;


import android.util.Log;

public class DataModel {

    private String name;
    private String rating;
    private String distance;
    private String address;
    private double latitude;
    private double longitude;
    private int distanceFilter;
    private int ratingFilter;

    private static DataModel instance = null;

    private DataModel(){}
    synchronized static public DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistanceFilter() {
        return distanceFilter;
    }

    public void setDistanceFilter(int distanceFilter) {
        this.distanceFilter = distanceFilter;
    }

    public int getRatingFilter() {
        return ratingFilter;
    }

    public void setRatingFilter(int ratingFilter) {
        this.ratingFilter = ratingFilter;
    }

}

