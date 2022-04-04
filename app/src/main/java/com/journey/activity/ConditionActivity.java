package com.journey.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.journey.adapter.ReqResApi;
import com.journey.model.ConditionInfo;
import com.journey.model.Peer;
import com.journey.service.database.DialogueHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
    private TextInputEditText preferGender;
    private TextInputEditText minAge;
    private TextInputEditText maxAge;
    private TextInputEditText score;

    private static final int GET_PLACE_INFORMATION = 485;
    private Calendar calendar;
    private Button submit;
    String[] conditionGenders;
    private ArrayAdapter<String> adapterItems;
    private static final String TAG ="postRequestActivity" ;
    private static final String ORIGIN_LOCATION = "0";
    private static final String END_LOCATION = "1";
    //gender radio group
    private RadioGroup radioGroup;
    private RadioButton male;
    private RadioButton female;
    private RadioButton other;
    final String[] gender = new String[1];
    //mode radio group
    private RadioGroup modeRadioGroup;
    private RadioButton carRadio;
    private RadioButton bikeRadio;
    private RadioButton walkingRadio;
    final String[] mode = new String[1];

    AwesomeValidation awesomeValidation;
    //location contains latitude and longitude
    List<Double> location = new ArrayList<Double>();
    //condition form key
    public final static String CONDITION_INFO = "CONDITION_INFO";
    private String choose_date;
    private String origin_address;
    private String end_address;
    private String prefer_gender;
    private String min_age;
    private String max_age;
    private String min_score;
    private String origin_lon;
    private String origin_lat;
    private String end_lon;
    private String end_lat;
    private String startAddress;
    private String destination;
    private String journeyMode;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.137:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void init(){
        chooseDateTime = (TextInputEditText)findViewById(R.id.choose_date_time_dt);
        originPlace = (TextInputEditText)findViewById(R.id.origin_address_dt);
        endPlace = (TextInputEditText)findViewById(R.id.end_address_dt);
        preferGender = (TextInputEditText) findViewById(R.id.prefer_gender_dt);
        minAge = (TextInputEditText)findViewById(R.id.min_age_dt);
        maxAge = (TextInputEditText)findViewById(R.id.max_age_dt);
        score = (TextInputEditText)findViewById(R.id.score_dt);

        calendar = Calendar.getInstance();
        submit = findViewById(R.id.submit_btn);
        conditionGenders = getResources().getStringArray(R.array.condition_gender);
        adapterItems = new ArrayAdapter<>(ConditionActivity.this,
                R.layout.gender_dropdown_item,
                conditionGenders);

        //initialize validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //preferGender radio group
        radioGroup = (RadioGroup) findViewById(R.id.gender_group);
        male = (RadioButton) findViewById(R.id.male_radio);
        female = (RadioButton) findViewById(R.id.female_radio);
        other = (RadioButton) findViewById(R.id.other_radio);
        //mode radio group
        modeRadioGroup = (RadioGroup) findViewById(R.id.mode_radio_group);
        carRadio = (RadioButton) findViewById(R.id.car_radio);
        bikeRadio = (RadioButton) findViewById(R.id.bike_radio);
        walkingRadio = (RadioButton) findViewById(R.id.walking_radio);
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
        //gender listener
        genderListener();
        //mode listener
        modeListener();
        //submit listener
        submitConditionData();
    }
    private void submitConditionData() {
        submit.setOnClickListener(view -> {
            sendConInfo();
//            leaderSelection();
//            testNavigationActivity();
        });
    }

    /**
     *@className: TEST
     *@desc:
     *@author: Guowen Liu
     *@date: 2022/3/12 14:45
     */
    private void testNavigationActivity(){
        String testJson  = "[{\n" +
                " \"email\": \"11@qq.com\",\n" +
                " \"gender\": 1,\n" +
                " \"age\": 29,\n" +
                " \"score\": 4.1,\n" +
                " \"longitude\": 53.13424,\n" +
                " \"latitude\": -6.13929,\n" +
                " \"dLongtitude\": 53.15922,\n" +
                " \"dLatitude\": -6.1012,\n" +
                " \"startTime\": 2193932002,\n" +
                " \"endTime\": 2196662719,\n" +
                " \"limit\": 5,\n" +
                " \"isLeader\": true\n" +
                "}, {\n" +
                " \"email\": \"11@qq.com\",\n" +
                " \"gender\": 1,\n" +
                " \"age\": 29,\n" +
                " \"score\": 4.1,\n" +
                " \"longitude\": 53.13424,\n" +
                " \"latitude\": -6.13929,\n" +
                " \"dLongtitude\": 53.15922,\n" +
                " \"dLatitude\": -6.1012,\n" +
                " \"startTime\": 2193932002,\n" +
                " \"endTime\": 2196662719,\n" +
                " \"limit\": 5,\n" +
                " \"isLeader\": false\n" +
                "}]";

        Intent conInfo = new Intent(this, NavigationActivity.class);
        conInfo.putExtra("list", testJson);
        startActivity(conInfo);
    }
    /**
     *@desc: send the information into realTimeJourneyTable
     *@author: Congqin yan
     *@date: 2022/3/7 19:05
     */
    private void getText(){
        choose_date = chooseDateTime.getText().toString().trim();
        origin_address = originPlace.getText().toString().trim();
        end_address = endPlace.getText().toString().trim();
        prefer_gender = gender[0];
        journeyMode = mode[0];
        min_age = minAge.getText().toString().trim();
        max_age = maxAge.getText().toString().trim();
        min_score = score.getText().toString().trim();
        origin_lon = location.get(1).toString();
        origin_lat = location.get(0).toString();
        end_lon = location.get(3).toString();
        end_lat = location.get(2).toString();
    }
    private void sendConInfo(){
        infoChecker();
        Intent conInfo = new Intent(this, RealTimeJourneyTableActivity.class);
        if(awesomeValidation.validate()){
            getText();
            ConditionInfo conditionInfo = new ConditionInfo(choose_date,origin_address,
                    end_address,prefer_gender,min_age,max_age,
                    min_score,origin_lon,origin_lat,end_lon,end_lat,startAddress,destination,journeyMode);
            //send serialized conditionInfo to real time activity
            conInfo.putExtra(CONDITION_INFO,conditionInfo);
            startActivityForResult(conInfo,1);
            //startActivity(conInfo);
        }else if(!male.isChecked() && !female.isChecked() && !other.isChecked()){
            Toast.makeText(ConditionActivity.this,"please select gender",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please complete your information!", Toast.LENGTH_SHORT).show();
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
    public boolean genderAgeScoreChecker(){
        if(!male.isChecked() && !female.isChecked() && !other.isChecked()){
            Toast.makeText(ConditionActivity.this,"please select gender",Toast.LENGTH_SHORT).show();
            return false;
        }else if(Integer.valueOf(min_age) >= Integer.valueOf(max_age)){
            Toast.makeText(ConditionActivity.this,"minimum age is bigger than maximum age",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
    //sent maker to select location activity to identify if it's a originAddress or endAddress
    private Intent setLocationMarker(String locationMarker)
    {
        Intent intent = new Intent(this, SelectLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putCharSequence(getString(R.string.locationMarker),locationMarker);
        intent.putExtras(bundle);
        return intent;
    }
    private String genderListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
                switch (checkedButtonId){
                    case R.id.male_radio:
                        Toast.makeText(ConditionActivity.this,"select male",Toast.LENGTH_SHORT).show();
                        gender[0] = male.getText().toString();
                        break;
                    case R.id.female_radio:
                        Toast.makeText(ConditionActivity.this,"select female",Toast.LENGTH_SHORT).show();
                        gender[0] = female.getText().toString();
                        break;
                    case R.id.other_radio:
                        Toast.makeText(ConditionActivity.this,"select other",Toast.LENGTH_SHORT).show();
                        gender[0] = other.getText().toString();
                        break;
                }
            }
        });
        radioGroup.clearCheck();
        return gender[0];
    }
    private String modeListener(){
        modeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
                switch (checkedButtonId){
                    case R.id.car_radio:
                        Toast.makeText(ConditionActivity.this,"Car mode",Toast.LENGTH_SHORT).show();
                        mode[0] = carRadio.getText().toString();
                        break;
                    case R.id.bike_radio:
                        Toast.makeText(ConditionActivity.this,"Bike mode",Toast.LENGTH_SHORT).show();
                        mode[0] = bikeRadio.getText().toString();
                        break;
                    case R.id.walking_radio:
                        Toast.makeText(ConditionActivity.this,"Walking mode",Toast.LENGTH_SHORT).show();
                        mode[0] = walkingRadio.getText().toString();
                        break;
                }
            }
        });
        radioGroup.clearCheck();
        return gender[0];
    }
    /**
     *@desc:Get the location information in map
     *@author: Guowen Liu
     *@date: 2022/3/3 11:31
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -2)
        {
            finish();
        }
        if (resultCode == RESULT_OK && requestCode == GET_PLACE_INFORMATION) {
            String placeName = this.getString(R.string.placeName);
            String latitude = this.getString(R.string.latitude);
            String longitude = this.getString(R.string.longitude);
            String locationMarker = this.getString(R.string.locationMarker);
            //Get place name
            if (data.hasExtra(placeName)) {

                if(data.getExtras().getString(locationMarker).equals(ORIGIN_LOCATION))
                {
                    startAddress = data.getExtras().getString(placeName);
                    originPlace.setText(startAddress);
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
                    destination = data.getExtras().getString(placeName);
                    endPlace.setText(destination);
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
