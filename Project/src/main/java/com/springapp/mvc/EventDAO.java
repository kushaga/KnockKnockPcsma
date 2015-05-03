package com.springapp.mvc;

import java.util.List;

/**
 * Created by suhanth on 12/4/15.
 */
public interface EventDAO
{
    public void insert(Event event);

    public List<Event> fetch(String query);
}