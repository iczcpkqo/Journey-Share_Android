package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.R;
import com.journey.adapter.RatingItemAdapter;
import com.journey.entity.Rating;
import com.journey.entity.Record;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.Peer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;



public class RateComActivity extends AppCompatActivity {

    ArrayList<Rating> ratingTarget;
    RatingItemAdapter rid;
    Handler mainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what == 1)
            {
                Map<String, Object> data = (Map<String, Object>) message.obj;
                Long orderNumber = (Long) data.get("order");
                orderNumber++;
                long mark = (long) data.get("mark");

                Rating rt = findRating((String) data.get("email"));
                double currentMark = rt.getRating();
                String uuid = (String) data.get("uuid");

                currentMark = ((orderNumber*mark)+currentMark)/(orderNumber+1);

                currentMark = (double) Math.round(currentMark * 100) / 100;
                FirebaseOperation.updata("users",uuid,"mark",currentMark);
                FirebaseOperation.updata("users",uuid,"order",orderNumber);
                FirebaseOperation.updata("users",uuid,"uuid",uuid);
            }
            return false;
        }
    });

    private  Rating findRating(String email)
    {
        Rating rt = null;
        Iterator<Rating> iter = ratingTarget.iterator();
        while (iter.hasNext()) {
            rt = iter.next();
            if (rt.getTo().equals(email)){
                break;
            }
        };
        return  rt;
    }
    public void ratingChangeHandler(RatingBar view){

        String target = view.getTag().toString();

        for (Rating rating:ratingTarget){
            if (rating.getTo() == target){
                rating.setRating((double) view.getRating());
                Log.d("rating",rating.toString());
            }
        }

        // once any change is made, turn submit btn to clickable
        Button button = (Button) findViewById(R.id.submitRatingBtn);
        button.setEnabled(true);
    }

    private void submitRating(){
        Button button = (Button) findViewById(R.id.submitRatingBtn);
        button.setEnabled(false);
        button.setText("Sending your Rating");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (Rating rating:ratingTarget){
            if (rating.isRated()) continue;
            db.collection("rating")
                    .add(rating.toHashmap())
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Log.d("rating","Document written");
                        }
                    });
            rating.setRated(true);

        }
        rid.notifyDataSetChanged();
        button.setText("Rated");

        Iterator<Rating> iter = ratingTarget.iterator();
        while (iter.hasNext()) {
            Rating currentRating = iter.next();

            FirebaseOperation.fuzzyQueriesToData("users","email",currentRating.getTo(),mainHandler);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Intent intent = getIntent();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ratingTarget = new ArrayList<>();
        String targetRecordID = intent.getStringExtra("ratingRecordID");
        TextView ratingRecord = (TextView) findViewById(R.id.recordIDtextview);
        ratingRecord.setText(targetRecordID);

        setTitle(targetRecordID);

        ListView ratingList = (ListView) findViewById(R.id.ratingList);
        rid = new RatingItemAdapter(this,ratingTarget);
        ratingList.setAdapter(rid);
        Button submitBtn = (Button) findViewById(R.id.submitRatingBtn);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("record").document(targetRecordID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Log.d("intent", String.valueOf(document.exists()));
                Map<String, Object> data = document.getData();
                String companionList = (String) data.get("companion");
                for (String retval: companionList.split(";")){

                    db.collection("rating")
                            .whereEqualTo("from",currentUser.getEmail())
                            .whereEqualTo("to",retval)
                            .whereEqualTo("record",targetRecordID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                Rating tmp = new Rating(targetRecordID,currentUser.getEmail(),retval,5,true);
                                Log.d("rating", "onComplete: querySnapshot.isEmpty()"+querySnapshot.isEmpty());
                                if(querySnapshot.isEmpty()){
                                    tmp.setRated(false);
                                }else{
                                    for (QueryDocumentSnapshot doc:querySnapshot){
                                        tmp.setRating(doc.getDouble("rating"));
                                    }
                                }
                                rid.add(tmp);
                                submitBtn.setEnabled(false);
                            }
                        }
                    });

                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRating();
            }
        });
        submitBtn.setEnabled(false);




    }
}