package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

/**
 * 订单
 */
public final class BOrder {

    public short start = BConstants.orderStart;

    public short length;

    public int   id;

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

        position = fillShort(bOrder.start, bytes, position);

        position = fillShort(bOrder.length, bytes, position);

        position = fillInt(bOrder.id, bytes, position);

        position = fillInt(bOrder.seconds, bytes, position);

        position = fillInt(bOrder.orderNumber, bytes, position);


        position = fillShort(bOrder.bulkId, bytes, position);

        position = fillShort(bOrder.inNumber, bytes, position);

        position = fillShort(bOrder.checkNum, bytes, position);

        position = fillShort(bOrder.padding0, bytes, position);

        position = fillByte(bOrder.data, bytes, position);

        position = fillShort(bOrder.padding1, bytes, position);

        position = fillShort(bOrder.end, bytes, position);

        return bytes;
    }


    private static int fillShort(short number, byte[] bytes, int start) {

        byte[] b = BytesConvert.shortToBytes(number);

        System.arraycopy(b, 0, bytes, start, b.length);

        return start + b.length;
    }

    private static int fillInt (int number, byte[] bytes, int start) {

        byte[] b = BytesConvert.intToBytes(number);

        System.arraycopy(b, 0, bytes, start, b.length);

        return start + b.length;
    }

    private static int fillByte (byte[]srcByte, byte[] bytes, int start){
        System.arraycopy(srcByte, 0, bytes, start, srcByte.length);

        return start + srcByte.length;
    }
}

