package com.qg.smpt.web.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.qg.smpt.web.model.Order;

public interface OrderService {
	public List<Order> queryByUser(int userId);
	
	public int insertOrder(Order order);
}
