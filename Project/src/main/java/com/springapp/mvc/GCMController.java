package com.springapp.mvc;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * Created by suhanth on 15/4/15.
 */

@Controller("/gcm")
public class GCMController {

    private static final long serialVersionUID = 1L;

    private static final String GOOGLE_SERVER_KEY = "AIzaSyD0WPTgM0Jpu7QwSM9QZRw6emToK0_ifsg";
    static final String MESSAGE_KEY = "message";


    @RequestMapping(method = RequestMethod.GET)
    public  String show(ModelMap model)
    {
        return "gcm";
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.HEAD})
    public @ResponseBody String gcmpost(@RequestParam("shareRegId") String share,@RequestParam("regId") String regId,@RequestParam("message") String userMessage, ModelMap model) {
        Result result = null;

        System.out.println(share);
        if (share != null && !share.isEmpty()) {

            System.out.println("if");
            System.out.println(regId);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter("/home/suhanth/GCMRegId.txt");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            writer.println(regId);
            writer.close();
            model.put("pushStatus", "GCM RegId Received.");

        }
        else {

            System.out.println("else");
            try {
                BufferedReader br = new BufferedReader(new FileReader(
                        "/home/suhanth/GCMRegId.txt"));
                regId = br.readLine();
                br.close();

                Sender sender = new Sender(GOOGLE_SERVER_KEY);
                Message message = new Message.Builder().timeToLive(30)
                        .delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
                System.out.println("regId: " + regId);
                result = sender.send(message, regId, 1);
                model.put("pushStatus", result.toString());


            } catch (IOException ioe) {
                ioe.printStackTrace();
                model.put("pushStatus",
                        "RegId required: " + ioe.toString());
            } catch (Exception e) {
                e.printStackTrace();
                model.put("pushStatus", e.toString());
            }

        }

        return "gcm";

    }

}

