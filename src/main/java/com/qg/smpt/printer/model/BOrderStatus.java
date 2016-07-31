package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

import java.util.Arrays;

/**
 * 订单状态(打印机-服务器)
 */
public final class BOrderStatus extends AbstactStatus{

    // line1
    public int printerId;   // 主控板id
    // line2
    public int seconds;
    // line3
    public short bulkId;    // 批次id ; 低16bit
    public short inNumber;  // 批次内序号; 高16bit

    public static BOrderStatus bytesToOrderStatus(byte[] bytes) {

        AbstactStatus status = AbstactStatus.bytesToAbstractStatus(bytes);

        BOrderStatus bos = new BOrderStatus();

        bos.flag = status.flag;

        bos.printerId = status.line1;

        bos.seconds = status.line2;

        bos.bulkId = (short)( (status.line3 >> 16 ) & 0xFFFF) ;

        bos.inNumber = (short)(status.line3 & 0xFFFF);

        return bos;
    }
}
