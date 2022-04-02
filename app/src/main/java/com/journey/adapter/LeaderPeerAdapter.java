package com.journey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.activity.LeaderPeerGroupActivity;
import com.journey.activity.LoadingDialog;
import com.journey.model.Peer;

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
public class LeaderPeerAdapter extends RecyclerView.Adapter<LeaderPeerAdapter.PeerViewHolder> {

    List<Peer> peerList;
    Context context;
    public LeaderPeerAdapter(Context context , List<Peer> peers){
        this.context = context;
        this.peerList = peers;
    }
    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(this.peerList.isEmpty()){
            view = LayoutInflater.from(context).inflate(R.layout.no_peer_showing, parent , false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.leader_member_card, parent , false);
        }
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
        Button delete;
        LeaderPeerAdapter leaderPeerAdapter;

        public PeerViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.user_id_tv);
            gender = itemView.findViewById(R.id.id_tv);
            age = itemView.findViewById(R.id.title_tv);
            score = itemView.findViewById(R.id.body_tv);
            delete = itemView.findViewById(R.id.delete_btn);
        }
        @Override
        public void onClick(View view) {
            itemView.findViewById(R.id.delete_btn).setOnClickListener(view1 -> {
//                leaderPeerAdapter.peerList.remove(getAdapterPosition());
//                leaderPeerAdapter.notifyItemRemoved(getAdapterPosition());
//                notifyItemRangeChanged(getAdapterPosition(), peerList.size());
            });
        }
    }
}
