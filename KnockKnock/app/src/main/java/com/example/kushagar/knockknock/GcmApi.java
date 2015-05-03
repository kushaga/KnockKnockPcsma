package com.example.kushagar.knockknock;

/**
 * Created by Kushagar on 4/16/2015.
 */
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface GcmApi {

    @POST("/gcm")
    @FormUrlEncoded
    public void postData(@Field("shareRegId")String share,@Field("regId") String regId,@Field("message") String userMessage, Callback<String> cb);
}
