package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import com.journey.R;
import com.journey.adapter.DailyJourneyCardAdapter;
import com.journey.adapter.ParseCondition;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.ConditionInfo;
import com.journey.model.DailyInfo;
import com.journey.service.database.DialogueHelper;

import java.util.ArrayList;
import java.util.List;

// implement onDailyItemLister interface
public class DailyJourneyTableActivity extends AppCompatActivity implements DailyJourneyCardAdapter.OnDailyItemListener{
    private RecyclerView recyclerView;
    private TextView addJourney;
    public static final String CONDITION_INFO = "CONDITION_INFO";
    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what == 1)
            {
                //Transform a json to java object
                String json = (String) message.obj;
                ParseCondition parseCondition = new ParseCondition(json);
                List<ConditionInfo> conInfoList = parseCondition.parseJsonArray();
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_journey_table);
        addJourney = findViewById(R.id.add_journey_tv);
        recyclerView = findViewById(R.id.daily_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ConditionInfo> cList = getConInfo();
        showDailyTable(cList);
        addDailyJourney();
    }

    private List<ConditionInfo> getConInfo(){
        List<ConditionInfo> conList = new ArrayList<ConditionInfo>();
        ConditionInfo conditionInfo = new ConditionInfo("123@qq.com", "2022-02-22",
                "dame street","capel street",
                "male","12", "50","50",
                "-6.04", "53.343","-6.877", "56.877",
                "car",null);
        ConditionInfo cInfo = new ConditionInfo("123@qq.com", "2022-02-22",
                "dame street","capel street",
                "male","12", "50","50",
                "-6.04", "53.343","-6.877", "56.877",
                "car",null);
        conList.add(conditionInfo);
        conList.add(cInfo);
        return conList;
    }
    private void showDailyTable(List<ConditionInfo> conList){
        FirebaseOperation.fuzzyQueries("daily","email", DialogueHelper.getSender().getEmail(),mhandler);
        //passing array list and onDailyItemLister interface to Daily Journey card adapter
        DailyJourneyCardAdapter dailyJourneyCardAdapter = new DailyJourneyCardAdapter(DailyJourneyTableActivity.this, conList, this);
        recyclerView.setAdapter(dailyJourneyCardAdapter);
    }

    private void addDailyJourney(){
        addJourney.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConditionActivity.class);
            startActivityForResult(intent,1);
        });
    }

    @Override
    public void onDailyItemClick(int position) {
        List<ConditionInfo> cList = getConInfo();


        Toast.makeText(this,"item"+ position, Toast.LENGTH_SHORT).show();
        Intent conInfo = new Intent(this, RealTimeJourneyTableActivity.class);
        //send serialized conditionInfo to real time activity
        conInfo.putExtra(CONDITION_INFO, (ConditionInfo) cList.get(position));
        startActivityForResult(conInfo,1);
    }
}