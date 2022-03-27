package com.journey.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.journey.LoginActivity;
import com.journey.R;
import com.journey.RegisterActivity;
import com.journey.activity.ConditionActivity;
import com.journey.activity.InformationActivity;
import com.journey.fragments.journeyModeFragments.DailyFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


public class AccountFragment extends Fragment {
    private Button information_container_fragment;
    private TextView username;
    private TextView email;
    private TextView mark;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        information_container_fragment = view.findViewById(R.id.information_container_fragment);
        username = view.findViewById(R.id.Username);
        email = view.findViewById(R.id.Email);
        mark = view.findViewById(R.id.Mark);
        readData();
        information_container_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void readData() {
        File file = new File(android.os.Environment.getExternalStorageDirectory()+"/UserInformation.txt");
        FileReader fis = null;
        BufferedReader br = null;
        if(file.exists()) {
            try {
                fis = new FileReader(file);
                br = new BufferedReader(fis);
                String str = br.readLine();
                String jsonMsg = "";
                while(str != null){
                    jsonMsg += str;
                    str = br.readLine();
                }
                Map<String, Object> mp = JSON.parseObject(jsonMsg, new TypeReference<Map<String, Object>>(){});
                username.setText(mp.get("username").toString());
                email.setText(mp.get("email").toString());
                mark.setText(mp.get("mark").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}