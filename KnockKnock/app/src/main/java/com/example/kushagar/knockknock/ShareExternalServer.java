package com.example.kushagar.knockknock;

import android.content.Context;
import android.util.Log;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Kushagar on 4/16/2015.
 */
public class ShareExternalServer {

    public String shareRegIdWithAppServer(final Context context,
                                          final String regId) {

        GcmApi demoService = new RestAdapter.Builder()
                .setEndpoint("http://192.168.50.241:8084")
                .build()
                .create(GcmApi.class);

        demoService.postData("1",regId,"messageee",new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("Upload", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Upload/fail", error.toString());
            }
        });
        return regId;

    }

}