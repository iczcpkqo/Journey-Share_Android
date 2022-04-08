package com.journey.activity;

import static com.google.firebase.Timestamp.*;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.Timestamp;
import java.sql.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.R;
import com.journey.adapter.DailyJourneyCardAdapter;
import com.journey.adapter.ParseCondition;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.ConditionInfo;
import com.journey.model.DailyInfo;
import com.journey.service.database.DialogueHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

// implement onDailyItemLister interface
public class DailyJourneyTableActivity extends AppCompatActivity implements DailyJourneyCardAdapter.OnDailyItemListener{
    private RecyclerView recyclerView;
    private TextView addJourney;
    public static final String CONDITION_INFO = "CONDITION_INFO";
    List<ConditionInfo> cList = new ArrayList<>();
    ConditionInfo conditionInfo;
    // mhandler call back from firebase
    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what == 1)
            {
//                Map<String,Object> l = (Map<String, Object>) message.obj;
                ConditionInfo c = (ConditionInfo) message.obj;
                parseMessage(c);
                showDailyTable();
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
        readDaily();
        showDailyTable();
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
    private void showDailyTable(){
        cList.add(conditionInfo);
        if(cList != null){
            //passing array list and onDailyItemLister interface to Daily Journey card adapter
            DailyJourneyCardAdapter dailyJourneyCardAdapter = new DailyJourneyCardAdapter(DailyJourneyTableActivity.this, cList, this);
            recyclerView.setAdapter(dailyJourneyCardAdapter);
        }else {
            Toast.makeText(DailyJourneyTableActivity.this, "the data doesn't add into list", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDailyJourney(){
        addJourney.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConditionActivity.class);
            startActivityForResult(intent,1);
        });
    }

    @Override
    public void onDailyItemClick(int position) {
        ConditionInfo cur = cList.get(position);
        long curTime = new Date().getTime();
        // convert timestamp to long
        java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(cur.getDateTime());
        long jTime = ts2.getTime();
        if(jTime == curTime){
            Intent conInfo = new Intent(this, RealTimeJourneyTableActivity.class);
            //send serialized conditionInfo to real time activity
            conInfo.putExtra(CONDITION_INFO, (ConditionInfo) cList.get(position));
            startActivityForResult(conInfo,1);
        }else if(curTime > jTime){
            Toast.makeText(DailyJourneyTableActivity.this, "The journey has finished", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(DailyJourneyTableActivity.this, "Please wait, journey will start at "+cur.getDateTime(), Toast.LENGTH_SHORT).show();
        }

    }
    public void readDaily() {
        FirebaseOperation.fuzzyQueriesToDailyData("daily", "email",
                DialogueHelper.getSender().getEmail(), mhandler);
    }
    public void parseMessage(ConditionInfo c){
        String email = (String) c.getUserEmail();
        String dateTime = (String) c.getDateTime();
        String originAddress = (String) c.getOriginAddress();
        String endAddress = (String) c.getEndAddress();
        String preferGender = (String) c.getPreferGender();
        String minAge = (String) c.getMinAge();
        String maxAge = (String) c.getMaxAge();
        String minScore = (String) c.getMinScore();
        String origin_lon = (String) c.getOrigin_lon();
        String origin_lat = (String) c.getOrigin_lat();
        String end_lon = (String) c.getEnd_lon();
        String end_lat = (String) c.getEnd_lat();
        String journeyMode = (String) c.getJourneyMode();
        String route = (String) c.getRoute();
        conditionInfo = new ConditionInfo(email,dateTime,originAddress,endAddress,preferGender,
                minAge,maxAge, minScore,origin_lon,origin_lat,end_lon,
                end_lat,journeyMode,route);
    }
}