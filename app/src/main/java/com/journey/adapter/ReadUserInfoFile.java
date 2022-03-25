package com.journey.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class ReadUserInfoFile {
    public Map<String, Object> readUserFile() {
        File file = new File(android.os.Environment.getExternalStorageDirectory()+"/UserInformation.txt");
        FileReader fis = null;
        BufferedReader br = null;
        Map<String, Object> map = null;
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
                System.out.println(jsonMsg);
                map = JSON.parseObject(jsonMsg, new TypeReference<Map<String, Object>>(){});
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
        return map;
    }

}
