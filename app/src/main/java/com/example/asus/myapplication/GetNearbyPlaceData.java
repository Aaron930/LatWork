package com.example.asus.myapplication;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.asus.myapplication.GooglePlaceApi.DataParser;
import com.example.asus.myapplication.GooglePlaceApi.DownloadURL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GetNearbyPlaceData extends AsyncTask<Object,String,String>{

    private String googlePlacesData,
            allData,
            url,
            newPage,
            newPage2,
            newpageURL;

    private String placeName ;
    private String vicinity ;
    private String rating ;
    private int distance;
    private int ratingFilter;
    private List<HashMap<String, String>> ratingPlaceData;
    public AsyncTaskResult<String> connectionTestResult;

    @Override
    protected String doInBackground(Object[] objects) {
        url = (String) objects[0];
        ratingFilter = (int) objects[1];
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

    private List<HashMap<String, String>> reloadData(List<HashMap<String, String>> getRating){
        ratingPlaceData=new ArrayList<>();
        int ratingInt= ratingFilter;
        for(HashMap<String,String>onlyData : getRating){
            if ((Float.valueOf(onlyData.get("rating"))) > ratingInt)
            {
                ratingPlaceData.add(onlyData);
            }
        }
        return ratingPlaceData;
    }

    private void randomPlaceData(List<HashMap<String, String>> nearbyPlaceList){
        DataModel dataModel =DataModel.getInstance();
        ratingPlaceData = reloadData(nearbyPlaceList);
        if (ratingPlaceData.size()!=0){
            int random = new Random().nextInt(ratingPlaceData.size());
            HashMap<String, String> googlePlace = ratingPlaceData.get(random);

            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            double myLat = dataModel.getLatitude();
            double myLng = dataModel.getLongitude();

            placeName = googlePlace.get("place_name");
            vicinity = googlePlace.get("vicinity");
            rating = googlePlace.get("rating");
            distance=getDistance(lat,lng,myLat,myLng);

            Log.i("PlaceName",placeName);
            Log.i("Vicinity",vicinity);
            Log.i("Lat",Double.toString(lat));
            Log.i("Lng",Double.toString(lng));
            Log.i("rating",rating);
            Log.i("distance",String.valueOf(distance));
        }
        else{
            placeName = "";
            vicinity = "";
            rating = "";
            distance= 0;
        }
    }

    private int getDistance(double latFromJson,double lngFromJson,double myLat,double myLng){
        Location myLocation = new Location("my loc");
        Location locFromJson = new Location("loc from json");

        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLng);

        locFromJson.setLatitude(latFromJson);
        locFromJson.setLongitude(lngFromJson);

        double distance = myLocation.distanceTo(locFromJson);
        int intDistance=(int)distance;
        return intDistance;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        randomPlaceData(nearbyPlaceList);

        this.connectionTestResult.taskFinish(placeName,vicinity,rating,distance);
    }
}
