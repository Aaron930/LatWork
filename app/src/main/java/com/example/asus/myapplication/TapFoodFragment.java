package com.example.asus.myapplication;

import android.Manifest;
import android.content.ContentResolver;
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
import com.google.android.gms.location.places.Places;

import java.net.URLEncoder;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Random;
=======
>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5

public class TapFoodFragment extends Fragment implements
        AsyncTaskResult<String>,
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

    public LinearLayout linearLayout;
    public  TextView txtPlaceName;
    public  TextView txtAddress;
    public  TextView txtRating;
    public  TextView txtDistance;
    public Button btnMap,btnAdd;
    public ImageButton btnTap;
    public  GeoDataClient geoDataClient;

    private static ContentResolver mContRes;

    DataBaseProgress dataBaseProgress = new DataBaseProgress();
<<<<<<< HEAD
=======
    FilterFragment filterFragment =new  FilterFragment();
    DataModel dataModel =DataModel.getInstance();
>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5

    public TapFoodFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContRes = getActivity().getContentResolver();

        linearLayout =getActivity().findViewById(R.id.tap_food_second);
        txtPlaceName =getActivity().findViewById(R.id.PlaceNameTV);
        txtAddress = getActivity().findViewById(R.id.AddressTV);
        txtRating = getActivity().findViewById(R.id.RatingTV);
        txtDistance =getActivity().findViewById(R.id.DistanceTV);

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
        btnMap.setOnClickListener(btnMapOnClickListener);
        btnTap.setOnClickListener(btnTapOnClickListener);
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

    @Override
    public void taskFinish(String placeName,String vicinity,String rating,int distance) {
        if ( placeName.equals("")) {
            Toast.makeText( getContext(), "Failed", Toast.LENGTH_LONG ).show();
        }
        else {
            linearLayout.setVisibility(View.VISIBLE);
            txtPlaceName.setText(placeName);
            txtAddress.setText(vicinity);
            txtRating.setText(rating);
            txtDistance.setText(String.valueOf(distance));
            Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
        }
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
<<<<<<< HEAD
            DataModel dataModel = new DataModel();
=======

>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5

            if (!txtPlaceName.getText().toString().equals("")) {
//                dataModel.setName(txtPlaceName.getText().toString());
//                dataModel.setDistance(txtDistance.getText().toString());
//                dataModel.setAddress(txtAddress.getText().toString());
//                dataModel.setRating(txtRating.getText().toString());

                String placeName = txtPlaceName.getText().toString();
                String distance =txtDistance.getText().toString();
                String address =txtAddress.getText().toString();
                String rating = txtRating.getText().toString();

                dataBaseProgress.addData(mContRes,placeName,distance,address,rating);

                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
            }
        }
    };


    public View.OnClickListener btnTapOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GetNearbyPlaceData getNearbyPlaceData = new GetNearbyPlaceData();
           getNearbyPlaceData.connectionTestResult=TapFoodFragment.this;

            if (mCurrentLocation != null) {
                Log.i("Location", mCurrentLocation.toString());
            } else {
                Log.i("Location", "nothing");
            }
            Object dataTransfer[] = new Object[2];



<<<<<<< HEAD
            String restaurant = "restaurant";
            String url = getUrl(latitude, longitude, restaurant);

            dataTransfer[0] = url;

=======
            int abd=dataModel.getDistanceValue();
            Log.d("Distance", String.valueOf(abd));
            String restaurant = "restaurant";
            int distance=dataModel.getDistanceValue();
            String url = getUrl(latitude, longitude, restaurant,distance);
            int rating=dataModel.getRatingValue();
            dataTransfer[0] = url;
            dataTransfer[1]=rating;
>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5
            getNearbyPlaceData.execute(dataTransfer);

            Toast.makeText(getActivity(), "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
        }
    };

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
<<<<<<< HEAD
        DataModel dataModel = new DataModel();
=======

>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5

        latitude = location.getLatitude();
        longitude = location.getLongitude();



        dataModel.setLatitude(latitude);
        dataModel.setLongitude(longitude);

        lastlocation = location;
        mCurrentLocation = location;

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }


<<<<<<< HEAD
    public String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
=======
    public String getUrl(double latitude, double longitude, String nearbyPlace,int distance) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + distance);
>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&type=" + "cafe");
        googlePlaceUrl.append("&opennow="+"true");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyCWdzMBPjMgF8XwEaiEI7h_h-NpshHAlCA");
        googlePlaceUrl.append("&pagetoken=");


        Log.i("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
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

