package com.qg.smpt.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.repository.OrderMapper;
import com.qg.smpt.web.service.OrderService;



@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderMapper orderMapper;
	
	public List<Order> queryByUser(int userId) {
		return orderMapper.selectByUser(userId);
	}
	
	
	@Override
	public int insertOrder(Order order) {
		// TODO Auto-generated method stub
		return 0;
	}

}
