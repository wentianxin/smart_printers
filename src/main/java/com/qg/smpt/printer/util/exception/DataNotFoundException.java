package com.qg.smpt.printer.util.exception;

/**
 * Created by tisong on 3/27/17.
 */
public class DataNotFoundException extends Exception{

    public DataNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public DataNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public DataNotFoundException(String msg) {
        super(msg);
    }
}
