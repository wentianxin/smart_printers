package com.qg.smpt.web.processor;

import com.qg.smpt.receive.ReceOrderServlet;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.OrderBuilder;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tisong on 7/31/16.
 */
@Controller
@RequestMapping("/orders")
public class OrdersController {
    @RequestMapping(value="/{userId}/{orderNumbers}", method= RequestMethod.GET)
    @ResponseBody
    public void buildOrders(@PathVariable int userId, @PathVariable int orderNumbers) {

        Integer expeNumbers = 0;

        try {
            ReceOrderServlet receOrderServlet = new ReceOrderServlet();
            for (int i = 0; i < orderNumbers; i++) {
                Order order = OrderBuilder.produceOrder(false);
                receOrderServlet.doGet(userId, order);
            }

            for (int i = 0; i < expeNumbers; i++) {
                Order order = OrderBuilder.produceOrder(true);
                receOrderServlet.doGet(userId, order);
            }
        } catch (Exception e) {

        }
    }
}
