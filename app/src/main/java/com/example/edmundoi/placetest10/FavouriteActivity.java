package com.example.edmundoi.placetest10;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String>alName=new ArrayList<>();
    ArrayList<Model>allModel=new ArrayList<>();
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        listView=findViewById(R.id.listView);
        userDB=new UserDB(this);
        //allModel = userDB.getAllData();
        userDB.getAllData();
        for (int i=0;i<allModel.size();i++){
            alName.add(allModel.get(i).getNAME());
        }
        /*UserDB userDBOpenHelper = new UserDB(getApplicationContext(),"favourite.db",null,1);
        favouriteDb= userDBOpenHelper.getReadableDatabase();
        */
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,alName);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FavouriteActivity.this, "Place Name :"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
