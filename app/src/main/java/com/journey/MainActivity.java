package com.journey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.entity.User;
import com.journey.service.database.UserDb;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button save;
    private Button update;
    private Button delete;
    private Button index;
    private FirebaseFirestore db;

    public void init() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        save = (Button) findViewById(R.id.save);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        index = (Button) findViewById(R.id.index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        init();
        save.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        index.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.save:
//                User user = new User(username.getText().toString(),password.getText().toString(),new Date());
//                String result = UserDb.getInstance().save(user);
//                if("failed".equals(result)){
//                    Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(this, "successful", Toast.LENGTH_LONG).show();
//                }
//                break;
//            case R.id.update:
//                User user2 = new User(username.getText().toString(),password.getText().toString(),new Date());
//                String result2 = UserDb.getInstance().updateByDocumentId(user2,"G5V5CALe1CoDW2sWJjQg");
//                Toast.makeText(this, result2, Toast.LENGTH_LONG).show();
//                break;
            case R.id.index:
                startActivity(new Intent(this,IndexActivity.class));
                break;
            default:
                break;
        }
    }
}