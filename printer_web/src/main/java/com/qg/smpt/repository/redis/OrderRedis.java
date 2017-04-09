package com.qg.smpt.repository.redis;

import com.qg.smpt.eneity.OrderStatus;
import com.qg.smpt.model.Order;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1. OrderId - Order信息(包括Order状态)
 *  Key = order:orderId
 *  Value = orderTime : xxx
 *  Value = orderContent : xxx
 *  Value = orderStatus : xxx
 *
 * 2. OrderId - Value (子增)
 */
@Repository
public class OrderRedis {

    private Jedis jedis;

    private static final String KEY = "order:";

    /**
     * 加入订单
     * @param userId
     * @param order
     */
    public void putOrder(Integer userId, Order order) {
        jedis.hmset("order:" + order.getId(), getOrderMap(order));
    }

    public Order getOrder(Integer orderId) {
        return null;
    }

    public OrderStatus getOrderState(Long orderId) {
        if (orderId == null) {
            return null;
        }
        List<String> result = jedis.hmget(KEY + orderId, "orderStatus");
        return result.size() > 0 ? OrderStatus.valueOf(result.get(0)) : null;
    }



    public List<Order> getOrderStatesTyped (Integer userId) {
        return null;
    }

    public List<Order> getOrderStatesTyping (Integer userId) {
        return null;
    }

    /**
     * 将Order对象转化为Map
     * @param order
     * @return
     */
    private Map<String, String> getOrderMap(Order order) {
        Map<String, String> map = new HashMap<>();
        try {
            BeanUtils.populate(order, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 更新状态字段
     * @param orderId
     * @param state
     */
    public void updateOrderState(Long orderId, OrderStatus state) {
        Map<String, String> map = new HashMap<>();
        map.put("orderStatus", state.toString());
        jedis.hmset(KEY + orderId, map);
    }

    /**
     * OrderId 子增
     * @return
     */
    public long buildOrderId() {
        return jedis.incr("orderIdNumber");
    }

}
