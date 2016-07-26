package com.qg.smpt.web.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;

@Controller
@RequestMapping("/orders")
public class OrderController {
	private static final Logger LOGGER = Logger.getLogger(JsonUtil.class);
	
	
	@RequestMapping(value = "/typing",produces="application/json;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody
	public String queryOrderByUser(int userId) {
		// info message
		LOGGER.log(Level.INFO, "In OrderController, User is requesting see typing orders");
		
		// get printers by userId
		List<Printer> printers = ShareMem.userListMap.get(userId);
		
		// check the printers
		// if has no object, return ""
		// if has object, install orderList
		if(!checkNormal(printers)) {
			return "";
		}
		
		// install orderList
		
	}
	
	
	private boolean checkNormal(List<Printer> printers) {
		if(printers == null || printers.isEmpty())
			return false;
		
		return true;
	}
	
	private List<Order> installOrders(List<Printer> printers) {
		// foreach printers to install orderList
		List<Order> orderList = new ArrayList<>(); 
		
		Queue<Order> orderQueue = null;
		Queue<BulkOrder> bulkQueue = null;
		for(Printer p : printers) {
			orderQueue = ShareMem.priBufferQueueMap.get(p);
			bulkQueue = ShareMem.priSentQueueMap.get(p);
			
			// filling not typing order
			fillOrders(orderQueue, orderList);
		}
	}
	
	private void fillOrders(Queue<Order> srcList, List<Order> descList) {
		descList.addAll(srcList);
	}
}
