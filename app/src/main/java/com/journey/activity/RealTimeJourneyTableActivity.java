package com.journey.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.journey.R;
import com.journey.adapter.JSONPlaceholder;
import com.journey.adapter.PeerAdapter;
import com.journey.adapter.ReqResApi;
import com.journey.model.ConditionInfo;
import com.journey.model.Peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    //condition info
    String dateTime;
    String originAddress;
    String endAddress;
    String preferGender;
    String minAge;
    String maxAge;
    String score;
    String originLat;
    String originLon;
    String endLat;
    String endLon;
    Boolean isLeader = false;

    //user info
    String birthday;
    String gender;
    String phone;
    String username;
    String email;
    String mark;
    Integer age;

    final static public String PEER_KEY = "PEER";

    private Button findPeers;
    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_journey_table);
        findPeers = findViewById(R.id.find_peers);

        getConInfo();
        setConInfo();
        getUserInfo();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.137:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        postConditionInfo(retrofit);
    }
    private void postConditionInfo(Retrofit retrofit) {
        findPeers.setOnClickListener(view -> {
            try {
                loadingDialog.startLoadingDialog();
                createPost(retrofit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
//    private void keepMatching(Retrofit retrofit){
//        Timer timerObj = new Timer();
//        TimerTask timerTaskObj = new TimerTask() {
//            public void run() {
//                createPost(retrofit);
//            }
//        };
//        timerObj.schedule(timerTaskObj, 0, 10000);
//    }
    private void getConInfo(){
        if(getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(ConditionActivity.CONDITION_INFO)){
            //deserialization condition info
            ConditionInfo conditionInfo = (ConditionInfo) getIntent().getSerializableExtra(ConditionActivity.CONDITION_INFO);
            dateTime = conditionInfo.getDateTime();
            originAddress = conditionInfo.getOriginAddress();
            endAddress = conditionInfo.getEndAddress();
            preferGender = conditionInfo.getPreferGender();
            minAge = conditionInfo.getMinAge();
            maxAge = conditionInfo.getMaxAge();
            score = conditionInfo.getMinScore();
            originLat = conditionInfo.getOrigin_lat();
            originLon = conditionInfo.getOrigin_lon();
            endLat = conditionInfo.getEnd_lat();
            endLon = conditionInfo.getEnd_lon();
        }
    }
    private void setConInfo(){
        TextView dt, origin, end, gen, min, max, s;
        dt = findViewById(R.id.datetime_tv);
        origin = findViewById(R.id.oAddress_tv);
        end = findViewById(R.id.eAddress_tv);
        gen = findViewById(R.id.gender_tv);
        min = findViewById(R.id.min_age_tv);
        max = findViewById(R.id.max_age_tv);
        s = findViewById(R.id.score_tv);

        dt.setText(dateTime);
        origin.setText(originAddress);
        end.setText(endAddress);
        gen.setText(preferGender);
        min.setText(minAge);
        max.setText(maxAge);
        s.setText(score);
    }
    private void getUserInfo() {
        File file = new File(android.os.Environment.getExternalStorageDirectory()+"/UserInformation.txt");
        FileReader fis = null;
        BufferedReader br = null;
        if(file.exists()) {
            try {
                fis = new FileReader(file);
                br = new BufferedReader(fis);
                String str = br.readLine();
                String jsonMsg = "";
                while(str != null){
                    jsonMsg += str;
                    str = br.readLine();
                }
                System.out.println(jsonMsg);
                Map<String, Object> mp = JSON.parseObject(jsonMsg, new TypeReference<Map<String, Object>>(){});

                birthday = mp.get("birthDate").toString();
                gender = mp.get("gender").toString();
                phone = mp.get("phone").toString();
                username = mp.get("username").toString();
                email = mp.get("email").toString();
                mark = mp.get("mark").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void createPost(Retrofit retrofit) {
        Peer peer = new Peer(username, birthday, gender, phone,
                email, 5.0,
                4, 3, dateTime,
                originAddress,endAddress,
                preferGender,Integer.valueOf(minAge),Integer.valueOf(maxAge),Double.parseDouble(score),
                Double.parseDouble(originLon), Double.parseDouble(originLat),Double.parseDouble(endLon),
                Double.parseDouble(endLat),null,null,5,false);
        Intent intent = new Intent(RealTimeJourneyTableActivity.this,PeerGroupActivity.class);
        intent.putExtra(PEER_KEY,peer);

        final ReqResApi[] reqResApi = {retrofit.create(ReqResApi.class)};
        try {
            reqResApi[0].createUser(peer).enqueue(new Callback<List<Peer>>() {
                @Override
                public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                    Toast.makeText(RealTimeJourneyTableActivity.this, response.code() + "Send data to the server successfully", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<List<Peer>> call, Throwable t) {
                    System.out.println(t.toString());
                    Toast.makeText(RealTimeJourneyTableActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(RealTimeJourneyTableActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }

}