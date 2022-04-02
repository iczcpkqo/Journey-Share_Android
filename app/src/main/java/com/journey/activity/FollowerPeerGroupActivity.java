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
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.R;
import com.journey.adapter.FollowerPeerAdapter;
import com.journey.adapter.LeaderPeerAdapter;
import com.journey.adapter.ReqResApi;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.Peer;
import com.journey.service.database.DialogueHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    CountDownTimer countDownTimer;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private static String MATCHED_PEERS = "MATCHED_PEERS";
    LoadingDialog loadingDialog = new LoadingDialog(this,  8000, 2000, "");
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.81:8080/")
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
        setContentView(R.layout.activity_leader_peer_group);
        follower_cancel = findViewById(R.id.follower_cancel_btn);
        follower_confirm = findViewById(R.id.follower_confirm_btn);
        recyclerView = findViewById(R.id.leader_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// create recyclerview in linear layout
        try {
            loadingDialog.startLoadingDialog();
            sendMultiRequests(8000, 2000);
            cancelJourney();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMultiRequests(Integer totalTime, Integer interval) {
        new CountDownTimer(totalTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                joinPost(retrofit);
            }
            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void joinPost(Retrofit retrofit) {
        //  get the deserialized peer from RealTimeJourneyTableA
        Peer peer = (Peer) getIntent().getSerializableExtra(RealTimeJourneyTableActivity.PEER_KEY);
        ReqResApi reqResApi = retrofit.create(ReqResApi.class);
        try {
            reqResApi.matchMemeber(peer).enqueue(new Callback<List<Peer>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                    Toast.makeText(FollowerPeerGroupActivity.this, response.code() + " Response", Toast.LENGTH_SHORT).show();
                    List<Peer> peerList = response.body();
                    LeaderPeerAdapter leaderPeerAdapter = new LeaderPeerAdapter(FollowerPeerGroupActivity.this, peerList);
                    recyclerView.setAdapter(leaderPeerAdapter);
                    sendPeersToNavigation(peerList);
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
    private void sendPeersToNavigation(List<Peer> peerList) {
        follower_confirm.setOnClickListener(view -> {
            List<Peer> peers = peerList;
            int peerIndex = 0;
            if(!peers.isEmpty()){
                //check which peer in peer list is current user
                for (Peer peer : peerList) {
                    peerIndex++;
                    if(DialogueHelper.getSender().getEmail().equals(peer.getEmail())){
                        //do not display the current user
//                        peerList.remove(peerIndex-1);

                        //send peer to navigation page
                        Intent intent = new Intent(FollowerPeerGroupActivity.this, NavigationActivity.class);
                        intent.putExtra(getString(R.string.PEER_LIST), FirebaseOperation.getObjectString(peerList));
                        intent.putExtra(getString(R.string.CURRENT_PEER_EMAIL), peer.getEmail());
                        //startActivity(intent);
                        startActivityForResult(intent,1);
                    }
                }
            }else {
                Toast.makeText(FollowerPeerGroupActivity.this, "There is no peer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelJourney() {
        follower_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(this, JourneyActivity.class);
            startActivity(intent);
        });
    }


}