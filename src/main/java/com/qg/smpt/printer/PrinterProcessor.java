package com.qg.smpt.printer;

import com.qg.smpt.printer.model.*;
import com.qg.smpt.printer.util.FormatUtil;
import com.qg.smpt.printer.util.database.DatabaseUtil;
import com.qg.smpt.printer.util.exception.DataNotFoundException;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.util.TimeUtil;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 打印机的线程调度：将缓存队列中数据组装并发送到给打印机，将缓存队列转发到已发队列
 */
public class PrinterProcessor implements Runnable, Lifecycle{

    private final Logger LOGGER = Logger.getLogger(PrinterProcessor.class);

    private Thread thread = null;

    private String threadName = null;

    private int id;                 // 当前线程id


    // TODO 关于字节数组分配过下,而导致需要两次调用的问题
    private ByteBuffer byteBuffer;

    private boolean available;     // 唤醒线程池中的线程的标志-普通订单发送标志

    private boolean started;

    private boolean stopped;

    private volatile boolean isConnected;

    private SocketChannel socketChannel;


    public PrinterProcessor(int id, PrinterConnector printerConnector) {
        this.id = id;
        this.threadName = "PrinterProcessor[" + printerConnector.getPort() + "][" + id + "}";
        this.available = false;
        this.started = false;
        this.stopped = false;
        this.isConnected = false;
    }


    /* 生命周期管理 */
    public void start() throws LifecycleException{
        if (started) {
            throw new LifecycleException("printerProcessor already started");
        }

        started = true;

        threadStart();
    }

    @Override
    public void stop() throws LifecycleException {

    }

    /**
     * 开启后台线程
     */
    private void threadStart() {
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }


