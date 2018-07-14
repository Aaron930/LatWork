package com.example.asus.myapplication;

<<<<<<< HEAD
public class DataModel {
=======
import android.util.Log;

public class DataModel {

>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5
    private String name;
    private String rating;
    private String distance;
    private String address;
    private double latitude;
    private double longitude;
<<<<<<< HEAD
=======
    private int distanceValue;
    private int ratingValue;

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    private static DataModel instance = null;
    private DataModel(){}
    synchronized static public DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5

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
<<<<<<< HEAD
}
=======


    public int getDistanceValue() {
        return distanceValue;

    }

    public void setDistanceValue(int distanceValue) {
        Log.d("ra",String.valueOf(distanceValue) );
        this.distanceValue = distanceValue;
    }


}
>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5
