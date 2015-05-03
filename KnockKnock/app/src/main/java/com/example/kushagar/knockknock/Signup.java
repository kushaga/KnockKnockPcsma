package com.example.kushagar.knockknock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Signup extends ActionBarActivity {


    EditText uname,pwd,email;

    Button ok;

   ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Parse.initialize(this, "frOgjjRtLedrzGc3kZDTYwwFcI57ol6yoJdSE3Jf", "puGlxT7VKobSs5E4t1sYxgiAEyAQEMcFTtSruQ8C");


        uname=(EditText)findViewById(R.id.editText);
        pwd=(EditText)findViewById(R.id.editText2);
        email=(EditText)findViewById(R.id.editText3);

        ok=(Button)findViewById(R.id.button3);

        user= new ParseUser();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                user.setUsername(uname.getText().toString());
                user.setPassword(pwd.getText().toString());
                user.setEmail(email.getText().toString());
                SharedPreferences.Editor editor = getSharedPreferences("mypref",MODE_PRIVATE).edit();

                //put the user name and password in shared preferences
                editor.putString("username",uname.getText().toString() );
                editor.putString("password", pwd.getText().toString());
                editor.putString("user_email_id", email.getText().toString());
                editor.commit();
                String userid ="";

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {

                        if (e == null) {
                            Log.d("sfsfsf", email.getText().toString());

                            //post request to get userid from server

                            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                                @Override
                                public void intercept(RequestFacade request) {
                                    request.addHeader("Content-Type", "application/json");
                                    request.addHeader("Accept", "application/json");
                                }
                            };

                            UserTestApi demoService = new RestAdapter.Builder()
                                    .setEndpoint("http://192.168.50.241:8084")
                                    .setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                                        public void log(String msg) {
                                            Log.i("retrofit", msg);
                                        }
                                    })
                                    .build()
                                    .create(UserTestApi.class);

                            User user = new User();
                            user.setEmail_id(email.getText().toString());
                            user.setPassword(pwd.getText().toString());
                            user.setUsername(uname.getText().toString());

                            //Log.e("Putting fb event",event.getEvent_id());
                            final String[] userid = new String[1];

                            demoService.register(user, new Callback<String>() {
                                @Override
                                public void success(String s, Response response) {
                                    Log.e("signup_sucess:",response.toString());
                                    userid[0] =response.toString();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e("signup_failure",error.toString());
                                }
                            });

                            //Log.e("user_id",userid);

                            //put this in shared preferences
                            SharedPreferences.Editor editor = getSharedPreferences("mypref",MODE_PRIVATE).edit();

                            //put the user name and password in shared preferences
                            editor.putString("userid", userid[0]);
                            editor.commit();
                            Intent i = new Intent(Signup.this, MainActivity.class);
                            startActivity(i);

                        } else {
                            System.err.println(e);
                        }

                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
