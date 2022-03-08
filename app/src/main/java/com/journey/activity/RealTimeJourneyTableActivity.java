package com.journey.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.journey.R;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-03-01-20:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class RealTimeJourneyTableActivity extends AppCompatActivity {
    String dateTime;
    String originAddress;
    String endAddress;
    String gender;
    int minAge;
    int maxAge;
    double score;

    double originLat;
    double originLon;
    double endLat;
    double endLon;

    private Button findPeers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_journey_table);
        findPeers = findViewById(R.id.find_peers);

        getConInfo();
        setConInfo();
        postConditionInfo();
    }

    private void postConditionInfo() {
        findPeers.setOnClickListener(view -> {
            Intent intent = new Intent(this, PeerGroupActivity.class);
            startActivity(intent);
        });
    }
    private void getConInfo()
    {
        Intent conInfo = getIntent();
        Bundle bundle = conInfo.getExtras();
        dateTime = bundle.getString(getString(R.string.time));
        originAddress = bundle.getString(getString(R.string.origin));
        endAddress = bundle.getString(getString(R.string.end));
        gender = bundle.getString(getString(R.string.gender));
    }
    private void setConInfo()
    {
        TextView dt, origin, end, gen, min, max, s;
        dt = findViewById(R.id.datetime_tv);
        origin = findViewById(R.id.oAddress_tv);
        end = findViewById(R.id.eAddress_tv);
        gen = findViewById(R.id.gender_tv);

        dt.setText(dateTime);
        origin.setText(originAddress);
        end.setText(endAddress);
        gen.setText(gender);
    }
}