package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by suhanth on 16/4/15.
 */
@Controller
@RequestMapping("/user/register")
public class UserController
{

            @RequestMapping(method = {RequestMethod.POST,RequestMethod.HEAD},consumes = "application/json" , headers = "Accept=application/json, application/xml")

            public @ResponseBody String register(@RequestBody User user)
            {
                System.out.println("in user_reg.");
                Counter.increement();

                return Counter.getCounter()+"";

            }
}
