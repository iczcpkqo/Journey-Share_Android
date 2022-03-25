package com.journey.map;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUserGroups {

    private static final String emailTag = "email";
    private static final String genderTag = "gender";
    private static final String ageTag = "age";
    private static final String scoreTag = "score";
    private static final String longitudeTag = "longitude";
    private static final String latitudeTag = "latitude";
    private static final String dLongitudeTag = "dLongtitude";
    private static final String dLatitudeTag = "dLatitude";
    private static final String  startTimeTag = "startTime";
    private static final String  endTimeTag = "endTime";
    private static final String  limitTag = "limit";
    private static final String  isLeaderTag = "isLeader";
    private String userListData;

    public ParseUserGroups(String jsonData) {
        userListData = jsonData;
    }


    public List<OrderUser> parseJsonArray()
    {
        List<OrderUser> orderUserList = new ArrayList<OrderUser>();
        try {
            JSONArray userList = new JSONArray(userListData);


            for (int i =0;i<userList.length();i++)
            {

                JSONObject jsonObject = userList.getJSONObject(i);
                String email = jsonObject.optString(emailTag);
                String gender = jsonObject.optString(genderTag);
                int age = jsonObject.optInt(ageTag);
                double score = jsonObject.optDouble(scoreTag);
                double longitude = jsonObject.optDouble(longitudeTag);
                double latitude = jsonObject.optDouble(latitudeTag);
                double dlongitude = jsonObject.optDouble(dLongitudeTag);
                double dLatitude = jsonObject.optDouble(dLatitudeTag);
                long startTime = jsonObject.optLong(startTimeTag);
                long endTime = jsonObject.optLong(endTimeTag);
                int limit = jsonObject.optInt(limitTag);
                boolean isLeader = jsonObject.optBoolean(isLeaderTag);
                orderUserList.add( new OrderUser(email,gender,age,score,longitude,latitude,dlongitude,dLatitude,startTime,endTime,limit,isLeader));
            }
        }catch (Exception ex){

        }

        return orderUserList;

    }
}
