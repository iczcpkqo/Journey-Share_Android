package com.journey.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.journey.entity.User;

public class Dialogue {
    private User sender;
    private User receiver;
    private Date lastTime;

    public String dialogueTitle;

    public Dialogue(User sender, User receiver) throws ParseException {
        this.sender = setSender(sender);
        this.receiver = setReceiver(receiver);
        this.dialogueTitle = this.receiver.getUsername();
//        this.lastTime = (new SimpleDateFormat("dd-mm-yyyy hh:mm:ss")).parse("11-11-2021 22:22:22");
    }

    public User getSender(){ return this.sender; }

    public User setSender(User sender){ return this.sender = sender; }

    public User getReceiver(){ return this.receiver; }

    public User setReceiver(User receiver){ return  this.receiver = receiver; }
}
