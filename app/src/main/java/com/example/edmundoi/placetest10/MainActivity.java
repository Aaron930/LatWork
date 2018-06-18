package com.example.edmundoi.placetest10;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.Places;

import java.net.URLEncoder;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {
    public static final int REQUEST_LOCATION_CODE = 99;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    int PROXIMITY_RADIUS = 500;
    static double latitude, longitude;
    private Location mCurrentLocation;
   public static ConstraintLayout constraintLayout;
   public static TextView PlaceNameTV;
   public static TextView AddressTV;
   public static Button button;
   public static TextView RatingTV;
    public static TextView DistanceTV;
    public static ImageView imageView;
    public static Button favourite;
    private static List<PlacePhotoMetadata> photosDataList;
    private static int currentPhotoIndex = 0;
    public static GeoDataClient geoDataClient;
    UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout=findViewById(R.id.layout2);
        PlaceNameTV=findViewById(R.id.PlaceNameTV);
        AddressTV = findViewById(R.id.AddressTV);
        button = findViewById(R.id.GoToMap);
        RatingTV = findViewById(R.id.RatingTV);
        DistanceTV = findViewById(R.id.DistanceTV);
        imageView = findViewById(R.id.imageView);
        favourite=findViewById(R.id.favouriteList);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this,FavouriteActivity.class);
                startActivity(i2);
            }
        });
        userDB=new UserDB(this);

        //favouriteDb=userDBOpenHelper.getWritableDatabase();
        /*Cursor cursor =favouriteDb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+DB_TABLE+"'",null);
        if(cursor!=null){
            if (cursor.getCount()==0)
                favouriteDb.execSQL("CREATE TABLE IF NOT EXISTS favourite("+"_id INTERGER PRIMARY KEY,"+"name TEXT NOT NULL,"+"rate REAL,"+"address TEXT,"+"distance REAL);");
            cursor.close();
        }
        */
        geoDataClient = Places.getGeoDataClient(this,null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();

    }
       client=new GoogleApiClient.Builder(getApplicationContext())
               .addApi(LocationServices.API)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .build();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);

        client.disconnect();
    }
    public void favouriteList(View view){
        Intent i2 = new Intent(MainActivity.this,FavouriteActivity.class);
        startActivity(i2);
    }
    public void favourite(View view){
        if (userDB.insertData(PlaceNameTV.getText().toString(),AddressTV.getText().toString(),RatingTV.getText().toString(),DistanceTV.getText().toString())!=-1){
        Toast.makeText(MainActivity.this, "Saved to Favourite", Toast.LENGTH_SHORT).show();
        }
}
    public void openInMap(View view) {

        String address = AddressTV.getText().toString();
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:0,0?q=%s", URLEncoder.encode(address))));
        startActivity(it);


    }

    public void taptap(View view){
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


        Toast.makeText(MainActivity.this, "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
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
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

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


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
