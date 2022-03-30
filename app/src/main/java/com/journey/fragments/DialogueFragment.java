package com.journey.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.R;
import com.journey.adapter.Chating;
import com.journey.adapter.DialogueAdapter;
import com.journey.entity.Dialogue;
import com.journey.entity.User;
import com.journey.service.database.DebugHelper;
import com.journey.service.database.DialogueHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
// TODO: 传参增加会话不启动
// TODO: 头像判断
// FIXME: 聊天列表时间索引排序

public class DialogueFragment extends Fragment {

    private static final String TAG = "DialogueFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View diaFrame;
    private LayoutInflater inflater;
    private ViewGroup container;
    private List<Dialogue> dialogueList = new ArrayList<>();
    private RecyclerView dialogueRecycler;
    private DialogueAdapter adapter;
    private LinearLayoutManager layoutManager;
    private User sender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.diaFrame = inflater.inflate(R.layout.fragment_dialogue, container, false);
        this.dialogueRecycler = (RecyclerView) diaFrame.findViewById(R.id.dialogue_recycler_view);
        this.adapter = new DialogueAdapter(dialogueList, getActivity());
        this.layoutManager = new LinearLayoutManager(getContext());
        this.dialogueRecycler.setLayoutManager(layoutManager);
        this.dialogueRecycler.setAdapter(adapter);
        this.inflater = inflater;
        this.container = container;
        this.sender = DialogueHelper.getSender();

        // DONE: 传参启动
//         Chating.go(getActivity(), Arrays.asList("Lucy@qq.com", "liu@qq.com","race@123.com"));
//        for (int i =0; i<10; i++)
//            Chating.go(getActivity(), DebugHelper.getNRandomEmail((int) Math.ceil((Math.random()*1)+1)));
//        Chating.go(getActivity(), DebugHelper.getNRandomEmail((int) Math.ceil((Math.random()*1)+1)));
        //iris@123.com

        return diaFrame;
    }

    @Override
    public void onResume() {
        super.onResume();
        dialogueList.clear();
        reFreshDialogue();
    }

    @Override
    public void onPause(){
        super.onPause();
        dialogueList.clear();
    }

    private void reFreshDialogue() {
        //Direction.DESCENDING
        db.collection("dialogue").whereArrayContainsAny("playerList", Arrays.asList(sender.getEmail())).orderBy("lastTime", Query.Direction.DESCENDING).orderBy("createTime", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "@@@@" + value);
                dialogueList.clear();
                if (error == null) {
                    for (QueryDocumentSnapshot document : value) {
                        Map<String, Object> data = document.getData();
                        String dialogueId = document.getId();
                        Log.d(TAG, dialogueId + " => #####" + data);

                        db.collection("dialogue").document(dialogueId).collection("players")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Log.d(TAG, "@@@@" + task.getResult());
                                        try {
                                            Dialogue oneDialogue = new Dialogue();
                                            oneDialogue.setDialogueId(dialogueId);
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Map<String, Object> data = document.getData();
                                                    Log.d(TAG, document.getId() + " => #user#user#user#user" + data);

                                                    User player = new User();
                                                    player.setUuid(document.getId());
                                                    player.setEmail(data.get("email").toString());
                                                    player.setUsername(data.get("username").toString());
                                                    player.setGender(data.get("gender").toString());
                                                    oneDialogue.addPlayer(player);
                                                    oneDialogue.setDialogueId(dialogueId);
                                                }
                                                dialogueList.add(oneDialogue);
                                                dialogueRecycler.setAdapter(adapter);

                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ");
                }

            }
        });

    }
}
















