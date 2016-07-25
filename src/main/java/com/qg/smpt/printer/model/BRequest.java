package com.qg.smpt.printer.model;

/**
 * 请求数据包, 建立连接时
 * 100-阈值请求
 * 011-请求建立连接
 */
public final class BRequest extends AbstactStatus{

    // line1
    public int printerId; // 主控板id
    // line2
    public int seconds;   // 主控板发送给服务器的时间戳
    // line3
    public int padding;   // 填充位

    public static BRequest bytesToRequest(byte[] bytes) {
    	
    	AbstactStatus status = AbstactStatus.bytesToAbstractStatus(bytes);

        BRequest br = new BRequest();
        
        br.flag = status.flag;

        br.printerId = status.line1;

        br.seconds = status.line2;

        br.padding = status.line3;

        return br;
    }

}
