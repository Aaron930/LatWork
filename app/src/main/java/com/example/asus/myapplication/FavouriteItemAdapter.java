package com.example.asus.myapplication;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FavouriteItemAdapter extends  RecyclerView.Adapter<FavouriteItemAdapter.ViewHolder> {

    // 儲存要顯示的資料。
    private ArrayList<LinkedHashMap<String, String>> mListString;
    private static ContentResolver mContRes;
    private Context context;
    FavouriteFragment favouriteFragment = new FavouriteFragment();

    // ViewHolder 是把項目中所有的 View 物件包起來。
    // 它在 onCreateViewHolder() 中使用。
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImgView;
        public TextView mTxtName, mTxtScore, mTxtRating,mTxtAddress;
        DataBaseProgress dataBaseProgress = new DataBaseProgress();

        public ViewHolder(View itemView) {
            super(itemView);
            mImgView =  itemView.findViewById(R.id.imgView);
            mTxtName = itemView.findViewById(R.id.txtName);
            mTxtScore = itemView.findViewById(R.id.txtScore);
            mTxtRating = itemView.findViewById(R.id.txtRating);
            mTxtAddress = itemView.findViewById(R.id.txtAddress);
            mContRes=context.getContentResolver();

            // 處理按下的事件。
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // 按下後執行的程式碼。
            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String value = mTxtName.getText().toString();

//                            LinkedHashMap<String, String> selectedFromList = (LinkedHashMap<String, String>) recyclerView.getItemAtPosition(position);
//                            Map.Entry<String, String> entry = selectedFromList.entrySet().iterator().next();
//                            String value = entry.getValue();

//                            Log.d("item", String.valueOf(selectedFromList));
                            Log.d("name", String.valueOf(value));
                            dataBaseProgress.deleteData(mContRes,value);
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Maps",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String value = mTxtName.getText().toString();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:0,0?q=%s", URLEncoder.encode(value))));
                            Log.d("name", String.valueOf(value));
                            context.startActivity(intent);
                        }
                    });
            alertDialog.show();
        }
    }

    // 建構式，用來接收外部程式傳入的項目資料。
    public FavouriteItemAdapter(Context context,ArrayList<LinkedHashMap<String, String>> listString) {
        this.context = context;
        mListString = listString;
    }

    @Override
    public FavouriteItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 建立一個 view。
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        // 建立這個 view 的 ViewH                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            older。
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavouriteItemAdapter.ViewHolder holder, int position) {
        // 把資料設定給 ViewHolder。
        holder.mImgView.setImageResource(android.R.drawable.star_on);
        holder.mTxtName.setText(mListString.get(position).get("name"));
        holder.mTxtScore.setText(mListString.get(position).get("score"));
        holder.mTxtRating.setText(mListString.get(position).get("rating"));
        holder.mTxtAddress.setText(mListString.get(position).get("address"));
    }

    @Override
    public int getItemCount() {
        return mListString.size();
    }

}
