package com.journey.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.LoginActivity;
import com.journey.R;
import com.journey.RegisterActivity;
import com.journey.activity.ConditionActivity;
import com.journey.activity.InformationActivity;
import com.journey.fragments.journeyModeFragments.DailyFragment;
import com.journey.service.database.DialogueHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


public class AccountFragment extends Fragment {
    private Button modify;
    private ImageView img;
    private EditText username;
    private TextView email;
    private EditText phone;
    private TextView mark;
    private TextView gender;
    private TextView age;
    private TextView birthday;
    private Button logout;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    String[] month_list = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        modify = view.findViewById(R.id.Modify);
        img = view.findViewById(R.id.imageIV);
        username = view.findViewById(R.id.Username);
        email = view.findViewById(R.id.Email);
        phone = view.findViewById(R.id.Phone);
        mark = view.findViewById(R.id.Mark);
        gender = view.findViewById(R.id.Gender);
        age = view.findViewById(R.id.Age);
        birthday = view.findViewById(R.id.Birthday);
        logout = view.findViewById(R.id.Logout);
        readData();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent_login = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent_login);
            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_Phone = phone.getText().toString();
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
                                                            "username", txt_username,
                                                            "phone",txt_Phone
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
                phone.setText(mp.get("phone").toString());
                gender.setText(mp.get("gender").toString());
                age.setText(mp.get("age").toString());
                img.setImageResource(DialogueHelper.getHeadCupboard().get(Integer.parseInt(mp.get("headImg").toString())));
                String original_birthday = mp.get("birthDate").toString();
                Integer index = Integer.valueOf(original_birthday.substring(0, original_birthday.indexOf("-")));
                birthday.setText(month_list[index - 1] + " " + original_birthday.substring(original_birthday.indexOf("-") + 1));
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