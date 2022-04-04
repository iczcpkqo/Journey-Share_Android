package com.journey.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.R;
import com.journey.adapter.FollowerPeerAdapter;
import com.journey.adapter.LeaderPeerAdapter;
import com.journey.adapter.ReqResApi;
import com.journey.dontremoveme.Chating;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.Peer;
import com.journey.service.database.DialogueHelper;
import com.mapbox.bindgen.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-03-19-10:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class FollowerPeerGroupActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button follower_cancel;
    Button follower_confirm;
    Button follower_chat;
    CountDownTimer countDownTimer;

    List<Peer> returnPeers;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private static String MATCHED_PEERS = "MATCHED_PEERS";
    LoadingDialog loadingDialog = new LoadingDialog(this,  8000, 2000, "");
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.137:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent inte = new Intent();
        FollowerPeerGroupActivity.this.setResult(RESULT_OK, inte);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_peer_group);
        follower_chat = (Button) findViewById(R.id.follower_chat_btn);
        follower_cancel = (Button) findViewById(R.id.follower_cancel_btn);
        follower_confirm = findViewById(R.id.follower_confirm_btn);
//        follower_cancel.setVisibility(View.INVISIBLE);
        recyclerView = findViewById(R.id.follower_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// create recyclerview in linear layout
        try {
            loadingDialog.startLoadingDialog();
            sendMultiRequests(8000, 2000);
            sendPeersToNavigation();
            cancelJourney();
            chatWithOthers();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMultiRequests(Integer totalTime, Integer interval) {
        Peer peer = (Peer) getIntent().getSerializableExtra(RealTimeJourneyTableActivity.PEER_KEY);
        new CountDownTimer(totalTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                joinPost(retrofit, peer);
            }
            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void joinPost(Retrofit retrofit, Peer peer) {
        //  get the deserialized peer from RealTimeJourneyTableA
        ReqResApi reqResApi = retrofit.create(ReqResApi.class);
        try {
            reqResApi.matchMemeber(peer).enqueue(new Callback<List<Peer>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                    Toast.makeText(FollowerPeerGroupActivity.this, response.code() + " Response", Toast.LENGTH_SHORT).show();
                    List<Peer> peerList = response.body();
                    FollowerPeerAdapter followerPeerAdapter = new FollowerPeerAdapter(FollowerPeerGroupActivity.this, peerList);
                    recyclerView.setAdapter(followerPeerAdapter);
                    returnPeers = peerList;
                }

                @Override
                public void onFailure(Call<List<Peer>> call, Throwable t) {
                    System.out.println("-------------------on-failure--------------" + t.toString());
                    Toast.makeText(FollowerPeerGroupActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(FollowerPeerGroupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendPeersToNavigation() {
        follower_confirm.setOnClickListener(view -> {
            if(returnPeers != null && returnPeers.size() > 0){
                //check which peer in peer list is current user
                System.out.println("-------------------"+returnPeers.get(1).getLeader());
                Intent intent = new Intent(FollowerPeerGroupActivity.this, NavigationActivity.class);
                intent.putExtra(getString(R.string.PEER_LIST), FirebaseOperation.getObjectString(returnPeers));
                intent.putExtra(getString(R.string.CURRENT_PEER_EMAIL), DialogueHelper.getSender().getEmail());
                startActivityForResult(intent,1);
            }else {
                Toast.makeText(FollowerPeerGroupActivity.this, "There is no peer", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void chatWithOthers() {
        List<String> emailList = new ArrayList<>();
        follower_chat.setOnClickListener(view -> {
            for (Peer p : returnPeers) {
                emailList.add(p.getEmail());
            }
            Chating.goWithMe(FollowerPeerGroupActivity.this,emailList);
        });
    }
    private void cancelJourney() {
        follower_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(this, JourneyActivity.class);
            startActivityForResult(intent,1);
        });
    }


}