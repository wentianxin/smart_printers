package com.qg.smpt.receive;

import com.qg.smpt.printer.Constants;
import com.qg.smpt.printer.PrinterProcessor;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.printer.util.database.DatabaseUtil;
import com.qg.smpt.printer.util.exception.DataNotFoundException;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.util.SqlSessionFactoryBuild;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.PrinterMapper;
import com.qg.smpt.web.repository.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.border.Border;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 接收外卖订单数据的Servlet
 */
public class ReceOrderServlet {

    private static final Logger LOGGER =  Logger.getLogger(ReceOrderServlet.class);


    public void doGet(Integer userId, Order order) throws IOException, DataNotFoundException {
        User user = DatabaseUtil.getSafeUser(userId);
        order.setUserId(userId);
        setOrderData(user, order);
    }


    /**
     * 进行订单组装, 将该订单组装到一个批次订单里, 如果该批次订单满足容量, 则放到user下的双端队列中
     * @param order
     */
    private void setOrderData(User user, Order order) {
        BulkOrder bOrders = user.getPackingBulkOrder().get();
        BOrder bOrder = order.orderToBOrder((short) user.getCurrentBulkOrderId(), (short) bOrders.getbOrders().size());

        LOGGER.log(Level.INFO, "用户ID: {0}; 已组装批次大小: {1}; 该订单大小: {2}; 阈值: {3}", user.getId(),
                bOrders.getDataSize(), bOrder.size, Constants.MAX_TRANSFER_SIZE);

        if (bOrders.getDataSize() + bOrder.size > Constants.MAX_TRANSFER_SIZE) {
            user.getNonSendBulkOrder().add(bOrders);
            BulkOrder newBulkOrder = new BulkOrder();
            newBulkOrder.addOrders(order, bOrder);
            user.getPackingBulkOrder().set(newBulkOrder);

            notifyOrderToPrinter(user);

            return ;
        }

        bOrders.addOrders(order, bOrder);
        bOrders.setId(bOrder.getBulkId());

    }


    private void notifyOrderToPrinter(User user) {
        synchronized (user.getOrderToPrinter().get()) {
            user.getOrderToPrinter().get().notifyAll();
            user.getOrderToPrinter().get().setSendAvailable(true);
        }
    }

}

