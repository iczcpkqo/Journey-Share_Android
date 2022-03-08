package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.entity.User;
import com.journey.service.database.UserDb;
import com.journey.LoginActivity;
import com.journey.R;

import java.util.Date;
import java.util.Map;

public class InformationActivity extends AppCompatActivity {
    private Button submit;
    private Button birthday;
    private Spinner gender;
    private EditText phone;
    private EditText username;
    private EditText email;
    private EditText password;
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
        password = (EditText) findViewById(R.id.password);
        mark = (EditText) findViewById(R.id.mark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_information);
        mAuth = FirebaseAuth.getInstance();
        onStart();
        init();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        System.out.println("ZHUyi" + id);
        if(currentUser != null){
            String email = currentUser.getEmail();
            DocumentReference docRef = db.collection("users").document("a4KxDl9mZJtNBzXDAteU");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            System.out.println(document.getData());
                        } else {
//                            Log.d(TAG, "No such document");
                        }
                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}