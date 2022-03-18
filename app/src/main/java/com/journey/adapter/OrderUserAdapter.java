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
import com.journey.map.OrderUser;

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
public class OrderUserAdapter extends RecyclerView.Adapter<OrderUserAdapter.PeerViewHolder> {

    List<OrderUser> orderUserList;
    Context context;
    public OrderUserAdapter(Context context , List<OrderUser> orders){
        this.context = context;
        orderUserList = orders;
    }
    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leader_member_card, parent , false);
        return new PeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
        OrderUser orderUser = orderUserList.get(position);
        holder.email.setText("Peer : " + orderUser.getEmailTag());
        holder.gender.setText("gender : " + orderUser.getGenderTag());
        holder.age.setText("age :" + orderUser.getAgeTag());
        holder.score.setText("score :" + orderUser.getScoreTag());
    }

    @Override
    public int getItemCount() {
        return orderUserList.size();
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
        }

        @Override
        public void onClick(View view) {

        }
    }
}
