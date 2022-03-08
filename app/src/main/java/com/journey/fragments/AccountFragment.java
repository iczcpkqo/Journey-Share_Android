package com.journey.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journey.LoginActivity;
import com.journey.R;
import com.journey.RegisterActivity;
import com.journey.activity.ConditionActivity;
import com.journey.activity.InformationActivity;
import com.journey.fragments.journeyModeFragments.DailyFragment;


public class AccountFragment extends Fragment {
    private Button information_container_fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        information_container_fragment = view.findViewById(R.id.information_container_fragment);
        information_container_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}