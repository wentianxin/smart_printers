package com.qg.smpt.service;

import com.qg.smpt.model.Order;
import com.qg.smpt.util.AbstractPrinterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by asus2015 on 2017/4/9.
 */
@Service
public class OrderService {

    private AbstractPrinterUtil abstractPrinterUtil;

    public boolean bookOrder(Order order) {
        return abstractPrinterUtil.bookOrder(order);
    }

    public List<Order> getOrders(int userId, boolean isPrinted) {
        return abstractPrinterUtil.getOrders(userId,isPrinted);
    }
}
