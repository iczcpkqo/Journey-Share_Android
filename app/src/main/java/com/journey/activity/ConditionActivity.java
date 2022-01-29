package com.journey.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.journey.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConditionActivity extends AppCompatActivity {
    private EditText chooseDateTime;
    private EditText originPlace;
    private EditText endPlace;
    private Calendar calendar;
    private AutoCompleteTextView autoCompleteGender;
    String[] conditionGenders;
    private ArrayAdapter<String> adapterItems;

    public  void init(){
        chooseDateTime = findViewById(R.id.choose_date_time);
        originPlace = findViewById(R.id.origin_edit);
        endPlace = findViewById(R.id.end_edit);
        calendar = Calendar.getInstance();
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
        Intent intent = new Intent(this, DemoActivity.class);
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