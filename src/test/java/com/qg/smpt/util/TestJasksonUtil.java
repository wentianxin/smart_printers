package com.qg.smpt.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.qg.smpt.web.model.Order;

public class TestJasksonUtil {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Ignore
	@Test
	public void test() {
		List<Order> orders = new ArrayList<Order>();
		
//		for(int i = 0; i < 10; i++){
//			Order o = OrderBuilder.produceOrder();
//			orders.add(o);
//			o.setOrderStatus("110");
//		}
		
		Map<String, List<Order>> maps = new HashMap<>();
		maps.put("data", orders);
		System.out.println(JsonUtil.objectToJson(maps));
	}
	@Ignore
	@Test 
	public void testString() {
		String status = "SUCCESS";
		System.out.println(JsonUtil.objectToJson(status));
	}
	

}
