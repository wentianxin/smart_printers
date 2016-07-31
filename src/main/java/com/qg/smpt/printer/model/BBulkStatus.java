package com.qg.smpt.printer.model;

/**
 * 批量订单状态（打印机-订单）
 */
public final class BBulkStatus extends AbstactStatus{

    // line1
    public short bulkId;  // 低16bit
    public short padding; // 高16bit
    // line2
    public int   seconds;
    // line3
    public int   printerId;
    
    public static BBulkStatus bytesToBulkStatus(byte[] bytes) {
        if (bytes.length != 20) {
            return null;
        }

        AbstactStatus status = AbstactStatus.bytesToAbstractStatus(bytes);

        BBulkStatus bbs = new BBulkStatus();

        bbs.flag = status.flag;

        bbs.bulkId = (short)(status.line3 & 0xFFFF);

        bbs.padding = (short)((status.line3 >> 16) & 0xFFFF);

        bbs.seconds = status.line2;

        bbs.printerId = status.line1;

        return bbs;
    }
}
