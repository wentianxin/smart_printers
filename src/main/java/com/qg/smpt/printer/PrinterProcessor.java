package com.qg.smpt.printer;

import com.qg.smpt.printer.model.*;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.repository.PrinterMapper;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.qg.smpt.share.ShareMem.priSentQueueMap;
import static java.lang.System.currentTimeMillis;

/**
 * 打印机的线程调度：将缓存队列中数据组装并发送到给打印机，将缓存队列转发到已发队列
 * Created by tisong on 7/21/16.
 */
public class PrinterProcessor implements Runnable, Lifecycle{
    @Resource
    private PrinterMapper printerMapper;

    private final Logger LOGGER = Logger.getLogger(PrinterConnector.class);

    private Thread thread = null;

    private String threadName = null;

    private Object threadSync = new Object();

    private int id;

    private long waitTime;

    // TODO 关于字节数组分配过下,而导致需要两次调用的问题
    private ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

    private boolean available;   // 唤醒线程池中的线程的标志

    private boolean sendAvailable; // 唤醒因发送条件不满足而进入睡眠状态的线程

    private boolean started;

    private boolean stopped;

    private SocketChannel socketChannel;

    public PrinterProcessor(int id, PrinterConnector printerConnector) {
        this.id = id;
        this.threadName = "PrinterProcessor[" + printerConnector.getPort() + "][" + id + "}";
        this.available = false;
        this.sendAvailable = false;
        this.started = false;
        this.started = false;
    }

    public void start() throws LifecycleException{
        if (started) {
            throw new LifecycleException("printerProcessor already started");
        }

        started = true;

        threadStart();
    }

    public void stop() {

    }

    /**
     * 开启后台线程
     */
    private void threadStart() {
        LOGGER.log(Level.DEBUG, "printerProcessor starting");
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    private void threadStop() {

    }

    public void run() {
        // 数据转发
        while (!stopped) {
            SocketChannel socketChannel = await();
            if (socketChannel == null) {
                continue;
            }

            parseData(socketChannel);
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

        LOGGER.log(Level.DEBUG, "printerProcessor thread is notified");

        SocketChannel socketChannel = this.socketChannel;

        available = false;

        notifyAll();

        return socketChannel;
    }

    /**
     * 唤醒线程池中睡眠的线程
     * @param sc
     */
    public synchronized void assign(SocketChannel sc) {

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

            }
        }

        available = true;

        this.socketChannel = sc;

        notifyAll();
    }

    /**
     * 解析数据
     */
    private void parseData(SocketChannel socketChannel){

        LOGGER.log(Level.DEBUG, "printerProcessor [{0}] parse data", this);

        try {
            byteBuffer.clear();

            socketChannel.read(byteBuffer);

            byteBuffer.flip();

            // 将byteBuffer 中的字节数组进行提取
            byte[] bytes = byteBuffer.array();

            LOGGER.log(Level.DEBUG, "printerProcessor receive bytes is: [{0}] ", bytes.toString());

            if (bytes[0] == (byte)0xCF && bytes[1] == (byte)0xFC) {
                switch (bytes[2]) {
                    case BConstants.connectStatus :
                        LOGGER.log(Level.DEBUG, "打印机发送连接数据，初始化打印机对象和打印机对象所拥有的缓存批次订单队列，异常订单队列，打印机处理线程");
                        parseConnectStatus(bytes);
                        break;
                    case BConstants.okStatus:
                        LOGGER.log(Level.DEBUG, "客户端发送过来可以请求数据");
                    	parseOkStatus(bytes, socketChannel);
                    	break;
                    case BConstants.orderStatus:
                        LOGGER.log(Level.DEBUG, "接收订单状态数据");
                    	parseOrderStatus(bytes, socketChannel);
                    	break;
                    case BConstants.bulkStatus:
                        LOGGER.log(Level.DEBUG, "接收打印机状态数据");
                    	parseBulkStatus(bytes);
                    	break;

                    case BConstants.printStatus:

                    default:
                }
            }
        } catch (IOException e) {

        }
    }

