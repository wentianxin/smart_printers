package com.qg.smpt.web.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"userId", "userName", "currentBulk", "currentOrder",
		"canAccept","busy", "lastSendTime"})
public final class Printer {

    private Integer id;
    private String printerStatus;
    private int userId;                      //用户id
    private String userName;                 // 商家名
    private volatile int currentBulk;        //当前已发送批次的最大id
    private volatile int currentOrder;       //当前已接受订单的最大id
    private volatile boolean canAccept;      //能否接收数据
    private volatile boolean isBusy;         //true-忙时，false-闲时
    private volatile long lastSendTime;      //上一次发送批次的时间
    private boolean connected = false;               // 是否建立连接

    public Printer() {this.currentBulk = 0;}
    public Printer(int id){this.id = id;this.currentBulk = 0;}

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

    public int getCurrentBulk() {
        return currentBulk;
    }

    public void setCurrentBulk(int currentBulk) {
        this.currentBulk = currentBulk;
    }

    public int getCurrentOrder() {
        return currentOrder;
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
}