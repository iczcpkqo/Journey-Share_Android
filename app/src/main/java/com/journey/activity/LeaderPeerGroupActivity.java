package com.journey.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.R;
import com.journey.adapter.LeaderPeerAdapter;
import com.journey.adapter.ReqResApi;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.Peer;
import com.journey.service.database.DialogueHelper;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
public class LeaderPeerGroupActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button cancel;
    Button confirm;
    CountDownTimer countDownTimer;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private static String MATCHED_PEERS = "MATCHED_PEERS";
    LoadingDialog loadingDialog = new LoadingDialog(this, 8000, 2000, "");
    List<Peer> returnPeers;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.81:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent inte = new Intent();
        LeaderPeerGroupActivity.this.setResult(RESULT_OK, inte);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_peer_group);
        cancel = findViewById(R.id.cancel_btn);
        confirm = findViewById(R.id.confirm_btn);
        cancel.setVisibility(View.INVISIBLE);
        recyclerView = findViewById(R.id.leader_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// create recyclerview in linear layout
        try {
            loadingDialog.startLoadingDialog();
            sendMultiRequests(8000, 2000);
            sendPeersToNavigation();
            cancelJourney();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMultiRequests(Integer totalTime, Integer interval) {
        new CountDownTimer(totalTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                createLeaderPost(retrofit);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void createLeaderPost(Retrofit retrofit) {
        ReqResApi reqResApi = retrofit.create(ReqResApi.class);
        Peer peer = (Peer) getIntent().getSerializableExtra(RealTimeJourneyTableActivity.PEER_KEY);
        try {
            reqResApi.matchLeader(peer).enqueue(new Callback<List<Peer>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                    Toast.makeText(LeaderPeerGroupActivity.this, response.code() + " Response", Toast.LENGTH_SHORT).show();
                    List<Peer> peerList = response.body();
                    LeaderPeerAdapter leaderPeerAdapter = new LeaderPeerAdapter(LeaderPeerGroupActivity.this, peerList);
                    recyclerView.setAdapter(leaderPeerAdapter);
                    returnPeers = peerList;
                }

                @Override
                public void onFailure(Call<List<Peer>> call, Throwable t) {
                    System.out.println("-------------------on-failure1--------------" + t.fillInStackTrace());
                    Toast.makeText(LeaderPeerGroupActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(LeaderPeerGroupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void sendPeersToNavigation() {
        confirm.setOnClickListener(view -> {
            System.out.println("--------------------------------------------" + returnPeers.size());
            if (returnPeers != null && returnPeers.size() > 0) {
                Intent intent = new Intent(LeaderPeerGroupActivity.this, NavigationActivity.class);
                intent.putExtra(getString(R.string.PEER_LIST), FirebaseOperation.getObjectString(returnPeers));
                intent.putExtra(getString(R.string.CURRENT_PEER_EMAIL), DialogueHelper.getSender().getEmail());
                startActivityForResult(intent,1);
            } else {
                Toast.makeText(LeaderPeerGroupActivity.this, "There is no peer", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cancelJourney() {
        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(this, JourneyActivity.class);
            startActivity(intent);
        });
    }
}