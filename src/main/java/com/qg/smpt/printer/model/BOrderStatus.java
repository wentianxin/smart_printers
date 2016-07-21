package com.qg.smpt.printer.model;

/**
 * 订单状态
 */
public final class BOrderStatus {

    public short start;

    public short flag; // 3bit:type; 12bit:padding; 1bit: STA(0-打印成功; 1-打印失败)

    public short id;

    public short bulkId; // 批次id

    public short inNumber;

    public short checkSum;

    public short end;


}
