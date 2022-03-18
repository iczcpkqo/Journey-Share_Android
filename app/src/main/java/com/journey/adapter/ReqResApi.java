package com.journey.adapter;

import com.journey.model.Peer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReqResApi{
    @POST("matchLeader")
    Call<List<Peer>> createUser(@Body Peer peer);
}
