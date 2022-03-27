package com.journey;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.activity.JourneyActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    private Button login_in;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    public void init() {
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login_in = (Button) findViewById(R.id.login_in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        init();
        login_in.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_in:
                String txt_email = username.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                }else{
                    signIn(txt_email,txt_password);
                }
                break;
//            case R.id.login:
//                Intent intent_login = new Intent(this, LoginActivity.class);
//                startActivity(intent_login);
            case R.id.register:
                Intent intent_register = new Intent(this, RegisterActivity.class);
                startActivity(intent_register);
            default:
                break;
        }
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if(currentUser!=null){
                                String userEmail = currentUser.getEmail();
                                saveUserInformation(userEmail);
                            }
                            updateUI(currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Please check the email address and password.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, JourneyActivity.class));
        }
    }

    private void saveUserInformation(String userEmail) {
        if(userEmail != null){
            System.out.println(userEmail);
            db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String jsonMsg = JSON.toJSON(document.getData()).toString();
                                    try {
                                        File fs = new File(android.os.Environment.getExternalStorageDirectory()+"/UserInformation.txt");
                                        FileOutputStream outputStream =new FileOutputStream(fs, false);
                                        outputStream.write(jsonMsg.getBytes());
                                        outputStream.flush();
                                        outputStream.close();
                                        Toast.makeText(getBaseContext(), "File created successfully", Toast.LENGTH_LONG).show();
                                    } catch (FileNotFoundException e) {
                                        Toast.makeText(getBaseContext(), "Please turn on folder permissions for this application.", Toast.LENGTH_LONG).show();
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

}