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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.journey.R;
import com.journey.adapter.JSONPlaceholder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-01-17-11:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class ConditionActivity extends AppCompatActivity {
    private TextInputEditText chooseDateTime;
    private TextInputEditText originPlace;
    private TextInputEditText endPlace;
    private TextInputEditText minAge;
    private TextInputEditText maxAge;
    private TextInputEditText score;

    private static final int GET_PLACE_INFORMATION = 485;
    private Calendar calendar;
    private Button submit;
    private AutoCompleteTextView autoCompleteGender;
    String[] conditionGenders;
    private ArrayAdapter<String> adapterItems;
    private static final String TAG ="postRequestActivity" ;
    private static final String ORIGIN_LOCATION = "0";
    private static final String END_LOCATION = "1";
    JSONPlaceholder jsonPlaceholder;
    AwesomeValidation awesomeValidation;
    //location contains latitude and longitude
    List<Double> location = new ArrayList<Double>();


    public  void init(){
        chooseDateTime = (TextInputEditText)findViewById(R.id.choose_date_time_dt);
        originPlace = (TextInputEditText)findViewById(R.id.origin_address_dt);
        endPlace = (TextInputEditText)findViewById(R.id.end_address_dt);
        minAge = (TextInputEditText)findViewById(R.id.min_age_dt);
        maxAge = (TextInputEditText)findViewById(R.id.max_age_dt);
        score = (TextInputEditText)findViewById(R.id.score_dt);

        calendar = Calendar.getInstance();
        submit = findViewById(R.id.submit_btn);
        autoCompleteGender = findViewById(R.id.auto_complete_gender);
        conditionGenders = getResources().getStringArray(R.array.condition_gender);
        adapterItems = new ArrayAdapter<>(ConditionActivity.this,
                R.layout.gender_dropdown_item,
                conditionGenders);

        //initialize validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
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
            getConInfo();
        });
    }
    /**
     *@desc: send the information into realTimeJourneyTable
     *@author: Congqin yan
     *@date: 2022/3/7 19:05
     */
    private void getConInfo(){
        infoChecker();
        Intent conInfo = new Intent(this, RealTimeJourneyTableActivity.class);
        if(awesomeValidation.validate()){

            conInfo.putExtra("datetime", chooseDateTime.getText().toString());
            conInfo.putExtra("originAddress", originPlace.getText().toString());
            conInfo.putExtra("endAddress", endPlace.getText().toString());
            conInfo.putExtra("gender", autoCompleteGender.getText().toString());
            conInfo.putExtra("minAge", minAge.getText().toString());
            conInfo.putExtra("maxAge", maxAge.getText().toString());
            conInfo.putExtra("score", score.getText().toString());
            conInfo.putExtra("origin_lat",location.get(0).toString());
            conInfo.putExtra("origin_lon",location.get(1).toString());
            conInfo.putExtra("end_lat",location.get(2).toString());
            conInfo.putExtra("end_lon",location.get(3).toString());
            startActivity(conInfo);
        }else{
            Toast.makeText(this, "Validation Failed, Please check your information!", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     *@desc: check the user input fields
     *@author: Congqin yan
     *@date: 2022/3/7 19:05
     */
    private void infoChecker(){
        //add validation for data time
        awesomeValidation.addValidation(this,R.id.choose_date_time_dt,
                RegexTemplate.NOT_EMPTY,R.string.invalid_datetime);
        //add validation for origin address
        awesomeValidation.addValidation(this,R.id.origin_address_dt,
                RegexTemplate.NOT_EMPTY,R.string.invalid_origin_address);
        //add validation for end address
        awesomeValidation.addValidation(this,R.id.end_address_dt,
                RegexTemplate.NOT_EMPTY,R.string.invalid_end_address);
        //add validation for gender
        awesomeValidation.addValidation(this,R.id.auto_complete_gender,
                RegexTemplate.NOT_EMPTY,R.string.invalid_gender);
        //add validation for min age
        awesomeValidation.addValidation(this,R.id.min_age_dt,
                RegexTemplate.NOT_EMPTY,R.string.invalid_min_age);
        //add validation for max age
        awesomeValidation.addValidation(this,R.id.max_age_dt,
                RegexTemplate.NOT_EMPTY,R.string.invalid_max_age);
        //add validation for score
        awesomeValidation.addValidation(this,R.id.score_dt,
                RegexTemplate.NOT_EMPTY,R.string.invalid_min_score);
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
        originPlace.setOnClickListener(view -> openOriginLocationActivity());
    }

    //set end place onclick listener
    private void setEndPlaceListener(EditText originPlace) {
        originPlace.setOnClickListener(view -> openEndLocationActivity());
    }

    // open origin place map
    private void openOriginLocationActivity() {

        startActivityForResult(setLocationMarker(ORIGIN_LOCATION), GET_PLACE_INFORMATION);
    }

    // open end place map
    private void openEndLocationActivity() {

        startActivityForResult(setLocationMarker(END_LOCATION), GET_PLACE_INFORMATION);
    }

    private Intent setLocationMarker(String locationMarker)
    {
        Intent intent = new Intent(this, SelectLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putCharSequence(getString(R.string.locationMarker),locationMarker);
        intent.putExtras(bundle);
        return intent;
    }
    /**
     *@desc:Get the location information in map
     *@author: Guowen Liu
     *@date: 2022/3/3 11:31
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GET_PLACE_INFORMATION) {
            String placeName = this.getString(R.string.placeName);
            String latitude = this.getString(R.string.latitude);
            String longitude = this.getString(R.string.longitude);
            String locationMarker = this.getString(R.string.locationMarker);
            //Get place name
            if (data.hasExtra(placeName)) {
                String address = data.getExtras().getString(placeName);

                if(data.getExtras().getString(locationMarker).equals(ORIGIN_LOCATION))
                {
                    originPlace.setText(address);
                    //Get place Longitude and latitude
                    if (data.hasExtra(latitude) && data.hasExtra(longitude)) {
                        double origin_lat = data.getExtras().getDouble(latitude);
                        double origin_lon = data.getExtras().getDouble(longitude);
                        location.add(origin_lat);
                        location.add(origin_lon);
                    }
                }
                else if(data.getExtras().getString(locationMarker).equals(END_LOCATION))
                {
                    endPlace.setText(address);
                    //Get place Longitude and latitude
                    if (data.hasExtra(latitude) && data.hasExtra(longitude)) {
                        double end_lat = data.getExtras().getDouble(latitude);
                        double end_lon = data.getExtras().getDouble(longitude);
                        location.add(end_lat);
                        location.add(end_lon);
                    }
                }
            }
        }
//        else if (resultCode == RESULT_CANCELED)
//        {
            // No results
//        }
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
