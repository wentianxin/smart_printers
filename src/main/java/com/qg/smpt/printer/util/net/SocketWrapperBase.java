package com.qg.smpt.printer.util.net;

import com.qg.smpt.printer.connect.AbstractConnector;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by tisong on 3/31/17.
 */
public abstract class SocketWrapperBase<E> {

    public abstract boolean isClosed();

    private final E socket;

    private final AbstractConnector<E> endpoint;

    private volatile long readTimeout = -1;

    private volatile long writeTimeout = -1;

    private volatile int keepAliveLeft = 100;

    /**
     * 默认写缓存
     */
    protected int bufferedWriteSiz = 64 * 1024;

    public SocketWrapperBase(E socket, AbstractConnector<E> endpoint) {
        this.socket = socket;
        this.endpoint = endpoint;
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//        this.blockingStatusReadLock = lock.readLock();
//        this.blockingStatusWriteLock = lock.writeLock();
    }

    public E getSocket() {
        return socket;
    }
}
