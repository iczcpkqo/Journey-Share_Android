package com.journey.entity;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.journey.entity.User;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
public class Dialogue {
    private User sender;
    private ArrayList<User> receiver = new ArrayList<>() ;
    private Date lastTime;

    public String dialogueTitle;
    // TODO: 增加最近消息, 会话类型

    public Dialogue(User sender, User receiver) throws ParseException {
        setSender(sender);
        setReceiver(receiver);
        this.dialogueTitle = this.receiver.get(0).getUsername();
//        this.lastTime = (new SimpleDateFormat("dd-mm-yyyy hh:mm:ss")).parse("11-11-2021 22:22:22");
    }

    public User getSender(){ return this.sender; }

    public User setSender(User sender){ return this.sender = sender; }

    public  ArrayList<User> getReceiver(){ return this.receiver;}

    public ArrayList<User> setReceiver(User receiver){
        this.receiver.add(receiver);
        return this.receiver;
    }

    public String getTitle(){
        return dialogueTitle;
    }
}
