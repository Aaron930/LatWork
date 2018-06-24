package com.example.asus.myapplication;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.asus.myapplication.providers.FavouriteContentProvider;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FavouriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ListView listView;
    //    private ArrayAdapter<String> listAdapter;
    private ArrayList listStr = new ArrayList();
    private SwipeRefreshLayout mSwipeLayout;
    private static ContentResolver mContRes;

    public FavouriteFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("Tag", "onCreate");
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        mContRes = getActivity().getContentResolver();

        listView = getView().findViewById(R.id.list_item);

        mSwipeLayout = getView().findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                LinkedHashMap<String, String> selectedFromList = (LinkedHashMap<String, String>) listView.getItemAtPosition(position);
                                Map.Entry<String, String> entry = selectedFromList.entrySet().iterator().next();
                                String value = entry.getValue();

                                Log.d("item", String.valueOf(selectedFromList));
                                Log.d("name", String.valueOf(value));
                                deleteData(value);
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Maps",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LinkedHashMap<String, String> selectedFromList = (LinkedHashMap<String, String>) listView.getItemAtPosition(position);
                                Map.Entry<String, String> entry = selectedFromList.entrySet().iterator().next();
                                String value = entry.getValue();

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:0,0?q=%s", URLEncoder.encode(value))));
                                startActivity(intent);
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("Tag", "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView)
                menu.findItem(R.id.menuItemSearch).getActionView();
        searchView.setOnQueryTextListener(searchViewOnQueryTextLis);
    }

    private SearchView.OnQueryTextListener searchViewOnQueryTextLis = new
            SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchData(query);
                    return true;
                }
            };


    private void searchData(String word) {
        listStr.clear();

        LinkedHashMap<String, String> item = new LinkedHashMap<>();
        Cursor c = null;
        String[] projection = new String[]{"name", "rating", "distance", "address"};

        if (!word.equals("")) {
            c = mContRes.query(FavouriteContentProvider.CONTENT_URI, projection
                    , "name=" + "\"" + word
                            + "\"", null, null);
        }
        if (c == null)
            return;

        if (c.getCount() == 0) {
            Toast.makeText(getActivity(), "沒有這筆資料", Toast.LENGTH_LONG)
                    .show();
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
        ListAdapter listAdapter = new SimpleAdapter(getActivity(), listStr, R.layout.list_item,
                new String[]{"name", "data"},
                new int[]{R.id.txtName, R.id.txtData});
        listView.setAdapter(listAdapter);
    }

    private void deleteData(String value) {
        long test = mContRes.delete(FavouriteContentProvider.CONTENT_URI, "name=" + "\"" + value
                + "\"", null);
        if (test > 0) {
            Toast.makeText(getActivity(), "刪除成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "刪除失敗", Toast.LENGTH_SHORT).show();
        }
        getData();
    }

    public void getData() {
        listStr.clear();
        LinkedHashMap<String, String> item = new LinkedHashMap<>();

        String[] projection = new String[]{"name", "rating", "distance", "address"};

        Cursor c = mContRes.query(FavouriteContentProvider.CONTENT_URI, projection,
                null, null, null);


        if (c == null)
            return;

        if (c.getCount() == 0) {
            Toast.makeText(getActivity(), "沒有資料", Toast.LENGTH_LONG)
                    .show();
        } else {
            c.moveToFirst();
            item.put("name", c.getString(0));
            item.put("data", "\t" + c.getString(1) + "\n" + c.getString(2)+ "\n" + c.getString(3));
            listStr.add(item);

            while (c.moveToNext()) {
                LinkedHashMap<String, String> itemNew = new LinkedHashMap<>();
                itemNew.put("name", c.getString(0));
                itemNew.put("data", "\t" + c.getString(1) + "\n" + c.getString(2)+ "\n" + c.getString(3));
                listStr.add(itemNew);
            }
        }

        Log.d("datalist", String.valueOf(listStr));
        ListAdapter listAdapter = new SimpleAdapter(getActivity(), listStr, R.layout.list_item,
                new String[]{"name", "data"},
                new int[]{R.id.txtName, R.id.txtData});
        listView.setAdapter(listAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                // 停止刷新
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);

    }

}
