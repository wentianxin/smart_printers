package com.qg.smpt.printer.model;

/**
 * 请求
 */
public final class BRequest {

    public short start;

    public short flag;  // 100: 阈值； 011: 请求建立连接

    public short id;

    public int   seconds;

    public int   padding;

    public short checkSum;

    public short end;
}
