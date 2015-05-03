package com.example.kushagar.knockknock;

/**
 * Created by Kushagar on 4/12/2015.
 */
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface FacebookTestApi {

    @POST("/fbevent/{userid}")
    public void addfbevent(@Path("userid") int userid, @Body Event event, Callback<String> cb);

    @GET("/fbevent/{userid}")
    public List<Event> getfbevent(@Path("userid") int userid);
}
