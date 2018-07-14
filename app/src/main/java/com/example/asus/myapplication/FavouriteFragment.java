package com.example.asus.myapplication;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FavouriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private ArrayList<LinkedHashMap<String, String>> listStr;
    private SwipeRefreshLayout mSwipeLayout;
    private static ContentResolver mContRes;
    private FavouriteItemAdapter favouriteItemAdapter;

    DataBaseProgress dataBaseProgress = new DataBaseProgress();

    public FavouriteFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("Tag", "onCreate");
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        mContRes = getActivity().getContentResolver();

        listStr = new ArrayList<>();
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeLayout = getView().findViewById(R.id.container);
        mSwipeLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                    dataBaseProgress.searchData(mContRes,listStr,query);
                    renewUI();
                    return true;
                }
            };

    public void renewUI(){
        listStr.clear();
        listStr = dataBaseProgress.getData(mContRes,listStr);

        Log.d("datalist", String.valueOf(listStr));
        if (listStr == null){
            Toast.makeText(getContext(), "沒有資料", Toast.LENGTH_SHORT).show();
        }
        else {
            favouriteItemAdapter = new FavouriteItemAdapter(getContext(), listStr);
            recyclerView.setAdapter(favouriteItemAdapter);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                renewUI();
                // 停止刷新
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);

    }

}
