package com.qg.smpt.web.model;

import com.qg.smpt.printer.model.BBulkOrder;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.util.BytesConvert;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 批次订单
 * Created by tisong on 7/20/16.
 */
public final class BulkOrder {

    private long sendtime;          // 订单批次的发送时间

    public long getSendtime() {
        return sendtime;
    }

    public void setSendtime(long sendtime) {
        this.sendtime = sendtime;
    }

    private final static Logger LOGGER = Logger.getLogger(BulkOrder.class);

    private int id;

    private int userId;

    private List<BOrder> bOrders = null;

    private List<Order> orders = null;

    private int dataSize = 0;       //批次订单数据的总大小

    private short bulkType = 0;    //0-普通 1-加急

    private int receNum = 0;       // 从打印机中接收到的订单数

    public BulkOrder(List<BOrder> bOrders) {
        this.dataSize = 0;
        this.bOrders = bOrders;
        this.orders = new ArrayList<Order>();
        this.bulkType = 0;
        //this.userId = userId;
    }

    public static BBulkOrder convertBBulkOrder(BulkOrder bulkOrder, boolean isExcep) {
        BBulkOrder bBulk = new BBulkOrder();

        // 设置订单个数
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
        byte[] data = installOrders(bulkOrder, isExcep);
        bBulk.setData(data);

        return bBulk;
    }

    private static byte[] installOrders(BulkOrder bulkOrder, boolean isExcep) {
        byte[] data = new byte[bulkOrder.getDataSize()];

        int pos = 0;
        for(BOrder o : bulkOrder.getbOrders()) {
            // 若是异常单，则将之前的保留为设置为 0x0002;
            if (isExcep)
                o.padding0 = (short) 0x2;
            byte[] orderB = BOrder.bOrderToBytes(o);
            pos = BytesConvert.fillByte(orderB, data, pos);
        }

        return data;
    }


    public static void convertBulkOrder(BBulkOrder bBulkOrder) {
        BulkOrder bulkOrder = new BulkOrder(new ArrayList<>());

        bulkOrder.id = bBulkOrder.bulkId;
        bulkOrder.dataSize = bBulkOrder.size - 20;
        bulkOrder.bulkType = bBulkOrder.padding0;

        byte[] bytes = bBulkOrder.data;

        int start = 2;

        while (start < bBulkOrder.size - 20) {
            short orderLength = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, start, start+4));  // 订单总长度

            BOrder bOrder = BOrder.bytesToOrder(Arrays.copyOfRange(bytes, 24, orderLength));

            try {
                LOGGER.log(Level.DEBUG, "打印机接收订单数据 : [{0}]", new String( bOrder.data, "gb2312" ) );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            start += orderLength;
        }
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
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setReceNum(int receNum) {
        this.receNum = receNum;
    }

    public int getReceNum() {
        return receNum;
    }

    public synchronized void increaseReceNum() {
        receNum++;
    }
}
