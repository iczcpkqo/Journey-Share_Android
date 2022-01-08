package com.journey;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.entity.User;
import com.journey.service.UserDb;

import java.util.Calendar;
import java.util.Date;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.Button;
import android.widget.DatePicker;

public class Register extends AppCompatActivity {
    private Button login;
    private Button register;
    private Button signup;
    private Button datepicker;
    private Spinner gender;
    private EditText login_name;
    private EditText phone;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirm;
    private FirebaseFirestore db;

    public void init() {
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        signup = (Button) findViewById(R.id.signup);
        datepicker = (Button) findViewById(R.id.datepicker);
        gender = (Spinner) findViewById(R.id.gender);
        login_name = (EditText) findViewById(R.id.login_name);
        phone = (EditText) findViewById(R.id.phone);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register);
        db = FirebaseFirestore.getInstance();
        init();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(username.getText().toString(),password.getText().toString(),new Date());
                String result = UserDb.getInstance().save(user);
                if("failed".equals(result)){
                    Toast.makeText(Register.this, "failed", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(Register.this, "successful", Toast.LENGTH_LONG).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(Register.this, Login.class);
                startActivity(intent_login);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(Register.this, Register.class);
                startActivity(intent_login);
            }
        });
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDlg();
            }
        });
    }

    public void showDatePickDlg () {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Register.this.datepicker.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}
