package com.example.asus.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.asus.myapplication.providers.FavouriteContentProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DataBaseProgress {

    public void searchData(ContentResolver mContRes,ArrayList<LinkedHashMap<String, String>> listStr,String word) {
        LinkedHashMap<String, String> item = new LinkedHashMap<>();
        Cursor c = null;
        String[] projection = new String[]{"name", "rating", "distance", "address"};

        if (!word.equals("")) {
            c = mContRes.query(FavouriteContentProvider.CONTENT_URI, projection
                    , "name=" + "\"" + word
                            + "\"", null, null);
        }
        if (c == null)
            return ;

        if (c.getCount() == 0) {
//            Toast.makeText(getActivity(), "沒有這筆資料", Toast.LENGTH_LONG)
//                    .show();
        } else {
            c.moveToFirst();
            item.put("name", c.getString(0));
            item.put("data", "\t" + c.getString(1) + "\n" + c.getString(2)+ "\n" + c.getString(3));
            listStr.add(item);
            while (c.moveToNext()) {
                LinkedHashMap<String, String> itemNew = new LinkedHashMap<>();
                itemNew.put("name", c.getString(0));
                itemNew.put("data", "\t" + c.getString(1) + "\n" + c.getString(2)+ "\n" + c.getString(3));
                listStr.add(item);
            }
        }
    }

    public void deleteData(ContentResolver mContRes, String value) {
        long test = mContRes.delete(FavouriteContentProvider.CONTENT_URI, "name=" + "\"" + value
                + "\"", null);
        if (test > 0) {
//
        } else {
//            Toast.makeText(getContext(), "刪除失敗", Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList getData(ContentResolver mContRes, ArrayList<LinkedHashMap<String, String>> listStr) {

        LinkedHashMap<String, String> item = new LinkedHashMap<>();

        String[] projection = new String[]{"name", "rating", "distance", "address"};

        Cursor c = mContRes.query(FavouriteContentProvider.CONTENT_URI, projection,
                null, null, null);


        if (c == null)
            return listStr;

        if (c.getCount() == 0) {
            listStr = null;

        } else {
            c.moveToFirst();
            item.put("name", c.getString(0));
            item.put("score", c.getString(1));
            item.put("rating", c.getString(2));
            item.put("address", c.getString(3));
            listStr.add(item);

            while (c.moveToNext()) {
                LinkedHashMap<String, String> itemNew = new LinkedHashMap<>();
                itemNew.put("name", c.getString(0));
                itemNew.put("score", c.getString(1));
                itemNew.put("rating", c.getString(2));
                itemNew.put("address", c.getString(3));
                listStr.add(itemNew);
            }
        }
        return listStr;
    }

    public void addData(ContentResolver mContRes,String placeName, String distance,String address,String rating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", placeName);
        contentValues.put("rating",rating);
        contentValues.put("distance", distance);
        contentValues.put("address", address);

        mContRes.insert(FavouriteContentProvider.CONTENT_URI, contentValues);
    }
}
