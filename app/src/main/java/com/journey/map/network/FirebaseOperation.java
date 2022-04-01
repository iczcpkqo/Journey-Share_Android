package com.journey.map.network;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.journey.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FirebaseOperation {
    public static final int REQUEST_ROUTES = 0;
    public static final int UPDATE_POINT = 1;
    public static final int ROUTES = 2;
    public static final int FILE_EXISTS = 3;
    public static final int FILE_NOT_FOUND = 4;
    public static final int GET_ROUTES = 5;
    public static final String ROUTE_1 = "ROUTE_1";
    public static final int GET_SINGLE_ROUTE = 6;
    public static final int GET_MULTIPLE_ROUTE = 7;
    public static final int FILE_EXISTS_RECORD = 8;
    public static final int FILE_NOT_FOUND_RECORD = 9;
    public static final int ARRIVED_LEADER = 10;
    public static final int SAVE_ROUTE = 11;
    public static final int NAVIGATION_ACTIVITY_VIEW = 12;
    public static final int FIELD_NOT_FOUND = 13;
    public static final String ROUTE_2 = "ROUTE_2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Handler mainHandler;
    private DocumentReference noteRef;
    private int sleepTime = 2000;

    static public void isExist(String collectionPath,String documentPath,Handler currentHandler)
    {

        FirebaseFirestore sdb = FirebaseFirestore.getInstance();

        DocumentReference  noteRef = sdb.collection(collectionPath).document(documentPath);
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Message message = new Message();
                        message.what = FILE_EXISTS_RECORD;
                        message.obj = "Your network is working, the record is uploaded successfully !";
                        currentHandler.sendMessage(message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Message message = new Message();
                        message.what = FILE_NOT_FOUND_RECORD;
                        message.obj = "Your Recorded upload failure, saved in local cache !";
                        currentHandler.sendMessage(message);
                    }
                });

    }
    public void  startListnere(String collectionPath,String documentPath,String field)
    {
        new Thread(){
            @Override
            public void run() {
                noteRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Message message = new Message();

                                try {
                                    Thread.sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(documentSnapshot.exists())
                                {
                                    String datas = documentSnapshot.getString(field);
                                    if(datas != null)
                                    {
                                        message.obj = null;
                                        message.what = FILE_EXISTS;
                                    }
                                    else
                                    {
                                        message.obj = null;
                                        message.what = FIELD_NOT_FOUND;
                                    }
                                }



                                mainHandler.sendMessage(message);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                try {
                                    Thread.sleep(sleepTime);
                                } catch (InterruptedException d) {
                                    d.printStackTrace();
                                }
                                Message message = new Message();
                                message.what = FILE_NOT_FOUND;
                                message.obj = "Network is down";
                                mainHandler.sendMessage(message);
                            }
                        });
            }
        }.start();
    }


    public FirebaseOperation(String collectionPath,String documentPath,Handler handler)
    {
        noteRef = db.collection(collectionPath).document(documentPath);
        mainHandler = handler;
    }

    public void  saveDocData(String collectionPath,String documentPath,Map<String, Object> data)
    {
        //collectionPath = map
        //documentPath = UID + something(_ROUTE)
        //data after serialization ListPeer
        db.collection(collectionPath).document(documentPath)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("firebaseSuccess", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firebaseFailure", "Error writing document", e);
                    }
                });
    }
    public static Object encodeNetworkData(String baseContent)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(baseContent);
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
        ObjectInput in = null;
        Object networkData = null;
        try {
            in = new ObjectInputStream(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            networkData =  in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return networkData;
    }
    static public String getObjectString(Object route)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(route);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] dataBytes = bos.toByteArray();
        String data = Base64.getEncoder().encodeToString(dataBytes);

        return data;
    }

    public void  saveDocData(Map<String, Object> data)
    {
        //collectionPath = map
        //documentPath = UID + something(_ROUTE)
        //data after serialization ListPeer
        noteRef.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Message message = new Message();
                        message.what = FILE_EXISTS_RECORD;
                        message.obj = "Your network is working, the record is uploaded successfully !";
                        mainHandler.sendMessage(message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Message message = new Message();
                        message.what = FILE_NOT_FOUND_RECORD;
                        message.obj = "Your Recorded upload failure, saved in local cache !";
                        mainHandler.sendMessage(message);
                    }
                });
    }
    public void loadNote(String Field)
    {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {

                           String dataString = documentSnapshot.getString(Field);
                           FirebaseNetworkData data = (FirebaseNetworkData) encodeNetworkData(dataString);

                        }
                        else {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firebaseFailure", "Error writing document", e);
                    }
                });
    }
    public FirebaseOperation() {

    }
}
