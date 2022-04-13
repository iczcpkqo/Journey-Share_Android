package com.journey.adapter;

import com.journey.model.Peer;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReqResApi{
    @POST("isLeader")
    Call<Peer> isLeader(@Body Peer peer);
    @POST("matchLeader")
    Call<List<Peer>> matchLeader(@Body Peer peer);
    @POST("matchMember")
    Call<List<Peer>> matchMemeber(@Body Peer peer);
}
