package com.qg.smpt.web.service;

import javax.annotation.Resource;

import com.qg.smpt.web.model.Json.OrderDetail;
import com.qg.smpt.web.model.Order;

import java.util.List;

/**
 * Created by tisong on 7/27/16.
 */


public interface OrderService {

    public List<Order> queryByUser(int userId);

    public int insertOrder(Order order);

    public String bookOrder(int userId, Order order);

    public OrderDetail queryByIdAndPriner(int printerId, int id);

}
