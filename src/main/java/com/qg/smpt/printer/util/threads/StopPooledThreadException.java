package com.qg.smpt.printer.util.threads;


/**
 * A custom {@link RuntimeException} thrown by the {@link ThreadPoolExecutor}
 * to signal that the thread should be disposed of.
 */
public class StopPooledThreadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StopPooledThreadException(String msg) {
        super(msg);
    }
}
