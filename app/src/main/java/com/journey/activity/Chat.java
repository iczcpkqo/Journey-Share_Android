package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.journey.R;
import com.journey.adapter.DialogueAdapter;
import com.journey.adapter.MsgAdapter;
import com.journey.entity.ChatDeliver;
import com.journey.entity.Dialogue;
import com.journey.entity.Msg;
import com.journey.entity.User;
import com.journey.service.database.DialogueHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Chat
 */
public class Chat extends AppCompatActivity {

    private static final String TAG = "Chat";
    private List<Msg> msgList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView msgRecycler;
    private EditText msgInput;
    private Button msgBtn;
    private MsgAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Dialogue dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.msgRecycler = (RecyclerView) findViewById(R.id.msg_recycler_view);
        this.msgInput = (EditText) findViewById(R.id.msg_input_text);
        this.msgBtn = (Button) findViewById(R.id.msg_btn);
        this.adapter = new MsgAdapter(msgList);
        this.layoutManager = new LinearLayoutManager(this);

        // 移除test data
//        initMsgTestData();
        //

        // 参数接收
        Intent intent = getIntent();
        ChatDeliver deliver =(ChatDeliver) intent.getSerializableExtra("deliver");

        try {
            dialogue = new Dialogue();
            dialogue.setTitle(deliver.getDialogueTitle());
            dialogue.setDialogueId(deliver.getDialogueId());
            dialogue.setType(deliver.getType());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // 设置
        setChatBar(dialogue.getTitle());
        msgRecycler.setLayoutManager(layoutManager);

        msgRecycler.setAdapter(adapter);
        msgRecycler.scrollToPosition(msgList.size() -1);

        msgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                msgSend(msgInput.getText().toString());
            }
        });
        msgInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // TODO: 调试初始滚动
                    // TODO: 移除测试数据
//                    Toast.makeText(Chat.this, "input click", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        msgRefresh();
    }

    @Override
    public void onPause(){
        super.onPause();
        msgList.clear();
    }

    private void msgSend(String content){
        if(!"".equals(content)){
            Msg msg = new Msg(content, dialogue.getDialogueId());
            msgInput.setText("");
            db.collection("message").add(msg);
            db.collection("dialogue").document(dialogue.getDialogueId()).update("lastTime", System.currentTimeMillis());
        }
    }

    private void msgRefresh(){

        db.collection("message").orderBy("time")
                .whereEqualTo("dialogueId", dialogue.getDialogueId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d(TAG, "@@@@" + value);
                        msgList.clear();
                        if (error == null) {
                            for (QueryDocumentSnapshot document : value) {
                                Map<String, Object> data = document.getData();
                                HashMap<String, String> sender = (HashMap<String, String>) data.get("sender");
                                String dialogueId = document.getId();
                                Log.d(TAG, dialogueId + " => onEvent msg #####" + data);
                                Log.d(TAG, data.get("sender").getClass().toString());

                                Msg msg = new Msg(new User(sender.get("email"), sender.get("username"), sender.get("gender")),
                                        data.get("content").toString(),
                                        (long)data.get("time"),
                                        data.get("dialogueId").toString());
                                msgList.add(msg);
                            }
                            msgRecycler.setAdapter(adapter);
                            msgRecycler.scrollToPosition(msgList.size() - 1);
                        } else {
                            Log.d(TAG, "Error getting documents: ");
                        }
                    }
                });
    }

    private void initMsgTestData(){
        for(int i=0; i<15; i++){
            Msg msg = new Msg("HhhhhHHH"+i);
            msgList.add(msg);
        }
    }

    // set the style for Chat actionBar
    private void setChatBar(String tit) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(tit);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);//display the back arrow icon
    }

    // set default actionbar
    private void setDefaultChatBar() {
        setChatBar("Default tit - xiang mao");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
