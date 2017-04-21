package com.qg.smpt.model;

public class Printer {
    private Integer id;             // 打印机编号

    private String printerStatus;   // 打印机状态

    private int userId;             // 用户编号

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
        this.printerStatus = printerStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}