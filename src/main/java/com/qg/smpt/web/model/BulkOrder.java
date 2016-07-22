package com.qg.smpt.web.model;

import java.util.List;

/**
 * 批次订单
 * Created by tisong on 7/20/16.
 */
public final class BulkOrder {
    private int id;
    private int UserId;
    private List<Order> orders;

    public BulkOrder(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
