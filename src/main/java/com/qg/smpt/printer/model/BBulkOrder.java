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
