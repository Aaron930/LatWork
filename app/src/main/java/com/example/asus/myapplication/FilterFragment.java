package com.example.asus.myapplication;
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
    public SeekBar mSeekBar;
    public TextView mTxtSeekBar, mTxtRatingBar;
    public RatingBar mRatingBar;
    DataModel dataModel = DataModel.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRatingBar =getView().findViewById(R.id.ratingBar);
        mTxtRatingBar =getView().findViewById(R.id.txtRatingBar);
        mSeekBar =getView().findViewById(R.id.SeekBar);
        mTxtSeekBar =getView().findViewById(R.id.txtSeekBar);

        mRatingBar.setRating(3);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRatingValue();
                mTxtRatingBar.setText(String.valueOf(ratingBar.getRating()));
            }
        });
        mTxtRatingBar.setText(String.valueOf(mRatingBar.getRating()));

        mSeekBar.setMax(500);
        mSeekBar.setProgress(300);
        String seekBarValue =mSeekBar.getProgress()+100+"/"+(Integer.toString(mSeekBar.getMax()+100));
        mTxtSeekBar.setText(seekBarValue);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value;
            int max_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value=progress+100;
                max_value=seekBar.getMax()+100;
                String seekBarValue = progress_value+"/"+max_value;
                mTxtSeekBar.setText(seekBarValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String seekBarValue = progress_value+"/"+max_value;
                mTxtSeekBar.setText(seekBarValue);
                setDistanceValue();
            }
        });
        setRatingValue();
        setDistanceValue();
    }

    public void setRatingValue(){
        int ratingValue = Math.round(mRatingBar.getRating());
        dataModel.setRatingFilter(ratingValue);
    }

    public void setDistanceValue() {
        int distanceValue = mSeekBar.getProgress()+100;
        dataModel.setDistanceFilter(distanceValue);
    }
}
