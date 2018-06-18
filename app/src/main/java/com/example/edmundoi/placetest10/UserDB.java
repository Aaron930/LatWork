package com.example.edmundoi.placetest10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class UserDB extends SQLiteOpenHelper {
    public static final String DBNAME="favourite" +
            ".db";
    public static final String TABLE="favourite";
    public static final String NAME="Name";
    public static final String ADDRESS="Address";
    public static final String RATE="Rate";
    public static final String DISTANCE="Distance";
    public static final String ID="ID";
    public UserDB(Context context) {
        super(context, DBNAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE+"("+ID+"INTERGER PRIMARY KEY AUTOINCREMENT,"+NAME+" TEXT ,"+RATE+" REAL,"+ADDRESS+"TEXT,"+DISTANCE+" REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE+"");
        onCreate(db);
    }
    public long insertData(String name,String address,String rate,String distance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newRow = new ContentValues();
        newRow.put(NAME,name);
        newRow.put(ADDRESS,address);
        newRow.put(RATE,rate);
        newRow.put(DISTANCE,distance);
        return db.insert(TABLE,null,newRow);
    }
    public ArrayList<Model> getAllData(){
        ArrayList<Model>alName=new ArrayList<>();
        SQLiteDatabase dbR=this.getReadableDatabase();

        Cursor res=dbR.rawQuery("Select * From "+TABLE+"",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            Model m = new Model();
            m.setID(res.getString(res.getColumnIndex(ID)));
            m.setNAME(res.getString(res.getColumnIndex(NAME)));
            m.setADDRESS(res.getString(res.getColumnIndex(ADDRESS)));
            m.setRATE(res.getString(res.getColumnIndex(RATE)));
            m.setDISTANCE(res.getString(res.getColumnIndex(DISTANCE)));
            alName.add(m);
            res.moveToNext();
        }
        return alName;
    }
}
