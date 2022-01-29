package com.journey.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journey.R;
import com.journey.fragments.AccountFragment;
import com.journey.fragments.JourneyFragment;
import com.journey.fragments.MessageFragment;
import com.journey.fragments.RecordFragment;

public class JourneyActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //setting journey fragment as main fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment,new JourneyFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.journeyFragment);

        //items selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }
    // navigate to different fragment
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId())
            {
                case R.id.recordFragment:
                    selectedFragment = new RecordFragment();
                    break;
                case R.id.journeyFragment:
                    selectedFragment = new JourneyFragment();
                    break;
                case R.id.messageFragment:
                    selectedFragment = new MessageFragment();
                    break;
                case R.id.accountFragment:
                    selectedFragment = new AccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, selectedFragment).commit();

            return true;
        }
    };
}