    /**
     * 处理打印机的连接请求
     * @param bytes
     */
    private void parseConnectStatus(byte[] bytes) {
        BRequest bRequest = BRequest.bytesToRequest(bytes);

        int printerId = bRequest.printerId;

        // 建立用户-printer 关系
        Printer printer = null;

        printer = ShareMem.printerIdMap.get(printerId);
        if (printer == null) {
            // id - printer 建立在商家注册时 进行建立
            printer = printerMapper.selectPrinter(printerId);
            if (printer == null) {
                LOGGER.log(Level.ERROR, "打印机信息并未注册[{0}]", printerId);
                return ;
            }

            // TODO 如果有两个线程同时向 HashMap中添加相同printerId， 是否会出现重复问题
            synchronized (ShareMem.printerIdMap) {
                ShareMem.printerIdMap.put(printerId, printer);
            }
            LOGGER.log(Level.ERROR, "将打印机[{0}]并为建立打印对象；打印机状态:[{1}];用户:[{2}]", printerId,
                    printer.getPrinterStatus(), printer.getUserId());
        }
        else {
            LOGGER.log(Level.WARN, "共享对象中已存在打印机[{0}]与打印机对象", printerId);
        }

        // TODO printer 锁是否有用
        /* 锁住 printer 打印机对象，避免出现创建多次队列现象*/
        synchronized (printer) {
            if (ShareMem.priExceQueueMap.get(printer) == null) {
                // 异常队列
                LOGGER.log(Level.INFO, "初始化打印机[{0}] 异常队列", printerId);
                ShareMem.priExceQueueMap.put(printer, new ArrayList<BulkOrder>());
            }
            if (ShareMem.priBufferMapList.get(printer) == null) {
                LOGGER.log(Level.INFO, "初始化打印机[{0}] 缓存队列", printerId);
                ShareMem.priBufferMapList.put(printer, new ArrayList<BulkOrder>());
            }
            if (priSentQueueMap.get(printer) == null) {
                LOGGER.log(Level.INFO, "初始化打印机[{0}] 已发队列", printerId);
                ShareMem.priSentQueueMap.put(printer, new ArrayList<BulkOrder>());
            }
        }

    }

