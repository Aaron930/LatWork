package com.example.asus.myapplication;

public interface AsyncTaskResult<T extends Object>
{
    // T是執行結果的物件型態
    public void taskFinish( String placeName,String vicinity,String rating,int distance);
}
