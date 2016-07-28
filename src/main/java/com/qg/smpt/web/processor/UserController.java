package com.qg.smpt.web.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qg.smpt.util.Logger;
import com.qg.smpt.web.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger LOGGER = Logger.getLogger(UserController.class);
	
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/register", method=RequestMethod.POST, produces="application/json;charset=utf-8" )
	@ResponseBody
	public String register(String data) {
		
		return "";
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json;charset=utf-8" )
	@ResponseBody
	public String login(String data) {
		
		return "";
	}
}
