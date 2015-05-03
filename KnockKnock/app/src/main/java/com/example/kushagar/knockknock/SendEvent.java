package com.example.kushagar.knockknock;


import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Kushagar on 4/16/2015.
 */
public class SendEvent{
    private static ArrayList<Event>arrayList;
    private static Event myevent;
    private static int layout_id;

    public static ArrayList<Event> getArrayList() {
        return arrayList;
    }

    public static void setArrayList(ArrayList<Event> arrayList) {
        SendEvent.arrayList = arrayList;
    }

    public static Event getMyevent() {
        return myevent;
    }

    public static void setMyevent(Event myevent) {
        SendEvent.myevent = myevent;
    }

    public static int getLayout_id() {
        return layout_id;
    }

    public static void setLayout_id(int layout_id) {
        SendEvent.layout_id = layout_id;
    }
}

