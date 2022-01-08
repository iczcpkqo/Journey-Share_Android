package com.journey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.entity.User;
import com.journey.service.UserDb;

import java.util.Date;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    private Button login_in;
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();
        init();
        login_in.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_in:
                User user = new User(username.getText().toString(),password.getText().toString(),new Date());
                String result = UserDb.getInstance().save(user);
                if("failed".equals(result)){
                    Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "successful", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login:
                Intent intent_login = new Intent(this, Login.class);
                startActivity(intent_login);
            case R.id.register:
                Intent intent_register = new Intent(this, Register.class);
                startActivity(intent_register);
            default:
                break;
        }
    }
}