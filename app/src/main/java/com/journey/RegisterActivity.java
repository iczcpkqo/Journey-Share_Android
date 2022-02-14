package com.journey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

import android.widget.DatePicker;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.journey.entity.User;
import com.journey.service.database.UserDb;


public class RegisterActivity extends AppCompatActivity {
    private Button back;
    private Button signup;
    private Button datepicker;
    private Spinner gender;
    private EditText phone;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirm;

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;

    public void init() {
        back = (Button) findViewById(R.id.back);
        signup = (Button) findViewById(R.id.signup);
        datepicker = (Button) findViewById(R.id.datepicker);
        gender = (Spinner) findViewById(R.id.gender);
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
        mAuth = FirebaseAuth.getInstance();
        init();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prime key:email
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_birthDate = datepicker.getText().toString();
                String txt_gender = gender.getSelectedItem().toString();
                String txt_phone = phone.getText().toString();
                String txt_username = username.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                }else{
                    createAccount(txt_username,txt_password,txt_birthDate,txt_gender,txt_phone,txt_email);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                RegisterActivity.this.datepicker.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    private void createAccount(String username, String password,String birthDate,String gender,String phone,String email) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            User user1 = new User(username,  password, new Date(),  birthDate, gender,  phone,  email);
                            String result = UserDb.getInstance().save(user1);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}
