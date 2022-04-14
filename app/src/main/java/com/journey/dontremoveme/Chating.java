package com.journey.dontremoveme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.activity.ChatActivity;
import com.journey.entity.ChatDeliver;
import com.journey.entity.Dialogue;
import com.journey.entity.User;
import com.journey.service.database.DialogueHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface Chating {

    @SuppressLint("StaticFieldLeak")
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "Chating";

    /**
     * Get a list of all my friends' emails, not including my own
     * @return ArrayList<String> User emails list
     */
    static ArrayList<String> getMyFriends(){
        ArrayList<String> myFriends = getFriendShip();
        myFriends.remove(DialogueHelper.getSender().getEmail());
        return myFriends;
    }

    /**
     * Get a list of all my friends' emails, including my own
     * @return ArrayList<String> User emails list
     */
    static ArrayList<String> getFriendShip(){
        File file = new File(android.os.Environment.getExternalStorageDirectory()+"/FriendsList.txt");
        FileReader fis = null;
        BufferedReader br = null;
        ArrayList<String> friendsList = null;

        if(file.exists()) {
            try {
                fis = new FileReader(file);
                br = new BufferedReader(fis);
                String str = br.readLine();
                String jsonMsg = "";
                while(str != null){
                    jsonMsg += str;
                    str = br.readLine();
                }
                friendsList = JSON.parseObject(jsonMsg, new TypeReference<ArrayList<String>>(){});
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return friendsList;
    }

    static void refreshFriends() {
       refreshFriends(DialogueHelper.getSender().getEmail());
    }

    static void refreshFriends(String userEmail) {
        db.collection("dialogue").whereArrayContainsAny("playerList", Arrays.asList(userEmail)).orderBy("lastTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashSet<String> friendsList = new HashSet<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> dia = document.getData();
                                friendsList.addAll(DialogueHelper.convertStringToList(dia.get("playerList").toString()));
                            }
                            String jsonFriends = JSON.toJSON(friendsList).toString();
                            try {
                                File fs = new File(android.os.Environment.getExternalStorageDirectory()+"/FriendsList.txt");
                                FileOutputStream outputStream = null;
                                outputStream = new FileOutputStream(fs, false);
                                outputStream.write(jsonFriends.getBytes());
                                outputStream.flush();
                                outputStream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

    }

    static void addWithMe(List<String> players) {
        if(0==players.size())
            return;
        if(2>players.size() && DialogueHelper.getSender().getEmail().equals(players.get(0)))
            return;
        add(withMe(players));
    }

    static void goWithMe(Context context, List<String> players) {
        if(0==players.size())
            return;
        if(2>players.size() && DialogueHelper.getSender().getEmail().equals(players.get(0)))
            return;
        go(context, withMe(players));
    }

    static List<String> withMe(List<String> players) {
        User sender = DialogueHelper.getSender();
        List<String> arr = new ArrayList<>(players);
        arr.add(sender.getEmail());
        HashSet<String> uni = new HashSet<>(arr);
        if(2>uni.size())
            return null;
        return new ArrayList<>(uni);
    }

    static void go(Context context,List<String> players) {
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

    static void go(Context context, Dialogue dialogue) {
        Intent intent = new Intent(context, ChatActivity.class);
        ChatDeliver deliver = new ChatDeliver();

        deliver.setDialogueTitle(dialogue.getTitle());
        deliver.setDialogueId(dialogue.getDialogueId());
        deliver.setType(dialogue.getType());

        intent.putExtra("deliver", deliver);
        context.startActivity(intent);
    }

    static void add(List<String> players) {
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

    static void insertDialogue(Map<String, Object> newDialogue) {
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
