package com.qg.smpt.web.processor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.service.UserService;

@Controller
public class RegisterController {
	private static final Logger LOGGER = Logger.getLogger(RegisterController.class);
	
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/register_app", method=RequestMethod.POST, produces="application/json;charset=utf-8" )
	@ResponseBody
	public String register(@RequestBody String data) {
		User newUser = (User)JsonUtil.jsonToObject(data, User.class);
		
		// 检查用户信息是否正确,正确则执行注册用户方法,错误则返回错误状态
		// check the user information is correct
		// true-run register method;
		// false- return ERROR status
		try{
			int retcode = (checkUserInfo(newUser) ? userService.registerUser(newUser) : Constant.FALSE);
			
			LOGGER.log(Level.DEBUG, "此次注册结果为 [{0}]", retcode);
			
			return JsonUtil.jsonToMap(new String[]{"retcode"}, new Object[]{retcode});
			
		}catch(Exception e){
			return JsonUtil.jsonToMap(new String[]{"retcode"}, new Object[]{Constant.FALSE});
		}
	}	
		
	@RequestMapping(value="/register", method=RequestMethod.POST, produces="application/html;charset=utf-8" )
	public String register(HttpServletRequest request) {
		User newUser = installUser(request);
		
		// 检查用户信息是否正确,正确则执行注册用户方法,错误则返回错误状态
		// check the user information is correct
		// true-run register method;
		// false- return ERROR status
		int status = (checkUserInfo(newUser) ? userService.registerUser(newUser) : Constant.FALSE);
		
		String url = (status == Constant.TRUE) ? "redirect:/html/user_login.html" : "redirect:/html/register.html";
		
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
}
