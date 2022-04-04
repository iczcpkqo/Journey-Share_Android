package com.journey.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.journey.R;
import com.journey.adapter.DailyJourneyCardAdapter;
import com.journey.adapter.FollowerPeerAdapter;
import com.journey.model.ConditionInfo;

import java.util.ArrayList;
import java.util.List;

public class DailyJourneyTableActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
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
        DailyJourneyCardAdapter dailyJourneyCardAdapter = new DailyJourneyCardAdapter(DailyJourneyTableActivity.this, conditionInfoList);
        recyclerView.setAdapter(dailyJourneyCardAdapter);
    }
}