package com.qg.smpt.web.service.impl;

import java.util.List;

import com.qg.smpt.web.model.Json.OrderDetail;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qg.smpt.receive.ReceOrderServlet;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.repository.OrderMapper;
import com.qg.smpt.web.service.OrderService;



@Service
public class OrderServiceImpl implements OrderService{
	private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);
	
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
	
	@Override
	public String bookOrder(int userId, Order order) {
		try{
			// 插入订单
			new ReceOrderServlet().doGet(userId, order);
			return Constant.SUCCESS;
		
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "商家id为[{0}]下单的时候出现了错误，下单内容为\n{[1]}", userId, order.toString(), e );
			return Constant.ERROR;
		}
		
	}

	public OrderDetail queryByIdAndPriner(int printerId, int id) {
		Order order = orderMapper.queryByIdAndPrinter(printerId,id);
		OrderDetail orderDetail = new OrderDetail(order);
		return orderDetail;
	}

}
