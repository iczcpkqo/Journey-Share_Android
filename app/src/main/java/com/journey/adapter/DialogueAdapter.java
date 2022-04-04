package com.journey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.journey.R;
import com.journey.entity.Dialogue;
import com.journey.service.database.ChatingService;
import com.journey.service.database.DialogueHelper;

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
        List<Integer> headCupboard;

        public ViewHolder(View view){
            super(view);
            this.theDialogue = view;
            this.img = (ImageView) view.findViewById(R.id.dialogue_item_img);
            this.title = (TextView) view.findViewById(R.id.dialogue_item_title);
            // TODO: 做头像资源
            initHeadCupboard();
        }

        private void initHeadCupboard(){
            headCupboard = DialogueHelper.getHeadCupboard();
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
                ChatingService.go(context, dialogueList.get(position));
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
