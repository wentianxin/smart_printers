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
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(bytes[0]);
        bb.put(bytes[1]);
        return bb.getShort(0);
    }

    private static int getInt(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(bytes[0]);
        bb.put(bytes[1]);
        bb.put(bytes[2]);
        bb.put(bytes[3]);
        return bb.getInt(0);
    }

    public static byte[] intToBytes(int number) {
        return ByteBuffer.allocate(4).putInt(number).array();
    }

    public static byte[] shortToBytes(short number) {
        return  ByteBuffer.allocate(2).putShort(number).array();
    }


    public static int fillShort(short number, byte[] bytes, int start) {

        byte[] b = shortToBytes(number);

        System.arraycopy(b, 0, bytes, start, b.length);

        return start + b.length;
    }

    public static int fillInt (int number, byte[] bytes, int start) {

        byte[] b = intToBytes(number);

        System.arraycopy(b, 0, bytes, start, b.length);

        return start + b.length;
    }

    public static int fillByte (byte[]srcByte, byte[] bytes, int start){
        System.arraycopy(srcByte, 0, bytes, start, srcByte.length);

        return start + srcByte.length;
    }

    public static byte int0_7ToByte(int number) {
        return (byte)(number & 0xFF);
    }

    public static byte int8_15ToByte(int number) {
        return (byte)((number >> 8) &0xFF);
    }

    public static byte int16_23ToByte(int number) {
        return (byte)((number >> 16) &0xFF);
    }

    public static byte int24_31ToByte(int number) {
        return (byte)((number >> 24) &0xFF);
    }

    public static byte short0_7ToByte(short number) {
        return (byte)(number & 0xFF);
    }

    public static byte short8_15ToByte(short number) {
        return (byte)((number >> 8) &0xFF);
    }
}
