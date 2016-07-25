package com.qg.smpt.web.model;

import com.mysql.jdbc.log.Log;
import com.qg.smpt.printer.PrinterConnector;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.util.BytesConvert;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * 订单
 * Created by tisong on 7/20/16.
 */
public final class Order {
	private static final Logger LOGGER = Logger.getLogger(Order.class.getName());
	
    private int mpu;             //主控板id
    private int orderId;        //打印订单序号
    private String status;      //订单状态

    private String company;     //公司名称

    private String shopName;   //商家名称
    private String from;      //商家地址
    private String shopContact;    //商家联系方式

    private String orderNum;   //订单编号
    private String orderTime;  //订单时间
    private String expectTime; //预计送达时间
    private String remark;    //备注

    private List<Item> items;  //订单物品

    private int mealCost;     //餐盒费
    private int deliveryCost;  //配送费
    private int reducePrice;   //优惠额
    private int cost = 0;        //总额
    private boolean hasCompute = false;    //是否已经计算总价
    private boolean hasPay;       //付款状态

    private String customer;   //顾客
    private String to;       //顾客地址
    private String comContact; //顾客联系方式

    private byte[] data;
    private boolean isConvert = false;


    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getShopContact() {
        return shopContact;
    }
    public void setShopContact(String shopContact) {
        this.shopContact = shopContact;
    }
    public String getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
    public String getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public int getMealCost() {
        return mealCost;
    }
    public void setMealCost(int mealCost) {
        this.mealCost = mealCost;
    }
    public int getDeliveryCost() {
        return deliveryCost;
    }
    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
    public void setReducePrice(int cost) {
        if(cost < 10)
            reducePrice = 0;
        else if(cost < 18)
            reducePrice = 5;
        else if(cost < 27)
            reducePrice = 9;
        else
            reducePrice = 12;
    }
    public int getReducePrice() {
        return reducePrice;
    }
    public boolean isHasPay() {
        return hasPay;
    }
    public void setHasPay(boolean hasPay) {
        this.hasPay = hasPay;
    }
    public String getCustomer() {
        return customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getComContact() {
        return comContact;
    }
    public void setComContact(String comContact) {
        this.comContact = comContact;
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
    public int getCost() {
        if(!hasCompute){
            cost = getTotalCost();
            hasCompute = true;
        }

        return cost;
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

        c += deliveryCost;
        c += mealCost;
        setReducePrice(c);
        c -= getReducePrice();

        return c;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(company + "\n");
        buffer.append(shopName + "\n");
        buffer.append("订单编号: " + getOrderNum() + "\n");
        buffer.append("下单时间: " + getOrderTime() + "\n");
        buffer.append("预计下单时间: " + getExpectTime() + "\n");
        buffer.append("备注: " + getRemark() + "\n");
        buffer.append("菜单名                                 数量               小计\n");
        for(Item item : items){
            buffer.append(item.toString() + "\n");
        }
        buffer.append("餐盒费: " + getMealCost() + "\n");
        buffer.append("配送费: " + getDeliveryCost() + "\n");
        buffer.append("优惠额: " + getReducePrice() + "\n");
        buffer.append("合 计: " + getTotalCost() + "\n");
        buffer.append("已付款" + "\n");
        buffer.append("顾客姓名: " + getCustomer() + "\n");
        buffer.append("送餐地址: " + getTo() + "\n");
        buffer.append("电话: " + getComContact() + "\n");
        buffer.append("商家地址: " + getFrom() + "\n");
        buffer.append("联系方式: " + getShopContact() + "\n");
        return buffer.toString();

    }

    //Order对象转换为BOrder
    public BOrder orderToBOrder(short bulkId, short index) {
        BOrder bo = new BOrder();

        //设置主控板id
        bo.setId(mpu);

        //设置时间戳
        bo.setSeconds((int)(System.currentTimeMillis()));

        //设置订单序号
        bo.setOrderNumber(orderId);

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
    private byte[] convertOrder(){
        //通过GB2312编码获取订单内容的字节数组
    	
    		byte[] orderB;
			try {
				orderB = this.toString().getBytes("gb2312");
				
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
		        
			} catch (UnsupportedEncodingException e) {
				LOGGER.log(Level.ERROR, "in class Order,convert order error", e);
				e.printStackTrace();
				return new byte[4];
			}
    	

        
    }


}