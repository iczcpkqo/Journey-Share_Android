package com.journey.service.database;

import com.journey.adapter.ReadUserInfoFile;
import com.journey.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DialogueHelper {

    // TODO: 创建单聊Dialogue
    // TODO: 创建群聊Dialogue
    // TODO: 去单聊
    // TODO: 去群聊

    // Done: 获取我是谁
    public static User getSender(){
        Map<String,Object> userInfo = new ReadUserInfoFile().readUserFile();
        System.out.println(userInfo);
        User sender = new User(userInfo.get("email").toString(),
                userInfo.get("username").toString(),
                userInfo.get("gender").toString());
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

}
