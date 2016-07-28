package com.qg.smpt.printer.model;

import com.qg.smpt.util.BytesConvert;

import java.util.Arrays;

/**
 * Created by tisong on 7/22/16.
 */
public class AbstactStatus {
    public final short start = BConstants.statusStart;

    public short flag;

    public int line1;

    public int line2;

    public int line3;

    public short checkSum;

    public final short end = BConstants.statusEnd;

    protected static AbstactStatus bytesToAbstractStatus(byte[] bytes) {
        AbstactStatus as = new AbstactStatus();

        as.flag = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 2, 4));

        as.line1 = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 4, 8));

        as.line2 = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 8, 12));

        as.line3 = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 12, 16));

        as.checkSum = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 16, 18));

        return as;
    }

    /**
     * 测试使用
     * @param abstactStatus
     * @return
     */
    public static byte[] abstratcStatusToBytes(AbstactStatus abstactStatus) {
        byte[] bytes = new byte[20];

        int position = 0;

        position = BytesConvert.fillShort(abstactStatus.start, bytes, 0);

        position = BytesConvert.fillShort(abstactStatus.flag, bytes, position);

        position = BytesConvert.fillInt(abstactStatus.line1, bytes, position);

        position = BytesConvert.fillInt(abstactStatus.line2, bytes, position);

        position = BytesConvert.fillInt(abstactStatus.line3, bytes, position);

        position = BytesConvert.fillShort(abstactStatus.checkSum, bytes, position);

        BytesConvert.fillShort(abstactStatus.end, bytes, position);

        return bytes;
    }


    public static void main(String[] args) {

        byte[] bytes = new byte[4];
        bytes[0] = (byte)0x0;
        bytes[1] = (byte)0x0;
        bytes[2] = (byte)0x0;
        bytes[3] = (byte)0x1;

        int x = BytesConvert.bytesToInt(bytes);  // x = 1;

        System.out.println(x);
    }
}
