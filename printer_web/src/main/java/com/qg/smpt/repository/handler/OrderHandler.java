package com.qg.smpt.repository.handler;

import com.qg.smpt.model.Order;

import java.util.List;

/**
 * Created by asus2015 on 2017/4/9.
 */
public interface OrderHandler {
    public abstract boolean bookOrder(Order order);

    public abstract List<Order> getOrders(int userId, boolean isPrinted);
}
