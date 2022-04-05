package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.journey.R;
import com.journey.adapter.DailyJourneyCardAdapter;
import com.journey.adapter.FollowerPeerAdapter;
import com.journey.adapter.ParseDailyInfo;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.ConditionInfo;
import com.journey.model.DailyInfo;
import com.journey.service.database.DialogueHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DailyJourneyTableActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if(message.what == 1)
            {
                //Transform a json to java object
                String json = (String) message.obj;
                ParseDailyInfo parseDailyInfo = new ParseDailyInfo(json);
                List<DailyInfo> dailyInfoList = parseDailyInfo.parseJsonArray();
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
        List<DailyInfo> dailyInfoList = new ArrayList<>();
        FirebaseOperation.fuzzyQueries("daily","email", DialogueHelper.getSender().getEmail(),mhandler);
        DailyJourneyCardAdapter dailyJourneyCardAdapter = new DailyJourneyCardAdapter(DailyJourneyTableActivity.this, dailyInfoList);
        recyclerView.setAdapter(dailyJourneyCardAdapter);
    }

    private void addDailyJourney(ConditionInfo conditionInfo) {
        List<ConditionInfo> conditionInfoList = new ArrayList<>();
        Intent conInfo = new Intent(this, DailyJourneyTableActivity.class);
        conInfo.putExtra(getString(R.string.CONDITION_LIST), conditionInfoList.add(conditionInfo));
        startActivityForResult(conInfo,1);
    }
}