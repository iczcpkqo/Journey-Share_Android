package com.journey.service;


import com.alibaba.fastjson.JSON;
import com.journey.entity.User;
import com.journey.firebase.FirebaseUtil;

import java.util.Map;

public class UserDb {
    private static final String collectionName = "users";

    private static FirebaseUtil firebaseUtil = FirebaseUtil.getInstance();

    private static UserDb instance;

    public static UserDb getInstance() {
        if (instance == null) {
            instance = new UserDb();
        }
        return instance;
    }

    public String saveUser(User user) {
        return firebaseUtil.insert(collectionName, user);
    }


    public String updateUserByDocumentId(User user2, String documentId) {
        Map<String, Object> result = selectByDocumentId(documentId);
        if (result == null) {
            return "failed! not fund data";
        }
        String jsonString1 = JSON.toJSONString(user2);
        Map map = JSON.parseObject(jsonString1, Map.class);
        String result2 = firebaseUtil.updateDocumentByDocumentId(map, collectionName, documentId);
        return result2;
    }

    private Map<String, Object> selectByDocumentId(String documentId) {
        return firebaseUtil.selectByDocumentId(collectionName, documentId);
    }
}
