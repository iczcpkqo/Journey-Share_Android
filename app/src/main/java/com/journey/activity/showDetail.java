package com.journey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.R;
import com.journey.entity.Record;

import java.util.Map;

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
        TextView marrvialDate = (TextView)this.findViewById(R.id.arr_time_detail_text_view);
        TextView mcompanion = (TextView)this.findViewById(R.id.companion_list_detail);
        Button ratingBtn = (Button) this.findViewById(R.id.rateCompBtn);
        TextView morderNo = (TextView)this.findViewById(R.id.order_number_detail);
        ratingBtn.setTag(record.getDocId());
        mdeparture.setText(record.getDeparture());
        marrival.setText(record.getArrival());
        mdate.setText(record.getCreateDateString());
        marrvialDate.setText(record.getArrivalDateString());
        mcompanion.setText(record.getcompanion());
        morderNo.setText(record.getDocumentId());

    }


    public void showRatingCompanion(View view){
        Intent intent = new Intent(this, RateComActivity.class);
        String ratingRecordID = view.getTag().toString();
        Log.d("rating",ratingRecordID);
        intent.putExtra("ratingRecordID",ratingRecordID);
        startActivity(intent);

    }
}