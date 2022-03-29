package com.journey.adapter;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.journey.service.database.DialogueHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat {

    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DialogueFragment";

    public static void go(List<String> players) {
        Map<String, Object> dialogue = new HashMap<>();
        dialogue.put("type", players.size() > 2 ? "group" : "single");
        dialogue.put("playerString", players.toString());
        dialogue.put("playerList", players);
        dialogue.put("createTime", FieldValue.serverTimestamp());
        dialogue.put("orderID", "testOrderId-123");

        db.collection("dialogue").whereEqualTo("playerString", players.toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "@@@@" + task.getResult());
//                        List<QueryDocumentSnapshot> dotest = (List<QueryDocumentSnapshot>) task.getResult();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, document.getId() + " => #####" + data);
                                // TODO: 跳转
                            }
                            if (0 == task.getResult().size()) {
                                System.out.println("#$$$$%%%%%%%%%%%%%%%%%%%$#%#%#$%");
                                System.out.println(dialogue.get("playerString"));
                                insertDialogue(dialogue);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private static void insertDialogue(Map<String, Object> dialogue) {
        db.collection("dialogue")
                .add(dialogue)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        String dialogueId = documentReference.getId();
//                                Arrays.asList(dialogue.get("playerString").toString().split(","));
//                        db.collection("users").whereArrayContainsAny("email", arrPlayers)
//                        db.collection("users").whereEqualTo("email", "liuguowen@qq.com")
                        db.collection("users").whereIn("email", DialogueHelper.convertStringToList(dialogue.get("playerString").toString()))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        Log.d(TAG, "!@@@@##@@$@$@$@$@#$@#$@#$" + task.getResult());
//                                        System.out.println(dialogue);
//                                        System.out.println("###################yyp");
//                                        System.out.println(task.getResult().size());
                                        //                        List<QueryDocumentSnapshot> dotest = (List<QueryDocumentSnapshot>) task.getResult();
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, Object> data = document.getData();
                                                data.put("uuid", document.getId());
                                                Log.d(TAG, document.getId() + " => #####" + data);
                                                db.collection("dialogue").document(dialogueId).collection("players")
                                                        .document(document.getId())
                                                        .set(data, SetOptions.merge())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                // TODO: 跳转
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error writing document", e);
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        ;

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
