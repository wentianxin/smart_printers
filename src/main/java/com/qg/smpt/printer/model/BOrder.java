package com.qg.smpt.printer.model;

/**
 * 订单
 */
public final class BOrder {

    public short start = BConstants.start;

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

    public short end;

    public static byte[] bOrderToBytes(BOrder bOrder){

    }
}
