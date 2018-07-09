package com.example.edmundoi.placetest10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class BlankFragment extends Fragment {
    public static SeekBar seekBar;
    public static TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_blank, container, false);
        seekBar=view.findViewById(R.id.seekBar);
        textView=view.findViewById(R.id.textView);
        textView.setText(seekBar.getProgress()+"/"+seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress_value=progress;
                textView.setText(progress+"/"+seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progress_value+"/"+seekBar.getMax());
            }
        });
        //        // Inflate the layout for this fragment
        return view;
    }
}
