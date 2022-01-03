package com.journey.entity;

import java.util.Date;
import com.journey.entity.User;

public class Message {
    private User sender;
    private User receiver;

    public String senderName;
    public String receiverName;
    public String text;
    public Date time;

    public Message(User sender, User receiver, String text, Date time){
        this.sender = setSender(sender);
        this.receiver = setReceiver(receiver);
        this.text = text;
        this.time = time;

        this.senderName = this.sender.getUsername();
        this.receiverName = this.receiver.getUsername();
    }

    public User getSender(){ return this.sender; }

    public User setSender(User sender){ return this.sender = sender; }

    public User getReceiver(){ return this.receiver; }

    public User setReceiver(User receiver){ return  this.receiver = receiver; }
}
