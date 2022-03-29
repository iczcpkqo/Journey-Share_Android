package com.journey.service.database;

import com.journey.adapter.ReadUserInfoFile;
import com.journey.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
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

}
