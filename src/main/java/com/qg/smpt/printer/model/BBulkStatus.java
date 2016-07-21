package com.qg.smpt.printer.model;

/**
 * Created by tisong on 7/21/16.
 */
public final class BBulkStatus {

    public short start;

    public short flag; // 3bit:type; 12bit:padding; 1bit:0, 1

    public short id;

    public short padding;

    public int   seconds;

    public int   retention;

    public short checkSum;

    public short end;
}
