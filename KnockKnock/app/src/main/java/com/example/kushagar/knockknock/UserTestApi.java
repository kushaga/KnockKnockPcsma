package com.example.kushagar.knockknock;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Kushagar on 4/15/2015.
 */
public interface UserTestApi {
    @POST("/user/register")
    public void register(@Body User user , Callback<String> cb);
}
