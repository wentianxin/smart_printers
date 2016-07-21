package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

/**
 * 文本
 */
public final class BText {

    public short start = BConstants.textStart;

    public short length;

    public byte[] data;

    public short padding;

    public short end = BConstants.textEnd;

    public int size;

    public static byte[] bTestToBytes(BText bText) {
        byte[] bytes = new byte[bText.size];

        int position = 0;

        position = BytesConvert.fillShort(bText.start, bytes, position);

        position = BytesConvert.fillShort(bText.length, bytes, position);

        position = BytesConvert.fillByte(bText.data, bytes, position);

        position = BytesConvert.fillShort(bText.padding, bytes, position);

        BytesConvert.fillShort(bText.end, bytes, position);

        return bytes;
    }
}
