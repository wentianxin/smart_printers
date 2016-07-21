package com.qg.smpt.printer.model;

/**
 * 常量
 */
public final class BConstants {

    public static final short orderStart = (short)0x113E;

    public static final short orderEnd = (short)0x3E11;

    public static final short bulkOrderStart = (short)0x55aa;

    public static final short bulkOrderEnd = (short)0xaa55;

    public static final short statusStart = (short)0xFCCF;

    public static final short statusEnd   = (short)0xCFFC;

    public static final short textStart = (short)0xFF7E;

    public static final short textEnd = (short)0x7EFF;

    public static final short photoStart = (short)0x7EFF;

    public static final short photoEnd = (short)0xFF7E;
}
