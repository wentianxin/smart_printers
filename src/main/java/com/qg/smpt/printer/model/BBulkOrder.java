package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

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

    public int size;

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

        position = BytesConvert.fillShort(bBulkOrder.orderNumber, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.bulkId, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.padding0, bytes, position);

        position = BytesConvert.fillInt(bBulkOrder.seconds, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.checkSum, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.urg, bytes, position);

        position = BytesConvert.fillByte(bBulkOrder.data, bytes, position);

        position = BytesConvert.fillShort(bBulkOrder.padding1, bytes, position);

        BytesConvert.fillShort(bBulkOrder.end, bytes, position);

        return bytes;
    }
}
