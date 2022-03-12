package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.journey.RegisterActivity;
import com.journey.entity.User;
import com.journey.service.database.UserDb;
import com.journey.LoginActivity;
import com.journey.R;
import android.app.DatePickerDialog;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.journey.entity.User;
import com.journey.service.database.UserDb;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InformationActivity extends AppCompatActivity {
    private Button submit;
    private Button birthday;
    private Spinner gender;
    private EditText phone;
    private EditText username;
    private EditText email;
    private EditText mark;

    private FirebaseAuth mAuth;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void init() {
        submit = (Button) findViewById(R.id.submit);
        birthday = (Button) findViewById(R.id.birthday);
        gender = (Spinner) findViewById(R.id.gender);
        phone = (EditText) findViewById(R.id.phone);
        username = (EditText) findViewById(R.id.UserName);
        email = (EditText) findViewById(R.id.email);
        email.setEnabled(false);
        mark = (EditText) findViewById(R.id.mark);
        mark.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_information);
        mAuth = FirebaseAuth.getInstance();
        onStart();
        init();
        readData();
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDlg();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_birthDate = birthday.getText().toString();
                String txt_gender = gender.getSelectedItem().toString();
                String txt_phone = phone.getText().toString();
                String txt_username = username.getText().toString();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    String userEmail = currentUser.getEmail();
                    db.collection("users")
                            .whereEqualTo("email", userEmail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            db.collection("users").document(document.getId())
                                                    .update(
                                                            "birthDate", txt_birthDate,
                                                            "gender", txt_gender,
                                                            "phone", txt_phone,
                                                            "username", txt_username
                                                    );
                                            // After change the information of user, save them in file
                                            DocumentReference documentReference = db.collection("users").document(document.getId());
                                            String jsonMsg = JSON.toJSON(documentReference.toString()).toString();
                                            try {
                                                File fs = new File(android.os.Environment.getExternalStorageDirectory()+"/UserInformation.txt");
                                                FileOutputStream outputStream =new FileOutputStream(fs);
                                                outputStream.write(jsonMsg.getBytes());
                                                outputStream.flush();
                                                outputStream.close();
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                }
            }
        });
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
                System.out.println(jsonMsg);
                Map<String, Object> mp = JSON.parseObject(jsonMsg, new TypeReference<Map<String, Object>>(){});
                System.out.println(mp.get("birthDate"));
                birthday.setText(mp.get("birthDate").toString());
                gender.post(new Runnable(){
                    @Override
                    public void run(){
                        String gender_ = mp.get("gender").toString();
                        if(gender_.equals("Female")){
                            gender.setSelection(0);
                        }else{
                            gender.setSelection(1);
                        }
                    }
                });
                phone.setText(mp.get("phone").toString());
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

    @Override
    public void onStart() {
        super.onStart();
    }

    public void showDatePickDlg () {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(InformationActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                InformationActivity.this.birthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}