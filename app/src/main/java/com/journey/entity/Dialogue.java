package com.journey.entity;

import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.journey.service.database.DialogueHelper;
import com.mapbox.mapboxsdk.style.expressions.Expression;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
public class Dialogue {
    private User sender;
    private ArrayList<User> receiver = new ArrayList<>();
    private ArrayList<User> playerList = new ArrayList<>();
    private List<CollectionReference> players;
    private Date createTime;
    private StringBuffer playerString = new StringBuffer();
    private StringBuffer dialogueTitle = new StringBuffer();
    private StringBuffer dialogueId = new StringBuffer();
    private StringBuffer type = new StringBuffer();
    private StringBuffer orderId = new StringBuffer();

    public Dialogue() throws ParseException {
        this.sender = DialogueHelper.getSender();
        this.playerList.add(this.sender);
        this.playerString = new StringBuffer(this.sender.getUsername());
    }

    public Dialogue(User sender) throws ParseException {
        this.sender = sender;
    }

    public Dialogue(User sender, User receiver) throws ParseException {
        setSender(sender);
        addReceiver(Collections.singletonList(receiver));
        this.dialogueTitle.append(this.receiver.get(0).getUsername());
//        this.lastTime = (new SimpleDateFormat("dd-mm-yyyy hh:mm:ss")).parse("11-11-2021 22:22:22");
    }

    public void setSender(User sender){
        this.sender = sender;
    }

    public User getSender(){ return this.sender; }

    public void setReceiver(ArrayList<User> receiver){
        this.receiver =  receiver;
    }

    public void addReceiver(List<User> receiver){
        this.receiver.addAll(receiver);
    }
    public  ArrayList<User> getReceiver(){ return this.receiver;}

    public String getPlayerString(){
        return this.playerString.toString();
    }

    public void setPlayerList(ArrayList<User> playerList) {
        playerList.removeIf(p->p.getEmail().equals(this.sender.getEmail()));
        this.receiver.clear();
        this.playerList = new ArrayList<>(Arrays.asList(this.sender));
        this.playerList.add(this.sender);
        this.playerString = new StringBuffer(this.sender.getUsername());
        this.dialogueTitle.setLength(0);
        this.dialogueId.setLength(0);
        this.type.setLength(0);

        for(User r : playerList)
            addPlayer(r);
    }

    public void addPlayer(User player) {
        if(this.sender.getEmail().equals(player.getEmail()))
            return;
        this.receiver.add(player);
        Collections.sort(this.receiver);
        this.playerList.add(player);
        Collections.sort(this.playerList);
        String s = this.playerString.append(",").append(player.getUsername()).toString();
        this.playerString.setLength(0);
        this.playerString.append(DialogueHelper.cleanDialogueString(DialogueHelper.sortString(s)));
//        this.dialogueTitle.append(this.dialogueTitle.length()==0?"":",").append(player.getUsername());
        this.dialogueTitle.setLength(0);
        this.dialogueTitle.append(DialogueHelper.userListToUserString(this.receiver));
        this.type = new StringBuffer(this.receiver.size() < 1? "single":"group");
    }

    public ArrayList<User> getPlayerList() { return playerList; }

    public void setDialogueId(String dialogueId) {
        this.dialogueId = new StringBuffer(dialogueId);
    }

    public String getDialogueId(){return this.dialogueId.toString();}

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setOrderId(String orderId) {
        this.orderId = new StringBuffer(orderId);
    }

    public String getOrderId() {
        return this.orderId.toString();
    }

    public void setType(String type) {
        this.type = new StringBuffer(type);
    }

    public String getType() {
        return type.toString();
    }

    public void setTitle(String dialogueTitle) {
        this.dialogueTitle = new StringBuffer(dialogueTitle);
    }

    public String getTitle(){
        return this.dialogueTitle.toString();
    }
}
