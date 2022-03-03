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
/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-01-17-11:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class RealTimeFragment extends Fragment {

    private Button real_time_condition;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_time, container, false);

        real_time_condition = view.findViewById(R.id.real_time_condition_btn);
        conditionBtnListener();
        return view;
    }

    // realtime button listener
    private void conditionBtnListener() {
        real_time_condition.setOnClickListener(view -> moveToCondition());
    }
    //passing id to condition activity
    private void moveToCondition(){
        Intent intent = new Intent(getActivity(), ConditionActivity.class);
        intent.putExtra("id", 1);
        startActivity(intent);
    }
}