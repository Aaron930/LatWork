package com.example.asus.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.asus.myapplication.GooglePlaceApi.DataParser;
import com.example.asus.myapplication.GooglePlaceApi.DownloadURL;
import com.example.asus.myapplication.TapFoodFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GetNearbyPlaceData extends AsyncTask<Object,String,String> {
    private String googlePlacesData,
            allData,
            url,
            newPage,
            newPage2,
            newpageURL;




    @Override
    protected String doInBackground(Object[] objects) {

        url = (String) objects[0];
        String token = "";
        String resultData="";
        DataParser parser = new DataParser();
        DownloadURL downloadURL = new DownloadURL();

        try {
            googlePlacesData = downloadURL.readURL(url);
            Log.i("firstPageData",googlePlacesData);
            resultData = parser.parseData(googlePlacesData);
            token = parser.nextPageToken(googlePlacesData);
            if(token!=null || !token.matches("")){
                Thread.sleep(1400);
                newpageURL = url+ token;
                newPage=downloadURL.readURL(newpageURL);
                Log.i("nextPageData",newPage);
                token=parser.nextPageToken(newPage);
                if(token!=null || !token.matches("")){
                    Thread.sleep(1400);
                    newpageURL = url+ token;
                    newPage2=downloadURL.readURL(newpageURL);
                    Log.i("nextPageData2",newPage2);
                }

            }
            allData = googlePlacesData+"XXX"+newPage+"XXX"+newPage2;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public void randomPlaceData(List<HashMap<String, String>> nearbyPlaceList){
        int random = new Random().nextInt(nearbyPlaceList.size());

        HashMap<String, String> googlePlace = nearbyPlaceList.get(random);
        String placeName = googlePlace.get("place_name");
        String vicinity = googlePlace.get("vicinity");
        String rating = googlePlace.get("rating");

        double lat = Double.parseDouble(googlePlace.get("lat"));
        double lng = Double.parseDouble(googlePlace.get("lng"));

        double myLat = TapFoodFragment.latitude;
        double myLng = TapFoodFragment.longitude;


        Log.i("PlaceName",placeName);
        Log.i("Vicinity",vicinity);
        Log.i("Lat",Double.toString(lat));
        Log.i("Lng",Double.toString(lng));
        Log.i("rating",rating);


        int distance= TapFoodFragment.getDistance(lat,lng,myLat,myLng);
        Log.i("distance",Double.toString(distance));

        TapFoodFragment.linearLayout.setVisibility(View.VISIBLE);
        TapFoodFragment.txtPlaceName.setText(placeName);
        TapFoodFragment.txtAddress.setText(vicinity);
        TapFoodFragment.txtRating.setText(rating);
        TapFoodFragment.txtDistance.setText(Integer.toString(distance));

    }


    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        //  nearbyPlaceList = parser.getPlaces()
        nearbyPlaceList = parser.parse(s);
        randomPlaceData(nearbyPlaceList);
    }

}
