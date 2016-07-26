package com.qg.smpt.receive;

import com.qg.smpt.printer.Constants;
import com.qg.smpt.printer.PrinterProcessor;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 接收外卖订单数据的Servlet
 */
@WebServlet(name = "orderservlet")
public class ReceOrderServlet extends HttpServlet {
    private static final Logger LOGGER =  Logger.getLogger(ReceOrderServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取商家id
        Integer userId = null;
        try {
            userId = Integer.valueOf(request.getParameter("userId"));
        } catch (NumberFormatException e) {

        }
        // 根据商家id获取商家的打印机信息
        List<Printer> printers =  ShareMem.userListMap.get(userId);

        if (printers != null && printers.size() > 0) {
            if (ShareMem.priBufferMapList != null) {
                // TODO 关于如何获取Order数据
                Order order = parseOrder(request);
                // TODO 缺少智能分发算法
                Printer printer = printers.get(0);

                LOGGER.log(Level.DEBUG, "分发的打印机id: [{0}], 商家: [{1}]", printer.getId(), printer.getUserName());

                List<BulkOrder> bOrdersList = ShareMem.priBufferMapList.get(printer);

                synchronized (printer) {
                    if (bOrdersList == null) {
                        bOrdersList = new ArrayList<BulkOrder>();

                        ShareMem.priBufferMapList.put(printer, bOrdersList);
                        BulkOrder bOrders = new BulkOrder(new ArrayList<BOrder>());
                        bOrdersList.add(bOrders);

                    }
                    if (bOrdersList.size() == 0) {
                        bOrdersList.add(new BulkOrder(new ArrayList<BOrder>()));
                    }

                    // 批次订单
                    BulkOrder bOrders = bOrdersList.get(bOrdersList.size() - 1);

                    BOrder bOrder = order.orderToBOrder((short) printer.getCurrentBulk(), (short) bOrders.getbOrders().size());

                    if (bOrders.getDataSize() + bOrder.size > Constants.MAX_TRANSFER_SIZE) {
                        // 需要将该订单分发给下一个批次订单
                        LOGGER.log(Level.DEBUG, "批次容量已满足" + bOrders.getId());
                        bOrders = new BulkOrder(new ArrayList<BOrder>());
                        bOrdersList.add(bOrders);
                        bOrder.inNumber = (short) 0x1;

                        // 如果打印机可以接收数据，唤醒线程
                        if (printer.isCanAccpet()) {
                            PrinterProcessor processor = ShareMem.priPriProcessMap.get(printer);
                            if (processor == null) {
                                LOGGER.log(Level.ERROR, "printerProcessor is null, send data if failed");
                                return ;
                            }
                            processor.notifyOK();
                        }
                    } else {
                        LOGGER.log(Level.DEBUG, "批次容量并不满足" + bOrders.getId());
                    }
                }
            }
        } else {
            LOGGER.log(Level.ERROR, "商家并未购买打印机");
        }

    }

    private Order parseOrder(HttpServletRequest request) {

        return new Order();

    }


}

