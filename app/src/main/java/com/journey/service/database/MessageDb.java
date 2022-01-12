package com.journey.service.database;

import com.journey.entity.Message;
import com.journey.tools.firebase.FirebaseUtil;

public class MessageDb implements Db<Message> {
    private static final String collectionName = "message";

    private static FirebaseUtil firebaseUtil = FirebaseUtil.getInstance();

    private static MessageDb instance;

    public static MessageDb getInstance() {
        if (instance == null) {
            instance = new MessageDb();
        }
        return instance;
    }

    @Override
    public String save(Message message) {
        return null;
    }

    @Override
    public String updateByDocumentId(Message message, String id) {
        return null;
    }

    @Override
    public String deleteByDocumentId(String id) {
        return null;
    }

    @Override
    public Message selectByDocumentId(String id) {
        return null;
    }
}
