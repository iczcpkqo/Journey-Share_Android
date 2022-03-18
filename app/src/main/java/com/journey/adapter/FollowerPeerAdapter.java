package com.journey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.model.Peer;

import java.util.List;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-03-19-10:56
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class FollowerPeerAdapter extends RecyclerView.Adapter<FollowerPeerAdapter.PeerViewHolder> {

    List<Peer> peerList;
    Context context;
    public FollowerPeerAdapter(Context context , List<Peer> peers){
        this.context = context;
        peerList = peers;
    }
    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.follower_member_card, parent , false);
        return new PeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
        Peer peer = peerList.get(position);
        holder.email.setText(peer.getEmail());
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

        public PeerViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.follower_user_id_tv);
            gender = itemView.findViewById(R.id.follower_id_tv);
            age = itemView.findViewById(R.id.follower_title_tv);
            score = itemView.findViewById(R.id.follower_body_tv);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
