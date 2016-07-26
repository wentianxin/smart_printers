package com.qg.smpt.web.model;

import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.util.BytesConvert;
import com.qg.smpt.web.repository.OrderMapper;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 订单
 */
@JsonIgnoreProperties({"mpu","items","data", "isConvert", "orderTime", "orderRemark", "orderMealFee",
	"orderPreAmount", "orderPayStatus", "orderDisFee","orderPreAmount","orderSum","userName", 
	"userAddress", "userTelephone", "orderContent", "company", "expectTime"})
public final class Order {
    private int mpu;             //主控板id

    private List<Item> items;  //订单物品

    private byte[] data;
    private boolean isConvert = false;

    private Integer id;


    private Date orderTime;

    private String orderRemark;


    private String orderMealFee;

    private String orderPayStatus;

    private Integer orderDisFee;

    private Integer orderPreAmount;

    private Integer orderSum;

    private String orderStatus;

    private String userName;

    private String userAddress;

    private String userTelephone;

    private String orderContent;

    private String company;

    private String expectTime;

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public void setMpu(int mpu) {
        this.mpu = mpu;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setConvert(boolean convert) {
        isConvert = convert;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getCompany() {
        return company;
    }

    public int getMpu() {
        return mpu;
    }

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
        this.orderRemark = orderRemark == null ? null : orderRemark.trim();
    }

    public String getOrderMealFee() {
        return orderMealFee;
    }

    public void setOrderMealFee(String orderMealFee) {
        this.orderMealFee = orderMealFee == null ? null : orderMealFee.trim();
    }

    public String getOrderPayStatus() {
        return orderPayStatus;
    }

    public void setOrderPayStatus(String orderPayStatus) {
        this.orderPayStatus = orderPayStatus == null ? null : orderPayStatus.trim();
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
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone == null ? null : userTelephone.trim();
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent == null ? null : orderContent.trim();
    }



    public byte[] getData() {
        if(!isConvert){

        }
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    private int getTotalCost() {
        int c = 0;
        for(Item item : items) {
            c += item.getCost();
        }

        c += orderDisFee;
        c += Integer.getInteger(orderMealFee);
        setOrderPreAmount(c);
        c -= getOrderPreAmount();

        return c;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(company + "\n");
        buffer.append(userName + "\n");
        buffer.append("订单编号: " + getId() + "\n");
        buffer.append("下单时间: " + getOrderTime() + "\n");
        buffer.append("预计下单时间: " + getExpectTime() + "\n");
        buffer.append("备注: " + getOrderContent() + "\n");
        buffer.append("菜单名                                 数量               小计\n");
        for(Item item : items){
            buffer.append(item.toString() + "\n");
        }
        buffer.append("餐盒费: " + getOrderMealFee() + "\n");
        buffer.append("配送费: " + getOrderDisFee() + "\n");
        buffer.append("优惠额: " + getOrderPreAmount() + "\n");
        buffer.append("合 计: " + getOrderSum() + "\n");
        buffer.append("已付款" + "\n");
        buffer.append("顾客姓名: " + getUserName() + "\n");
        buffer.append("送餐地址: " + getUserAddress() + "\n");
        buffer.append("电话: " + getUserTelephone() + "\n");
//        buffer.append("商家地址: " + getFrom() + "\n");
//        buffer.append("联系方式: " + getShopContact() + "\n");
        return buffer.toString();

    }

    //Order对象转换为BOrder
    public BOrder orderToBOrder(short bulkId, short index) throws Exception{
        BOrder bo = new BOrder();

        //设置主控板id
        bo.setId(mpu);

        //设置时间戳
        bo.setSeconds((int)(System.currentTimeMillis()));

        //设置订单序号
        bo.setOrderNumber(id);

        //设置批次,批次内序号
        bo.setBulkId(bulkId);
        bo.setInNumber(index);

        //设置校验和和填充
        bo.setCheckNum((short)0);
        bo.setPadding0((short)0);

        //设置数据域,数据长度
        byte[] data = convertOrder();
        short length = (short)data.length;
        bo.setData(data);
        bo.setLength(length);

        //设置填充
        bo.setPadding1((short)0);
        bo.size = 28 + length;
        return bo;
    }

    //将订单内容转化为字节数组
    private byte[] convertOrder() throws Exception{
        //通过GB2312编码获取订单内容的字节数组
        byte[] orderB = this.toString().getBytes("gb2312");

        //获取订单内容的长度
        int length = orderB.length;

        //因为要字节对齐,以4字节为为单位,所以计算要填充多少位字节
        int fillLength = 4 - (length % 4);

        //创建字节数组,大小为订单数据长度  8主要是文本内容前后要各加4字节的头尾部信息
        byte[] data = new byte[length + 8 + fillLength];

        int pos = 0;

        //填充文本开始字符
        pos = BytesConvert.fillShort(BConstants.textStart,data,pos);

        //填充文本长度
        pos = BytesConvert.fillShort((short)(length + fillLength),data,pos);

        //填充文本数据
        pos = BytesConvert.fillByte(orderB, data, pos);

        //填充填充位
        pos  += (fillLength + 2);

        //填充文本结束字符
        pos = BytesConvert.fillShort(BConstants.textEnd, data, pos);

        return data;
    }


}