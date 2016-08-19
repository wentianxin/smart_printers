package com.qg.smpt.util;

import static org.junit.Assert.*;

import org.apache.ibatis.javassist.expr.NewArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})

public class TestOrderBuilder {

	@Autowired
	private UserService userService;
	
	
	private OrderBuilder orderBuilder;
	
	@Before
	public void setUp() throws Exception {
		orderBuilder = new OrderBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void test() {
		Order o = orderBuilder.produceOrder(false,false);
		String json = JsonUtil.objectToJson(o);
		System.out.println(json);
	}
	
	
}
