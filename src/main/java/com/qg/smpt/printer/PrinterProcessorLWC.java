package com.qg.smpt.printer;

import com.qg.smpt.printer.model.BBulkOrder;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
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
        } catch (IOException e) {

        }

    }

    /**
     * 建立用户打印机-关系
     */
    private void buildUserPrintRelation() {

    }

    /**
     * 发送批次订单
     */
    private void sendBatchOrder() throws Exception{
        //获取打印机与他的订单集合
        Printer p = ShareMem.printerIdMap.get(id);
        Queue<Order> os = ShareMem.priBufferQueueMap.get(p);

        //判断 闲时/忙时
        if((os != null && os.size() <= 0) || !p.isCanAccpet()) {
            return;
        }


        doSend(p,os);

    }

    private void doSend(Printer printer, Queue<Order> orders) {
        try {
            //准备批次
            prepareBulk(printer.getCurrentBulk(), orders);

            //检验批次是否有错

            //通过 socketChenal 发送数据
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备批次订单,遍历订单集合取出订单组装成批次
     * @param bulkId    批次id
     * @param orders    订单集合
     * @throws Exception    暂定异常全抛,之后逻辑设计后再根据具体情况在里面抓获具体的异常
     */
    private void prepareBulk(int bulkId, Queue<Order> orders) throws Exception{
        //创建批次
        BulkOrder bulk = new BulkOrder(bulkId);
        BBulkOrder bulkB = new BBulkOrder();
        int currSize = 0;

        //遍历订单缓存队列,组装批次,发送窗口大小不能超过最大值MAX_TRANSFER_SIZE
        List<Order> os = new ArrayList<Order>();
        Iterator<Order> it = orders.iterator();
        short i = 0;
        while(it.hasNext()){
            Order o = it.next();
            BOrder oB = o.orderToBOrder((short)bulkId, ++i);
            byte[] orderB = BOrder.bOrderToBytes(oB);
            o.setData(orderB);

        }


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
