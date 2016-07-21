package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

/**
 * 图片
 */
public final class BPhoto {

    public short start = BConstants.photoStart;

    public short length;

    public byte[] data;

    public short padding;

    public short end = BConstants.photoEnd;

    public int size;

    public static byte[] bPhotoToBytes(BPhoto bPhoto) {

        byte[] bytes = new byte[bPhoto.size];

        int position = 0;

        position = BytesConvert.fillShort(bPhoto.start, bytes, position);

        position = BytesConvert.fillShort(bPhoto.length, bytes, position);

        position = BytesConvert.fillByte(bPhoto.data, bytes, position);

        position = BytesConvert.fillShort(bPhoto.padding, bytes, position);

        BytesConvert.fillShort(bPhoto.end, bytes, position);

        return bytes;
    }

}
