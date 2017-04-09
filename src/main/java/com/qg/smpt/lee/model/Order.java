package com.qg.smpt.lee.model;

import java.util.Date;

/**
 * Created by asus2015 on 2017/4/8.
 */
public class Order {
    private Integer id;             // 订单编号

    private Date orderTime;         // 下单时间

    private String orderRemark;     // 订单备注

    private Integer orderMealFee;   // 餐盒费

    private String orderPayStatus;  // 订单支付状态

    private Integer orderDisFee;    // 配送费

    private Integer orderPreAmount; // 优惠额

    private Integer orderSum;       // 订单总金额

    private String orderStatus;     // 订单状态

    private String userName;        // 订单用户名字

    private String userAddress;     // 订单用户地址

    private String userTelephone;   // 订单用户电话

    private String orderContent;    // 订单内容

    private String company;         // 卖家所属公司

    private String expectTime;      // 期望送达时间

    private String clientName;      // 卖家店铺名字

    private String clientAddress;   // 卖家地址

    private String clientTelephone; // 卖家电话

    private boolean hasCompute = false;	//是否已计算总价

    private char orderType;         // 0-非加急; 1-加急

    private int userId;             // 卖家用户编号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public Integer getOrderMealFee() {
        return orderMealFee;
    }

    public void setOrderMealFee(Integer orderMealFee) {
        this.orderMealFee = orderMealFee;
    }

    public String getOrderPayStatus() {
        return orderPayStatus;
    }

    public void setOrderPayStatus(String orderPayStatus) {
        this.orderPayStatus = orderPayStatus;
    }

    public Integer getOrderDisFee() {
        return orderDisFee;
    }

    public void setOrderDisFee(Integer orderDisFee) {
        this.orderDisFee = orderDisFee;
    }

    public Integer getOrderPreAmount() {
        return orderPreAmount;
    }

    public void setOrderPreAmount(Integer orderPreAmount) {
        this.orderPreAmount = orderPreAmount;
    }

    public Integer getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(Integer orderSum) {
        this.orderSum = orderSum;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientTelephone() {
        return clientTelephone;
    }

    public void setClientTelephone(String clientTelephone) {
        this.clientTelephone = clientTelephone;
    }

    public boolean isHasCompute() {
        return hasCompute;
    }

    public void setHasCompute(boolean hasCompute) {
        this.hasCompute = hasCompute;
    }

    public char getOrderType() {
        return orderType;
    }

    public void setOrderType(char orderType) {
        this.orderType = orderType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
