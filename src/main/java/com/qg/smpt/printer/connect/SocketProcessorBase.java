package com.qg.smpt.printer.connect;

import com.qg.smpt.printer.util.net.SocketEvent;
import com.qg.smpt.printer.util.net.SocketWrapperBase;

import java.util.Objects;

/**
 * Created by tisong on 3/31/17.
 */
public abstract class SocketProcessorBase<S> implements Runnable {

    protected SocketWrapperBase<S> socketWrapper;
    protected SocketEvent event;

    public SocketProcessorBase(SocketWrapperBase<S> socketWrapper, SocketEvent event) {
        reset(socketWrapper, event);
    }


    public void reset(SocketWrapperBase<S> socketWrapper, SocketEvent event) {
        Objects.requireNonNull(event);
        this.socketWrapper = socketWrapper;
        this.event = event;
    }

    @Override
    public void run() {
        // TODO
        if (socketWrapper.isClosed()) {
            return ;
        }
        doRun();
    }

    protected abstract void doRun();
}
