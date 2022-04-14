package com.journey.adapter;

import com.journey.model.ConditionInfo;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseCondition {

    public static final String emailTag = "email";;
    public static final String dateTimeTag = "dateTime";
    public static final String originAddressTag = "originAddress";
    public static final String endAddressTag = "endAddress";
    public static final String origin_lonTag = "origin_lon";
    public static final String origin_latTag = "origin_lat";
    public static final String end_lonTag = "end_lon";
    public static final String end_latTag = "end_lat";
    public static final String maxAgeTag = "maxAge";
    public static final String minAgeTag = "minAge";
    public static final String minScoreTag = "minScore";
    public static final String preferGenderTag = "preferGender";
    public static final String journeyModeTag = "journeyMode";
    public static final String routeTag = "route";
    private final String conInfoJson;

    public ParseCondition(String jsonData) {
        conInfoJson = jsonData;
    }

    public List<ConditionInfo> parseJsonArray()
    {
        List<ConditionInfo> InfoList = new ArrayList<>();
        try {
            JSONArray dailyList = new JSONArray(conInfoJson);
            for (int i =0;i<dailyList.length();i++)
            {
                JSONObject jsonObject = dailyList.getJSONObject(i);
                String email = jsonObject.optString(emailTag);
                String dateTime = jsonObject.optString(dateTimeTag);
                String originAddress = jsonObject.optString(originAddressTag);
                String endAddress = jsonObject.optString(endAddressTag);
                String origin_lon = jsonObject.optString(origin_lonTag);
                String origin_lat = jsonObject.optString(origin_latTag);
                String end_lon = jsonObject.optString(end_lonTag);
                String end_lat = jsonObject.optString(end_latTag);
                String maxAge = jsonObject.optString(maxAgeTag);
                String minAge = jsonObject.optString(minAgeTag);
                String minScore = jsonObject.optString(minScoreTag);
                String preferGender = jsonObject.optString(preferGenderTag);
                String journeyMode = jsonObject.optString(journeyModeTag);
                String route = jsonObject.optString(routeTag);
                InfoList.add( new ConditionInfo(email,
                                                dateTime,
                                                originAddress,
                                                endAddress,
                                                origin_lon,
                                                origin_lat,
                                                end_lon,
                                                end_lat,
                                                maxAge,
                                                minAge,
                                                minScore,
                                                preferGender,
                                                journeyMode,
                                                route));
                                                }
        }catch (Exception ex){

        }
        return InfoList;

    }
}

