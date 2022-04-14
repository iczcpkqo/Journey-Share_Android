package com.journey.activity;

import static com.google.firebase.Timestamp.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
            if(message.what == 1 && message.obj != null)
            {
                List<Map<String,Object>> listData   = (List<Map<String,Object>> ) message.obj;
                parseMessage(listData);
                showDailyTable();
            }
            return false;
        }
    });
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent inte = new Intent();
        DailyJourneyTableActivity.this.setResult(-2, inte);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_journey_table);
        addJourney = findViewById(R.id.add_journey_tv);
        recyclerView = findViewById(R.id.daily_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        readDaily();
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
//        cList.add(conditionInfo);
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
        if( jTime == curTime){
            Intent conInfo = new Intent(this, RealTimeJourneyTableActivity.class);
            //send serialized conditionInfo to real time activity
            conInfo.putExtra(CONDITION_INFO, (ConditionInfo) cList.get(position));
            startActivityForResult(conInfo,1);
        }else if(jTime < curTime){
            Toast.makeText(DailyJourneyTableActivity.this, "The journey has finished", Toast.LENGTH_SHORT).show();
        }else if(jTime > curTime){
            Toast.makeText(DailyJourneyTableActivity.this, "Please wait, journey will start at "+cur.getDateTime(), Toast.LENGTH_SHORT).show();
        }

    }
    public void readDaily() {
        FirebaseOperation.fuzzyQueriesToDailyData("daily", "email",
                DialogueHelper.getSender().getEmail(), mhandler);

    }
    public void parseMessage(List<Map<String, Object>> listData){

        for (Map<String, Object> map : listData) {
            ConditionInfo cInfo = new ConditionInfo();
            Object v = map.toString();
            List<String> a = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                a.add(key);
                a.add(value);
            }
            //{(0)dateTime=2022-04-08 19:22:48, (2)minScore=1, (4)end_lat=53.34653, (6)preferGender=Male, (8)origin_lon=-6.331199157991676,
            // (10)originAddress=Chapelizod Road, Dublin, Dublin D20, Ireland, (12)journeyMode=Car, (14)origin_lat=53.3464057372658,
            // (16)route=null, (18)maxAge=99, (20)minAge=1, (22)email=liu@tcd.com, (24)endAddress=Chapelizod Road, Dublin, Dublin D08, Ireland,
            // (26)end_lon=-6.321053}
            cInfo = new ConditionInfo(a.get(23),a.get(1),a.get(11),a.get(25),a.get(7),a.get(21),a.get(19),a.get(3),a.get(9),
                                      a.get(15),a.get(27), a.get(5),a.get(13),a.get(17));
            cList.add(cInfo);
        }
    }
}