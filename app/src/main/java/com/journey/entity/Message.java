package com.journey.entity;
import java.util.Date;

public class Message {
    private User sender;
    private User receiver;

    public String senderName;
    public String receiverName;
    public String text;
    public Date time;

    public int type;


    public Message(User sender, User receiver, String text, Date time, int type){
        this.sender = setSender(sender);
        this.receiver = setReceiver(receiver);
        this.text = text;
        this.type = type;
        this.time = time;

        this.senderName = this.sender.getUsername();
        this.receiverName = this.receiver.getUsername();

    }

    public Message(User sender, User receiver, String text, Date time){
        this(sender, receiver, text, time, 1);
    }

    public Message(String text, int type){

        this(new User("test_name_111", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0),
             new User("test_name_111", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0),
             text, new Date(), type);
    }

    public User getSender(){ return this.sender; }

    public User setSender(User sender){ return this.sender = sender; }

    public User getReceiver(){ return this.receiver; }

    public User setReceiver(User receiver){ return  this.receiver = receiver; }
}

//import com.google.type.DateTime;
//
//public class Message {
//    private String message_id;
//    private User message_from;
//    private User message_to;
//    private DateTime message_time;
//
//    public Message(String message_id, User message_from, User message_to, DateTime message_time) {
//        this.message_id = message_id;
//        this.message_from = message_from;
//        this.message_to = message_to;
//        this.message_time = message_time;
//    }
//
//    public String getMessage_id() {
//        return message_id;
//    }
//
//    public void setMessage_id(String message_id) {
//        this.message_id = message_id;
//    }
//
//    public User getMessage_from() {
//        return message_from;
//    }
//
//    public void setMessage_from(User message_from) {
//        this.message_from = message_from;
//    }
//
//    public User getMessage_to() {
//        return message_to;
//    }
//
//    public void setMessage_to(User message_to) {
//        this.message_to = message_to;
//    }
//
//    public DateTime getMessage_time() {
//        return message_time;
//    }
//
//    public void setMessage_time(DateTime message_time) {
//        this.message_time = message_time;
//    }

