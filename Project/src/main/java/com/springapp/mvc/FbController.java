package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * Created by suhanth on 13/4/15.
 */
@Controller
@RequestMapping("/fbevent")

public class FbController {

    @RequestMapping(value = "/{userid}",method = {RequestMethod.POST,RequestMethod.HEAD} , consumes = "application/json" , headers = "Accept=application/json, application/xml")
    public @ResponseBody
    String addfbevent(@PathVariable("userid") int userid,@RequestBody Event ex)
    {
        System.out.println("in method");
        JdbcEventDAO jed = new JdbcEventDAO();
        jed.insert(ex);

        return "sucess";
    }


    /*@RequestMapping(method = {RequestMethod.GET,RequestMethod.HEAD})
    public @ResponseBody String print1(ModelMap model) {
        //model.addAttribute("message", "Hello world!");
        System.out.println("in method get1");

        return "hello";
    }*/


}
