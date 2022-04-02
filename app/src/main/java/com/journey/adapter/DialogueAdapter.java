package com.journey.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.protobuf.DescriptorProtos;
import com.journey.R;
import com.journey.activity.Chat;
import com.journey.activity.JourneyActivity;
import com.journey.entity.ChatDeliver;
import com.journey.entity.Dialogue;
import com.journey.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
public class DialogueAdapter extends RecyclerView.Adapter<DialogueAdapter.ViewHolder> {

    private List<Dialogue> dialogueList;
    private static final String TAG = "DialogueAdapter";
    Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View theDialogue;
        ImageView img;
        TextView title;
        List<Integer> headCupboard = new ArrayList<>();

        public ViewHolder(View view){
            super(view);
            this.theDialogue = view;
            this.img = (ImageView) view.findViewById(R.id.dialogue_item_img);
            this.title = (TextView) view.findViewById(R.id.dialogue_item_title);
            // TODO: 做头像资源
            initHeadCupboard();
        }

        private void initHeadCupboard(){
            headCupboard.add(R.drawable.h_0);
            headCupboard.add(R.drawable.h_1);
            headCupboard.add(R.drawable.h_2);
            headCupboard.add(R.drawable.h_3);
            headCupboard.add(R.drawable.h_4);
            headCupboard.add(R.drawable.h_5);
            headCupboard.add(R.drawable.h_6);
            headCupboard.add(R.drawable.h_7);
            headCupboard.add(R.drawable.h_8);
            headCupboard.add(R.drawable.h_g);
            headCupboard.add(R.drawable.h_nijita);
        }
    }

    public DialogueAdapter(List<Dialogue> data, Context context){
        dialogueList = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogue_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.theDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                Chating.go(context, dialogueList.get(position));
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Dialogue dia = dialogueList.get(position);

        // 根据用户情况设置会话标题
        holder.title.setText(dia.getTitle().replace(",", ", "));

        // 根据用户信息设置头像
//        holder.img.setImageResource(holder.headCupboard.get((int) (dialogueList.get(position).getSender().getGender().equals("Female")?0:1) ));
        holder.img.setImageResource(holder.headCupboard.get(dia.getType().equals("single") ? (int)dia.getReceiver().get(0).getHeadImg():9));
//        if(dia.getReceiver() != null) {
//            Log.d(TAG, "The Recever ==> " +  dia.getReceiver().get(0).getHeadImg());
//            Log.d(TAG, "The Recever ==> " +  dia.getReceiver().get(0).getEmail());
//            Log.d(TAG, "The Recever ==> " +  dia.getReceiver().get(0).getUsername());
//        }
    }

    @Override
    public int getItemCount(){
        return dialogueList.size();
    }
}
