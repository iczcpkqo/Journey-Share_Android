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
        this(new User("test_name_111", "1231231", new Date()),
             new User("test_name_222", "1231231", new Date()),
             text, new Date(), type);
    }

    public User getSender(){ return this.sender; }

    public User setSender(User sender){ return this.sender = sender; }

    public User getReceiver(){ return this.receiver; }

    public User setReceiver(User receiver){ return  this.receiver = receiver; }
}
