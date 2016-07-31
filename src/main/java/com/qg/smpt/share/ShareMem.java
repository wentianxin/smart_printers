package com.qg.smpt.share;

import com.qg.smpt.printer.PrinterProcessor;
import com.qg.smpt.web.model.*;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 全局生命周期的共享对象
 */
public final class ShareMem {
	
	public static Integer currentOrderNum = 0;

    public static Map<Integer, User> userIdMap = null;                      // 用户id-用户

    // TODO 编码严重漏洞，共享变量未实现共享
    public static Map<Integer, Printer> printerIdMap = null;                // 打印机id-打印机

    public static Map<Integer, List<Printer>> userListMap = null;           // 用户-打印机

    public static Map<Printer, List<BulkOrder>> priSentQueueMap = null;     // 打印机-已发批次队列

    public static Map<Printer, List<BulkOrder>> priExceQueueMap = null;     // 打印机-异常队列

    public static Map<Printer, List<BulkOrder>> priBufferMapList = null;    // 打印机-待发送批次队列

    public static Map<Printer, PrinterProcessor> priPriProcessMap = null;   // 打印机对应的处理线程, 做成动态效果。
                                                                            // 当触发一个读事件时，进行一次线程绑定，触发完毕，解除绑定
    public static Map<Printer, SocketChannel> priSocketMap = null;          // 打印机-socket

    static {
        userIdMap = new HashMap<Integer, User>();

        printerIdMap = new HashMap<Integer, Printer>();

        userListMap = new HashMap<Integer, List<Printer>>();

        priSentQueueMap = new HashMap<Printer, List<BulkOrder>>();

        priExceQueueMap = new HashMap<Printer, List<BulkOrder>>();

        priBufferMapList = new HashMap<Printer, List<BulkOrder>>();

        priPriProcessMap = new HashMap<Printer, PrinterProcessor>();

        priSocketMap = new HashMap<Printer, SocketChannel>();
    }
}
