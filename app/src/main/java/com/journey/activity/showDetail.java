package com.journey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.journey.R;
import com.journey.entity.Record;

public class showDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        Intent intent = getIntent();
        Record record = (Record)intent.getSerializableExtra("DocObj");
        Log.d("detailpage", "onCreate: "+record);
//        String targetID = intent.getStringExtra("targetID");
//        String departure = intent.getStringExtra("departure");
//        String arrival = intent.getStringExtra("arrival");
//        String date = intent.getStringExtra("date");
        TextView mdeparture = (TextView)this.findViewById(R.id.dep_detail_text_view);
        TextView marrival = (TextView)this.findViewById(R.id.arr_detail_text_view);
        TextView mdate = (TextView)this.findViewById(R.id.dep_time_detail_text_view);
        mdeparture.setText(record.getDeparture());
        marrival.setText(record.getArrival());
        mdate.setText(record.getCreateDateString());

    }
}