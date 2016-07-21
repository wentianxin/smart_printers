package com.qg.smpt.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by tisong on 7/21/16.
 */
public final class BytesConvert {

    public static short bytesToShort(byte[] bytes) {
        if (bytes.length != 2) {
            return -1;
        }
        return getShort(bytes);
    }

    public static int bytesToInt(byte[] bytes) {
        if (bytes.length != 4) {
            return -1;
        }
        return getInt(bytes);
    }

    private static short getShort(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(bytes[0]);
        bb.put(bytes[1]);
        return bb.getShort(0);
    }

    private static int getInt(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(bytes[0]);
        bb.put(bytes[1]);
        bb.put(bytes[2]);
        bb.put(bytes[3]);
        return bb.getInt(0);
    }
}
