package com.example.asus.myapplication.GooglePlaceApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String , String > getPlace(JSONObject googleplaceJson){

        HashMap<String,String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String placeId="";
        String vicinity= "--NA--";
        String latitude = "";
        String longitude="";
        String reference="";
        String rating ="";
        String photoReference = "";
        try {
            if (!googleplaceJson.isNull("name")){
                placeName = googleplaceJson.getString("name");
            }
            if (!googleplaceJson.isNull("vicinity")){
                vicinity = googleplaceJson.getString("vicinity");
            }
            latitude=googleplaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googleplaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googleplaceJson.getString("reference");
            rating = googleplaceJson.getString("rating");
            placeId = googleplaceJson.getString("place_id");
            photoReference = googleplaceJson.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            googlePlaceMap.put("place_name",placeName);
            googlePlaceMap.put("vicinity",vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);
            googlePlaceMap.put("reference",reference);
            googlePlaceMap.put("rating",rating);
            googlePlaceMap.put("place_id",placeId);
            googlePlaceMap.put("photo_reference",photoReference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  googlePlaceMap;
    }

    public List<HashMap<String,String>>getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;

        for (int i = 0 ;i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject)jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        String[] str_array = jsonData.split("XXX");
        String stringA = str_array[0];
        String stringB = str_array[1];
        String stringC = str_array[2];

        List<HashMap<String, String>> list1 = null;
        try {
            jsonObject = new JSONObject(stringA);
            String object = jsonObject.toString();
            Log.i("objectText", object);
            jsonArray = jsonObject.getJSONArray("results");
            list1 = new ArrayList<>();
            list1.addAll(getPlaces(jsonArray));
            jsonObject = new JSONObject(stringB);
            jsonArray = jsonObject.getJSONArray("results");
            list1.addAll(getPlaces(jsonArray));
            jsonObject = new JSONObject(stringC);
            jsonArray = jsonObject.getJSONArray("results");
            list1.addAll(getPlaces(jsonArray));
            //String test=jsonArray.toString();
            //Log.i("resultData",test);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list1;
    }
    public List<HashMap<String,String>> parseResultdata(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;


        Log.i("json data",jsonData);

        try {
            jsonArray = new JSONArray(jsonData);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    public String parseData(String jsonData){

        JSONObject jsonObject;
        String resultData ="";

        try {
            jsonObject = new JSONObject(jsonData);
            resultData = jsonObject.getJSONObject("results").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  resultData;
    }

    public String getPhotoReference(String jsonData){
        JSONObject jsonObject;
        String photoReference="";
        try {
            jsonObject = new JSONObject(jsonData);
            photoReference = jsonObject.getString("");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photoReference;
    }

    public String nextPageToken(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        String nextPageToken ="";
        try {
            jsonObject = new JSONObject(jsonData);
            nextPageToken = jsonObject.getString("next_page_token");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nextPageToken;
    }

}
