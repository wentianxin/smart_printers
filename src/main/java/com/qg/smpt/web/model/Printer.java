package com.qg.smpt.web.model;

/**
 * 打印机
 * Created by tisong on 7/20/16.
 */
public final class Printer {
    private int id;                 //打印机id
    private int userId;             //用户id
    private String status;          //打印机状态
    private int currentBulk;        //当前已发送批次的最大id
    private int currentOrder;       //当前已接受订单的最大id
    private boolean canAccpet;      //能否接收数据
    private boolean isBusy;         //true-忙时，false-闲时
    private long lastSendTime;      //上一次发送批次的时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrentBulk() {
        return ++currentBulk;
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

    public boolean isCanAccpet() {
        return canAccpet;
    }

    public void setCanAccpet(boolean canAccpet) {
        this.canAccpet = canAccpet;
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

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(!(obj instanceof  Printer))
            return false;

        Printer printer = (Printer)obj;
        if(printer.id == this.id)
            return true;
        else
            return false;
    }
}
