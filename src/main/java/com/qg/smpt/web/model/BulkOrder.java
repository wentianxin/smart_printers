package com.qg.smpt.web.model;

import com.qg.smpt.printer.model.BBulkOrder;
import com.qg.smpt.printer.model.BOrder;
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

    private List<BOrder> bOrders = null;

    private int dataSize;       //批次订单数据的总大小

    private short bulkType;    //0-普通 1-加急

    public BulkOrder(List<BOrder> bOrders) {
        this.dataSize = 0;
        this.bOrders = bOrders;
    }

    public static BBulkOrder convertBBulkOrder(BulkOrder bulkOrder) {
        BBulkOrder bBulk = new BBulkOrder();

        bBulk.setOrderNumber((short)bulkOrder.getbOrders().size());

        //设置批次编号
        bBulk.setBulkId((short)bulkOrder.getId());

        //设置批次报文长度
        bBulk.setSize((short)(bulkOrder.getDataSize() + 20));

        //设置时间戳
        bBulk.setSeconds((int)System.currentTimeMillis());

        //设置校验和
        bBulk.setCheckSum((short)0);

        //设置填充和保留
        bBulk.setPadding0(bulkOrder.getBulkType());

        //设置填充
        bBulk.setPadding1((short)0);

        //设置订单数据
        byte[] data = installOrders(bulkOrder);
        bBulk.setData(data);

        return bBulk;
    }

    private static byte[] installOrders(BulkOrder bulkOrder) {
        byte[] data = new byte[bulkOrder.getDataSize()];

        int pos = 0;
        for(BOrder o : bulkOrder.getbOrders()) {
            byte[] orderB = BOrder.bOrderToBytes(o);
            pos = BytesConvert.fillByte(orderB, data, pos);
        }

        return data;
    }




    public BulkOrder(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setbOrders(List<BOrder> bOrders) {
        this.bOrders = bOrders;
    }

    public List<BOrder> getbOrders() {
        return bOrders;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }


    public int getDataSize() {
        return dataSize;
    }

    public  void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }


    public short getBulkType() {
        return bulkType;
    }

    public void setBulkType(short bulkType) {
        this.bulkType = bulkType;
    }

}
