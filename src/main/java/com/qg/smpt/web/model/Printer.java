package com.qg.smpt.web.model;

import com.qg.smpt.share.ShareMem;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

@JsonIgnoreProperties({"userId", "userName", "currentBulk", "currentOrder",
		"canAccept","busy", "lastSendTime"})
public final class Printer {
    private long okTime;                    // 接收到 ok 请求的时间

    private Integer id;
    private String printerStatus;
    private int userId;                      //用户id
    private String userName;                 // 商家名
    private AtomicInteger currentBulk;        //当前已发送批次的最大id
    private volatile int currentOrder;       //当前已接受订单的最大id
    private volatile boolean canAccept;      //能否接收数据
    private volatile boolean isBusy;         //true-忙时，false-闲时
    private volatile long lastSendTime;      //上一次发送批次的时间
    private volatile boolean connected = false;       // 是否建立连接

    private volatile int oredrsNum;          // 总数量
    private volatile int sendedOrdersNum;     // 已发送订单数量
    private volatile int unsendedOrdersNum;  // 未发送订单数量

    private volatile int printSuccessNum;    // 打印成功数量
    private volatile int printErrorNum;      // 打印失败数量
    private volatile int successRate;        // 成功率

    private User user;

    private List<BulkOrder> sendedBulkOrder; // 已发送订单

    private List<BulkOrder> exceptionBulkOrder; // 异常订单

    public void setExceptionBulkOrder(List<BulkOrder> exceptionBulkOrder) {
        this.exceptionBulkOrder = exceptionBulkOrder;
    }

    public List<BulkOrder> getExceptionBulkOrder() {
        return exceptionBulkOrder;
    }

    public void setSendedBulkOrder(List<BulkOrder> sendedBulkOrder) {
        this.sendedBulkOrder = sendedBulkOrder;
    }

    public List<BulkOrder> getSendedBulkOrder() {
        return sendedBulkOrder;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    private SocketChannel socketChannel; // 打印机关联的socketChannel

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public int getOredrsNum() {
        return oredrsNum;
    }

    public void setOredrsNum(int oredrsNum) {
        this.oredrsNum = oredrsNum;
    }

    public int getSendedOrdersNum() {
        return sendedOrdersNum;
    }

    public void setSendedOrdersNum(int sendedOrdersNum) {
        this.sendedOrdersNum = sendedOrdersNum;
    }

    public int getUnsendedOrdersNum() {
        return unsendedOrdersNum;
    }

    public void setUnsendedOrdersNum(int unsendedOrdersNum) {
        this.unsendedOrdersNum = unsendedOrdersNum;
    }

    public int getPrintSuccessNum() {
        return printSuccessNum;
    }

    public void setPrintSuccessNum(int printSuccessNum) {
        this.printSuccessNum = printSuccessNum;
    }

    public int getPrintErrorNum() {
        return printErrorNum;
    }

    public void setPrintErrorNum(int printErrorNum) {
        this.printErrorNum = printErrorNum;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public Printer() {this.currentBulk = new AtomicInteger(0);}
    public Printer(int id){this(); this.id = id;}

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(!(obj instanceof com.qg.smpt.web.model.Printer))
            return false;

        if(((Printer)obj).id == this.id)
            return true;
        else
            return false;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public AtomicInteger getCurrentBulk() {
        return currentBulk;
    }

    public void setCurrentBulk(AtomicInteger currentBulk) {
        this.currentBulk = currentBulk;
    }

    public int getCurrentOrder() {
        return currentOrder;
    }

    public void increaseBulkId() {
        currentBulk.incrementAndGet();
    }

    public void setCurrentOrder(int currentOrder) {
        this.currentOrder = currentOrder;
    }

    public boolean isCanAccept() {
        return canAccept;
    }

    public void setCanAccept(boolean canAccept) {
        this.canAccept = canAccept;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public long getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrinterStatus() {
        return printerStatus;
    }

    public void setPrinterStatus(String printerStatus) {
        this.printerStatus = printerStatus == null ? null : printerStatus.trim();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public synchronized void reset() {
        // 清空各队列的打印作业
        sendedBulkOrder.clear();
        exceptionBulkOrder.clear();
        // 重置打印机状态
        oredrsNum = 0;
        sendedOrdersNum = 0;
        unsendedOrdersNum = 0;
        printSuccessNum = 0;
        printErrorNum = 0;
    }
}