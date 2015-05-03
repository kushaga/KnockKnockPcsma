package com.springapp.mvc;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by suhanth on 16/4/15.
 */

@Controller
@RequestMapping("/knock/{userid}")
public class KnockController
{

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.HEAD})
    public @ResponseBody
    String postKnock(@RequestParam("regId")String regId,@RequestParam("location") String location,@PathVariable("userid") int userid)
    {

        JdbcEventDAO jed = new JdbcEventDAO();

        String query = "SELECT * FROM EVENT WHERE USER_ID="+"\""+ userid+"\"" + "AND LOCATION=" +"\""+ location+"\"";
        System.out.println(query);
        List<Event> knocklist = jed.fetch(query);


        Event kevent = knocklist.get(0);

        String notifyMessage = "Hey,Event "+kevent.getEvent_name()+" is happening at "+kevent.getRoom_number();

        Result result = null;

        try {

            Sender sender = new Sender("AIzaSyD0WPTgM0Jpu7QwSM9QZRw6emToK0_ifsg");
            Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("message", notifyMessage).build();
            System.out.println("regId: " + regId);
            result = sender.send(message, regId, 1);

            //model.put("pushStatus", result.toString());


        } catch (IOException ioe) {
            ioe.printStackTrace();
            //model.put("pushStatus","RegId required: " + ioe.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //model.put("pushStatus", e.toString());
        }



        return "success";

    }

}
