package com.qg.smpt.web.repository;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

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
	private OrderService orderService;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Ignore
	@Test
	public void test() {
		List<Order> orders = orderService.queryByUser(4);
		System.out.println(orders.size());
	}

}
