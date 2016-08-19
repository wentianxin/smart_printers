package com.qg.smpt.receive;

import com.qg.smpt.util.OrderBuilder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 7/29/16.
 */
public class TestReceOrderServlet {

    public static void main(String[] args) throws ServletException, IOException {

        OrderBuilder orderBuilder = new OrderBuilder();

        Order order = orderBuilder.produceOrder(false,false);

        ReceOrderServlet receOrderServlet = new ReceOrderServlet();

        receOrderServlet.doGet(1, order);

        //System.out.println(order.toString());
    }


}
