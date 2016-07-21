package com.qg.smpt.printer.model;

/**
 * 打印机状态
 */
public class BPrinterStatus extends AbstactStatus{

    // line1
    public int printerId; // 主控板id
    // line2
    public int seconds;   // 发送给服务器的时间戳
    // line3
    public int number;    // 主控板打印单元序号

    public static BPrinterStatus bytesToPrinterStatus(byte[] bytes) {
        BPrinterStatus bps = (BPrinterStatus)BPrinterStatus.bytesToAbstractStatus(bytes);

        bps.printerId = bps.line1;

        bps.seconds = bps.line2;

        bps.number = bps.line3;

        return bps;
    }
}
