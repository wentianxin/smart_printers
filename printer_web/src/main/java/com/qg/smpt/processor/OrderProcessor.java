package com.qg.smpt.processor;

import com.qg.smpt.eneity.ReturnJSON;
import com.qg.smpt.model.Order;
import com.qg.smpt.model.User;
import com.qg.smpt.service.OrderService;
import com.qg.smpt.service.UserService;
import com.qg.smpt.util.Constant;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by tisong on 4/8/17.
 */

@Controller
@RequestMapping("/user/{userId}")
public class OrderProcessor {
    private static final Logger LOGGER = Logger.getLogger(OrderProcessor.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="/order", method= RequestMethod.POST, produces="application/json;charset=utf-8" )
    @ResponseBody
    public String bookOrder(@PathVariable int userId,@RequestBody String data) {
        LOGGER.log(Level.DEBUG, "前台传来的json数据为 {0},当前用户为[{1}]", data, userId);

        // 将订单数据转化为订单对象
        Order order = (Order) JsonUtil.jsonToObject(data, Order.class);

        User user = userService.getUser(userId);

        // 检查订单信息，无错则执行下订订单，有则返回错误状态
        String status = (checkOrder(user, order) ? (orderService.bookOrder(order) ? Constant.SUCCESS : Constant.ERROR) : Constant.ERROR);

        int retcode = status.equals(Constant.SUCCESS)? Constant.TRUE : Constant.FALSE;

        LOGGER.log(Level.DEBUG, "下单处理的结果为[{0}]", status);

        return JsonUtil.jsonToMap(new String[]{"retcode","status"}, new Object[]{retcode,status});
    }

    private boolean checkOrder(User user, Order order) {
        // 检查订单内容

        // 检查商家信息
        if(user != null) {
            order.setClientName(user.getUserName());
            order.setClientAddress(user.getUserAddress());
            order.setClientTelephone(user.getUserPhone());
            order.setUserId(user.getId());
            return true;
        }

        return false;
    }


    @RequestMapping(value="/order/{isPrinted}", method= RequestMethod.GET, produces="application/json;charset=utf-8" )
    @ResponseBody
    public String getOrders(@PathVariable  int userId,@PathVariable int isPrinted) {
        LOGGER.log(Level.DEBUG, "正在查询 用户[{0}] 的订单", userId);

        // 根据用户id获取订单
        List<Order> orderList = orderService.getOrders(userId, isPrinted == 1 ? true : false);

        String json =  JsonUtil.jsonToMap(new String[]{"retcode","data"},
                new Object[]{Constant.TRUE,orderList});

        return json;
    }

}
