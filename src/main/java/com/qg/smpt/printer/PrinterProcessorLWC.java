package com.qg.smpt.printer;

import com.qg.smpt.printer.model.*;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by asus2015 on 2016/7/22.
 */
public class PrinterProcessorLWC implements Runnable, Lifecycle{
    private Thread thread = null;

    private String threadName = null;

    private Object threadSync = new Object();

    private int id;

    // TODO 关于字节数组分配过下,而导致需要两次调用的问题
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private boolean available;

    private boolean started;

    private boolean stopped;

    private SocketChannel socketChannel;

    public PrinterProcessorLWC(int id) {
        this.id = id;
    }

    public void start() throws LifecycleException{
        if (started) {

        }

        started = true;

        threadStart(id);
    }

    public void stop() {

    }

    /**
     * 开启后台线程
     */
    private void threadStart(int i) {
        threadName = "Processor" + id;
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

    private SocketChannel await() {

        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        SocketChannel socketChannel = this.socketChannel;

        available = false;

        // notifyAll();

        return socketChannel;
    }

    public void assign(SocketChannel sc) {

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
        try {
            socketChannel.read(byteBuffer);

            byteBuffer.clear();

            byteBuffer.flip();

            // 将byteBuffer 中的字节数组进行提取
            byte[] bytes = byteBuffer.array();

            if (bytes[0] == (byte)0xCF && bytes[1] == (byte)0xFC) {
                switch (bytes[2]) {
                    case BConstants.connectStatus :

                    case BConstants.okStatus:

                    case BConstants.orderStatus:

                    case BConstants.bulkStatus:

                    case BConstants.printStatus:

                    default:
                }
            }
        } catch (IOException e) {

        }

    }

    private void parseConnectStatus(byte[] bytes) {
        BRequest bRequest = BRequest.bytesToRequest(bytes);

        int printerId = bRequest.printerId;

        // 建立用户-printer 关系


        if (ShareMem.printerIdMap.get(printerId) == null) {
            synchronized (ShareMem.printerIdMap) {
                ShareMem.printerIdMap.put(printerId, new Printer(printerId));
            }
        }
    }

    private void parseOkStatus(byte[] bytes) {
        // 解析OK请求
        BRequest request = BRequest.bytesToRequest(bytes);

        // 获取打印机主控板id,获取打印机
        int printerId = request.printerId;
        Printer p = ShareMem.printerIdMap.get(printerId);
        p.setCanAccpet(true);

        //执行发送数据
        try {

            OrderService orderService = new OrderService();
            orderService.sendBatchOrder(p);

        }catch(Exception e){
            //异常暂不处理,之后填上
        }
    }

    private void parseOrderStatus(byte[] bytes) {
        BOrderStatus bOrderStatus = BOrderStatus.bytesToOrderStatus(bytes);

        if ( (byte)((bOrderStatus.flag >> 8) & 0xFF ) == (byte) BConstants.orderSucc) {


        }else if((byte)((bOrderStatus.flag >> 8) & 0xFF ) == (byte) BConstants.orderFail) {
            // 订单异常 需要重新发送订单
            OrderService orderService = new OrderService();
            orderService.handleFailOrder(bOrderStatus.printerId, bOrderStatus.bulkId, bOrderStatus.inNumber);

        }


    }

    private void parseBulkStatus(byte[] bytes) {
        BBulkStatus bBulkStatus = BBulkStatus.bytesToBulkStatus(bytes);
        OrderService orderService = new OrderService();

        if ( (byte)((bBulkStatus.flag >> 8) & 0xFF) == (byte) BConstants.bulkSucc) {
            // 批次订单成功
            // 将已发队列中数据装填到数据库中，并清除已发队列
            orderService.handleSuccessfulBulk(bBulkStatus.printerId, bBulkStatus.bulkId);

        } else  if ( (byte)((bBulkStatus.flag >> 8) & 0xFF) == (byte) BConstants.bulkSucc) {
            // 批次订单失败 忽略失败信息-bug
            orderService.handleFailBulk(bBulkStatus.printerId,bBulkStatus.bulkId);
        }
    }

    private void parsePrintStatus(byte[] bytes) {
        BPrinterStatus bPrinterStatus = BPrinterStatus.bytesToPrinterStatus(bytes);

        //TODO 状态待分析
    }




    /**
     * 更新订单状态
     */
    private void receiveOrderStatus() {

    }

    /**
     * 更新打印机状态
     */
    private void receivePrinterStatus() {

    }
}
