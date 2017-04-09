package com.qg.smpt.repository.redis;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * 用户所属的订单
 *   Key = userOrder:userId
 *      Value = orderId : time
 *      Value = orderId : time
 */
@Service
public class UserOrderRedis {


    private Jedis jedis;

    private final static String KEY = "userOrder:";

    public void putUserIdOrderId(Integer userId, Integer orderId, Double time) {
        jedis.zadd(KEY + userId, time, orderId.toString());
    }

    public Set<String> getOrderIdByUserId(Integer userId) {
        return jedis.zrevrange(KEY + userId, 0, -1);
    }
}
