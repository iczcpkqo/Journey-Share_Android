package com.journey.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.journey.R;
import com.journey.adapter.JSONPlaceholder;
import com.journey.adapter.PeerAdapter;
import com.journey.adapter.ReqResApi;
import com.journey.model.Peer;

import java.util.List;
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
public class PeerGroupActivity extends AppCompatActivity {
    JSONPlaceholder jsonPlaceholder;
    RecyclerView recyclerView;
    Button cancel;
    Button confirm;
    final private static String MATCHED_PEERS = "MATCHED_PEERS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_group);

        cancel = findViewById(R.id.cancel_btn);
        confirm = findViewById(R.id.confirm_btn);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// create recyclerview in linear layout

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.137:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceholder = retrofit.create(JSONPlaceholder.class);

        try {
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
                    Toast.makeText(PeerGroupActivity.this, response.code() + " Response", Toast.LENGTH_SHORT).show();
                    List<Peer> peerList = response.body();
                    PeerAdapter peerAdapter = new PeerAdapter(PeerGroupActivity.this , peerList);
                    recyclerView.setAdapter(peerAdapter);
                    sendPeersToNavigation(peerList);
                }
                @Override
                public void onFailure(Call<List<Peer>> call, Throwable t) {
                    System.out.println(t.toString());
                    Toast.makeText(PeerGroupActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(PeerGroupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPeersToNavigation(List<Peer> peerList) {
        confirm.setOnClickListener(view -> {
            List<Peer> peers = peerList;
            Intent intent = new Intent(PeerGroupActivity.this, NavigationActivity.class);
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