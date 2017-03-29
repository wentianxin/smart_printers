package com.qg.smpt.printer;

import com.qg.smpt.printer.model.BBulkOrder;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.DebugUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 订单发送到打印机的线程
 */
public class OrderToPrinter implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(OrderToPrinter.class);
    /**
     * 防止信号量丢失
     */
    private volatile boolean sendAvailable = false;

    private User user;

    private Queue<Printer> printerQueue;


    public OrderToPrinter(User user) {
        this.user = user;
        printerQueue = user.getCanUsePrinters();
    }

    public void setSendAvailable(boolean sendAvailable) {
        this.sendAvailable = sendAvailable;
    }

    public boolean isSendAvailable() {
        return sendAvailable;
    }


    @Override
    public void run() {
        LOGGER.log(Level.INFO, "用户线程开启; 用户: {0}", user.getId());
        while (true) {
            check();

            Printer printer = selectPrinter();

            send(printer);
        }
    }

    private void check() {
        int count = 0;

        if (printerQueue.size() >= 1 && user.getNonSendBulkOrder().size() >= 1) {
            LOGGER.log(Level.INFO, "用户发送条件已满足: 打印机队列数 {0}; 待发送批次订单数: {1}; 用户: {2}", printerQueue.size(), user.getNonSendBulkOrder().size(), user.getId());
            return ;
        }

        synchronized (this) {
            while (true) {
                try {
                    wait(Constants.SEND_INTERVAL);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (count >= 6) {
                    count = 0;
                    if (user.getPackingBulkOrder().get().getOrders().size() > 0 && printerQueue.size() > 0) {
                        BulkOrder bulkOrder = user.getPackingBulkOrder().get();
                        user.getPackingBulkOrder().set(new BulkOrder());
                        user.getNonSendBulkOrder().add(bulkOrder);
                        break;
                    }
                }
            }
        }

        LOGGER.log(Level.INFO, "商家等待时间达到, 发送正在包装的订单; 商家: {0}; 打印机数: {1}; 待发送订单数: {2}",
                user.getId(), user.getCanUsePrinters().size(), user.getNonSendBulkOrder().size());
    }

    private Printer selectPrinter() {
        return printerQueue.remove();
    }

    private void send(Printer printer) {
        printer.setCanAccept(false);

        BulkOrder bulkOrder = user.getNonSendBulkOrder().poll();

        for(Order order: bulkOrder.getOrders()) {
            order.setMpu(printer.getId());
        }

        BBulkOrder bBulkOrder = BulkOrder.convertBBulkOrder(bulkOrder, bulkOrder.getBulkType() == (short)0x2);
        byte[] bBulkOrderBytes = BBulkOrder.bBulkOrderToBytes(bBulkOrder);
        if (bBulkOrderBytes.length % 4 != 0) {
          //  LOGGER.log(Level.ERROR, "打印机 [{0}] 线程 printerConnector[{1}] 字节并未对齐");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(bBulkOrderBytes);
        try {
            int n = printer.getSocketChannel().write(byteBuffer);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final long sendtime = System.currentTimeMillis();
        final String orderSent = String.valueOf(BConstants.orderSent);
        for (Order o : bulkOrder.getOrders()) {
            o.setSendTime(sendtime);
            o.setOrderStatus(orderSent);
        }

        printer.getSendedBulkOrder().add(bulkOrder);
        printer.setSendedOrdersNum(printer.getSendedOrdersNum() + bulkOrder.getOrders().size());

       // DebugUtil.printBytes(byteBuffer.array());

        LOGGER.log(Level.INFO, "发送成功");
    }

    public void stop() {

    }
}
