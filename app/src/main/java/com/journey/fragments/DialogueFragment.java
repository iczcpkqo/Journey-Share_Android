package com.journey.fragments;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.journey.R;
import com.journey.adapter.Chat;
import com.journey.adapter.DialogueAdapter;
import com.journey.adapter.ReadUserInfoFile;
import com.journey.entity.Dialogue;
import com.journey.entity.Message;
import com.journey.entity.Msg;
import com.journey.entity.Record;
import com.journey.entity.User;
import com.journey.service.database.DebugHelper;
import com.journey.service.database.DialogueHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
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
        this.inflater = inflater;
        this.container = container;
        this.sender = DialogueHelper.getSender();

        // DONE: 获取一名测试接收者email
//        Log.d(TAG, DebugHelper.getOneRandomEmail());

        // DOING: 增加随机单聊Dialogue
        List<String> players = new ArrayList<>();
        players.add("liuguowen@qq.com");
        players.add("race@123.com");


//        for (int i=0; i<3; i++)
//            Chat.go(DebugHelper.getTwoRandomEmail());
//        Chat.go(players);
//        goToChat(players);
        // DOING: 增加随机群聊Dialogue
        return diaFrame;

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("IMIMIMIMIMIMIMIMII ###################################################");
        // TODO: 获取多名测试接收者

        // TODO: 系统消息
        // TODO: 移除测试数据
//        try {
//            initDialogueTestData();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        // TODO: 刷新会话列表
//        reFreshDialogue();
//
//        // TODO: 新消息标记
//
//        // TODO: 连接访问测试, Create
////        testForDialogueData();
//
//        // TODO: 连接访问测试, Check
//        // TODO: 连接访问测试, Update
//        // TODO: 连接访问测试, Delete
//        // TODO: 连接访问测试, Condition
//
//        // TODO: 侦听访问测试
//        testForListener();
//
//
//        dialogueRecycler = (RecyclerView) diaFrame.findViewById(R.id.dialogue_recycler_view);
//        adapter = new DialogueAdapter(dialogueList, getActivity());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//
//        dialogueRecycler.setLayoutManager(layoutManager);
//        dialogueRecycler.setAdapter(adapter);


//        ArrayList<String> players = new ArrayList<>();
//        players.add("liu@qq.com");
//        players.add("iris@123.com");

