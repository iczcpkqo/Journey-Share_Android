package com.journey.fragments.journeyModeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.journey.R;
import com.journey.activity.ConditionActivity;
import com.journey.activity.DailyJourneyTableActivity;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-01-17-11:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class DailyFragment extends Fragment {

    private Button daily_journey_condition;
    private Button daily_table;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        daily_table = view.findViewById(R.id.daily_table_btn);
        daily_journey_condition = view.findViewById(R.id.daily_condition_btn);

        conditionBtnListener();
        dailyTableBtnListener();
        return view;
    }
    // daily button listener and passing the id to condition activity
    private void conditionBtnListener() {
        daily_journey_condition.setOnClickListener(view -> moveToCondition());
    }
    private void dailyTableBtnListener() {
        daily_table.setOnClickListener(view -> moveToDailyTable());
    }
    private void moveToCondition(){
        Intent intent = new Intent(getActivity(), ConditionActivity.class);
        intent.putExtra("id", 0);
        startActivity(intent);
    }
    private void moveToDailyTable(){
        Intent intent = new Intent(getActivity(), DailyJourneyTableActivity.class);
        intent.putExtra("id", 0);
        startActivity(intent);
    }
}