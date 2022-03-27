package com.journey.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.activity.Chat;
import com.journey.activity.JourneyActivity;
import com.journey.entity.ChatDeliver;
import com.journey.entity.Dialogue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
public class DialogueAdapter extends RecyclerView.Adapter<DialogueAdapter.ViewHolder> {

    private List<Dialogue> dialogueList;
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
            headCupboard.add(R.drawable.u1);
            headCupboard.add(R.drawable.u2);
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
                Dialogue dia = dialogueList.get(position);

                Intent intent = new Intent(context, Chat.class);

                // TODO: 移除测试数据
                intent.putExtra("test", "oooossssooosss");
                Toast.makeText(v.getContext(), dia.dialogueTitle, Toast.LENGTH_SHORT).show();

                ChatDeliver deliver = new ChatDeliver();

                // TODO: 配置对谈参数
                deliver.setUsername(dia.getReceiver().get(0).getUsername());

                intent.putExtra("deliver", deliver);
                context.startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Dialogue dia = dialogueList.get(position);

        // TODO: 根据用户情况设置会话标题
        holder.title.setText(dia.getTitle());

        // TODO: 根据用户信息设置头像
        holder.img.setImageResource(holder.headCupboard.get((int) Math.floor(Math.random()*2)));
    }

    @Override
    public int getItemCount(){
        return dialogueList.size();
    }
}
