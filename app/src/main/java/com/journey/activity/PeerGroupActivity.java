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
 * @date: 2022-03-01-20:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class PeerGroupActivity extends AppCompatActivity {

    JSONPlaceholder jsonPlaceholder;
    RecyclerView recyclerView;

    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_group);

        cancel = findViewById(R.id.cancel_btn);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// create recyclerview in linear layout

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceholder = retrofit.create(JSONPlaceholder.class);

        getPeer();
        cancelJourney();
    }

    public void getPeer(){
        Call<List<Peer>> call = jsonPlaceholder.getPeer();
        call.enqueue(new Callback<List<Peer>>() {
            @Override
            public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(PeerGroupActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Peer> peerList = response.body();
                PeerAdapter peerAdapter = new PeerAdapter(PeerGroupActivity.this , peerList);
                recyclerView.setAdapter(peerAdapter);
            }

            @Override
            public void onFailure(Call<List<Peer>> call, Throwable t) {

                Toast.makeText(PeerGroupActivity.this, t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelJourney() {
        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(this,  JourneyActivity.class);
            startActivity(intent);
        });
    }

//    private ArrayList<Peer> getMylist() {
//        ArrayList<Peer> peers = new ArrayList<>();
//        Peer m = new Peer();
//        m.setName("john");
//        m.setScore("4");
//        m.setImage(R.drawable.u1);
//        peers.add(m);
//
//        m = new Peer();
//        m.setName("james");
//        m.setScore("8");
//        m.setImage(R.drawable.u2);
//        peers.add(m);
//
//        m = new Peer();
//        m.setName("matthew");
//        m.setScore("5");
//        m.setImage(R.drawable.u3);
//        peers.add(m);
//
//        m = new Peer();
//        m.setName("jordan");
//        m.setScore("2");
//        m.setImage(R.drawable.u4);
//        peers.add(m);
//
//        return peers;
//    }
}