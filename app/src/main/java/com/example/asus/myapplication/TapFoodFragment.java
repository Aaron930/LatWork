package com.example.asus.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.Places;

import java.net.URLEncoder;
import java.util.List;

import com.example.asus.myapplication.providers.FavouriteContentProvider;

public class TapFoodFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final int REQUEST_LOCATION_CODE = 99;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    int PROXIMITY_RADIUS = 500;
    static double latitude, longitude;
    private Location mCurrentLocation;
    public static LinearLayout linearLayout;

    public static TextView txtPlaceName;
    public static TextView txtAddress;
    public static TextView txtRating;
    public static TextView txtDistance;
    public Button btnMap,btnAdd;
    public ImageButton btnTap;
    private static List<PlacePhotoMetadata> photosDataList;
    private static int currentPhotoIndex = 0;
    public  GeoDataClient geoDataClient;

    private static ContentResolver mContRes;

    public TapFoodFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContRes = getActivity().getContentResolver();

        linearLayout =getView().findViewById(R.id.layout2);
        txtPlaceName =getView().findViewById(R.id.PlaceNameTV);
        txtAddress = getView().findViewById(R.id.AddressTV);
        txtRating = getView().findViewById(R.id.RatingTV);
        txtDistance =getView().findViewById(R.id.DistanceTV);

        btnAdd=getView().findViewById(R.id.AddFavourite);
        btnTap=getView().findViewById(R.id.taptap);
        btnMap = getView().findViewById(R.id.GoToMap);

        geoDataClient = Places.getGeoDataClient(getActivity(),null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();

        }
        client=new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        btnAdd.setOnClickListener(btnAddOnClickListener);
        btnTap.setOnClickListener(btnTapOnClickListener);
        btnMap.setOnClickListener(btnMapOnClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tap_food_main, container, false);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(client, (com.google.android.gms.location.LocationListener) this);

        client.disconnect();
    }


    public View.OnClickListener btnMapOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String address = txtAddress.getText().toString();
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:0,0?q=%s", URLEncoder.encode(address))));
            startActivity(it);
        }
    };


    public View.OnClickListener btnAddOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                addData();
        }
    };


    public View.OnClickListener btnTapOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurrentLocation != null) {
                Log.i("Location", mCurrentLocation.toString());
            } else {
                Log.i("Location", "nothing");
            }
            Object dataTransfer[] = new Object[2];
            GetNearbyPlaceData getNearbyPlaceData = new GetNearbyPlaceData();

            String restaurant = "restaurant";
            String url = getUrl(latitude, longitude, restaurant);

            dataTransfer[0] = url;
            getNearbyPlaceData.execute(dataTransfer);


            Toast.makeText(getActivity(), "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
        }
    };

    public void addData() {
        if (!txtPlaceName.getText().toString().equals("")) {
            ContentValues newRow = new ContentValues();
            newRow.put("name", txtPlaceName.getText().toString());
            newRow.put("rating", txtRating.getText().toString());
            newRow.put("distance", txtDistance.getText().toString());
            newRow.put("address", txtAddress.getText().toString());

            mContRes.insert(FavouriteContentProvider.CONTENT_URI, newRow);
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
        }
    }



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        mCurrentLocation = location;

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }


    public String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&type=" + "cafe");
        googlePlaceUrl.append("&opennow="+"true");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyCWdzMBPjMgF8XwEaiEI7h_h-NpshHAlCA");
        googlePlaceUrl.append("&pagetoken=");


        Log.i("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }


    public static int getDistance(double latFromJson,double lngFromJson,double myLat,double myLng){
        Location myLocation = new Location("my loc");

        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLng);
        Location locFromJson = new Location("loc from json");

        locFromJson.setLatitude(latFromJson);
        locFromJson.setLongitude(lngFromJson);

        double distance = myLocation.distanceTo(locFromJson);
        int intDistance=(int)distance;
        return intDistance;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

