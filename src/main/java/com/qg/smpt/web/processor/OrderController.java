package com.qg.smpt.web.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.qg.smpt.web.service.OrderService;


@Controller
@RequestMapping("/orders")
public class OrderController {
	private static final Logger LOGGER = Logger.getLogger(OrderController.class);
	
	static {
		int num = 0;
		
		List<Printer> printers = new ArrayList<>();
		
		for(int i = 0; i < 5; i++) {
			//生成打印机信息
			Printer p = new Printer();
			p.setId(i);
			p.setPrinterStatus("110");
			printers.add(p);
			
			//生成未打印批次
			List<BulkOrder> bulks = new ArrayList<>();
			for(int k = 0; k < 5; k++) {
				BulkOrder bulk = new BulkOrder(k);
				List<Order> notTypeOrder = new ArrayList<>();
				for(int j = 0; j < 10; j++ ) {
					Order o = new Order();
					o.setId(num++);
					o.setOrderStatus("130");
					notTypeOrder.add(o);
				}
				bulk.setOrders(notTypeOrder);
				bulks.add(bulk);
			}
			ShareMem.priBufferMapList.put(p, bulks);
			
			
			//生成正在打印批次
			List<BulkOrder> bulksT = new ArrayList<>();
			for(int k = 0; k < 5; k++) {
				BulkOrder bulk = new BulkOrder(k);
				List<Order> TypingOrder = new ArrayList<>();
				for(int j = 0; j < 10; j++ ) {
					Order o = new Order();
					o.setId(num++);
					o.setOrderStatus("120");
					TypingOrder.add(o);
				}
				bulk.setOrders(TypingOrder);
				bulksT.add(bulk);
			}
			ShareMem.priSentQueueMap.put(p, bulksT);
			
			
		}
		
		ShareMem.userListMap.put(1, printers);
	}
	
	
	@Autowired
	private OrderService orderService;
	

	
	/**
	 * 通过用户id获取商家的已打印的订单
	 * @return
	 */
	@RequestMapping(value="/typed", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String queryTypedOrders(Integer userId) {
		LOGGER.log(Level.DEBUG, "正在查询 用户[{0}] 的已打印订单", userId);
		
		// 根据用户id获取订单
		List<Order> orderList = orderService.queryByUser(userId);
		
		Map<String, List<Order>> map = new HashMap<>();
		
		map.put("data", orderList);
		
		String json = JsonUtil.objectToJson(map);
		
		LOGGER.log(Level.DEBUG, "转化后的json数据为[{0}]", json);
		
		return json;
	}
	
	
	/**
	 * 获取正在打印/未打印的订单状态
	 * @param userId - 用户id
	 * @return 订单集合的json对象
	 */
	@RequestMapping(value = "/typing",produces="application/json;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody
	public String queryTpyingOrders(Integer userId) {
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
		List<Order> orderList = installOrders(printers);
		
		
		Map<String, List<Order>> map = new HashMap<>();
		
		map.put("data", orderList);
		
		//convert object to json
		String json = JsonUtil.objectToJson(map);
		
		LOGGER.log(Level.DEBUG, "转化后的json数据为[{0}]", json);
		
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
	private List<Order> installOrders(List<Printer> printers) {
		List<Order> orderList = new ArrayList<Order>();

		List<BulkOrder> bulkUnsend = null;	//未发送的批次集合
		List<BulkOrder> bulkHasSend = null;	//已发送的批次集合
		List<Order> orderNotTyping = null;
		List<Order> ordersTyping = null;	//正在打印的订单集合

		// foreach printers to install orderList
		for(Printer p : printers) {
			bulkUnsend = ShareMem.priBufferMapList.get(p);
			bulkHasSend = ShareMem.priSentQueueMap.get(p);
			
			// filling not typing orders
			for(BulkOrder bulk : bulkUnsend) {
				orderNotTyping = bulk.getOrders();
				fillOrders(orderNotTyping, orderList);
			}

			// filling typing orders
			for(BulkOrder bulk : bulkHasSend) {
				ordersTyping = bulk.getOrders();
				fillOrders(ordersTyping, orderList);
			}
			
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
	

	
	
	
	
	
	
	
}


