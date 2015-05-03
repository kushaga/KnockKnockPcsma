package com.example.kushagar.knockknock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.io.IOException;


public class login extends ActionBarActivity {
    EditText luser,lpwd;
    Button loginbtn;
    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    static final String TAG = "Register Activity";

    private class ProgressTask extends AsyncTask<String , Void , Boolean> {
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
        }

        @Override
        protected Boolean doInBackground(String... params) {
            SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
            String username = sharedPreferences.getString("username",null);
            String password = sharedPreferences.getString("password",null);

            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null) {
                        boolean status = parseUser.getBoolean("emailVerified");
                        System.out.println(status);
                        if (status == true) {
                            Log.d("Takef", "In the App");
                            Intent intent = new Intent(getApplicationContext(), ApplicationDashboard.class);
                            intent.putExtra("regId", regId);
                            Log.e("regid",regId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(login.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.err.println(e);
                    }
                }
            });

            return null;
        }
    }
    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        String password = sharedPreferences.getString("password",null);
        context = getApplicationContext();

        /*if(username!=null&& password!=null){
            setContentView(R.layout.login_other);
            new ProgressTask(login.this).execute();
        }*/

        //else {
            setContentView(R.layout.activity_login);

            luser = (EditText) findViewById(R.id.editText4);
            lpwd = (EditText) findViewById(R.id.editText5);


            if (TextUtils.isEmpty(regId)) {
                regId = registerGCM();
                Log.d("RegisterActivity", "GCM RegId: " + regId);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Already Registered with GCM Server!",
                        Toast.LENGTH_LONG).show();
            }

            loginbtn = (Button) findViewById(R.id.button4);

            Parse.initialize(this, "frOgjjRtLedrzGc3kZDTYwwFcI57ol6yoJdSE3Jf", "puGlxT7VKobSs5E4t1sYxgiAEyAQEMcFTtSruQ8C");

            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseUser.logInInBackground(luser.getText().toString(), lpwd.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {

                            if (parseUser != null) {
                                boolean status = parseUser.getBoolean("emailVerified");
                                System.out.println(status);
                                if (status == true) {
                                    Log.d("Tafef", "In the App");

                                    Intent intent = new Intent(getApplicationContext(), ApplicationDashboard.class);
                                    intent.putExtra("regId", regId);
                                    Log.e("regid",regId);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(login.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                System.err.println(e);
                            }

                        }
                    });
                }
            });

        //}

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
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
