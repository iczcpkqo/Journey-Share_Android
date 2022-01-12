package com.journey.service.database;


import com.alibaba.fastjson.JSON;
import com.journey.entity.User;
import com.journey.tools.firebase.FirebaseUtil;

import java.util.Map;

public class UserDb implements Db<User> {
    private static final String collectionName = "users";

    private static FirebaseUtil firebaseUtil = FirebaseUtil.getInstance();

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


    public User selectByDocumentId(String documentId) {
        User user = null;
        Map<String, Object> map = firebaseUtil.selectByDocumentId(collectionName, documentId);
        if (map == null) {
            return user;
        }
        String jsonString1 = JSON.toJSONString(map);
        user = JSON.parseObject(jsonString1, User.class);
        return user;
    }


}
