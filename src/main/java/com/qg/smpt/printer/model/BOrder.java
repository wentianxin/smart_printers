package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

import java.util.Arrays;

/**
 * 订单
 */
public final class BOrder {

    public short start = BConstants.orderStart;

    public short length;   // 订单长度

    public int   id;      // 主控板id

    public int   seconds;

    public int   orderNumber;

    public short bulkId;

    public short inNumber;

    public short checkNum;

    public short padding0;

    public byte[] data;

    public short padding1;

    public short end = BConstants.orderEnd;

    public int size;        // BOrder 字节数

    public static byte[] bOrderToBytes(BOrder bOrder){

        byte[] bytes = new byte[bOrder.size];

        int position = 0;

        position = BytesConvert.fillShort(bOrder.start, bytes, position);

        position = BytesConvert.fillShort(bOrder.length, bytes, position);

        position = BytesConvert.fillInt(bOrder.id, bytes, position);

        position = BytesConvert.fillInt(bOrder.seconds, bytes, position);

        position = BytesConvert.fillInt(bOrder.orderNumber, bytes, position);

        position = BytesConvert.fillShort(bOrder.bulkId, bytes, position);

        position = BytesConvert.fillShort(bOrder.inNumber, bytes, position);

        position = BytesConvert.fillShort(bOrder.checkNum, bytes, position);

        position = BytesConvert.fillShort(bOrder.padding0, bytes, position);

        position = BytesConvert.fillByte(bOrder.data, bytes, position);

        position = BytesConvert.fillShort(bOrder.padding1, bytes, position);

        position = BytesConvert.fillShort(bOrder.end, bytes, position);

        return bytes;
    }


    public static BOrder bytesToOrder(byte[] bytes) {
        BOrder bOrder = new BOrder();

        bOrder.size = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 2, 4));
        bOrder.id = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 4, 8));
        bOrder.seconds = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 8, 12));
        bOrder.orderNumber = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 12, 16));
        bOrder.bulkId = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 16, 18));
        bOrder.checkNum = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 18, 20));
        bOrder.inNumber = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 20, 22));
        bOrder.padding0 = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 22, 24));

        bOrder.data = Arrays.copyOfRange(bytes, 24, bytes.length - 4);

        bOrder.padding1 = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, bytes.length - 4, bytes.length - 2));

        return  bOrder;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public short getBulkId() {
        return bulkId;
    }

    public void setBulkId(short bulkId) {
        this.bulkId = bulkId;
    }

    public short getInNumber() {
        return inNumber;
    }

    public void setInNumber(short inNumber) {
        this.inNumber = inNumber;
    }

    public short getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(short checkNum) {
        this.checkNum = checkNum;
    }

    public short getPadding0() {
        return padding0;
    }

    public void setPadding0(short padding0) {
        this.padding0 = padding0;
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
}

