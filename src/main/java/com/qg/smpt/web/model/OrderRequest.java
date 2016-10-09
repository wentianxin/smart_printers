package com.qg.smpt.web.model;

/**
 * Created by asus2015 on 2016/10/2.
 */
public class OrderRequest {
    private int number;
    private int size;
    private char orderType;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public char getOrderType() {
        return orderType;
    }

    public void setOrderType(char orderType) {
        this.orderType = orderType;
    }
}
