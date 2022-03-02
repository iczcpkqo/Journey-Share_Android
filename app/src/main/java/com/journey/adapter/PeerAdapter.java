package com.journey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.model.Peer;

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
        holder.userId.setText("userId : " + peer.getUserId());
        holder.id.setText("id : " + peer.getId());
        holder.title.setText("title :" + peer.getTitle());
        holder.body.setText("body :" + peer.getBody());
    }

    @Override
    public int getItemCount() {
        return peerList.size();
    }

    public class PeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView userId, id, title, body;
        Button delete;

        public PeerViewHolder(@NonNull View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.user_id_tv);
            id = itemView.findViewById(R.id.id_tv);
            title = itemView.findViewById(R.id.title_tv);
            body = itemView.findViewById(R.id.body_tv);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
