package com.journey.msg.message;

import static android.view.View.FOCUS_DOWN;
import static android.view.View.VISIBLE;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.entity.Message;

import java.util.ArrayList;
public class Chat extends AppCompatActivity implements View.OnClickListener {
//public class Chat extends Fragment{
    public static final int CHAT_MSG_TYPE_LEFT = 2;
    public static final int CHAT_MSG_TYPE_RIGHT = 1;

    private NestedScrollView scrollView;
    private RecyclerView recyclerView;
    private EditText msg;//消息
    private TextView send;//发送消息
    private TextView receive;//接收消息

    private ArrayList<Message> list;
    private ChatMsgAdapter adapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//    public void onCreateView(Bundle savedInstanceState) {
        System.out.println("dsf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_chat);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mHandler = new Handler();
        list = new ArrayList<>();

        msg = findViewById(R.id.msg);
        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);
        send.setOnClickListener(this);
        receive.setOnClickListener(this);

        scrollView = findViewById(R.id.scrollView);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setNestedScrollingEnabled(false);
        scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(FOCUS_DOWN);
                            msg.setFocusable(true);
                            msg.setFocusableInTouchMode(true);
                            msg.requestFocus();
                            msg.findFocus();
                        }
                    });
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Chat.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ChatMsgAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                generateMsg(CHAT_MSG_TYPE_RIGHT);
                break;
            case R.id.receive:
                generateMsg(CHAT_MSG_TYPE_LEFT);
                break;
            default:
                break;
        }
    }

    private void generateMsg(int type) {
        String message = msg.getText().toString().trim();
        if (message.equals("")) {
            Toast.makeText(Chat.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Message body = new Message(message, type);
        list.add(body);
        adapter.notifyDataSetChanged();

        msg.setText("");
    }

    private class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ViewHolder> {

        private ArrayList<Message> list;

        public ChatMsgAdapter(ArrayList<Message> list) {
            this.list = list;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView chatMsgLeft;
            public TextView chatMsgRight;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                chatMsgLeft = view.findViewById(R.id.chat_msg_left);
                chatMsgRight = view.findViewById(R.id.chat_msg_right);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_chat_content,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Message body = list.get(position);
            if (body.type == CHAT_MSG_TYPE_LEFT) {
                holder.chatMsgRight.setVisibility(View.GONE);
                holder.chatMsgLeft.setVisibility(VISIBLE);
                holder.chatMsgLeft.setText(body.text);
            } else if (body.type == CHAT_MSG_TYPE_RIGHT) {
                holder.chatMsgLeft.setVisibility(View.GONE);
                holder.chatMsgRight.setVisibility(VISIBLE);
                holder.chatMsgRight.setText(body.text);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}

