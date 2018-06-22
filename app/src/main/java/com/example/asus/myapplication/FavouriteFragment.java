package com.example.asus.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    ListView listView;
    ArrayList<String> alName=new ArrayList<>();
    ArrayList<Model>allModel=new ArrayList<>();
    UserDB userDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_favourite,container,false);

        listView=view.findViewById(R.id.listView);
        userDB=new UserDB(getActivity());
        //allModel = userDB.getAllData();
        userDB.getAllData();
        for (int i=0;i<allModel.size();i++){
            alName.add(allModel.get(i).getNAME());
        }
        /*UserDB userDBOpenHelper = new UserDB(getApplicationContext(),"favourite.db",null,1);
        favouriteDb= userDBOpenHelper.getReadableDatabase();
        */
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,alName);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Place Name :"+position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
