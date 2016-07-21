package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

import java.util.Arrays;

/**
 * 批量订单状态
 */
public final class BBulkStatus {

    public final short start = BConstants.start;

    public short flag; // 3bit:type; 12bit:padding; 1bit:0, 1

    public short id;

    public short padding;

    public int   seconds;

    public int   retention;

    public short checkSum;

    public final short end = BConstants.end;

    public static BBulkStatus bytesToBulkStatus(byte[] bytes) {
        if (bytes.length != 20) {
            return null;
        }

        BBulkStatus bbs = new BBulkStatus();

        bbs.flag = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 2, 3));

        bbs.id = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 3, 4));

        bbs.padding = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 4, 5));

        bbs.seconds = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 5, 8));

        bbs.retention = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 8, 9));

        bbs.checkSum = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 9, 10));

        return bbs;
    }
}
