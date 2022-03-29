package com.journey.entity;

import com.journey.service.database.DialogueHelper;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue, Chat
 */
public class Msg {
    private Sender sender;
    private String content;
    private String dialogueId;
    private long time;

    private class Sender{
        private String email;
        private String username;
        private String gender;

        public Sender(String email, String username, String gender){
            this.email = email;
            this.username = username;
            this.gender = gender;
        }
        public String getEmail() { return email; }
        public String getUsername() { return username; }
        public String getGender() { return gender; }
    }

    public Msg(String content) {
        User userInfo = DialogueHelper.getSender();
        this.sender = new Sender(userInfo.getEmail(), userInfo.getUsername(), userInfo.getGender());
        this.content = content;
        this.time = System.currentTimeMillis();
        this.dialogueId = "TEST-DATA-"+this.time;
    }


    public Msg(String content, String dialogueId){
        User userInfo = DialogueHelper.getSender();
        this.sender = new Sender(userInfo.getEmail(), userInfo.getUsername(), userInfo.getGender());
        this.content = content;
        this.time = System.currentTimeMillis();
        this.dialogueId = dialogueId;
    }



    public Sender getSender() {
        return sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setDialogueId(String dialogueId) {
        this.dialogueId = dialogueId;
    }

    public String getDialogueId() {
        return dialogueId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
