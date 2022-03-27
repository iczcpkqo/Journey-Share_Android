package com.journey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.adapter.ReadUserInfoFile;
import com.journey.adapter.ReqResApi;
import com.journey.model.ConditionInfo;
import com.journey.model.Peer;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
    String latitude;
    String longitude;
    String dLongitude;
    String dLatitude;
    Boolean isLeader;
    String startAddress;
    String destination;

    //user info
    String birthday;
    String gender;
    String phone;
    String username;
    String email;
    String mark;
    Integer age;
    Integer order;
    Integer limit;

    final static public String PEER_KEY = "PEER";
    RecyclerView recyclerView;
    private Button findPeers;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.6.40.176:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_journey_table);
        findPeers = findViewById(R.id.find_peers);
        getConInfo();
        setConInfo();
        getUserInfo();
        postInfo();

    }
    private void postInfo() {
        findPeers.setOnClickListener(view -> {
//            sendMultiRequests();
            createPostToPeerGroup(retrofit);
//            realTimeToLeaderGroup();
        });
    }

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
            latitude = conditionInfo.getOrigin_lat();
            longitude = conditionInfo.getOrigin_lon();
            dLatitude = conditionInfo.getEnd_lat();
            dLongitude = conditionInfo.getEnd_lon();
            startAddress = conditionInfo.getStartAddress();
            destination = conditionInfo.getDestination();
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
        origin.setText(startAddress);
        end.setText(destination);
        gen.setText(preferGender);
        min.setText(minAge);
        max.setText(maxAge);
        s.setText(score);
    }
    private void getUserInfo(){
        Map<String,Object> map = new ReadUserInfoFile().readUserFile();
        birthday = map.get("birthDate").toString();
        gender = map.get("gender").toString();
        phone = map.get("phone").toString();
        username = map.get("username").toString();
        email = map.get("email").toString();
        mark = map.get("mark").toString();
    }
    private void createPostToPeerGroup(Retrofit retrofit) {
        Peer peer = new Peer(email, gender, 20, Double.parseDouble(mark),
                Double.parseDouble(longitude), Double.parseDouble(latitude), Double.parseDouble(dLongitude),
                Double.parseDouble(dLatitude),0L,0L,
                123455,5,false, null,null,false, null,null,null,startAddress,destination);
        final ReqResApi[] reqResApi = {retrofit.create(ReqResApi.class)};
        try {
            reqResApi[0].createUser(peer).enqueue(new Callback<List<Peer>>() {
                @Override
                public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                    Toast.makeText(RealTimeJourneyTableActivity.this, response.code() + "Send successfully", Toast.LENGTH_SHORT).show();
                    List<Peer> peers = response.body();
                    getIsLeader(peers);
                }
                @Override
                public void onFailure(Call<List<Peer>> call, Throwable t) {
                    System.out.println("-------------------on-failure--------------"+ t.toString());
                    Toast.makeText(RealTimeJourneyTableActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(RealTimeJourneyTableActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(RealTimeJourneyTableActivity.this, LeaderPeerGroupActivity.class);
        intent.putExtra(PEER_KEY, peer);
        startActivity(intent);
    }
    private Boolean getIsLeader(List<Peer> peerList){

        return peerList.get(0).getLeader();
    }
//    private void realTimeToLeaderGroup(){
//        Intent intent = new Intent(this,LeaderPeerGroupActivity.class);
//        startActivity(intent);
//    }

}