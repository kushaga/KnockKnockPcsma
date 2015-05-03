package com.example.kushagar.knockknock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;


public class ApplicationDashboard extends NavigationLiveo implements NavigationLiveoListener {

    public List<String> mListNameItem;
    String url ="http://192.168.50.241:8084/fbevent/1";
    ArrayList<Event>events;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    DashboardFragment mFragment = null;
    int id;


    ShareExternalServer appUtil;
    String regId;
    AsyncTask<Void, Void, String> shareRegidTask;

    private class ProgressTask extends AsyncTask<String , Void , Boolean> {
        private ProgressDialog dialog;
        private Context context;
        private ActionBarActivity activity ;
        ArrayList<Event> e ;

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
            if (mFragment != null){
                mFragmentManager.beginTransaction().replace(id, mFragment).commit();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            MyJsonParser jsonParser = new MyJsonParser();
            JSONArray jsonArray = jsonParser.getJSONFromUrl(url);
            Log.e("events_net",jsonArray.toString());

            e = new ArrayList<Event>();


            /*Event e1 = new Event();
            e1.setEvent_category("Private");
            e1.setUser_id("1");
            e1.setEvent_name("name");
            e1.setEvent_description("descp");
            e1.setEvent_id("1234");
            e1.setRoom_number("C01");
            e1.setTime("time");

            Event e2 = new Event();
            e2.setEvent_category("Public");
            e2.setUser_id("1");
            e2.setEvent_name("name");
            e2.setEvent_description("descp");
            e2.setEvent_id("1234");
            e2.setRoom_number("C01");
            e2.setTime("time");
            e.add(e1);
            e.add(e2);*/

            //Log.e("Json_array_response",jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                try{
                    Event sevent = new Event();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    sevent.setEvent_category(jsonObject.getString("event_category"));
                    sevent.setUser_id(jsonObject.getString("user_id"));
                    sevent.setEvent_name(jsonObject.getString("event_name"));
                    sevent.setEvent_description(jsonObject.getString("event_description"));
                    sevent.setEvent_id(jsonObject.getString("event_id"));
                    sevent.setRoom_number(jsonObject.getString("room_number"));
                    sevent.setTime(jsonObject.getString("time"));
                    Log.e("e_desp",sevent.getEvent_description());
                    e.add(sevent);

                }catch(Exception e){
                    Log.d("exception", e.toString());
                    e.printStackTrace();
                }
            }

            /*Bundle extras = new Bundle();
            String json=SendEvent.serialize(e);
            extras.putString("data",json);*/

            SendEvent.setArrayList(e);

            mFragment = new DashboardFragment().newinstance("all_events",e);
            //mFragment.setArguments(extras);
            return null;
        }
    }
    @Override
    public void onUserInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        String email_id = sharedPreferences.getString("user_email_id",null);
        this.mUserName.setText(username);
        this.mUserEmail.setText(email_id);
        this.mUserPhoto.setImageResource(R.drawable.ic_no_user);
        this.mUserBackground.setImageResource(R.drawable.ic_user_background);
    }

    @Override
    public void onInt(Bundle bundle) {

        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(1);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, "See all events");
        mListNameItem.add(1, "See all Facebook events");
        mListNameItem.add(2, "See all Public Events");

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_send_black_24dp);
        mListIconItem.add(1, R.drawable.ic_send_black_24dp); //Item no icon set 0
        mListIconItem.add(2, R.drawable.ic_send_black_24dp);

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
        mListHeaderItem.add(4);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 123);
        mSparseCounterItem.put(6, 250);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer("Log-out", R.drawable.ic_delete_black_24dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_application_dashboard);
        appUtil = new ShareExternalServer();

        regId = getIntent().getStringExtra("regId");
        Log.d("MainActivity", "regId: " + regId);

        final Context context = this;

        shareRegidTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = appUtil.shareRegIdWithAppServer(context, regId);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                shareRegidTask = null;
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_application_dashboard, menu);
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
    }*/

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {
        //
        id = layoutContainerId;
        Log.e("cointainerid",layoutContainerId+"");

        SendEvent.setLayout_id(id);

        switch (position){
            case 0:
                //call the progress task over here
                url = "http://192.168.50.241:8084/fbevent";
                new ProgressTask(ApplicationDashboard.this).execute();
                                //mFragment = new DashboardFragment().newinstance("all_evenets",);
                //Log.e("on click", "on 0 clicked");
                break;
            case 1:
                //mFragment = new FragmentMain().newInstance(mListNameItem.get(position));
                url = "http://192.168.50.241:8084/fbevent/1";
                new ProgressTask(ApplicationDashboard.this).execute();
                Log.e("on click","on 1 clicked");
                break;
            case 2:
                //ArrayList<Event>event= new ArrayList<>();
                //mFragment = new FragmentMain().newInstance1(mListNameItem.get(position),event);
                url = "http://192.168.50.241:8084/fbevent";
                new ProgressTask(ApplicationDashboard.this).execute();
                Log.e("on click","on 2 clicked");
                break;

        }
        /*if (mFragment != null){
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }*/
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int i, boolean b) {

    }

    @Override
    public void onClickFooterItemNavigation(View view) {
        Toast.makeText(this, R.string.open_user_profile, Toast.LENGTH_SHORT).show();
        //new ProgressTask(ApplicationDashboard.this).execute();
    }

    @Override
    public void onClickUserPhotoNavigation(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
