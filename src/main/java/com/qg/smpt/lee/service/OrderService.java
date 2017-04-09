package com.qg.smpt.lee.service;

import com.qg.smpt.lee.model.Order;
import com.qg.smpt.lee.util.AbstractPrinterUtil;

import java.util.List;

/**
 * Created by asus2015 on 2017/4/8.
 */
public class OrderService {
    private AbstractPrinterUtil abstractPrinterUtil;

    public boolean bookOrder(Order order) {
        return abstractPrinterUtil.bookOrder(order);
    }

    public List<Order> getOrders(int userId, boolean isPrinted) {
        return abstractPrinterUtil.getOrders(userId,isPrinted);
    }
}
