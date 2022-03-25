package com.journey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.entity.Record;
import com.journey.fragments.AccountFragment;
import com.journey.R;
import com.journey.fragments.JourneyFragment;
import com.journey.fragments.MessageFragment;
import com.journey.fragments.RecordFragment;
import com.journey.msg.message.Chat;


import java.util.Map;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-01-17-11:00
 * @Modify date and time: 12th March 2022
 * @Modified by: Xiang Mao
 * @Modified remark: Update Nav case of Message
 */
public class JourneyActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //setting journey fragment as main fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment,new JourneyFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.journeyFragment);

        //items selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }
    // navigate to different fragment
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId())
            {
                case R.id.recordFragment:
                    selectedFragment = new RecordFragment();
                    break;
                case R.id.journeyFragment:
                    selectedFragment = new JourneyFragment();
                    break;
                case R.id.messageFragment:
//              布局页面, 默认跳转: R.id.messageFragment xml, 我改了,跳Chat.xml了
//              功能页面, 默认应用: new MessageFragment(), 我改了暂时使用Chat了
//                    selectedFragment = new MessageFragment();
//                    selectedFragment = (Fragment)(new Chat());
//                    Chat activityChat = new Chat();
//                    activityChat.startActivity();

                    Intent intent = new Intent(JourneyActivity.this, Chat.class);
                    startActivity(intent);
                    return true;
//                    break;
                case R.id.accountFragment:
                    selectedFragment = new AccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, selectedFragment).commit();

            return true;
        }
    };
    public void showDetail(View view){
        Log.d("intent test","good!");
        Intent intent = new Intent(this, showDetail.class);
        String targetDocumentId = view.getTag().toString();
        intent.putExtra("targetID",targetDocumentId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("record").document(targetDocumentId);
        Log.d("intent",targetDocumentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Log.d("intent", String.valueOf(document.exists()));
                Map<String, Object> data = document.getData();
                Record tmp = new Record((String)document.getId(),(String)data.get("departure"),(String)data.get("arrival"),((Timestamp)data.get("date")).toDate());
                intent.putExtra("DocObj",tmp);
                startActivity(intent);
            }
        });

    }
}