    /* 线程启动 睡眠 唤醒模块*/
    public void run() {
        // 数据转发
        while (!stopped) {
            SocketChannel socketChannel = await();
            if (socketChannel == null) {
                continue;
            }

            try {
                parseData(socketChannel, this.byteBuffer);
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 线程池中的线程进入睡眠状态
     * @return SocketChannel
     */
    private synchronized SocketChannel await() {

        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

    //    LOGGER.log(Level.DEBUG, "printerProcessor thread is notified");

        SocketChannel socketChannel = this.socketChannel;

        available = false;

        notifyAll();

        return socketChannel;
    }
    /**
     * 唤醒线程池中睡眠的线程
     * @param sc
     */
    public synchronized void assign(SocketChannel sc, ByteBuffer byteBuffer) {

        // 根据不同的请求数据进行不同的处理
        // 1. 第一次请求：建立用户与打印机关系；
        // 2. 内存发送
        // 3. 订单状态 （批次，单次）
        // 4. 打印机状态

        // 5. 接收数据后： 发送数据（条件判断-根据打印机id，即线程要拥有。来获取每个打印机所拥有的数据）
        // 内存容量，缓存容量，时间间隔

        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = true;

        this.socketChannel = sc;

        this.byteBuffer = byteBuffer;

        notifyAll();
    }

    /**
     * 解析数据
     */
    private void parseData(SocketChannel socketChannel, ByteBuffer byteBuffer) throws DataNotFoundException {

        // 将byteBuffer 中的字节数组进行提取
        byte[] bytes = byteBuffer.array();


        if (bytes[0] == (byte)0xCF && bytes[1] == (byte)0xFC) {
            switch (bytes[2]) {
                case BConstants.connectStatus :
                    parseConnectStatus(bytes, socketChannel);
                    break;
                case BConstants.okStatus:
                    parseOkStatus(bytes, socketChannel);
                    break;
                case BConstants.orderStatus:
                    parseOrderStatus(bytes, socketChannel);
                    break;
                case BConstants.bulkStatus:
                    parseBulkStatus(bytes);
                    break;
                case BConstants.printStatus:
                    parsePrinterStatus(bytes);
                    break;
                default:
                    LOGGER.log(Level.WARN, "打印机发送状态数据错误");
                    return ;
            }
        } else {
            LOGGER.log(Level.INFO, "收到无效数据");
        }

    }

    /**
     * 处理打印机的连接请求
     * 1. 检查是否是重复连接
     * 2. 检查打印机和用户是否存在， 若不存在，则需要线程安全的创建User对象
     * 3. 初始化打印机状态
     * @param bytes
     */
    private void parseConnectStatus(byte[] bytes, SocketChannel socketChannel) throws DataNotFoundException {
        BRequest bRequest = BRequest.bytesToRequest(bytes);
        int printerId = bRequest.printerId;

        if (checkPrinter(printerId)) {
            // TODO bug
            LOGGER.log(Level.WARN, "打印机已在内存（可能是断开后立刻重连)");
            return ;
        }

        Integer userId = DatabaseUtil.getUserId(printerId);
        User user = DatabaseUtil.getSafeUser(userId);
        DatabaseUtil.initUser(userId);

        Printer printer = DatabaseUtil.getPrinter(printerId);
        initPrinterStatus(printerId, printer, socketChannel, user);

        isConnected = true;

        LOGGER.log(Level.INFO, "打印机连接建立成功: 打印机: {0}", printerId);
    }


    /**
     * 解析打印机阈值请求, 并向打印机发送数据
     * @param bytes
     * @param socketChannel
     */
    private void parseOkStatus(byte[] bytes, SocketChannel socketChannel) throws DataNotFoundException {
        BRequest request = BRequest.bytesToRequest(bytes);
        int printerId = request.printerId;


        Printer p = checkConnectionHappersBeforeOK(printerId);
        if (p.isCanAccept()) {
            return ;
        }

        p.getUser().getPrinters().add(p);
        p.setCanAccept(true);

        synchronized (p.getUser().getOrderToPrinter().get()) {
            p.getUser().getOrderToPrinter().get().notifyAll();
        }

        LOGGER.log(Level.INFO, "打印机收到OK状态; 打印机: {0}", printerId);
    }

    /**
     * 解析打印机发送的订单状态
     * 状态码: 0x0 打印成功 从已发队列中的订单状态标志为成功
     *        0x1 | 0x4 打印出错 获取已发队列批次订单的单个订单数据，组装新的批次订单（加急标志），放入异常队列。改：并不删除
     *        0x2 | 0x3 打印状态(进入打印队列;开始打印) 从已发队列中的订单状态标志为成功
     *        0x5 因异常而打印成功
     * @param bytes
     * @param socketChannel
     */
    private void parseOrderStatus(byte[] bytes, SocketChannel socketChannel) {
        BOrderStatus bOrderStatus = BOrderStatus.bytesToOrderStatus(bytes);
        byte flag = (byte)(bOrderStatus.flag  & 0xFF);
        int printerId = bOrderStatus.printerId;
        Printer printer = ShareMem.printerIdMap.get(printerId);
        if (printer == null) {
            return ;
        }

        LOGGER.log(Level.INFO, "订单状态; 订单: {0}; 状态: {1}", bOrderStatus.inNumber, FormatUtil.getOrderStatus(flag));

        /* 获取批次订单队列 flag 0x5 : 获取异常批次订单队列; others : 获取已发送批次订单队列 */
        List<BulkOrder> bulkOrderList = printer.getSendedBulkOrder();

        /* 获取批次订单中的订单内容 */
        BulkOrder bulkOrderF = null; // 订单所在的批次订单
        Order order = null;          // 处理的订单
        BOrder bOrder = null;        // 处理的订单
        int position;               // 记录批次订单在缓存队列中的位置
        for (position = 0; position < bulkOrderList.size(); position++) {
            bulkOrderF = bulkOrderList.get(position);
            if (bulkOrderF.getId() == bOrderStatus.bulkId) { // TODO
                if (bulkOrderF.getbOrders().size() < bOrderStatus.inNumber) {
                    LOGGER.log(Level.ERROR, "批次 [{2}] 批次内序号 [{0}] 超出批次订单范围", bOrderStatus.bulkId, bOrderStatus.inNumber);
                    return;
                }
                order = getOrderInOrders(bOrderStatus.inNumber, bulkOrderF.getOrders());
                bOrder = getBOrderInBOrders(bOrderStatus.inNumber, bulkOrderF.getbOrders());
                changeOrderStatus(order,flag, bulkOrderF, bOrderStatus, printer);
                break;
            }
        }
        if (position == bulkOrderList.size()) {
            LOGGER.log(Level.WARN, "打印机[{0}] 批次订单号[{1}] 订单内序号[{2}] 批次并不存在", bOrderStatus.printerId,
                    bOrderStatus.bulkId, bOrderStatus.inNumber);
            return ;
        }


        /* 失败 重发数据*/
        if ( flag == BConstants.orderFail || flag == BConstants.orderDataW) {
            processFailOrder(bulkOrderF, order, bOrder, bOrderStatus, printer, flag);
        } else if ( flag == BConstants.orderSucc && processSuccOrder(bulkOrderF, order)) {
            bulkOrderList.remove(position);
        }else if ( flag == BConstants.orderExcep ) {
            processFailAfterSuccess(order, bulkOrderF);
            bulkOrderList.remove(position);
            for (int i = 0; i < printer.getExceptionBulkOrder().size(); i++) {
                if (printer.getExceptionBulkOrder().get(i).getId() == bOrderStatus.bulkId) {
                    printer.getExceptionBulkOrder().remove(i);
                    LOGGER.log(Level.INFO, "异常订单成功移除");
                    break;
                }
            }
        }

        switch (flag) {
            case BConstants.orderInQueue: LOGGER.log(Level.DEBUG, "订单进入打印队列; 订单: [0]", order.getId());
                                          order.setOrderStatus(Integer.valueOf(BConstants.orderInQueue).toString());
                                          break;
            case BConstants.orderTyping : LOGGER.log(Level.DEBUG, "订单正在打印; 订单: [0]", order.getId());
                                          order.setOrderStatus(Integer.valueOf(BConstants.orderTyping).toString());
                                          break;
            default: LOGGER.log(Level.WARN, "打印机 [{0}] 无g该状态 当前线程 [{1}]", bOrderStatus.printerId, this.id);
        }
    }

    /**
     * 解析打印机发送的批次订单状态 暂时废弃, 当批次中的单个订单全部反馈完状态后，插入数据库中
     * @param bytes
     */
    private void parseBulkStatus(byte[] bytes) {
        BBulkStatus bBulkStatus = BBulkStatus.bytesToBulkStatus(bytes);

//
//        if ( (byte)(bBulkStatus.flag  & 0xFF) == (byte) BConstants.bulkSucc) {
////        	// 批次订单成功
////            // 将已发队列中数据装填到数据库中，并清除已发队列
//
//            // TODO 模拟打印机id
//            int printerId = 1;
//            Printer printer = ShareMem.printerIdMap.get(printerId);
//            List<BulkOrder> bulkOrders = ShareMem.priSentQueueMap.get(printer);
//            if (bulkOrders == null || bulkOrders.size() < 1) {
//                LOGGER.log(Level.WARN, "打印机 [{0}] 的发送队列无数据 printerProcessor 线程 [{1}]", printerId, this.id);
//                return ;
//            }
//            BulkOrder bulkOrder = null;
//            synchronized (ShareMem.priSentQueueMap.get(printer)) {
//                for (int i = 0; i < bulkOrders.size(); i++) {
//                    bulkOrder = bulkOrders.get(i);
//                    if (bBulkStatus.bulkId == bBulkStatus.bulkId) {
//                        LOGGER.log(Level.DEBUG, "打印机 [{0}] 找到批次订单 [{1}] printerProcessor 线程 [{2}]", printerId, bBulkStatus.bulkId,
//                                this.id);
//                        bulkOrders.remove(i);
//                        break;
//                    }
//                }
//            }
//
//      //      LOGGER.log(Level.DEBUG, "打印机 [{0}] 处理成功批次订单， 向数据库中写入数据 printerProcessor 线程 [{1}]", printerId, this.id);
//
//            SqlSession sqlSession = sqlSessionFactory.openSession();
//            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
////            try {
////                Order o = null;
////                for (int i = 0; i < bulkOrder.getbOrders().size(); i++) {
////                    o = bulkOrder.getOrders().get(i);
////                    orderMapper.insert(o);
////                    orderMapper.insertUserOrder(o.getUserId(), o.getId());
////                }
////            } finally {
////                sqlSession.commit();
////                sqlSession.close();
////            }
//
//        } else if ( (byte)(bBulkStatus.flag  & 0xFF) == (byte) BConstants.bulkInBuffer) {
//        	// 批次进入缓冲区 超时重新传送
//            LOGGER.log(Level.DEBUG, "暂未处理批次订进入缓冲区 当前线程[{0}]", this.id);
//        } else if ( (byte)(bBulkStatus.flag & 0xFF) == (byte) BConstants.bulkFail) {
//            LOGGER.log(Level.DEBUG, "暂未处理批次订错误 当前线程[{0}]", this.id);
//        }
    }

    /**
     * 记录打印机状态到内存中
     * @param bytes
     */
    private void parsePrinterStatus(byte[] bytes) throws DataNotFoundException {
        BPrinterStatus bPrinterStatus = BPrinterStatus.bytesToPrinterStatus(bytes);

        Printer printer = checkConnectionHappersBeforeOK(bPrinterStatus.printerId);

        if (printer == null) {
            LOGGER.log(Level.WARN, "打印机[{0}]未找到内中中对应打印机对象", bPrinterStatus.printerId);
            return ;
        }

        printer.setPrinterStatus( ( (bPrinterStatus.flag ) & 0xFF ) + "");
    }


    /* getter 模块 */
    public int getId() {
        return id;
    }

    private boolean checkPrinter(int printerId) {
        return ShareMem.printerIdMap.get(printerId) != null;
    }

    /**
     * 初始化打印机状态
     * 1. 初始化打印机id - 打印机
     * 2. 设置打印机connect状态
     * 3. 设置打印机批次订单初始号
     * 4. 设置打印机对应的SocketChannel
     * 5. 初始化打印机id - 打印机异常已发队列/异常队列
     * 6. 创建输出线程
     * @param printerId
     * @param printer
     */
    private void initPrinterStatus(int printerId, Printer printer, SocketChannel socketChannel, User user) {
        ShareMem.printerIdMap.put(printerId, printer);
        printer.setConnected(true);
        printer.setSocketChannel(socketChannel);
        printer.setSendedBulkOrder(new CopyOnWriteArrayList<>());
        printer.setExceptionBulkOrder(new CopyOnWriteArrayList<>());
        printer.setUser(user);
        user.getCanUsePrinters().add(printer);
    }

    private Printer checkConnectionHappersBeforeOK(int printerId) throws DataNotFoundException {
        Printer p = ShareMem.printerIdMap.get(printerId);
        if (p == null) {
            synchronized (this) {
                while (!isConnected) {
                    try {
                        wait(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (ShareMem.printerIdMap.get(printerId) == null) {
                throw new DataNotFoundException("打印机为连接就发送了OK状态(也可能是未处理完Connection)" + printerId);
            }
        }
        return p;
    }

    private void changeOrderStatus(Order order, byte flag, BulkOrder bulkOrderF, BOrderStatus bOrderStatus, Printer printer) {
        order.setOrderStatus(String.valueOf(bOrderStatus.flag & 0xFF));  // 设置订单状态 //
        long time = System.currentTimeMillis();
        switch (flag) {
            case BConstants.orderInQueue     : order.setEnterQueueTime(time); break;
            case BConstants.orderTyping      : order.setStartPrintTime(time); break;
            case BConstants.orderFail        : order.setPrintResultTime(time);
                                               printer.setPrintErrorNum(printer.getPrintErrorNum() + 1);
                                               break;
            case BConstants.orderSucc        : printer.setPrintSuccessNum(printer.getPrintErrorNum() + 1);
                                               order.setPrintResultTime(time);
                                               break;
            case BConstants.orderExcepInQueue: order.setExecEnterQueueTime(time); break;
            case BConstants.orderExcepTyping : order.setExecStartPrintTime(time); break;
            case BConstants.orderExcep       :
            case BConstants.orderExcepDataW  :
            case BConstants.orderExcepFail   : order.setExecPrintResultTime(time);break;
        }
    }

    private void processFailOrder(BulkOrder bulkOrderF, Order order, BOrder bOrder, BOrderStatus bOrderStatus, Printer printer, byte flag) {
        bulkOrderF.increaseReceNum();

        // TODO 如何获取打印机发来的异常订单被更新后的数据
        order.setExecSendTime(System.currentTimeMillis());
        LOGGER.log(Level.INFO, "打印机 [{0}] 打印订单 (订单批次号 [{1}], 批次内序号 [{2}]) 失败; 当前时间为 [{4}]," +
                        " 离发送订单相差的时间为 [{5}]",
                bOrderStatus.printerId, bOrderStatus.bulkId, bOrderStatus.inNumber,
                TimeUtil.timeToString(order.getExecSendTime()), order.getExecSendTime() - bulkOrderF.getSendtime());


        BulkOrder bulkOrder = packFailedOrder(order, bOrder, printer, printer.getUserId(), flag);
        order.setOrderStatus(String.valueOf(flag));
        bulkOrder.setBulkType((short)2);
        printer.getExceptionBulkOrder().add(bulkOrder);
        printer.getUser().getNonSendBulkOrder().addFirst(bulkOrder);

        /*  转化发送批次订单数据 */
//        byte[] bBulkOrderByters = BBulkOrder.bBulkOrderToBytes(BulkOrder.convertBBulkOrder(bulkOrder, true));
//        bBulkOrderByters[15] = (byte)0x1;


        long resendTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "打印机 [{0}] 重新发送批次 [{1}] 中的异常订单 [{2}] ，当前时间为 [{3}] 距离接受到异常报告时" +
                        "所过去的时间为 [{4}] ms", bOrderStatus.printerId, bulkOrderF.getId(),bOrderStatus.inNumber,
                TimeUtil.timeToString(resendTime), resendTime - order.getExecSendTime());

    }

    private boolean processSuccOrder(BulkOrder bulkOrder, Order order) {
        bulkOrder.increaseReceNum();
        LOGGER.log(Level.DEBUG, "订单处理成功; 订单: [0]; 批次: [1]", order.getId(), bulkOrder.getId());
        order.setOrderStatus(Integer.valueOf(BConstants.orderSucc).toString());
        if (bulkOrder.getReceNum().get() < bulkOrder.getOrders().size()) {
            return false;
        }
        DatabaseUtil.insertBulkOrder(bulkOrder);
        return true;
    }

    private BulkOrder packFailedOrder(Order order, BOrder bOrder, Printer printer, int userId, byte flag) {
              /* 组装批次订单 */
        BulkOrder bulkOrder = new BulkOrder();
        LOGGER.log(Level.DEBUG, "订单处理失败; 订单: [0]; 批次: [1]", order.getId(), bulkOrder.getId());
        // 判断该次要求重发的订单是否是故意封装错误的订单，如果是重新封装成正确的订单
        if(order.getIndexError() >= 0 && order.getIndexError() < 3) {
            order.setIndexError(4);
        }

        // 根据bulkid 和 批次内序号 : 1
        printer.increaseBulkId();
        bulkOrder.setId(printer.getCurrentBulk().get()); // 批次订单id
        bulkOrder.setBulkType((short) 1); // 加急
        bulkOrder.addOrders(order, order.orderToBOrder((short)bulkOrder.getId(), (short)1));


        bulkOrder.setUserId(userId);
        order.setOrderStatus(Integer.valueOf(flag).toString());

        return bulkOrder;
    }

    private void processFailAfterSuccess(Order order, BulkOrder bulkOrder) {
        LOGGER.log(Level.DEBUG, "异常订单处理成功; 订单: [0]; 批次: [1]", order.getId(), bulkOrder.getId());
        order.setOrderStatus(Integer.valueOf(BConstants.orderExcep).toString());
        DatabaseUtil.insertBulkOrder(bulkOrder);
    }
    private Order getOrderInOrders(int inNumber, Queue<Order> orders) {
        for (Order o : orders) {
            if (--inNumber <= 0) {
                return  o;
            }
        }
        return null;
    }

    private BOrder getBOrderInBOrders(int inNumber, Queue<BOrder> bOrders) {
        for (BOrder o: bOrders) {
            if (--inNumber <= 0) {
                return o;
            }
        }
        return null;
    }
}
