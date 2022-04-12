package com.journey.service.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.R;
import com.journey.adapter.ReadUserInfoFile;
import com.journey.entity.Dialogue;
import com.journey.entity.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DialogueHelper {

    // DONE: 创建单聊Dialogue
    // DONE: 创建群聊Dialogue
    // DONE: 去单聊
    // DONE: 去群聊

    // Done: 获取我是谁
    private static final String TAG = "DialogueHelper";

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

    public static void saveDynamicIp() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dynamic_ip").document("love")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String jsonDynamicIp = Objects.requireNonNull(JSON.toJSON(document.getData())).toString();
                                try {
                                    File fs = new File(android.os.Environment.getExternalStorageDirectory()+"/DynamicIp.txt");
                                    FileOutputStream outputStream = null;
                                    outputStream = new FileOutputStream(fs, false);
                                    outputStream.write(jsonDynamicIp.getBytes());
                                    outputStream.flush();
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });
    }

    public static String getDynamicIp() {
        return getDynamicLink().get("ip").toString();
    }

    public static String getDynamicPort() {
        return getDynamicLink().get("port").toString();
    }

    public static Map<String, Object> getDynamicLink() {
        File file = new File(android.os.Environment.getExternalStorageDirectory()+"/DynamicIp.txt");
        FileReader fis = null;
        BufferedReader br = null;
        Map<String, Object> DynamicIp = null;

        if(file.exists()) {
            try {
                fis = new FileReader(file);
                br = new BufferedReader(fis);
                String str = br.readLine();
                String jsonMsg = "";
                while(str != null){
                    jsonMsg += str;
                    str = br.readLine();
                }
                DynamicIp = JSON.parseObject(jsonMsg, new TypeReference<Map<String, Object>>(){});
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return DynamicIp;
    }

}