    private synchronized void parseOkStatus(byte[] bytes, SocketChannel socketChannel) {
    	// 解析OK请求
        BRequest request = BRequest.bytesToRequest(bytes);

        // 获取打印机主控板id,获取打印机
        int printerId = request.printerId;

        LOGGER.log(Level.DEBUG, "解析请求打印机请求id:[{0}], flag:[{1}]," +
                "seconds:[{2}];checksum[{3}];" + request.printerId, request.flag, request.seconds,
                request.checkSum);

        Printer p = ShareMem.printerIdMap.get(printerId);
        if (p == null) {
            LOGGER.log(Level.ERROR, "共享内存中并为找到打印机id[{0}]对应printer对象", printerId);
            return ;
        }
        ShareMem.priPriProcessMap.put(p, this);

        LOGGER.log(Level.INFO, "建立打印机对象:[{1}] 与 PrinterConnector 线程之间连接, 处理 OKStatus", p);

        p.setCanAccpet(true);

        long requestTime = System.currentTimeMillis();
        LOGGER.log(Level.DEBUG, "当前时间: [{0}]", requestTime);
        try {
            while (!sendAvailable) {
                if (ShareMem.priBufferMapList.get(p).size() > 1)
                    break;
                LOGGER.log(Level.DEBUG, "打印机 [{0}] 的 线程printerConnector[{1}]并不满足发送条件，进入睡眠", printerId, this);
                wait(waitTime + 1 / 10 * waitTime);
                if (requestTime - System.currentTimeMillis() > waitTime) {
                    LOGGER.log(Level.INFO, "打印机 [{0}] 的 线程printerConnector[{1}]自动睡醒", printerId, this);
                    if (ShareMem.priBufferMapList.get(p).size() > 0) {
                        LOGGER.log(Level.INFO, "打印机 [{0}] 的 线程printerConnector[{1}] 发送缓冲区存有批次订单 [{2}] 条, 该批次中有订单数据 [{2}] 条, 容量 [{3}] 字节准备发送",
                                printerId, this, ShareMem.priBufferMapList.get(p).size(), ShareMem.priBufferMapList.get(p).get(0).getOrders().size(), ShareMem.priBufferMapList.get(p).get(0).getDataSize());
                        break;
                    }
                }
            }
        } catch (final InterruptedException e) {
            LOGGER.log(Level.ERROR, "打印机 [{0}] 的 线程printerConnector[{1}] 睡眠被打断", printerId, this, e);
        }

        LOGGER.log(Level.DEBUG, "打印机 [{0}] 解除绑定线程 printerConnector[{1}], 取消可发生状态, printer对象设置为不可接收数据状态", printerId, this);
        ShareMem.priPriProcessMap.remove(p);
        sendAvailable = false;
        p.setCanAccpet(false);

        notifyAll();

        BulkOrder bOrders = null;

        synchronized (ShareMem.priBufferMapList.get(p)) {
            LOGGER.log(Level.DEBUG, "打印机 [{0}] 解除绑定线程 printerConnector[{1}] 锁定打印机缓存队列 [{2}]," +
                    "从缓存队列中弹出批次订单数据", printerId, this, ShareMem.priBufferMapList.get(p));
            bOrders = ShareMem.priBufferMapList.get(p).get(0);

            ShareMem.priBufferMapList.get(p).remove(0);
        }

        synchronized (ShareMem.priSentQueueMap.get(p)) {
            LOGGER.log(Level.DEBUG, "打印机 [{0}] 绑定线程 printerConnector[{1}] 锁定打印机已发队列 [{2}]，" +
                    "并将批次订单数据加入已发队列中", printerId, this, ShareMem.priSentQueueMap.get(p));
            List<BulkOrder> bulkOrderList = ShareMem.priSentQueueMap.get(p);

            if (bulkOrderList == null) {
                bulkOrderList = new ArrayList<BulkOrder>();
                ShareMem.priSentQueueMap.put(p, bulkOrderList);
            }

            bulkOrderList.add(bOrders);
        }

        LOGGER.log(Level.DEBUG, "打印机 [{0}] 线程 printerConnector[{1}] 开始转换批次订单数据",
                printerId, this);
        BBulkOrder bBulkOrder = BulkOrder.convertBBulkOrder(bOrders);
        byte[] bBulkOrderBytes = BBulkOrder.bBulkOrderToBytes(bBulkOrder);

        if (bBulkOrderBytes.length % 32 != 0) {
            LOGGER.log(Level.ERROR, "打印机 [{0}] 线程 printerConnector[{1}] 字节并未对齐");
        }
        // TODO Debug 模式
        LOGGER.log(Level.DEBUG, "========== 订单数据字节流 start ============");
        for (int i = 0; i < bBulkOrderBytes.length / 32; i++) {
            byte[] test = new byte[32];
            System.arraycopy(bBulkOrderBytes, i * 32, test, 0, 32);
            LOGGER.log(Level.DEBUG, "[{0}]",Arrays.toString(test));
        }

        LOGGER.log(Level.DEBUG, "========== 订单数据字节流 end ============");
        ByteBuffer byteBuffer = ByteBuffer.wrap(bBulkOrderBytes);

        try {
            socketChannel.write(byteBuffer);
        } catch (final IOException e) {
            LOGGER.log(Level.ERROR, "打印机 [{0}] 打印机线程 printerProcessor [{1}] 发送订单数据异常", p.getId(), this);
        }

        LOGGER.log(Level.INFO, "打印机 [{0}] 对应 打印机线程 printerProcessor [{1}] 完成订单发送请求; 时间 [{2}]",
                printerId, this, System.currentTimeMillis());
    }



    public synchronized void notifyOK() {
        try {
            while (sendAvailable) {
                wait();
            }
        } catch (final InterruptedException e) {
            LOGGER.log(Level.ERROR, "线程 printerProcessor [{0}] 被中断", this, e);
        }

        sendAvailable = true;

        LOGGER.log(Level.INFO, "线程 printerProcessor [{0}] 设置可发送标志", this);

        notifyAll();
    }

