package com.qg.smpt.web.processor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.User;
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
		User newUser = (User)JsonUtil.jsonToObject(data, User.class);
		
		// 检查用户信息是否正确,正确则执行注册用户方法,错误则返回错误状态
		// check the user information is correct
		// true-run register method;
		// false- return ERROR status
		String status = (checkUserInfo(newUser) ? userService.registerUser(newUser) : Constant.ERROR);
		
		return JsonUtil.jsonToMap("status", status);
	}	
	
	
	private boolean checkUserInfo(User user) {
		user.setUserLogo("");
		user.setUserQrcode("");
		user.setUserPrinters(user.getPrinters().size());
		return true;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json;charset=utf-8" )
	@ResponseBody
	public String login(String data, HttpServletRequest request) {
		User user = (User)JsonUtil.jsonToObject(data, User.class);
		
		
		
		return "";
	}
	
	private boolean checkInput(User user) {
		return true;
	}
}
