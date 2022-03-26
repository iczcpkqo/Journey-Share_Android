package com.journey.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.entity.Msg;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> msgList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        View theMsg;

        public ViewHolder(View view) {
            super(view);
            this.theMsg = view;
            this.leftLayout = (LinearLayout) view.findViewById(R.id.msg_left_box);
            this.rightLayout = (LinearLayout) view.findViewById(R.id.msg_right_box);
            this.leftMsg = (TextView) view.findViewById(R.id.msg_left_txt);
            this.rightMsg = (TextView) view.findViewById(R.id.msg_right_txt);
        }
    }

    public MsgAdapter(List<Msg> data){
        this.msgList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Msg msg = msgList.get(position);

//        Toast.makeText(holder.theMsg.getContext(), msg.getContent(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(holder.theMsg.getContext(), "3333333333333" ,Toast.LENGTH_SHORT).show();


//        System.out.println("##############################");
//        System.out.println(msg.getContent());
//        System.out.println("##############################");

        if(position %2 == 0){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        } else{
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount(){
        return msgList.size();
    }
}