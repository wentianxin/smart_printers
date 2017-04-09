package com.qg.smpt.processor;

import com.qg.smpt.eneity.ReturnJSON;
import com.qg.smpt.model.Order;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tisong on 4/8/17.
 */

public class OrderProcessor {

    // 下单, 查询订单

    // 获取打印完成的订单
    @RequestMapping(value="/order/{userId}/typed", method= RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ResponseBody
    public ReturnJSON queryTypedOrders(@PathVariable Integer userId) {
        return null;
    }

    /**
     * 获取正在打印/未打印的订单状态
     * @param userId - 用户id
     * @return 订单集合的json对象
     */
    @RequestMapping(value = "/order/{userId}/typing",produces="application/json;charset=UTF-8",method = RequestMethod.GET)
    @ResponseBody
    public ReturnJSON queryTypingOrders(@PathVariable Integer userId) {
        return null;
    }

    // 下单
    public ReturnJSON insertOrders(@PathVariable Integer userId, @RequestBody Order order) {
        return null;
    }

    // 随机订单产生
    public ReturnJSON insertRandomOrders(@PathVariable Integer userId) {
        return null;
    }
}
