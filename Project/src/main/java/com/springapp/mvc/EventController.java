package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/")
public class EventController
{
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");

		return "hello";
	}



	@RequestMapping(value = "/fbevent/{userid}",method = {RequestMethod.POST,RequestMethod.HEAD} , consumes = "application/json" , headers = "Accept=application/json, application/xml")
	public @ResponseBody
	String addfbevent(@PathVariable("userid") int userid,@RequestBody Event ex)
	{
		System.out.println("in method");
		JdbcEventDAO jed = new JdbcEventDAO();
		jed.insert(ex);

		return "success";
	}

	@RequestMapping(value = "/fbevent/{userid}",method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
	public @ResponseBody
	Collection<Event> getEvent(@PathVariable("userid") String userid)
	{
		JdbcEventDAO jed = new JdbcEventDAO();
		return jed.fetch("SELECT * FROM EVENT WHERE USER_ID="+"\""+userid+"\""+"AND CATEGORY="+"\""+"private"+"\"");
	}

	@RequestMapping(value = "/fbevent",method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
	public @ResponseBody
	Collection<Event> getEvent()
	{
		JdbcEventDAO jed = new JdbcEventDAO();
		String query = "SELECT * FROM EVENT WHERE USER_ID="+"\""+"0"+"\""+"AND CATEGORY="+"\""+"public"+"\"";
		System.out.println(query);
		return jed.fetch(query);
	}


}