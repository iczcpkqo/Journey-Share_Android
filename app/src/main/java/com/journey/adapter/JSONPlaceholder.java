package com.journey.adapter;

import com.journey.model.Peer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-02-27-20:56
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public interface JSONPlaceholder {
    @GET("posts")
    Call<List<Peer>> getPeer();


    @POST("match")
    Call<Peer> postPeer();

    @DELETE("posts/{id}")
    Call<Void> deletePeer(@Path("id") int id);
}
