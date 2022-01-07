package com.journey;

import android.media.Ringtone;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

/**
 * @Description:
 * @author: Guowen Liu
 * @Email: liu.guowen@outlook.com
 * @date: 2022-01-07-18:36
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class IndexActivity extends AppCompatActivity {


    private BottomNavigationView navView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_layout);
        navView = findViewById(R.id.nav_view);
    }
}
