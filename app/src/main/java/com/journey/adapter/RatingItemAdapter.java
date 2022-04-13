package com.journey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.journey.R;
import com.journey.activity.RateComActivity;
import com.journey.entity.Rating;

import java.util.ArrayList;

public class RatingItemAdapter extends ArrayAdapter<Rating> {

    public RatingItemAdapter(Context context, ArrayList<Rating> ratingItemList){
        super(context,0,ratingItemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Rating rating = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_rating_listitem,parent,false);
        }
        TextView rateTarget = (TextView) convertView.findViewById(R.id.targetTextView);
        RatingBar rb = (RatingBar) convertView.findViewById(R.id.rateBar);
        rateTarget.setText(rating.getTo());
        rb.setRating((float) rating.getRating());
        rb.setTag(rating.getTo());

        if(rating.isRated()){
            rb.setIsIndicator(true);
        }

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (!b) return;
                ((RateComActivity) getContext()).ratingChangeHandler(ratingBar);
            }
        });


        return convertView;
    }



}
