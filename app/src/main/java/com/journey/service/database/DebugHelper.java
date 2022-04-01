package com.journey.service.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.adapter.Chating;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DebugHelper {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DebugHelper";
    private static ArrayList<String> emailList = Lists.newArrayList("iris@123.com",
            "liu@tcd.com",
            "mao@tcd.com",
            "yan@tcd.com");
    /**
     * 打印所有用户的邮箱
     */
    public static void printAllUserEmail(){
        db.collection("users").whereNotEqualTo("email", null)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> emailBox = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                emailBox.add(document.get("email").toString());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        Log.d(TAG, emailBox.toString());
                    }
                });
    }


    public static String getOneRandomEmail(){
        return emailList.get((int) Math.floor(Math.random()*emailList.size()));
    }

    public static List<String> getTwoRandomEmail(){
        return getNRandomEmail(2);
    }

    public static List<String> getNRandomEmail(int n){
        HashSet<String> players = new HashSet<>();
        for (; n>players.size(); players.add(getOneRandomEmail()));
        return (List<String>) new ArrayList<String>(players);
    }

    public static void addNRandomDialogue(int n, int t){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashSet<ArrayList<String>> dialogues = new HashSet<>();

                for (; n>dialogues.size(); dialogues.add((ArrayList<String>) getNRandomEmail(t)));

                for(ArrayList<String> players : dialogues)
                    Chating.add(players);
            }
        }).start();
    }

}
