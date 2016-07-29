package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

import java.util.Arrays;

/**
 * 批量订单
 */
public class BBulkOrder {

    public short start = BConstants.bulkOrderStart;

    public short orderNumber;

    public short bulkId;

    public short padding0;

    public int   seconds;

    public short checkSum;

    public short urg;

    public byte[] data;

    public short padding1;

    public short end = BConstants.bulkOrderEnd;

    public int size; // 全部大小

    public short getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(short orderNumber) {
        this.orderNumber = orderNumber;
    }

    public short getBulkId() {
        return bulkId;
    }

    public void setBulkId(short bulkId) {
        this.bulkId = bulkId;
    }

    public short getPadding0() {
        return padding0;
    }

    public void setPadding0(short padding0) {
        this.padding0 = padding0;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public short getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(short checkSum) {
        this.checkSum = checkSum;
    }

    public short getUrg() {
        return urg;
    }

    public void setUrg(short urg) {
        this.urg = urg;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public short getPadding1() {
        return padding1;
    }

    public void setPadding1(short padding1) {
        this.padding1 = padding1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    

    public static byte[] bBulkOrderToBytes(BBulkOrder bBulkOrder) {
        byte[] bytes = new byte[bBulkOrder.size];

        int position = 0;

        position = BytesConvert.fillShort(bBulkOrder.start, bytes, position);

        // 订单个数
        position = BytesConvert.fillShort(bBulkOrder.orderNumber, bytes, position);
        // 批次编号
        position = BytesConvert.fillShort(bBulkOrder.bulkId, bytes, position);

        position = BytesConvert.fillShort((short)bBulkOrder.size, bytes, position);

        position = BytesConvert.fillInt(bBulkOrder.seconds, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.checkSum, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.urg, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.padding1, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.end, bytes, position);

        BytesConvert.fillByte(bBulkOrder.data, bytes, position);



        return bytes;
    }

    /**
     * 客户端打印机测试使用
     * @param bytes
     * @return
     */
    public static BBulkOrder bytesTobBulkOrder(byte[] bytes) {

        BBulkOrder bBulkOrder = new BBulkOrder();

        bBulkOrder.orderNumber = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 2, 4));

        bBulkOrder.bulkId = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 4, 6));

        bBulkOrder.seconds = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 8, 12));

        bBulkOrder.checkSum = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 12, 14));

        bBulkOrder.padding0 = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 14, 16));

        bBulkOrder.data = Arrays.copyOfRange(bytes, 17, bytes.length -4);

        bBulkOrder.size = bytes.length;

        return bBulkOrder;
    }
}
