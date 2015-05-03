package com.example.kushagar.knockknock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    Button loginButton,signupbutton;
    String app_id ;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    String mypref = "my_pref";
    //review guidelines-- https://developers.facebook.com/docs/apps/review
    final AccessToken[] token = new AccessToken[1];
    HashMap<String,Boolean>map_events = new HashMap<>();
    HashMap<String,HashMap<String,String>> get_response = new HashMap<>();
    JSONArray jsonArray_all_events = null , jsonArray_acad_events =null ,jsonArray_btech_labs_events=null ;
    ArrayList<Event>fb_events = new ArrayList<>();
    String user_id="";

    private class ProgressTask extends AsyncTask<String , Void , Boolean>{
        private ProgressDialog dialog;
        private Context context;
        private ActionBarActivity activity ;

        public ProgressTask(ActionBarActivity a){
            this.activity =a;
            context = a;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Started>.......");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(dialog.isShowing()){
                this.dialog.dismiss();
            }
            final JSONArray[] jsonArray = new JSONArray[1];


            new GraphRequest(token[0],
                    "/"+token[0].getUserId()+"/events",
                    null,
                    HttpMethod.GET,new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    System.out.println("result:"+graphResponse.getJSONObject().toString());
                    JSONObject jsonObject = graphResponse.getJSONObject();
                    Log.w("jsonobject",":"+jsonObject);
                    try {
                        jsonArray[0] = jsonObject.getJSONArray("data");
                        Log.w("jsonarray",":"+ jsonArray[0].toString());

                        for(int i=0;i<jsonArray[0].length();i++){

                            JSONObject jsonObject1 = null;

                            try {
                                jsonObject1 = jsonArray[0].getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            assert jsonObject1 != null;

                            try {
                                final String id = jsonObject1.getString("id");

                                if(!(map_events.containsKey(id))){//do not add same events
                                    continue;
                                }


                                new GraphRequest(token[0],
                                        "/"+id,
                                        null,
                                        HttpMethod.GET,new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse graphResponse) {
                                        JSONObject jsonObject2 = null;
                                        try {
                                            jsonObject2 = new JSONObject(graphResponse.getRawResponse());
                                            //Log.e("json_object", String.valueOf(jsonObject2));
                                            //Log.e("description",jsonObject2.getString("description"));
                                        }catch (Exception e){

                                        }

                                        Log.d("event-details:",graphResponse.toString());
                                        JSONObject event_json_object = graphResponse.getJSONObject();
                                        JSONArray jsonArray1 = graphResponse.getJSONArray();
                                        if(jsonArray1!=null) {
                                            Log.e("event_json_object",jsonArray1.toString());
                                            if(event_json_object!=null){
                                                Log.e("event_json_object",event_json_object.toString());
                                            }
                                        }

                                        HashMap<String,String> event_get = get_response.get(id);
                                        Event event = new Event();
                                        event.setUser_id("1");
                                        event.setEvent_name(event_get.get("name"));
                                        event.setTime(event_get.get("start_time"));
                                        event.setRoom_number(event_get.get("location"));
                                        event.setEvent_id("1234");
                                        event.setEvent_category("private");
                                        fb_events.add(event);


                                        assert jsonObject2 != null;
                                        try {
                                            event.setEvent_description(jsonObject2.getString("description"));
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                        RequestInterceptor requestInterceptor = new RequestInterceptor() {
                                            @Override
                                            public void intercept(RequestFacade request) {
                                                request.addHeader("Content-Type", "application/json");
                                                request.addHeader("Accept", "application/json");
                                            }
                                        };

                                        FacebookTestApi demoService = new RestAdapter.Builder()
                                                .setEndpoint("http://192.168.50.241:8084")
                                                .setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                                                    public void log(String msg) {
                                                        Log.i("retrofit", msg);
                                                    }
                                                })
                                                .build()
                                                .create(FacebookTestApi.class);


                                            Log.e("Putting fb event",event.getEvent_id());
                                            demoService.addfbevent(1, event, new Callback<String>() {
                                                @Override
                                                public void success(String s, Response response) {
                                                    Log.e("Success", response.toString());
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    Log.e("Failure", error.toString());
                                                }

                                            });

                                    }
                                }   ).executeAsync();

                                post_fb_events_server();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }   ).executeAsync();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            getmygson();
            return null;
        }
    }

    public void post_fb_events_server(){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
            }
        };

        FacebookTestApi demoService = new RestAdapter.Builder()
                .setEndpoint("http://192.168.50.241:8084")
                .setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        Log.i("retrofit", msg);
                    }
                })
                .build()
                .create(FacebookTestApi.class);


        for(Event fb_event : fb_events){
                Log.e("Putting fb event",fb_event.getEvent_id());
                demoService.addfbevent(1, fb_event, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    Log.e("Success", response.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Failure", error.toString());
                }

            });
        }
    }

    public void getmygson(){
        JSONParser jsonParser = new JSONParser();
        String url_server_all_iiit_events = "https://graph.facebook.com/search?q=IIIT-Delhi&type=event&access_token="+token[0].getToken().toString();
        String url_server_acad_iiitd_events = "https://graph.facebook.com/search?q=Academic%20Building,%20IIITD&type=event&access_token="+token[0].getToken().toString();
        String url_server_btech_labs_iiitd_events = "https://graph.facebook.com/search?q=BTech%20Labs,%20IIIT-Delhi&type=event&access_token="+token[0].getToken().toString();

        JSONObject jsonObject_all_events = jsonParser.getJSONFromUrl(url_server_all_iiit_events);
        JSONObject jsonObject_acad_events = jsonParser.getJSONFromUrl(url_server_acad_iiitd_events);
        //System.out.println(jsonObject_acad_events.toString());
        JSONObject jsonObject_btech_labs_events = jsonParser.getJSONFromUrl(url_server_btech_labs_iiitd_events);

        try {
            jsonArray_all_events = jsonObject_all_events.getJSONArray("data");
            jsonArray_acad_events = jsonObject_acad_events.getJSONArray("data");
            jsonArray_btech_labs_events = jsonObject_btech_labs_events.getJSONArray("data");

            //System.out.println(jsonArray_all_events.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsonArray_acad_events!=null) {
            Log.e("acad_events",jsonArray_acad_events.toString());
            for (int i = 0; i < jsonArray_acad_events.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray_acad_events.getJSONObject(i);

                    System.out.println(jsonObject.toString());
                    String name = jsonObject.getString("name");
                    String start_time = jsonObject.getString("start_time");
                    String location = jsonObject.getString("location");
                    String id = jsonObject.getString("id");

                    HashMap<String,String>map = new HashMap<>();
                    map.put("name",name);
                    map.put("start_time",start_time);
                    map.put("location","C01");//set default acad location to C01
                    map.put("id",id);
                    map_events.put(id,true);
                    get_response.put(id, map);

                    //get event details
                    new GraphRequest(token[0],
                            "/"+id,
                            null,
                            HttpMethod.GET,new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {

                            Log.e("event-details:",graphResponse.toString());

                        }
                    }   ).executeAsync();


                } catch (Exception e) {
                    Log.d("exception", e.toString());
                    e.printStackTrace();
                }
            }
        }

        if(jsonArray_btech_labs_events!=null) {
            for (int i = 0; i < jsonArray_btech_labs_events.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray_btech_labs_events.getJSONObject(i);

                    System.out.println(jsonObject.toString());
                    String name = jsonObject.getString("name");
                    String start_time = jsonObject.getString("start_time");
                    String location = jsonObject.getString("location");
                    String id = jsonObject.getString("id");

                    HashMap<String,String>map = new HashMap<>();
                    map.put("name",name);
                    map.put("start_time",start_time);
                    map.put("location","BTech Labs");//set default acad location to C01
                    map.put("id",id);
                    map_events.put(id,true);
                    get_response.put(id, map);

                } catch (Exception e) {
                    Log.d("exception", e.toString());
                    e.printStackTrace();
                }
            }
        }

        if(jsonArray_all_events!=null) {
            for (int i = 0; i < jsonArray_all_events.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray_all_events.getJSONObject(i);

                    System.out.println(jsonObject.toString());
                    String id = jsonObject.getString("id");
                    new GraphRequest(token[0],
                            "/"+id,
                            null,
                            HttpMethod.GET,new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {

                            Log.e("event-details:",graphResponse.toString());

                        }
                    }   ).executeAsync();

                    //find if the entry already exists in the hashmap or not
                    if(map_events.containsKey(id)){//do not add same events
                        continue;
                    }
                    Log.e("tag","puttong values in hash map");
                    String name = jsonObject.getString("name");
                    String start_time = jsonObject.getString("start_time");
                    String location = jsonObject.getString("location");

                    HashMap<String,String>map = new HashMap<>();
                    map.put("name",name);
                    map.put("start_time",start_time);
                    map.put("location","IIIT-DELHI");//set default acad location to C01
                    map.put("id",id);
                    map_events.put(id,true);
                    get_response.put(id,map);

                } catch (Exception e) {
                    Log.d("exception", e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    public void intialise_facebook(){

        Log.d("intialise","intialise_facebook");

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("user_events"));


        final JSONArray[] jsonArray = new JSONArray[1];


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    final AccessToken currentAccessToken) {
                // App code
                Log.w("token:",currentAccessToken.getToken());
                Log.w("token_date:",currentAccessToken.getExpires().toString());
                Log.w("user_id:",currentAccessToken.getUserId());
                Log.w("permissions:",currentAccessToken.getPermissions().toString());

                token[0] = currentAccessToken;

                SharedPreferences.Editor editor = getSharedPreferences(mypref,MODE_PRIVATE).edit();

                editor.putString("access_token",currentAccessToken.getToken());
                editor.putString("token_exp_date",currentAccessToken.getExpires().toString());
                editor.putString("token_user_id",currentAccessToken.getUserId());
                editor.commit();

                Log.w("tag :","commited");

                new ProgressTask(MainActivity.this).execute();

                // getmygson();

            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        loginButton =(Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);

        signupbutton =(Button) findViewById(R.id.button_signup);
        signupbutton.setOnClickListener(this);


        Log.d("tag","here");

        app_id = getString(R.string.facebook_app_id);


        //FacebookSdk.sdkInitialize(getApplicationContext());

        //intialise_facebook();

/*        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.kushagar.knockknock",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_login:
                //Log.d("tag","here3");

                //check if the user info is already there in shared preferences

                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
                //Intent intent = new Intent(this,Dashboard.class);
                intialise_facebook();
                Intent intent = new Intent(this,login.class);

                startActivity(intent);
                break;
            case R.id.button_signup:
                Intent intent1 = new Intent(this,Signup.class);
                startActivity(intent1);
                break;
        }
    }

}
