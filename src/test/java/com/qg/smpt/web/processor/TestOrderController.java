package com.qg.smpt.web.processor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml"})

public class TestOrderController {
//	private MockHttpServletRequest request;
//	private MockHttpServletResponse response;
	
	@Autowired
	private OrderController orderController;
	
	
	@Before
	public void setUp() throws Exception {
//		request = new MockHttpServletRequest();      
//        request.setCharacterEncoding("UTF-8");      
//        response = new MockHttpServletResponse(); 
		init();
	}

	@After
	public void tearDown() throws Exception {
	}

	

	@Test
	public void testGetTyped() {
//		try{
////			request.setParameter("userId", "1");

//			String json = orderController.queryTypedOrders(1);
//			System.out.println(json);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	
	@Ignore
	@Test
	public void testGetTyping() {

//		try{
//			String json = orderController.queryTpyingOrders(1);
//			System.out.println(json);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}

	}

	
	private void init() {
		int num = 0;
		
		List<Printer> printers = new ArrayList<>();
		
		for(int i = 0; i < 5; i++) {
			//生成打印机信息
			Printer p = new Printer();
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
	
	@Ignore
	@Test
	public void testBuyOrder() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"company\":\"美团外卖\",");
		buffer.append("\"orderTime\":\"2016-7-29 18:30:00\",");
		buffer.append("\"expectTime\":\"18:30\",");
		buffer.append("\"orderRemark\":\"加饭\",");
		buffer.append("\"orderMealFee\":\"3\",");
		buffer.append("\"orderDisFee\":\"3\",");
		buffer.append("\"orderPreAmount\":\"3\",");
		buffer.append("\"orderPayStatus\":\"已付款\",");
		buffer.append("\"userName\":\"梁碧如\",");
		buffer.append("\"userAddress\":\"华南农业大学\",");
		buffer.append("\"userTelephone\":\"18819255400\",");
		buffer.append("\"items\":[");
		
		buffer.append("{\"name\":\"炒猪肉\",\"price\":\"8\",\"count\":\"3\"},");
		buffer.append("{\"name\":\"蒸饺子\",\"price\":\"7\",\"count\":\"2\"},");
		buffer.append("{\"name\":\"火龙果\",\"price\":\"4\",\"count\":\"3\"}");
		buffer.append("]}");
		
		System.out.println(buffer.toString());
		
		Order order = (Order)JsonUtil.jsonToObject(buffer.toString(), Order.class);
//		System.out.println(order);
//		System.out.println(order.toString());]
		System.out.println(order.toString());
	}

	@Test
	public void testSearchOrder() {
		System.out.println(orderController.searchOrder(1,1));
	}

}
