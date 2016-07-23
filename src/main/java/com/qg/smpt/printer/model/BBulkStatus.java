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

        BBulkStatus bbs = (BBulkStatus)(BBulkStatus.bytesToAbstractStatus(bytes));

        bbs.bulkId = (short)(bbs.line1 & 0xFFFF);

        bbs.padding = (short)((bbs.line1 >> 16) & 0xFFFF);

        bbs.seconds = bbs.line2;

        bbs.printerId = bbs.line3;

        return bbs;
    }
}
