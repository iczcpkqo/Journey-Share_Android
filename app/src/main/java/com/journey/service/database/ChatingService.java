package com.journey.service.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.journey.activity.ChatActivity;
import com.journey.dontremoveme.Chating;
import com.journey.entity.ChatDeliver;
import com.journey.entity.Dialogue;
import com.journey.entity.User;
import com.journey.service.database.DialogueHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ChatingService implements Chating {

    public static void addWithMe(List<String> players) { Chating.addWithMe(players); }

    public static void goWithMe(Context context, List<String> players) { Chating.goWithMe(context, players); }

    public static List<String> withMe(List<String> players) { return Chating.withMe(players) ; }

    public static void go(Context context,List<String> players) { Chating.go(context, players); }

    public static void go(Context context, Dialogue dialogue) { Chating.go(context, dialogue); }

    public static void add(List<String> players) { Chating.add(players);}

    private static void insertDialogue(Map<String, Object> newDialogue) { Chating.insertDialogue(newDialogue); }
}
