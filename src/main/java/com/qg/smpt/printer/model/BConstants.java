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

    public static final short codeStart = (short)0x7FFF;

    public static final short codeEnd = (short)0xFF7F;

    /* 请求类型 */
    public static final byte bulkStatus = (byte)0x00;   // 批次状态

    public static final byte okStatus = (byte)0x80;     // 阈值请求

    public static final byte printStatus = (byte)0x40;  // 打印机状态

    public static final byte orderStatus = (byte)0x20;  // 订单状态

    public static final byte connectStatus = (byte)0x60;// 连接请求 请求数据报

    /* 订单状态 STA */
    public static final byte orderSucc = (byte)0x00;    // 打印成功

    public static final byte orderFail = (byte)0x01;    // 打印失败

    public static final byte orderInQueue = (byte)0x02;  // 进入打印队列

    public static final byte orderTyping = (byte)0x03;   // 开始打印

    public static final byte orderDataW = (byte)0x04;    // 数据错误

    public static final byte orderExcep = (byte)0x05;    // 打印成功-之前的异常订单

    public static final byte orderExcepFail = (byte)0x06;

    public static final byte orderExcepInQueue = (byte)0x07;

    public static final byte orderExcepTyping = (byte)0x08;

    public static final byte orderExcepDataW = (byte)0x09;

    public static final byte orderWait = (byte)0x0A;     // 等待被发送

    public static final byte orderSent = (byte)0x0B;     // 订单已发送

    /* 批次订单状态 */
    public static final byte bulkSucc  = (byte)0x00;    // 批次订单成功

    public static final byte bulkInBuffer =  (byte)0x01;// 批次订单进入缓冲队列

    public static final byte bulkFail =  (byte)0x02;    // 批次订单失败


    /* 打印机状态 */
    public static final byte printer1 = (byte)0x1;      // 切刀错误

    public static final byte printer2 = (byte)0x2;      // 机盒打开

    public static final byte printer3 = (byte)0x3;      // 纸将用尽

    public static final byte printer4 = (byte)0x4;      // 正在进纸

    public static final byte printer5 = (byte)0x5;      // 机芯高温

    public static final byte printer6 = (byte)0x6;      // 正常状态

    public static final byte printer7 = (byte)0x7;      // 待定

    public static final byte printer8 = (byte)0x8;

    public static final byte printer9 = (byte)0x9;

    public static final byte printerA = (byte)0xA;

    public static final byte printerB = (byte)0xB;      // 待定

    public static final byte printerC = (byte)0xC;      // 普通缓冲区满

    public static final byte printerD = (byte)0xD;      // 紧急缓冲区满

    public static final byte printerE = (byte)0xE;      // 健康状态

    public static final byte printerF = (byte)0xF;      // 亚健康状态

    public static final byte printerNotConnect = (byte)0xFF; // 未连接

}
