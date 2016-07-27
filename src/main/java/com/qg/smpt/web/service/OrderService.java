package com.qg.smpt.web.service;

import com.qg.smpt.web.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tisong on 7/27/16.
 */


public interface OrderService {

    public List<Order> queryByUser(int userId);

    public int insertOrder(Order order);

}
