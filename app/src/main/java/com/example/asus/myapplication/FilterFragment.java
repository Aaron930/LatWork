package com.example.asus.myapplication;

<<<<<<< HEAD
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilterFragment extends Fragment {

=======
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class FilterFragment extends Fragment {
        public SeekBar seekBar;
        public TextView textView;
        public RatingBar ratingBar;
        public TextView textView2;
    DataModel dataModel = DataModel.getInstance();
>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }
<<<<<<< HEAD
=======

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ratingBar=getActivity().findViewById(R.id.ratingBar);
        textView2=getActivity().findViewById(R.id.textView2);
        seekBar=getActivity().findViewById(R.id.seekBar1);
        ratingBar.setRating(3);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating();
                textView2.setText(String.valueOf(ratingBar.getRating()));
            }
        });
        textView2.setText(String.valueOf(ratingBar.getRating()));
        seekBar.setMax(500);
        seekBar.setProgress(300);
        textView=getActivity().findViewById(R.id.textView);
        textView.setText(seekBar.getProgress()+100+"/"+Integer.toString(seekBar.getMax()+100));;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value;
            int max_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value=progress+100;
                max_value=seekBar.getMax()+100;
                textView.setText(progress_value+"/"+max_value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progress_value+"/"+max_value);
                abc();
            }
        });
        rating();
        abc();
    }

    public void rating(){
        int rating = Math.round(ratingBar.getRating());
        dataModel.setRatingValue(rating);
    }
    public void abc() {
        int abd = seekBar.getProgress()+100;
        dataModel.setDistanceValue(abd);
        Log.d("filterValue",String.valueOf(abd));
    }
>>>>>>> 206315d9d9b9d88fec18969cac616ab57e9280b5
}
