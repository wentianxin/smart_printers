package com.qg.smpt.web.service;

import javax.annotation.Resource;

import com.qg.smpt.web.model.Order;

import java.util.List;

/**
 * Created by tisong on 7/27/16.
 */


public interface OrderService {

    public List<Order> queryByUser(int userId);

    public int insertOrder(Order order);


}
