package com.qg.smpt.printer;

import com.qg.smpt.printer.model.*;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.web.model.Printer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 打印机的线程调度：将缓存队列中数据组装并发送到给打印机，将缓存队列转发到已发队列
 * Created by tisong on 7/21/16.
 */
public class PrinterProcessor implements Runnable, Lifecycle{

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

    public PrinterProcessor(int id) {
        this.id = id;
    }

    public void start() throws LifecycleException{
        if (started) {

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

    private void parseOkStatus() {

    }

    private void parseOrderStatus(byte[] bytes) {
        BOrderStatus bOrderStatus = BOrderStatus.bytesToOrderStatus(bytes);

        if ( (byte)((bOrderStatus.flag >> 8) & 0xFF ) == (byte) BConstants.orderSucc) {
            // 订单成功
        }else if((byte)((bOrderStatus.flag >> 8) & 0xFF ) == (byte) BConstants.orderFail) {
            // 订单异常 需要重新发送订单
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

    private void parsePrintStatus(byte[] bytes) {
        BPrinterStatus bPrinterStatus = BPrinterStatus.bytesToPrinterStatus(bytes);

        //TODO 状态待分析
    }

    /**
     * 建立用户打印机-关系
     */
    private void buildUserPrintRelation() {

    }

    /**
     * 发送批次订单
     */
    private void sendBatchOrder() {

        // 进行条件判断, 适当睡眠

        // 唤醒时，唤醒全局线程-所有线程再进行条件判断。

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
