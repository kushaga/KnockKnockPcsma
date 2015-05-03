package com.example.kushagar.knockknock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;
import com.dexafree.materialList.view.MaterialListView;
import com.google.android.gms.internal.ev;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import android.support.v4.app.Fragment;
import java.util.ArrayList;

/**
 * Created by Kushagar on 4/15/2015.
 */
public class DashboardFragment extends Fragment {

    ArrayList<Event>event = new ArrayList<>();
    String category="all_events";
    MaterialListView mListView;
    View rootView;

    public DashboardFragment newinstance(String text , ArrayList<Event>e){
        DashboardFragment mFragment = new DashboardFragment();
        Bundle mBundle = new Bundle();
        //this.event=e;
        this.category=text;
        for(int i=0;i<e.size();i++) {
            Log.e("Event", e.get(i).getEvent_name());
            event.add(e.get(i));
        }

        return  mFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);

        mListView = (MaterialListView) rootView.findViewById(R.id.material_listview);

        /*Bundle extras = getArguments();
        String json = extras.getString("data");
        Object deserializedObject=SendEvent.deserialize(json,ArrayList.class);
        ArrayList<Event> app_events = (ArrayList<Event>)deserializedObject;*/

        ArrayList<Event> app_events = SendEvent.getArrayList();

        Log.e("on create","in on create"+this.event.size());

        for(int i=0;i<app_events.size();i++){
            Log.e("card_name:",app_events.get(i).getEvent_name());

            SmallImageCard card = new SmallImageCard(getActivity());
            card.setDescription(app_events.get(i).getEvent_id());
            card.setTitle(app_events.get(i).getEvent_name());
            card.setDrawable(R.drawable.abc_btn_check_material);

            mListView.add(card);
            if(event.size()>0) {
                Log.e("Event", event.get(i).getEvent_name());
            }
        }

        /*SmallImageCard card = new SmallImageCard(getActivity());
        card.setDescription("event.get(i).getEvent_description()");
        card.setTitle("event.get(i).getEvent_name()");
        card.setDrawable(R.drawable.ic_launcher);
        //cards.add(card);
        mListView.add(card);*/

        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(CardItemView view, int position) {
                Log.d("CARD_TYPE", position+"");
                //open up a fragment to show the event details
                //FragmentManager mFragmentManager = getChildFragmentManager();

                SendEvent.setMyevent(SendEvent.getArrayList().get(position));
                Intent intent = new Intent(getActivity(), EventFragment.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(CardItemView view, int position) {
                //Log.d("LONG_CLICK", view.getTag().toString());
                Log.d("CARD_TYPE", position+"");
            }
        });

        /*mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int position) {
                // Do whatever you want here
            }
        });*/

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

        }
        return true;
    }

}
