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
import com.journey.model.DailyInfo;
import com.journey.model.Peer;

import java.util.List;

public class DailyJourneyCardAdapter extends RecyclerView.Adapter<DailyJourneyCardAdapter.DailyViewHolder> {

    List<DailyInfo> dailyInfoList;
    Context context;
    public DailyJourneyCardAdapter(Context context , List<DailyInfo> dailyInfoList){
        this.context = context;
        this.dailyInfoList = dailyInfoList;
    }
    @NonNull
    @Override
    public DailyJourneyCardAdapter.DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_journey_card, parent , false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        DailyInfo dailyInfo = dailyInfoList.get(position);
        holder.email.setText(dailyInfo.getEmail());
        holder.date.setText(dailyInfo.getDateTime());
        holder.oAddress.setText(dailyInfo.getOriginAddress());
        holder.eAddress.setText(dailyInfo.getEndAddress());
        holder.gender.setText(dailyInfo.getPreferGender());
        holder.min_age.setText(dailyInfo.getMinAge());
        holder.max_age.setText(dailyInfo.getMaxAge());
        holder.score.setText(dailyInfo.getMinScore());
        holder.mode.setText(dailyInfo.getJourneyMode());
    }

    @Override
    public int getItemCount() {
        return dailyInfoList.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView email, date, oAddress, eAddress,gender, min_age,max_age, score, mode;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.daily_userName_tv);
            date = itemView.findViewById(R.id.daily_datetime_tv);
            oAddress = itemView.findViewById(R.id.daily_oAddress_tv);
            eAddress = itemView.findViewById(R.id.daily_eAddress_tv);
            gender = itemView.findViewById(R.id.daily_gender_tv);
            min_age = itemView.findViewById(R.id.daily_min_age_tv);
            max_age = itemView.findViewById(R.id.daily_max_age_tv);
            score = itemView.findViewById(R.id.daily_score_tv);
            mode = itemView.findViewById(R.id.daily_mode_tv);
        }
//        public interface OnItemListener{
//            void onItemClick(int position);
//        }
        @Override
        public void onClick(View view) {

        }
    }
}

