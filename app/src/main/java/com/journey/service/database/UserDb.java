package com.journey.service.database;


import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journey.entity.User;
import com.journey.tools.firebase.FirebaseUtil;

import java.util.Map;

public class UserDb implements Db<User> {
    private static final String collectionName = "users";

    private static FirebaseUtil firebaseUtil = FirebaseUtil.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static Map<String, Object> resultMap;

    private static UserDb instance;

    public static UserDb getInstance() {
        if (instance == null) {
            instance = new UserDb();
        }
        return instance;
    }

    @Override
    public String save(User user) {
        return firebaseUtil.insert(collectionName, user);
    }


    @Override
    public String updateByDocumentId(User user, String documentId) {
        Map<String, Object> result = firebaseUtil.selectByDocumentId(collectionName, documentId);
        if (result == null) {
            return "failed! not fund data";
        }
        String jsonString1 = JSON.toJSONString(user);
        Map map = JSON.parseObject(jsonString1, Map.class);
        String result2 = firebaseUtil.updateDocumentByDocumentId(map, collectionName, documentId);
        return result2;
    }

    @Override
    public String deleteByDocumentId(String documentId) {
        return firebaseUtil.deleteByDoucumentId(collectionName, documentId);
    }


    public Map<String, Object> selectByDocumentId(String documentId) {
        try {
            DocumentReference document = db.collection(collectionName).document(documentId);

            document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public synchronized void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        resultMap = task.getResult().getData();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return resultMap;
    }

}