    private void parseOrderStatus(byte[] bytes, SocketChannel socketChannel) {
        BOrderStatus bOrderStatus = BOrderStatus.bytesToOrderStatus(bytes);

        if ( (byte)(bOrderStatus.flag & 0xFF) == BConstants.bulkStatus) {
            if ( (byte)((bOrderStatus.flag >> 8) & 0xFF ) == (byte) BConstants.bulkSucc) {
                // 将已发队列中数据删除并放到数据库中

                Printer printer = ShareMem.printerIdMap.get(bOrderStatus.printerId);

                List<BulkOrder> bulkOrderList = ShareMem.priSentQueueMap.get(printer);

                if (bulkOrderList == null || bulkOrderList.size() <= 0) {
                    LOGGER.log(Level.ERROR, "已发队列数据异常: [{0}]", bOrderStatus.printerId);
                }


                BulkOrder bulkOrder = null;
                synchronized (bulkOrderList) {
                    // 锁住打印机所对应的已发队列
                    for (int i = 0; i < bulkOrderList.size(); i++) {
                        if (bulkOrderList.get(i).getId() == bOrderStatus.bulkId) {
                            bulkOrder = bulkOrderList.get(i);
                            bulkOrderList.remove(i);
                        }
                    }


                }

                if (bulkOrder != null) {
                    // TODO 放入数据库中
                }


            }
        } else if ( (byte)((bOrderStatus.flag >> 8) & 0xFF) == BConstants.orderFail ) {
                // TODO 如何获取打印机发来的异常订单被更新后的数据
            Printer printer = ShareMem.printerIdMap.get(bOrderStatus.printerId);

            List<BulkOrder> bulkOrderList = ShareMem.priBufferMapList.get(printer);

            for (int i = 0; i < bulkOrderList.size(); i++) {
                BulkOrder bulkOrderF = bulkOrderList.get(i);
                if ( bulkOrderF.getId() == bOrderStatus.bulkId) {


                    Order order = bulkOrderF.getOrders().get(bOrderStatus.inNumber);

                    BOrder bOrder = bulkOrderF.getbOrders().get(bOrderStatus.inNumber);

                    order.setOrderStatus(String.valueOf(bOrderStatus.flag & 0xFF));


                    BulkOrder bulkOrder = new BulkOrder(new ArrayList<BOrder>());

                    bulkOrder.getOrders().add(order);

                    bulkOrder.setbOrders(new ArrayList<BOrder>());

                    bulkOrder.getbOrders().add(bOrder);

                    bulkOrder.setBulkType((short)1);

                    bulkOrder.setDataSize(bOrder.size + 160);

                    bulkOrder.setId(bulkOrderF.getId());

                    bulkOrder.getbOrders().add(order.orderToBOrder((short)(bulkOrder.getId()), (short)0));

                    bulkOrder.setUserId(bulkOrderF.getUserId());

                    ShareMem.priExceQueueMap.get(printer).add(bulkOrder);

                    // 发送订单数据

                    byte[] bBulkOrderByters = BBulkOrder.bBulkOrderToBytes(BulkOrder.convertBBulkOrder(bulkOrder));
                    try {
                        socketChannel.write(ByteBuffer.wrap(bBulkOrderByters));
                    } catch (IOException e) {

                    }
                }
            }
        } else if ( (byte)((bOrderStatus.flag >> 8) & 0xFF) == BConstants.orderSucc ) {
            // 订单成功
        }



    }

    private void parseBulkStatus(byte[] bytes) {
        BBulkStatus bBulkStatus = BBulkStatus.bytesToBulkStatus(bytes);

        if ( (byte)((bBulkStatus.flag >> 8) & 0xFF) == (byte) BConstants.bulkSucc) {
        	// 批次订单成功
            // 将已发队列中数据装填到数据库中，并清除已发队列

        } else  if ( (byte)((bBulkStatus.flag >> 8) & 0xFF) == (byte) BConstants.bulkSucc) {
        	// 批次订单失败 忽略失败信息-bug


        }
    }

}
