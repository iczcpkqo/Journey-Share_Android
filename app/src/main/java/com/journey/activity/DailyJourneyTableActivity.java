package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.journey.R;
import com.journey.adapter.DailyJourneyCardAdapter;
import com.journey.adapter.FollowerPeerAdapter;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.ConditionInfo;
import com.journey.service.database.DialogueHelper;

import java.util.ArrayList;
import java.util.List;

public class DailyJourneyTableActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if(message.what == 1)
            {
                String json = (String) message.obj;
            }

            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_journey_table);
        recyclerView = findViewById(R.id.daily_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showDailyTable();
    }
    private void showDailyTable(){
        List<ConditionInfo> conditionInfoList = new ArrayList<>();

        FirebaseOperation.fuzzyQueries("daily","email", DialogueHelper.getSender().getEmail(),mhandler);


        DailyJourneyCardAdapter dailyJourneyCardAdapter = new DailyJourneyCardAdapter(DailyJourneyTableActivity.this, conditionInfoList);
        recyclerView.setAdapter(dailyJourneyCardAdapter);
    }



}