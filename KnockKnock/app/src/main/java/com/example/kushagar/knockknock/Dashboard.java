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
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;


public class Dashboard extends NavigationLiveo implements NavigationLiveoListener {

    SharedPreferences sharedPreferences;
    String mypref = "my_pref";
    public List<String> mListNameItem;

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
            MyJsonParser jsonParser = new MyJsonParser();
            //JSONParser jparser = new JSONParser();
            //JSONObject jsonObject =jparser.getJSONFromUrl("http://192.168.50.241:8084/fbevent/1");
            JSONArray jsonArray = jsonParser.getJSONFromUrl("http://192.168.50.241:8084/fbevent/1");

            Log.e("Json_array_response",jsonArray.toString());
            /*for(int i=0;i<jsonArray.length();i++){
                try{
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int dur = jsonObject.getInt("duration");
                    String url = jsonObject.getString("url");
                    String type = jsonObject.getString("type");
                    String name = jsonObject.getString("name");
                    String title = jsonObject.getString("title");

                }catch(Exception e){
                    Log.d("exception", e.toString());
                    e.printStackTrace();
                }
            }*/

            return null;
        }
    }

    @Override
    public void onUserInformation() {
        this.mUserName.setText("Kushagar Lall");
        this.mUserEmail.setText("kushagarlall1993@gmail.com");
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
        mListNameItem.add(0, getString(R.string.inbox));
        mListNameItem.add(1, getString(R.string.starred));
        mListNameItem.add(2, getString(R.string.sent_mail));
        mListNameItem.add(3, getString(R.string.drafts));
        mListNameItem.add(4, getString(R.string.more_markers)); //This item will be a subHeader
        mListNameItem.add(5, getString(R.string.trash));
        mListNameItem.add(6, getString(R.string.spam));

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_inbox_black_24dp);
        mListIconItem.add(1, R.drawable.ic_star_black_24dp); //Item no icon set 0
        mListIconItem.add(2, R.drawable.ic_send_black_24dp); //Item no icon set 0
        mListIconItem.add(3, R.drawable.ic_drafts_black_24dp);
        mListIconItem.add(4, 0); //When the item is a subHeader the value of the icon 0
        mListIconItem.add(5, R.drawable.ic_delete_black_24dp);
        mListIconItem.add(6, R.drawable.ic_report_black_24dp);

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
        mListHeaderItem.add(4);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 123);
        mSparseCounterItem.put(6, 250);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer(R.string.settings, R.drawable.ic_settings_black_24dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        sharedPreferences = getSharedPreferences(mypref,MODE_PRIVATE);

        String accessToken = sharedPreferences.getString("access_token",null);
        String date_str = sharedPreferences.getString("token_exp_date",null);

        /*Date date =null;
        if(date_str!=null){
            System.out.println("date received");
            try {
                date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date_str);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/

      /*  if(accessToken!=null) {
            Log.w("token:", accessToken);
           // Log.w("token_date:", date.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //

        FragmentMain mFragment = null;

        switch (position){
            case 0:
                mFragment = new FragmentMain().newInstance(mListNameItem.get(position));
                Log.e("on click","on 0 clicked");
                break;
            case 1:
                mFragment = new FragmentMain().newInstance(mListNameItem.get(position));
                Log.e("on click","on 1 clicked");
                break;
            case 2:
                ArrayList<Event>event= new ArrayList<>();
                mFragment = new FragmentMain().newInstance1(mListNameItem.get(position),event);
                Log.e("on click","on 2 clicked");
                break;

        }
        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {
        switch (position) {
            case 0:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;

/*            case 1:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;*/
        }

    }

    @Override
    public void onClickFooterItemNavigation(View view) {
        Toast.makeText(this, R.string.open_user_profile, Toast.LENGTH_SHORT).show();
        new ProgressTask(Dashboard.this).execute();
    }

    @Override
    public void onClickUserPhotoNavigation(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
