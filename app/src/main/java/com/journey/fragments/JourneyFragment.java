package com.journey.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journey.R;
import com.journey.fragments.journeyModeFragments.DailyFragment;
import com.journey.fragments.journeyModeFragments.RealTimeFragment;
import com.journey.service.database.DialogueHelper;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-01-17-11:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class JourneyFragment extends Fragment {

    BottomNavigationView modeNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DialogueHelper.saveDynamicIp();

        View view = inflater.inflate(R.layout.fragment_journey, container, false);

        modeNavigationView = view.findViewById(R.id.mode_navigation);
        //setting journey fragment as main fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.mode_container_fragment,new DailyFragment()).commit();

        modeNavigationView.setSelectedItemId(R.id.dailyFragment);

        //items selected listener
        modeNavigationView.setOnNavigationItemSelectedListener(navListener);
        return view;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId())
            {
                case R.id.dailyFragment:
                    selectedFragment = new DailyFragment();
                    break;
                case R.id.realTimeFragment:
                    selectedFragment = new RealTimeFragment();
                    break;
            }
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mode_container_fragment, selectedFragment).commit();

            return true;
        }
    };

}