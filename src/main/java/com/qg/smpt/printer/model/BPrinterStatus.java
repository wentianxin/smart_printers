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
        AbstactStatus status = AbstactStatus.bytesToAbstractStatus(bytes);

        BPrinterStatus bps = new BPrinterStatus();

        bps.flag = status.flag;

        bps.printerId = status.line1;

        bps.seconds = status.line2;

        bps.number = status.line3;

        return bps;
    }
}
