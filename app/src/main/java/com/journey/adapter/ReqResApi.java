package com.journey.adapter;

import com.journey.map.OrderUser;
import com.journey.model.Peer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ReqResApi{
    @POST("isLeader")
    Call<Peer> isLeader(@Body Peer peer);
    @POST("matchLeader")
    Call<List<Peer>> createUser(@Body Peer peer);
    @POST("matchMember")
    Call<List<Peer>> matchUser(@Body Peer peer);
}
