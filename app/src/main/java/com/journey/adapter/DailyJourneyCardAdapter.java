package com.journey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.model.ConditionInfo;
import com.journey.model.Peer;

import java.util.List;

public class DailyJourneyCardAdapter extends RecyclerView.Adapter<DailyJourneyCardAdapter.DailyViewHolder> {

    List<ConditionInfo> conditionInfoList;
    Context context;
    public DailyJourneyCardAdapter(Context context , List<ConditionInfo> conditionInfoList){
        this.context = context;
        this.conditionInfoList = conditionInfoList;
    }
    @NonNull
    @Override
    public DailyJourneyCardAdapter.DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.follower_member_card, parent , false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        ConditionInfo conditionInfo = conditionInfoList.get(position);
        holder.email.setText(conditionInfo.getDateTime());
        holder.gender.setText("gender : " + conditionInfo.getStartAddress());
        holder.age.setText("age :" + conditionInfo.getDestination());
        holder.score.setText("score :" + conditionInfo.getMinScore());
    }

    @Override
    public int getItemCount() {
        return conditionInfoList.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView email, gender, age, score;
        ImageView imageView;

        public DailyViewHolder(@NonNull View itemView) {
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

