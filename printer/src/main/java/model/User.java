package model;

import receive.OrdersSenderProcessor;

import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by tisong on 4/7/17.
 */
public class User {

    private int userId;

    private SendOrderContext sendOrderContext;

    private volatile boolean hasThread = false;



    /**
     * 订单发送上下文
     */
    public static class SendOrderContext {
        /**
         * 接下来的批次ID
         */
        private AtomicInteger currentBulkOrderId;

        private AtomicReference<OrdersSenderProcessor> ordersSenderProcessor;

        private Thread orderToPrinterThread;

        private Deque<BulkOrder> nonSendBulkOrder; // 待发送的批次队列

        private AtomicReference<BulkOrder> packingBulkOrder; // 正在组装的批次队列

        private Queue<Printer> canUsePrinters = null;


        public AtomicInteger getCurrentBulkOrderId() {
            return currentBulkOrderId;
        }

        public void setCurrentBulkOrderId(AtomicInteger currentBulkOrderId) {
            this.currentBulkOrderId = currentBulkOrderId;
        }

        public AtomicReference<OrdersSenderProcessor> getOrdersSenderProcessor() {
            return ordersSenderProcessor;
        }

        public void setOrdersSenderProcessor(AtomicReference<OrdersSenderProcessor> ordersSenderProcessor) {
            this.ordersSenderProcessor = ordersSenderProcessor;
        }

        public Thread getOrderToPrinterThread() {
            return orderToPrinterThread;
        }

        public void setOrderToPrinterThread(Thread orderToPrinterThread) {
            this.orderToPrinterThread = orderToPrinterThread;
        }

        public Deque<BulkOrder> getNonSendBulkOrder() {
            return nonSendBulkOrder;
        }

        public void setNonSendBulkOrder(Deque<BulkOrder> nonSendBulkOrder) {
            this.nonSendBulkOrder = nonSendBulkOrder;
        }

        public AtomicReference<BulkOrder> getPackingBulkOrder() {
            return packingBulkOrder;
        }

        public void setPackingBulkOrder(AtomicReference<BulkOrder> packingBulkOrder) {
            this.packingBulkOrder = packingBulkOrder;
        }

        public Queue<Printer> getCanUsePrinters() {
            return canUsePrinters;
        }

        public void setCanUsePrinters(Queue<Printer> canUsePrinters) {
            this.canUsePrinters = canUsePrinters;
        }
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public SendOrderContext getSendOrderContext() {
        return sendOrderContext;
    }

    public void setSendOrderContext(SendOrderContext sendOrderContext) {
        this.sendOrderContext = sendOrderContext;
    }

    public boolean isHasThread() {
        return hasThread;
    }

    public void setHasThread(boolean hasThread) {
        this.hasThread = hasThread;
    }
}
