package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by suhanth on 15/4/15.
 */
@Controller
@RequestMapping("/admin")
public class AdminController
{

    @RequestMapping(method = RequestMethod.GET)
    public String view (ModelMap model)
    {
        Event eventForm= new Event();
        model.put("eventForm",eventForm);
        return "admin" ;
    }


    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String addEvent(@ModelAttribute("eventForm") Event event,ModelMap map)
    {
       JdbcEventDAO jed = new JdbcEventDAO();
        event.setEvent_category("public");
        event.setUser_id("0");
        jed.insert(event);

        return "successadmin";

    }
}
