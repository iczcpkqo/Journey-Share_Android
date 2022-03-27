package com.journey.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.journey.R;
import com.journey.adapter.MsgAdapter;
import com.journey.entity.ChatDeliver;
import com.journey.entity.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Chat
 */
public class Chat extends AppCompatActivity {

    List<Msg> msgList = new ArrayList<>();
    RecyclerView msgRecycler;
    EditText msgInput;
    Button msgBtn;
    MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // TODO: 移除test data
        initMsgTestData();
        Intent intent = getIntent();
        ChatDeliver deliver =(ChatDeliver) intent.getSerializableExtra("deliver");

        // TODO: 参数接收
        String testt = deliver.getUsername();
        setChatBar(testt);


        msgRecycler = (RecyclerView) findViewById(R.id.msg_recycler_view);
        msgInput = (EditText) findViewById(R.id.msg_input_text);
        msgBtn = (Button) findViewById(R.id.msg_btn);
        adapter = new MsgAdapter(msgList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // TODO: Recycler 刷新
        msgRefresh();
        msgRecycler.setLayoutManager(layoutManager);
        msgRecycler.setAdapter(adapter);
        msgRecycler.scrollToPosition(msgList.size() -1);
        msgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String content = msgInput.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content);

                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);
                    msgRecycler.scrollToPosition(msgList.size() -1);
                    msgInput.setText("");
                }
            }
        });
        msgInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // TODO: 调试初始滚动
                    // TODO: 移除测试数据
                    Toast.makeText(Chat.this, "input click", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void msgRefresh(){ }

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