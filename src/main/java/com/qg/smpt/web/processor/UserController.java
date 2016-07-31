package com.qg.smpt.web.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.service.UserService;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger LOGGER = Logger.getLogger(UserController.class);
	
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/register", method=RequestMethod.POST, produces="application/json;charset=utf-8" )
	@ResponseBody
	public String register(@RequestBody String data) {
		User newUser = (User)JsonUtil.jsonToObject(data, User.class);
		
		// 检查用户信息是否正确,正确则执行注册用户方法,错误则返回错误状态
		// check the user information is correct
		// true-run register method;
		// false- return ERROR status
		try{
			String status = (checkUserInfo(newUser) ? userService.registerUser(newUser) : Constant.ERROR);
			
			LOGGER.log(Level.DEBUG, "此次注册结果为 [{0}]", status);
			
			return JsonUtil.jsonToMap("status", status);
		}catch(Exception e){
			return Constant.ERROR;
		}
	}	
		
	@RequestMapping(value="/registerW", method=RequestMethod.POST, produces="application/html;charset=utf-8" )
	public String register(HttpServletRequest request) {
		User newUser = installUser(request);
		
		// 检查用户信息是否正确,正确则执行注册用户方法,错误则返回错误状态
		// check the user information is correct
		// true-run register method;
		// false- return ERROR status
		String status = (checkUserInfo(newUser) ? userService.registerUser(newUser) : Constant.ERROR);
		
		String url = (status.equals(Constant.SUCCESS) ? "redirect:/webContent/html/user_login.html" : "redirect:/webContent/html/register.html");
		
		return url;
	}	
	
	private User installUser(HttpServletRequest request) {
		User user = new User();
		
		String name = request.getParameter("userName");
		String account = request.getParameter("userAccount");
		String password = request.getParameter("userPassword");
		String store = request.getParameter("userStore");
		String address = request.getParameter("userAddress");
		String phone = request.getParameter("userPhone");
		
		user.setUserName(name);
		user.setUserAccount(account);
		user.setUserPassword(password);
		user.setUserStore(store);
		user.setUserAddress(address);
		user.setUserPhone(phone);
		
		String[] printerIds = request.getParameterValues("id");
		List<Printer> printers = new ArrayList<>();
		for(int i = 0; i < printerIds.length; i++) {
			Printer p = new Printer();
			p.setId(Integer.parseInt(printerIds[i]));
			printers.add(p);
		}
		user.setPrinters(printers);
		
		return user;
	}
	
	private boolean checkUserInfo(User user) {
		user.setUserLogo("");
		user.setUserQrcode("");
		user.setUserPrinters(user.getPrinters().size());
		return true;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json;charset=utf-8" )
	@ResponseBody
	public String login(@RequestBody String data,  HttpServletRequest request) {
		User user = (User)JsonUtil.jsonToObject(data, User.class);
		
		// check the login infomation is correct
		if(!checkInput(user)){
			return Constant.ERROR;
		}
		
		// run the login method.
		// login successful - return the user
		// login fail - return null
		User loginUser = userService.login(user);
		
		// set the login status
		String status = (loginUser != null ? Constant.SUCCESS : Constant.ERROR);
		
		// check the login status
		// if success, store the user
		if(status.equals(Constant.SUCCESS)) {
			 HttpSession session = request.getSession();
			 session.setAttribute("user", loginUser);
			 ShareMem.userIdMap.put(loginUser.getId(), loginUser);
		}
		
		return JsonUtil.jsonToMap("status", status);
	}
	
	@RequestMapping(value="/loginW", method=RequestMethod.POST, produces="application/html;charset=utf-8" )
	public String login(String userAccount, String userPassword,  HttpServletRequest request) {
		User user = installUser(userAccount, userPassword);
		
		// check the login infomation is correct
		if(!checkInput(user)){
			 return "redirect:/webContent/html/order_index.html";
		}
		
		// run the login method.
		// login successful - return the user
		// login fail - return null
		User loginUser = userService.login(user);
		
		// set the login status
		String status = (loginUser != null ? Constant.SUCCESS : Constant.ERROR);
		
		// check the login status
		// if success, store the user
		if(status.equals(Constant.SUCCESS)) {
			 HttpSession session = request.getSession();
			 session.setAttribute("user", loginUser);
//			 ShareMem.userIdMap.put(loginUser.getId(), loginUser);
			 return "redirect:/webContent/html/order_index.html";
			 
		}else{
			 return "redirect:/webContent/html/user_login.html";
		}
		
	}
	
	private User installUser(String account ,String password) {
		User user = new User();
		user.setUserAccount(account);
		user.setUserPassword(password);
		return user;
	}
	
	private boolean checkInput(User user) {
		return true;
	}
}
