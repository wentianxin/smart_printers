package com.qg.smpt.web.repository;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.web.model.Json.OrderDetail;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})

public class TestOrderMapper {

	@Resource
	private OrderMapper orderMapper;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public void test() {
		Order order = orderMapper.queryByIdAndPrinter(1,1);
		OrderDetail orderDetail = new OrderDetail(order);
		System.out.println(JsonUtil.objectToJson(orderDetail));
	}

	@Test
	public void testMaxId() {
		int count = orderMapper.selectMaxOrderId();
		System.out.println(count);
	}

}
