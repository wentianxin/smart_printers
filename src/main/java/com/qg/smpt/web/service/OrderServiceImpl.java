package com.qg.smpt.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.repository.OrderMapper;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderMapper orderMapper;
	
	public List<Order> queryByUser(int userId) {
		return orderMapper.selectByUser(userId);
	}
	
	public int insertOrder(Order order) {
		return orderMapper.insert(order);
	}

}
