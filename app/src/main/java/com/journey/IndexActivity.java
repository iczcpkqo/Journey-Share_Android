package com.journey;

import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
    private Button signout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_layout);
        navView = findViewById(R.id.nav_view);
        signout = findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(IndexActivity.this, LoginActivity.class));
            }
        });

    }
}