//        db.collection("dialogue").document("5G8FRXOZz8YPGeKqP87Y").collection("players").whereEqualTo("email", "liu@qq.com")

        reFreshDialogue();
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("IMIMIMIMIMIMIMIMII @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        dialogueList.clear();
    }



    public void goToChat(List<String> players){
        Map<String, Object> dialogue = new HashMap<>();
        dialogue.put("type", players.size()>2 ? "group" : "single");
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
                            if( 0 == task.getResult().size()){
                                System.out.println("#$$$$%%%%%%%%%%%%%%%%%%%$#%#%#$%");
                                System.out.println(dialogue.get("playerString"));
//                                insertDialogue(dialogue);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void testVVinsertDialogue(Map<String, Object> dialogue){
        db.collection("dialogue")
                .add(dialogue)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        String docId = documentReference.getId();
                        List<String> arrPlayers  = DialogueHelper.convertStringToList(dialogue.get("playerString").toString());
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
                                                Log.d(TAG, document.getId() + " => #####" + data);
                                                db.collection("dialogue").document(docId).collection("players")
                                                        .document(document.getId())
                                                        .set(document.getData())
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
                                });;

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void getAllDialogue(){

    }

    public void findDialogue(List<String> players){
        Map<String, Object> dialogue = new HashMap<>();
        dialogue.put("type", players.size()>2 ? "group" : "single");
//        dialogue.put("playerString", players);
//        dialogue.put("playerString", Arrays.asList("aaa", "eee"));
        dialogue.put("playerString", players.toString());
        dialogue.put("createTime", FieldValue.serverTimestamp());
        dialogue.put("orderID", "testOrderId-123");

//        testInsertDialogue(dialogue);
//        testInsertRandomDialogue(dialogue);

//        testInsertCity();

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
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void testInsertDialogue(Map<String, Object> dialogue){


        List<String> players = DialogueHelper.convertStringToList(dialogue.get("playerString").toString());
        db.collection("dialogue").whereEqualTo("playerString", players)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "@@@@" + task.getResult());
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, document.getId() + " => #####" + data);
                            }
                            if( 0 == task.getResult().size()){
                                System.out.println("#$$$$%%%%%%%%%%%%%%%%%%%$#%#%#$%");
                                System.out.println(dialogue.get("playerString"));
//                                insertDialogue(dialogue);
                                // TODO: 插入随机

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//        db.collection("dialogue")
//                .add(dialogue)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        String docId = documentReference.getId();
//                        db.collection("users").whereIn("email", players)
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                Map<String, Object> data = document.getData();
//                                                Log.d(TAG, document.getId() + " => #####" + data);
//                                                db.collection("dialogue").document(docId).collection("players").add(document.getData());
//                                            }
//                                        }
//                                    }
//                                });;
//                    }
//                });
    }

    private void testInsertRandomDialogue(Map<String, Object> dialogue){
        dialogue.put("playerString", DebugHelper.getTwoRandomEmail().toString());
        testInsertDialogue(dialogue);
    }


    private void testInsertCity(){
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);
    }

    // TODO: 获取数据
    public void initDialogueData() throws ParseException {

        for (int i=0; i<10; i++) {
            dialogueList.add(new Dialogue(
                    new User("test_Sender" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0),
                    new User("test_Receiver" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)
            ));
        }
    }

    private void reFreshDialogue() {
        // TODO: 刷新单聊
        // TODO: 刷新群聊


        db.collection("dialogue").whereArrayContainsAny("playerList", Arrays.asList(sender.getEmail()))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "@@@@" + value);
                dialogueList.clear();
                if (error == null) {
                    for (QueryDocumentSnapshot document : value) {
                        Map<String, Object> data = document.getData();
                        String dialogueId = document.getId();
//                                Map<String, Object> data2 = document.getDocumentReference(document.getId()).collection("players").document().get().getResult().getData();
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

//                List<Record> record = new ArrayList<Record>();
//                adapter.clear();
//                for (QueryDocumentSnapshot doc: value){
//                    Map<String, Object> data = doc.getData();
//                    Record tmp = new Record((String)doc.getId(),(String)data.get("departure"),(String)data.get("arrival"),((Timestamp)data.get("date")).toDate());
//                    adapter.add(tmp);
//                }
//                Log.d("listener","current:"+record);

            }
        });


//        db.collection("dialogue").whereArrayContainsAny("playerList", Arrays.asList(sender.getEmail()))
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        Log.d(TAG, "@@@@" + task.getResult());
////                        List<QueryDocumentSnapshot> dotest = (List<QueryDocumentSnapshot>) task.getResult();
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Map<String, Object> data = document.getData();
//                                String dialogueId = document.getId();
////                                Map<String, Object> data2 = document.getDocumentReference(document.getId()).collection("players").document().get().getResult().getData();
//                                Log.d(TAG, dialogueId + " => #####" + data);
//
//                                db.collection("dialogue").document(dialogueId).collection("players")
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                Log.d(TAG, "@@@@" + task.getResult());
//                                                try {
//                                                    Dialogue oneDialogue = new Dialogue();
//                                                    oneDialogue.setDialogueId(dialogueId);
//                                                    if (task.isSuccessful()) {
//                                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                                            Map<String, Object> data = document.getData();
//                                                            Log.d(TAG, document.getId() + " => #user#user#user#user" + data);
//
//                                                            User player = new User();
//                                                            player.setUuid(document.getId());
//                                                            player.setEmail(data.get("email").toString());
//                                                            player.setUsername(data.get("username").toString());
//                                                            player.setGender(data.get("gender").toString());
//                                                            oneDialogue.addPlayer(player);
//                                                            oneDialogue.setDialogueId(dialogueId);
//                                                        }
//                                                        dialogueList.add(oneDialogue);
//                                                        dialogueRecycler.setAdapter(adapter);
//
//                                                    } else {
//                                                        Log.d(TAG, "Error getting documents: ", task.getException());
//                                                    }
//
//                                                } catch (ParseException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        });
//
//
//
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });



//        db.collection("Dialogue")
//                .whereEqualTo("state", "CA")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
//                            return;
//                        }
//
//                        List<String> cities = new ArrayList<>();
//                        for (QueryDocumentSnapshot doc : value) {
//                            if (doc.get("name") != null) {
//                                cities.add(doc.getString("name"));
//                            }
//                        }
//                        Log.d(TAG, "Current cites in CA: " + cities);
//                    }
//                });
    }


    /**
     * test function
     */
    // Insert test Dialogue, 2 random people
    public void testInsertSingleDialogue(){
        String emailSender = DebugHelper.getOneRandomEmail();
        String emailReceiver = DebugHelper.getOneRandomEmail();
        for(int i = 0; i<11 && emailSender.equals(emailReceiver); i++)
            emailReceiver = DebugHelper.getOneRandomEmail();
    }


    // Create
    public void testForDialogueData(){
        db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> itemData = new HashMap<>();
        Map<String, Object> itemDataTwo = new HashMap<>();

        // 添加字符串
        itemData.put("first", "Ada");
        itemData.put("last", "Lovelace");
        itemData.put("born", 1815);
        itemDataTwo.put("twot", "dsfsf");

        // 添加对象
        Msg testDataMsg = new Msg("test content");
        itemData.put("testMsgObject", testDataMsg);

        // 添加带对象对象
        Message testDataMessage = new Message("test for add object Message", 1);
        itemData.put("testForMessageObject", testDataMessage);



        // 集合的合并和追加
        // 重复字段的merge
        /**
        * // test for add doc in collection which in a doc
        * db.collection("message").document("6sox5qDcXpK7rDEse47H").collection("ghh")
        *        .add(itemData)
        *        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        *            @Override
        *            public void onSuccess(DocumentReference documentReference) {
        *                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        *            }
        *        })
        *        .addOnFailureListener(new OnFailureListener() {
        *            @Override
        *            public void onFailure(@NonNull Exception e) {
        *                Log.w(TAG, "Error adding document", e);
        *            }
        *        });
        **/

        /**
        * // test for set doc
        * db.collection("message").document("6PvvQ3tdYp3sj19sbTv2")
        *         .set(itemDataTwo)
        *         .addOnSuccessListener(new OnSuccessListener<Void>() {
        *             @Override
        *             public void onSuccess(Void aVoid) {
        *                 Log.d(TAG, "DocumentSnapshot successfully written!");
        *             }
        *         })
        *         .addOnFailureListener(new OnFailureListener() {
        *             @Override
        *             public void onFailure(@NonNull Exception e) {
        *                 Log.w(TAG, "Error writing document", e);
        *             }
        *         });
        **/

        /**
        * Add a new document with a generated ID
        *db.collection("message")
        *        .add(itemData)
        *        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        *            @Override
        *            public void onSuccess(DocumentReference documentReference) {
        *                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        *            }
        *        })
        *        .addOnFailureListener(new OnFailureListener() {
        *            @Override
        *            public void onFailure(@NonNull Exception e) {
        *                Log.w(TAG, "Error adding document", e);
        *            }
        *        });
        **/

        // 获取已存在用户的uuid
        Map<String,Object> userInfo = new ReadUserInfoFile().readUserFile();
        System.out.println(userInfo);
        User sender = new User(userInfo.get("email").toString(),
                               userInfo.get("username").toString(),
                               userInfo.get("gender").toString());

        System.out.println(sender);

         // 通过docID获取某一个用户的信息
         /**
         * DocumentReference docRef = db.collection("users").document("a4KxDl9mZJtNBzXDAteU");
         * docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         *     @Override
         *     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
         *         if (task.isSuccessful()) {
         *             DocumentSnapshot document = task.getResult();
         *             if (document.exists()) {
         *                 Map<String, Object> box = document.getData();
         *                 Log.d(TAG, "DocumentSnapshot data: " + document.getData());
         *             } else {
         *                 Log.d(TAG, "No such document");
         *             }
         *         } else {
         *             Log.d(TAG, "get failed with ", task.getException());
         *         }
         *     }
         * });
         */

        // 无法外部调用数据, 因为在获取数据完成事件被执行的时候, 函数已经完成调用并返回null.
//        User userInfoByDocId = MsgHelper.getUserInfoByDocId("a4KxDl9mZJtNBzXDAteU");

        // 自定义对象
        DocumentReference docRef = db.collection("users").document("a4KxDl9mZJtNBzXDAteU");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User userInfo = documentSnapshot.toObject(User.class);
                System.out.println(userInfo);
            }
        });

        // 根据docID获取userInfo
        /**
         * DocumentReference docRef = db.collection("users").document("a4KxDl9mZJtNBzXDAteU");
         * docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         *     @Override
         *     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
         *         if (task.isSuccessful()) {
         *             DocumentSnapshot document = task.getResult();
         *             if (document.exists()) {
         *                 Map<String, Object> box = document.getData();
         *                 Log.d(TAG, "DocumentSnapshot data: " + document.getData());
         *             } else {
         *                 Log.d(TAG, "No such document");
         *             }
         *         } else {
         *             Log.d(TAG, "get failed with ", task.getException());
         *         }
         *     }
         * });
         */

        // 通过某个查询条件获取doc
        /**
        * db.collection("users")
        *         .whereEqualTo("email", "Tommama@123.com")
        *         .get()
        *         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        *             @Override
        *             public void onComplete(@NonNull Task<QuerySnapshot> task) {
        *                 if (task.isSuccessful()) {
        *                     for (QueryDocumentSnapshot document : task.getResult()) {
        *                         Map<String, Object> info = document.getData();
        *                         User userInfo = new User(info.get("email").toString(),
        *                                                      info.get("username").toString(),
        *                                                      info.get("gender").toString());
        *                         Log.d(TAG, document.getId() + " => " + document.getData());
        *                     }
        *                 } else {
        *                     Log.d(TAG, "Error getting documents: ", task.getException());
        *                 }
        *             }
        *         });
        */

        // (失败)通过某个查询条件获取doc, 自定义对象
        /**
         * db.collection("users").whereEqualTo("gender", "Male").
         *         get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
         *     @Override
         *     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
         *         User userInfo = (User) queryDocumentSnapshots.toObjects(User.class);
         *         System.out.println(userInfo);
         *     }
         * });
         */

        // 获取多个结果放在数组中

         db.collection("users")
             .whereEqualTo("gender", "Male")
             .get()
             .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     List<User> userBox = new ArrayList<>();
                     if (task.isSuccessful()) {
                         for (QueryDocumentSnapshot document : task.getResult()) {
                             Map<String, Object> info = document.getData();
                             User userInfo = new User(info.get("email").toString(),
                                                          info.get("username").toString(),
                                                          info.get("gender").toString());
                             Log.d(TAG, document.getId() + " => " + document.getData());
                             userBox.add(userInfo);
                         }

                     } else {
                         Log.d(TAG, "Error getting documents: ", task.getException());
                     }

                     System.out.println(userBox.size());
                 }
             });


        // 链接外键和主键
        // 通过存储的外键进行查询
        // 添加有已存在的用户的对象
        // 直接登录产生文件
        // 使用直接登录产生的文件


    }

    // Get
    // Get Source Cache
    public void testForListener(){

    }

    // Update
    // 更新嵌套对象

    // Delete

    public void initDialogueTestData() throws ParseException {
        for (int i=0; i<10; i++) {
            dialogueList.add(new Dialogue(
                    new User("test_Sender" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0),
                    new User("test_Receiver" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)
            ));
        }
    }

}
















