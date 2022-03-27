package com.journey.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.journey.R;
import com.journey.adapter.FollowerPeerAdapter;
import com.journey.adapter.LeaderPeerAdapter;
import com.journey.adapter.ReqResApi;
import com.journey.model.Peer;

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
    Button cancel;
    Button follow;
    final private static String MATCHED_PEERS = "MATCHED_PEERS";
    LoadingDialog loadingDialog = new LoadingDialog(this,null,null,null,null,null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_peer_group);

        cancel = findViewById(R.id.follower_cancel_btn);
        follow = findViewById(R.id.follower_confirm_btn);
        recyclerView = findViewById(R.id.follower_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// create recyclerview in linear layout

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.137:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        try {
            loadingDialog.startLoadingDialog();
            createPost(retrofit);
            cancelJourney();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void createPost(Retrofit retrofit) {
        //  get the deserialized peer from RealTimeJourneyTableA
        Peer peer = (Peer) getIntent().getSerializableExtra(RealTimeJourneyTableActivity.PEER_KEY);
        ReqResApi reqResApi = retrofit.create(ReqResApi.class);
        try {
            reqResApi.createUser(peer).enqueue(new Callback<List<Peer>>() {
                @Override
                public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                    Toast.makeText(FollowerPeerGroupActivity.this, response.code() + " Response", Toast.LENGTH_SHORT).show();
                    List<Peer> peerList = response.body();
                    FollowerPeerAdapter followerPeerAdapter = new FollowerPeerAdapter(FollowerPeerGroupActivity.this , peerList);
                    recyclerView.setAdapter(followerPeerAdapter);
                    sendFollowerToNavigation(peerList);
                }
                @Override
                public void onFailure(Call<List<Peer>> call, Throwable t) {
                    System.out.println(t.toString());
                    Toast.makeText(FollowerPeerGroupActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(FollowerPeerGroupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendFollowerToNavigation(List<Peer> peerList) {
        follow.setOnClickListener(view -> {
            List<Peer> peers = peerList;
            Intent intent = new Intent(FollowerPeerGroupActivity.this, NavigationActivity.class);
            intent.putExtra(MATCHED_PEERS, peers.get(0));
            intent.putExtra(MATCHED_PEERS, peers.get(1));
            startActivity(intent);
            //get data
//            List<Peer> peers = (List<Peer>) getIntent().getSerializableExtra(PeerGroupActivity.MATCHED_PEERS);
//            Collections.shuffle(peers);
//            Peer peer1 = peers.get(0);
        });
    }

    private void cancelJourney() {
        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(this, JourneyActivity.class);
            startActivity(intent);
        });
    }
}