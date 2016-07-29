package com.qg.smpt.web.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;

@Controller
public class PrinterController {
	private static final Logger LOGGER = Logger.getLogger(PrinterController.class);
	
	@RequestMapping(value="/printer", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String seePrinterStatus(HttpServletRequest request) {
		
		// 从session中获取用户
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int userId = ((user != null) ? user.getId() : 0);
		
		// 根据用户id获取打印机
		List<Printer> printers = ShareMem.userListMap.get(userId);
		
		Map<String, List<Printer>> map = new HashMap<>();
		
		map.put("data", printers);
		
		String json = JsonUtil.objectToJson(map);
		
		LOGGER.log(Level.DEBUG, "查看打印机状态 转化后的json数据为[{0}]", json);
		
		return json;
	}
}
