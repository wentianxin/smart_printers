package com.qg.smpt.web.model.Json;

import com.qg.smpt.web.model.Order;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by asus2015 on 2016/11/6.
 */
@JsonSerialize(using=OrderDetailSerializer.class)
public class OrderDetail {


    private Order order;

    public OrderDetail(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
