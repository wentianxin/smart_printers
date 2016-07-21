package com.qg.smpt.share;

import com.qg.smpt.web.model.*;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by tisong on 7/20/16.
 */
public final class ShareMem {

    public static Map<User, List<Printer>>   userListMap = null;         // 用户-打印机-打印机

    public static Map<Printer, Queue<Order>> priBufferQueueMap = null;   // 打印机-缓存队列

    public static Map<Printer, Queue<Order>> priSentQueueMap = null;     // 打印机-已发队列

    public static Map<Printer, Queue<Order>> priExceQueueMap = null;     // 打印机-异常队列

    public static Map<Printer, List<SocketChannel>> priLinkSocketMap = null; // 打印机-Socket连接
}
