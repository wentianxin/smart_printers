package com.qg.smpt.share;

import com.qg.smpt.web.model.*;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 全局生命周期的共享对象
 */
public final class ShareMem {

    public static Map<Integer, User> userIdMap = null;                       // 用户id-用户

    public static Map<Integer, Printer> printerIdMap = null;                 //打印机id-打印机

    public static Map<Integer, List<Printer>> userListMap = null;            // 用户-打印机-打印机


    public static volatile Map<Printer, Queue<Order>> priBufferQueueMap = null;   // 打印机-缓存队列

    public static Map<Printer, Queue<BulkOrder>> priSentQueueMap = null;     // 打印机-已发批次队列

    public static Map<Printer, Queue<Order>> priExceQueueMap = null;         // 打印机-异常队列

    public static Map<Printer, SocketChannel> priLinkSocketMap = null; // 打印机-Socket连接


}
