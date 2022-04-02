package com.journey.service.database;

import com.journey.R;
import com.journey.adapter.ReadUserInfoFile;
import com.journey.entity.Dialogue;
import com.journey.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DialogueHelper {

    // DONE: 创建单聊Dialogue
    // DONE: 创建群聊Dialogue
    // DONE: 去单聊
    // DONE: 去群聊

    // Done: 获取我是谁
    public static User getSender(){
        Map<String,Object> userInfo = new ReadUserInfoFile().readUserFile();
        User sender = new User(userInfo.get("email").toString(),
                userInfo.get("username").toString(),
                userInfo.get("gender").toString());
        sender.setAge((Integer)userInfo.get("age"));
        sender.setBirthDate(userInfo.get("birthDate").toString());
        sender.setEmail(userInfo.get("email").toString());
        sender.setGender(userInfo.get("gender").toString());
        sender.setMark(Double.valueOf(userInfo.get("mark").toString()));
        sender.setOrder(Integer.valueOf(userInfo.get("order").toString()));
        sender.setPhone(userInfo.get("phone").toString());

        return sender;
    }

    // Done: 特殊格式字符串转数组
    public static List<String> convertStringToList(String player){
        return  Arrays.asList(player.replaceAll("\\[|\\]|\\s","").split(","));
    }
    // Done: 特殊格式字符串转数组
    public static String cleanDialogueString(String title){
        return  title.replaceAll("\\[|\\]|\\s","");
    }

    public static String sortString(String str){
        String s = DialogueHelper.cleanDialogueString(str);
        ArrayList<String> arrStr = new ArrayList<>(Arrays.asList(s.split(",")));
        Collections.sort(arrStr);
        return arrStr.toString();
    }

    public static String userListToUserString(List<User> user){
        StringBuffer s = new StringBuffer();
        for(User u : user)
            s.append(s.toString().equals("")? u.getUsername() : ","+u.getUsername());
        return s.toString();
    }

    public static void putIntoRightDialogue(Dialogue dialogue, List<Dialogue> dialogueList){
        for(Dialogue dia : dialogueList)
            if (dialogue.getDialogueId() == dia.getDialogueId()) {
                dia = dialogue;
                return;
            }
    }


    public static List<Integer> getHeadCupboard(){
        List<Integer> headCupboard = new ArrayList<>();
        headCupboard.add(R.drawable.h_0);
        headCupboard.add(R.drawable.h_1);
        headCupboard.add(R.drawable.h_2);
        headCupboard.add(R.drawable.h_3);
        headCupboard.add(R.drawable.h_4);
        headCupboard.add(R.drawable.h_5);
        headCupboard.add(R.drawable.h_6);
        headCupboard.add(R.drawable.h_7);
        headCupboard.add(R.drawable.h_8);
        headCupboard.add(R.drawable.h_g);
        headCupboard.add(R.drawable.h_nijita);
        return headCupboard;
    }
}
