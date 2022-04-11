package com.journey.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.activity.ConditionActivity;
import com.journey.activity.RealTimeJourneyTableActivity;
import com.journey.model.ConditionInfo;
import com.journey.model.DailyInfo;
import com.journey.model.Peer;

import java.util.List;

public class DailyJourneyCardAdapter extends RecyclerView.Adapter<DailyJourneyCardAdapter.DailyViewHolder>{
    private static final String TAG = "adapter";
    private OnDailyItemListener monDailyItemListener;
    List<ConditionInfo> conList;
    Context context;
    public DailyJourneyCardAdapter(Context context , List<ConditionInfo> cList, OnDailyItemListener onDailyItemListener){
        this.context = context;
        this.conList = cList;
        //receive the on daily item listener from DailyJourneyTableActivity
        this.monDailyItemListener = onDailyItemListener;
    }
    @NonNull
    @Override
    public DailyJourneyCardAdapter.DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_journey_card, parent , false);
        // globe monDailyItemListener is passing to Daily view holder
        return new DailyViewHolder(view,monDailyItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        ConditionInfo conditionInfo = conList.get(position);
        holder.email.setText(conditionInfo.getUserEmail());
        holder.date.setText(conditionInfo.getDateTime());
        holder.oAddress.setText("From :" + conditionInfo.getOriginAddress());
        holder.eAddress.setText("To :" + conditionInfo.getEndAddress());
        holder.mode.setText("Mode :" + conditionInfo.getJourneyMode());
    }

    @Override
    public int getItemCount() {
        return conList.size();
    }

    // view holder class to render each holder
    public class DailyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView email, date, oAddress, eAddress, mode;
        Button dailyDelete;
        OnDailyItemListener onDailyItemListener;
        private DailyJourneyCardAdapter dailyJourneyCardAdapter;
        public DailyViewHolder(@NonNull View itemView, OnDailyItemListener onDailyItemListener) {
            super(itemView);
            this.onDailyItemListener = onDailyItemListener;
            email = itemView.findViewById(R.id.daily_userName_tv);
            date = itemView.findViewById(R.id.daily_datetime_tv);
            oAddress = itemView.findViewById(R.id.daily_oAddress_tv);
            eAddress = itemView.findViewById(R.id.daily_eAddress_tv);
            mode = itemView.findViewById(R.id.daily_mode_tv);
            // once the item view interface was clicked, the unknown listener interface calls
            // the onClick method and it pass the adapter position
            itemView.setOnClickListener(this);
            dailyDelete = itemView.findViewById(R.id.daily_table_btn);
        }

        @Override
        public void onClick(View view) {
//            Log.d(TAG,"onclick" + getAdapterPosition());
            onDailyItemListener.onDailyItemClick(getAdapterPosition());
        }
    }
    public interface OnDailyItemListener{
        void onDailyItemClick(int position);
    }
}

