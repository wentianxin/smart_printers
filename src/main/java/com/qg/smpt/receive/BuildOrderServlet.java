package com.qg.smpt.receive;

import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.util.OrderBuilder;
import com.qg.smpt.web.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tisong on 7/30/16.
 */
@WebServlet(name = "/buildorderservlet")
public class BuildOrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BuildOrderServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Integer userId = null;
        Integer orderNumbers = null;
        Integer expeNumbers = null;

        try {
            userId = Integer.valueOf(request.getParameter("d"));
            orderNumbers = Integer.valueOf(request.getParameter("n"));
        } catch (final ClassCastException e) {
            return  ;
        }

        String expedite = request.getParameter("e");
        if (expedite != null) {
            expeNumbers = Integer.valueOf(expedite);
        }

        LOGGER.log(Level.DEBUG, "生成订单请求 : 用户标志 [{0}}， 生成订单数 [{1}]", userId, orderNumbers);

        ReceOrderServlet receOrderServlet = new ReceOrderServlet();
        for (int i = 0; i < orderNumbers; i++) {
            Order order = OrderBuilder.produceOrder(false,false);
            receOrderServlet.doGet(userId, order);
        }

        for (int i = 0; i < expeNumbers; i++) {
            Order order = OrderBuilder.produceOrder(true, false);

            receOrderServlet.doGet(userId, order);
        }


   }
}
