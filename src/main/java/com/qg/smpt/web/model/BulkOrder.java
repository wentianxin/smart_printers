package com.qg.smpt.web.model;

import com.qg.smpt.printer.model.BBulkOrder;
import com.qg.smpt.util.BytesConvert;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.List;

/**
 * 批次订单
 * Created by tisong on 7/20/16.
 */
public final class BulkOrder {
    private int id;

    private int UserId;

    private List<Order> orders; //批次的订单集合

    private int dataSize;       //批次订单数据的总大小

    private byte[] bulkOrderB;  //批次转化后的字节数组

    private boolean isConvert = false;  //是否已经经过转化

    private short bulkType;    //0-普通 1-加急

    public BulkOrder(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getDataSize() {
        return dataSize;
    }

    public  void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public byte[] getBulkOrderB() {
        if(!isConvert) {
            convert();
            isConvert = true;
        }
        return bulkOrderB;
    }

    public void setBulkOrderB(byte[] bulkOrderB) {
        this.bulkOrderB = bulkOrderB;
    }

    public short getBulkType() {
        return bulkType;
    }

    public void setBulkType(short bulkType) {
        this.bulkType = bulkType;
    }

    private void convert() {
        BBulkOrder bBulk = convertBBulkOrder();
        bulkOrderB =  BBulkOrder.bBulkOrderToBytes(bBulk);
    }

    private BBulkOrder convertBBulkOrder() {
        BBulkOrder bBulk = new BBulkOrder();

        //设置订单个数
        bBulk.setOrderNumber((short)orders.size());

        //设置批次编号
        bBulk.setBulkId((short)id);

        //设置批次报文长度
        bBulk.setSize((short)(dataSize + 20));
        

        //设置时间戳
        bBulk.setSeconds((int)System.currentTimeMillis());

        //设置校验和
        bBulk.setCheckSum((short)0);

        //设置填充和保留
        bBulk.setPadding0(bulkType);

        //设置填充
        bBulk.setPadding1((short)0);

        //设置订单数据
        byte[] data = installOrders();
        bBulk.setData(data);

        return bBulk;
    }

    private byte[] installOrders() {
        byte[] data = new byte[dataSize];

        int pos = 0;
        for(Order o : orders) {
            byte[] orderB = o.getData();
            pos = BytesConvert.fillByte(orderB, data, pos);
        }

        return data;
    }
}
