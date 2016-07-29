package com.qg.smpt.printer.model;

/**
 * 常量
 */
public final class BConstants {

    public static final short orderStart = (short)0x3E11;

    public static final short orderEnd = (short)0x11E3;

    public static final short bulkOrderStart = (short)0xAA55;

    public static final short bulkOrderEnd = (short)0x55AA;

    public static final short statusStart = (short)0xCFFC;

    public static final short statusEnd   = (short)0xFCCF;

    public static final short textStart = (short)0x7EFF;

    public static final short textEnd = (short)0xFF7E;

    public static final short photoStart = (short)0xFF7E;

    public static final short photoEnd = (short)0x7EFF;


    /* 请求类型 */
    public static final byte bulkStatus = (byte)0x00;   // 批次状态

    public static final byte okStatus = (byte)0x80;     // 阈值请求

    public static final byte printStatus = (byte)0x40;  // 打印机状态

    public static final byte orderStatus = (byte)0x20;  // 订单状态

    public static final byte connectStatus = (byte)0x60;// 连接请求 请求数据报

    /* 订单状态 STA */
    public static final byte orderFail = (byte)0x01;

    public static final byte orderSucc = (byte)0x00;

    /* 批次订单状态 */
    public static final byte bulkSucc  = (byte)0x00;

    public static final byte bulkFail = (byte)0x01;





}
