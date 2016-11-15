package com.qg.smpt.web.processor;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.qg.smpt.web.model.Json.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.service.OrderService;
import com.qg.smpt.web.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Or;


@Controller
@RequestMapping("/order")
public class OrderController {
	private static final Logger LOGGER = Logger.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/{userId}", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	@ResponseBody
	public String bookOrder(@PathVariable int userId, @RequestBody String data) {
		try{
			// 从session中获取用户
//			HttpSession session = request.getSession();
//			User user = (User)session.getAttribute("user");
//			int userId = ((user != null) ? user.getId() : 0);
			
			LOGGER.log(Level.DEBUG, "前台传来的json数据为 {0},当前用户为[{1}]", data, userId);
			
			User user = userService.queryById(userId);
			
			// 将订单数据转化为订单对象
			Order order = (Order)JsonUtil.jsonToObject(data, Order.class);
			
			// 检查订单信息，无错则执行下订订单，有则返回错误状态
			String status = (checkOrder(user, order) ? orderService.bookOrder(userId, order) : Constant.ERROR);
			
			int retcode = status.equals(Constant.SUCCESS)? Constant.TRUE : Constant.FALSE;
			
			LOGGER.log(Level.DEBUG, "下单处理的结果为[{0}]", status);
			
			return JsonUtil.jsonToMap(new String[]{"retcode","status"}, new Object[]{retcode,status});
		}catch(Exception e){
			LOGGER.log(Level.DEBUG,"用户[{0}]下单出现了错误", userId, e);
			return JsonUtil.jsonToMap(new String[]{"retcode","status"}, new Object[]{0,Constant.ERROR});
		}
	}
	
	private boolean checkOrder(User user, Order order) {
		// 检查订单内容
		
		// 检查商家信息
		if(user != null) {
			synchronized(ShareMem.class) {
				order.setId(++ShareMem.currentOrderNum);
			}
			order.setOrderStatus(String.valueOf(BConstants.orderWait));
			order.setClientName(user.getUserName());
			order.setClientAddress(user.getUserAddress());
			order.setClientTelephone(user.getUserPhone());
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 通过用户id获取商家的已打印的订单
	 * @return
	 */
	@RequestMapping(value="/typed/{userId}", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String queryTypedOrders(@PathVariable Integer userId) {
		// 从session中获取用户
//		HttpSession session = request.getSession();
//		User user = (User)session.getAttribute("user");
//		int userId = ((user != null) ? user.getId() : 0);
		
		LOGGER.log(Level.DEBUG, "正在查询 用户[{0}] 的已打印订单", userId);
		
		// 根据用户id获取订单
		List<Order> orderList = orderService.queryByUser(userId);
		
		String json =  JsonUtil.jsonToMap(new String[]{"retcode","data"}, 
				new Object[]{Constant.TRUE,orderList});
		
		LOGGER.log(Level.DEBUG, "当前转化的信息为 [{0}]", json);
		
		return json;
		
		
	}
	
	
	/**
	 * 获取正在打印/未打印的订单状态
	 * @param userId - 用户id
	 * @return 订单集合的json对象
	 */
	@RequestMapping(value = "/typing/{userId}",produces="application/json;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody
	public String queryTpyingOrders(@PathVariable Integer userId) {
		// 从session中获取用户
//		HttpSession session = request.getSession();
//		User user = (User)session.getAttribute("user");
//		int userId = ((user != null) ? user.getId() : 0);
		
		
		// info message
		LOGGER.log(Level.DEBUG, "正在查询 用户[{0}] 的未打印/正在打印订单", userId);
		
		// get printers by userId
		User user = ShareMem.userIdMap.get(userId);
		List<Printer> printers = null;
		if(user != null) {
			printers = user.getPrinters();
		}
//		List<Printer> printers = ShareMem.userListMap.get(userId);

		// check the printers
		// if has no object, return ""
		// if has object, install orderList
		if(!checkNormal(printers)) {
			LOGGER.log(Level.DEBUG, "当前用户[{0}]没有打印机设备连入", userId);
			return JsonUtil.jsonToMap(new String[]{"retcode","data"}, 
					new String[]{String.valueOf(Constant.TRUE),"[]"});
		}
		
		// install orderList
		List<Order> orderList = installOrders(userId, printers);
		
		
		String json =  JsonUtil.jsonToMap(new String[]{"retcode","data"}, 
				new Object[]{Constant.TRUE,orderList});
		
		LOGGER.log(Level.DEBUG, "当前转化的信息为 [{0}]", json);
		
		return json;
	}
	

	/**
	 * 检查打印机集合是否为空
	 * @param printers 打印机集合
	 * @return 空-false 有-true
	 */
	private boolean checkNormal(List<Printer> printers) {
		if(printers == null || printers.isEmpty())
			return false;
		
		return true;
	}

	/**
	 * 获取打印机集合中未打印订单和正在打印订单,全部组装到一个订单集合中
	 * @param printers 打印机集合
	 * @return	未打印/正在打印的订单集合
	 */
	private List<Order> installOrders(int userId, List<Printer> printers) {
		List<Order> orderList = new ArrayList<Order>();

		List<Order> orderUnsend = null;
		List<BulkOrder> bulkUnsend = null;	//未发送的批次集合
		List<BulkOrder> bulkHasSend = null;	//已发送的批次集合
		List<BulkOrder> bulkError = null;	//异常批次
		List<Order> ordersNotTyping = null;	//未打印订单
		List<Order> ordersTyping = null;	//正在打印的订单集合
		List<Order> OrdersError = null;		//异常订单

		orderUnsend = ShareMem.userOrderBufferMap.get(userId);
		if(orderUnsend != null) {
			fillOrders(orderUnsend, orderList);
		}

		// foreach printers to install orderList
		for(Printer p : printers) {
			bulkUnsend = ShareMem.priBufferMapList.get(p);
			bulkHasSend = ShareMem.priSentQueueMap.get(p);
			bulkError = ShareMem.priExceQueueMap.get(p);

			if (bulkUnsend != null) {
				// filling not typing orders
				for (BulkOrder bulk : bulkUnsend) {
					ordersNotTyping = bulk.getOrders();
					fillOrders(ordersNotTyping, orderList);
				}
			}

			// filling typing orders
			if (bulkHasSend != null) {
				for (BulkOrder bulk : bulkHasSend) {
					ordersTyping = bulk.getOrders();
					fillOrders(ordersTyping, orderList);
				}
			}
//			for(BulkOrder bulk : bulkError) {
//				OrdersError = bulk.getOrders();
//				fillOrders(OrdersError, orderList);
//			}
			
		}
		
		return orderList;
	}


	/**
	 * 将源集合的元素添加到目的集合中
	 * @param srcList	源订单集合
	 * @param descList	目的订单集合
	 */
	private void fillOrders(List<Order> srcList, List<Order> descList) {
		descList.addAll(srcList);
	}
	

	@RequestMapping(value="/{printerId}/{orderId}" ,produces="application/json;charset=UTF-8",method = RequestMethod.GET )
	@ResponseBody
	public String searchOrder(@PathVariable int printerId, @PathVariable int orderId) {
		OrderDetail orderDetail = orderService.queryByIdAndPriner(printerId, orderId);
		return JsonUtil.jsonToMap(new String[]{"order"}, new Object[] {orderDetail});
	}


	
	
	
	
	
	
}


