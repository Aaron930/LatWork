package com.example.asus.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TapFoodResultFragment extends Fragment{

    public LinearLayout linearLayout;
    public TextView txtPlaceName;
    public  TextView txtAddress;
    public  TextView txtRating;
    public  TextView txtDistance;

    public TapFoodResultFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linearLayout =getView().findViewById(R.id.tap_food_second);
        txtPlaceName =getView().findViewById(R.id.PlaceNameTV);
        txtAddress = getView().findViewById(R.id.AddressTV);
        txtRating = getView().findViewById(R.id.RatingTV);
        txtDistance =getView().findViewById(R.id.DistanceTV);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        linearLayout.setVisibility(View.VISIBLE);
        return inflater.inflate(R.layout.tap_food_second, container, false);
    }

        public void getUI(String placeName,String vicinity,String rating,Integer distance){

       txtPlaceName.setText(placeName);
        txtAddress.setText(vicinity);
       txtRating.setText(rating);
         txtDistance.setText(String.valueOf(distance));

    }
}
