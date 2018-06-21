package com.example.asus.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GetNearbyPlaceData extends AsyncTask<Object,String,String> {
    String googlePlacesData;
    String nextPageData;
    String allData;
    String url;
    String data;
    String newPage;
    String newPage2;
    String newpageURL;



    @Override
    protected String doInBackground(Object[] objects) {

        url = (String) objects[0];
        String token="";
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
        double lat = Double.parseDouble(googlePlace.get("lat"));
        double lng = Double.parseDouble(googlePlace.get("lng"));
        String rating = googlePlace.get("rating");
        String photoReference=googlePlace.get("photo_reference");
        Log.i("PlaceName",placeName);
        Log.i("Vicinity",vicinity);
        Log.i("Lat",Double.toString(lat));
        Log.i("Lng",Double.toString(lng));
        Log.i("rating",rating);
        double myLat = TapFoodActivity.latitude;
        double myLng = TapFoodActivity.longitude;
        int distance=TapFoodActivity.getDistance(lat,lng,myLat,myLng);
        Log.i("distance",Double.toString(distance));
        /*ImageDownloader task = new ImageDownloader();
        try {
            Bitmap bitmap = task.execute("https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference="+photoReference+"&key=AIzaSyCWdzMBPjMgF8XwEaiEI7h_h-NpshHAlCA").get();
            MainActivity.imageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        */
        TapFoodActivity.constraintLayout.setVisibility(View.VISIBLE);
        TapFoodActivity.PlaceNameTV.setText(placeName);
        TapFoodActivity.AddressTV.setText(vicinity);
        TapFoodActivity.RatingTV.setText(rating);
        TapFoodActivity.DistanceTV.setText(Integer.toString(distance));




    }
    public class ImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
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
