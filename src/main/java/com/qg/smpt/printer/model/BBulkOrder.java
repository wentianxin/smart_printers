package com.qg.smpt.printer.model;

/**
 * Created by tisong on 7/21/16.
 */
public class BBulkOrder {

    public short start;

    public short orderNumber;

    public short id;

    public short padding0;

    public int   seconds;

    public short checkSum;

    public short urg;

    public byte[] data;

    public short padding1;

    public short end;
}
