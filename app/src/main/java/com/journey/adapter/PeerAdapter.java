package com.journey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.model.Peer;
import com.mapbox.mapboxsdk.maps.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-02-15-20:56
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class PeerAdapter extends RecyclerView.Adapter<PeerAdapter.PeerViewHolder> {

    List<Peer> peerList;
    Context context;
    public PeerAdapter(Context context , List<Peer> peers){
        this.context = context;
        peerList = peers;
    }
    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_card, parent , false);
        return new PeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
        Peer peer = peerList.get(position);
        holder.email.setText("Peer : " + peer.getEmail());
        holder.gender.setText("gender : " + peer.getGender());
        holder.age.setText("age :" + peer.getAge());
        holder.score.setText("score :" + peer.getScore());
    }

    @Override
    public int getItemCount() {
        return peerList.size();
    }

    public class PeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView email, gender, age, score;
        ImageView imageView;
        Button delete;

        public PeerViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.user_id_tv);
            gender = itemView.findViewById(R.id.id_tv);
            age = itemView.findViewById(R.id.title_tv);
            score = itemView.findViewById(R.id.body_tv);
//            delete = itemView.findViewById(R.id.delete);
//
//            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
