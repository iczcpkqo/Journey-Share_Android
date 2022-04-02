package com.journey.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.journey.activity.Chat;
import com.journey.entity.ChatDeliver;
import com.journey.entity.Dialogue;
import com.journey.entity.User;
import com.journey.service.database.DialogueHelper;

import org.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chating {

    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DialogueFragment";

    public static void go(Context context,List<String> players) {
        Collections.sort(players);
        User sender = DialogueHelper.getSender();
        Map<String, Object> newDialogue = new HashMap<>();
        newDialogue.put("type", players.size() > 2 ? "group" : "single");
        newDialogue.put("playerString", players.toString());
        newDialogue.put("playerList", players);
        newDialogue.put("createTime", FieldValue.serverTimestamp());
        newDialogue.put("lastTime", System.currentTimeMillis());
        newDialogue.put("orderID", "testOrderId-123");

        db.collection("dialogue").whereEqualTo("playerString", players.toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String dialogueId = document.getId();
                                // DONE: 跳转
                                db.collection("users").whereIn("email", DialogueHelper.convertStringToList(newDialogue.get("playerString").toString()))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> data = new HashMap<>();
                                                    StringBuffer dialogTitle = new StringBuffer();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Map<String, Object> item = document.getData();
                                                        data.put(document.getId(), item);
                                                        dialogTitle.append(item.get("email").equals(sender.getEmail()) ? "" : (dialogTitle.toString().equals("") ? item.get("username") : "," + item.get("username")));
                                                    }
                                                        Dialogue dialogue = new Dialogue();
                                                        dialogue.setTitle(dialogTitle.toString());
                                                        dialogue.setType(newDialogue.get("type").toString());
                                                        dialogue.setDialogueId(dialogueId);
                                                        go(context, dialogue);

                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                                break;
                            }
                            if (0 == task.getResult().size()) {
                                insertDialogue(newDialogue);
                                go(context,players);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void go(Context context, Dialogue dialogue) {
        Intent intent = new Intent(context, Chat.class);
        ChatDeliver deliver = new ChatDeliver();

        deliver.setDialogueTitle(dialogue.getTitle());
        deliver.setDialogueId(dialogue.getDialogueId());
        deliver.setType(dialogue.getType());

        intent.putExtra("deliver", deliver);
        context.startActivity(intent);
    }

    public static void add(List<String> players) {
        Collections.sort(players);
        User sender = DialogueHelper.getSender();
        Map<String, Object> newDialogue = new HashMap<>();
        newDialogue.put("type", players.size() > 2 ? "group" : "single");
        newDialogue.put("playerString", players.toString());
        newDialogue.put("playerList", players);
        newDialogue.put("createTime", FieldValue.serverTimestamp());
        newDialogue.put("lastTime", System.currentTimeMillis());
        newDialogue.put("orderID", "testOrderId-123");

        db.collection("dialogue").whereEqualTo("playerString", players.toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (0 == task.getResult().size()) {
                                insertDialogue(newDialogue);
                            }
                        } else {
                            Log.d(TAG, "add one dialogue");
                        }
                    }
                });
    }

    private static void insertDialogue(Map<String, Object> newDialogue) {
        User sender = DialogueHelper.getSender();
        db.collection("dialogue")
                .add(newDialogue)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String dialogueId = documentReference.getId();
                        db.collection("users").whereIn("email", (List<? extends Object>) newDialogue.get("playerList"))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            StringBuffer dialogTitle = new StringBuffer();
                                            // TODO: 无法插入, 可以插入, 但是没有标题, 考虑分离 Chat 和 Dialogue
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, Object> item = document.getData();
                                                dialogTitle.append(item.get("email").equals(sender.getEmail()) ? "" : (dialogTitle.toString().equals("") ? item.get("username") : "," + item.get("username")));
                                                db.collection("dialogue").document(dialogueId).collection("players").add(item);
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
