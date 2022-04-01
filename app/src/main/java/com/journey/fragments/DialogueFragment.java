package com.journey.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
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
    private Map<String, Integer> sortDialogue;
    private ListenerRegistration registration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.logFilter();
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
        this.sortDialogue = new HashMap<>();
        dialogueList.clear();

//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(false)
//                .build();
//        db.setFirestoreSettings(settings);

        // DONE: 传参启动
//        Chating.go(getActivity(), Arrays.asList("liu@qq.com", "race@123.com"));
//        Chating.go(getActivity(), Arrays.asList("liu@qq.com", "tomous@123.com"));
//        Chating.go(getActivity(), Arrays.asList("liu@qq.com", "liuguowen@qq.com"));
//        Chating.go(getActivity(), Arrays.asList("race@123.com", "yan123@qq.com"));
//        Chating.go(getActivity(), Arrays.asList("iris@123.com", "yan123@qq.com"));
//        for (int i =0; i<3; i++)
//            Chating.go(getActivity(), DebugHelper.getNRandomEmail((int) Math.ceil((Math.random()*1)+1)));
//        Chating.go(getActivity(), DebugHelper.getNRandomEmail((int) Math.ceil((Math.random()*1)+1)));
        //iris@123.com

        // DONE: 增加会话
//        Chating.add(Arrays.asList("123456@qq.com", "liu@qq.com"));

//        try {
//            uu.setBirthDate("1995-6-2");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String ss = "dsfs";
//        try {
//            uu.setBirthDate(ss);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        return diaFrame;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.sortDialogue.clear();
        dialogueList.clear();
        reFreshDialogue();
    }

    @Override
    public void onPause(){
        super.onPause();
        this.sortDialogue.clear();
        dialogueList.clear();
        this.registration.remove();
    }

    private void reFreshDialogue() {
        //Direction.DESCENDING
//        db.collection("dialogue").whereArrayContainsAny("playerList", Arrays.asList(sender.getEmail())).orderBy("lastTime", Query.Direction.DESCENDING).orderBy("createTime", Query.Direction.DESCENDING)
        this.registration = db.collection("dialogue").whereArrayContainsAny("playerList", Arrays.asList(sender.getEmail())).orderBy("lastTime", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                Log.d(TAG, "@@@@ number of dialogue: " + value);
//                Log.d(TAG, "@@@@ I'm " + DialogueHelper.getSender().getEmail());
                if (error == null) {
                    // TODO: 异步排序不能同步
//                    dialogueList = new ArrayList<>(value.size());
                    for (QueryDocumentSnapshot document : value) {
                        Map<String, Object> data = document.getData();
                        String dialogueId = document.getId();
//                        Log.d(TAG, dialogueId + " => #####" + data);
//                        sortDialogue.put(dialogueId, dialogueList.size());
                        db.collection("dialogue").document(dialogueId).collection("players")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        Log.d(TAG, "@@@@" + task.getResult());
                                        try {
                                            Dialogue oneDialogue = new Dialogue();
                                            oneDialogue.setDialogueId(dialogueId);
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Map<String, Object> data = document.getData();
//                                                    Log.d(TAG, document.getId() + " => #user#user#user#user" + data);

                                                    User player = new User();
                                                    player.setUuid(document.getId());
                                                    player.setEmail(data.get("email").toString());
                                                    player.setUsername(data.get("username").toString());
                                                    player.setGender(data.get("gender").toString());
                                                    oneDialogue.addPlayer(player);
                                                    oneDialogue.setDialogueId(dialogueId);
                                                }
//                                                dialogueList.add(sortDialogue.get(oneDialogue.getDialogueId()),oneDialogue);
//                                                dialogueList.add(oneDialogue);
                                                dialogueRecycler.setAdapter(adapter);


                                            } else {
//                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });



                    }
                } else {
//                    Log.d(TAG, "Error getting documents: ");
                }

            }
        });

    }
    private void logFilter(){

        java.util.logging.Logger.getLogger("com.journey").setLevel(Level.OFF);
    }

}
















