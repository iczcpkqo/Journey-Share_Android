package com.journey.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.journey.R;
import com.journey.adapter.JSONPlaceholder;
import com.journey.adapter.PeerAdapter;
import com.journey.model.Peer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConditionActivity extends AppCompatActivity {
    private EditText chooseDateTime;
    private EditText originPlace;
    private EditText endPlace;
    private EditText minAge;
    private EditText maxAge;
    private EditText score;

    private Calendar calendar;
    private Button submit;
    private AutoCompleteTextView autoCompleteGender;
    String[] conditionGenders;
    private ArrayAdapter<String> adapterItems;
    private static final String TAG ="postRequestActivity" ;

    JSONPlaceholder jsonPlaceholder;

    public  void init(){
        chooseDateTime = findViewById(R.id.choose_date_time);
        originPlace = findViewById(R.id.origin_edit);
        endPlace = findViewById(R.id.end_edit);
        minAge = findViewById(R.id.min_age_edit);
        maxAge = findViewById(R.id.max_age_edit);
        score = findViewById(R.id.min_score_edit);

        calendar = Calendar.getInstance();
        submit = findViewById(R.id.submit_btn);
        autoCompleteGender = findViewById(R.id.auto_complete_gender);
        conditionGenders = getResources().getStringArray(R.array.condition_gender);
        adapterItems = new ArrayAdapter<>(ConditionActivity.this,
                R.layout.gender_dropdown_item,
                conditionGenders);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
         init();
        //condition bar
        setConditionActionBar();
        //daily and realtime mode select
        selectTimeModeForCondition(calendar,chooseDateTime);
        //origin place picker
        setOriginPlaceListener(originPlace);
        //end place picker
        setEndPlaceListener(endPlace);
        //gender dropdown
        setGenderDropDown();
        //submit listener
        submitConditionData();
    }
    private void submitConditionData() {
        submit.setOnClickListener(view -> {
            infoChecker();
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
        });
    }
    private void infoChecker(){
        if(!chooseDateTime.getText().toString().equals("")){
            String dateTime = chooseDateTime.getText().toString();
        }else {
            Toast.makeText(ConditionActivity.this, "Please Choose DateTime" , Toast.LENGTH_SHORT).show();
        }
        if(!originPlace.getText().toString().equals("")){
            String oPlace = originPlace.getText().toString();
        }else {
            Toast.makeText(ConditionActivity.this, "Please Choose Origin Place" , Toast.LENGTH_SHORT).show();
        }
        if(!endPlace.getText().toString().equals("")){
            String ePlace = endPlace.getText().toString();
        }else {
            Toast.makeText(ConditionActivity.this, "Please Choose End Place" , Toast.LENGTH_SHORT).show();
        }
        if(!autoCompleteGender.getText().toString().equals("")){
            String gender = autoCompleteGender.getText().toString();
        }else {
            Toast.makeText(ConditionActivity.this, "Please Choose the Gender" , Toast.LENGTH_SHORT).show();
        }
        if(!minAge.getText().toString().equals("")){
            String iAge = minAge.getText().toString();
        }else {
            Toast.makeText(ConditionActivity.this, "Please Choose the minimum age" , Toast.LENGTH_SHORT).show();
        }
        if(!maxAge.getText().toString().equals("")){
            String aAge = maxAge.getText().toString();
        }else {
            Toast.makeText(ConditionActivity.this, "Please Choose the maximum age" , Toast.LENGTH_SHORT).show();
        }
        if(!score.getText().toString().equals("")){
            String s = score.getText().toString();
        }else {
            Toast.makeText(ConditionActivity.this, "Please Choose the score" , Toast.LENGTH_SHORT).show();
        }
    }

    private void createPost(){
        //   Post post = new Post("18" , "First Title" , "First Text");
        Call<Peer> call = jsonPlaceholder.createPeer("13" , "Second Title" , "Second Text");
        call.enqueue(new Callback<Peer>() {
            @Override
            public void onResponse(Call<Peer> call, Response<Peer> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ConditionActivity.this, response.code() , Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Peer> postList = new ArrayList<>();
                postList.add(response.body());
                Toast.makeText(ConditionActivity.this, response.code() + " Response", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Peer> call, Throwable t) {
                Toast.makeText(ConditionActivity.this, t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setGenderDropDown() {
        autoCompleteGender.setAdapter(adapterItems);
        autoCompleteGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Gender " + item,Toast.LENGTH_SHORT).show();
            }
        });
    }

    // set the style for condition actionBar
    private void setConditionActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Journey Condition");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);//display the back arrow icon

    }

    // select time mode for condition form
    private void selectTimeModeForCondition(Calendar calendar, EditText chooseDateTime) {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        // realtime fragment send id = 1,shows the current time.daily fragment send id to 0, then show the time dialogs
        if(id == 1){
            setCurrentTime(calendar,chooseDateTime);
        }else if(id == 0){
            chooseDateTime.setOnClickListener(view -> showDateTimeDialog(calendar, chooseDateTime));
        }
    }

    //show the current time
    private void setCurrentTime(Calendar calendar,EditText chooseDateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
        chooseDateTime.setText(simpleDateFormat.format(calendar.getTime()));
    }

    // show date and time dialog listener
    private void showDateTimeDialog(Calendar calendar, EditText chooseDateTime) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                        chooseDateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(ConditionActivity.this, R.style.MyTimePickerDialogTheme,
                        timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(ConditionActivity.this, R.style.MyTimePickerDialogTheme,
                dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //set origin place onclick listener
    private void setOriginPlaceListener(EditText originPlace) {
        originPlace.setOnClickListener(view -> openLocationActivity());
    }

    //set end place onclick listener
    private void setEndPlaceListener(EditText originPlace) {
        originPlace.setOnClickListener(view -> openLocationActivity());
    }

    // open map
    private void openLocationActivity() {
        Intent intent = new Intent(this, SelectLocationActivity.class);
        startActivity(intent);
    }

    // back journey home listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
           finish();
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

}