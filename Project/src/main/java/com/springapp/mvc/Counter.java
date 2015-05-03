package com.springapp.mvc;

/**
 * Created by suhanth on 16/4/15.
 */
public class Counter {

    private static long counter=0;

    public static long getCounter() {
        return counter;
    }

    public static void setCounter(long counter) {
        Counter.counter = counter;
    }

    public static void increement()
    {
       Counter.counter= Counter.getCounter()+1;
    }
}
